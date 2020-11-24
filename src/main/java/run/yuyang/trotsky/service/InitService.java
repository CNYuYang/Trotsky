package run.yuyang.trotsky.service;

import run.yuyang.trotsky.commom.exception.TrotskyException;
import run.yuyang.trotsky.model.conf.UserConf;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.Date;

@ApplicationScoped
public class InitService {


    @Inject
    FileService fileService;

    @Inject
    ConfService confService;

    public void init(String path) throws TrotskyException {
        String relPath = fileService.getRelPath(path);
        if (null == relPath) {
            throw new TrotskyException("未找到该文件夹，或无法创建文件夹。");
        }
        System.out.println("项目路径：" + relPath);
        if (fileService.copyStaticFile(relPath)) {
            System.out.println("配置文件初始完毕");
        } else {
            throw new TrotskyException("配置文件初始失败。");
        }
        confService.setWorkerPath(relPath);
        UserConf userConf = new UserConf();
        Console console = System.console();
        userConf.setVersion(confService.getVersion());
        userConf.setEmail(console.readLine("请输入你的email📮："));
        String password = "";
        String check = "";
        boolean flag = true;
        while (flag) {
            password = String.valueOf(console.readPassword("请输入密码️："));
            check = String.valueOf(console.readPassword("请再次输入密码："));
            if (password.equals(check)) {
                flag = false;
            } else {
                System.out.println("两次输入密码不同！请再试一次！");
            }
        }
        userConf.setPassword(password);
        userConf.setNickName("Trotsky");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        userConf.setBuildTime(format.format(new Date()));
        confService.saveUserConf(userConf);
        System.out.println("☭ 亲爱的达瓦里希，项目创建完成！");
    }


}
