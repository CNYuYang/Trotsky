package run.yuyang.trotsky.service.impl;

import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import run.yuyang.trotsky.service.FileService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class FileServiceImpl implements FileService {

    @Inject
    Vertx vertx;

    public boolean copyStaticFile(String path, boolean force) {
        FileSystem fileSystem = vertx.fileSystem();
        String[] files = new String[]{
                "index.html", "README.md", "_coverpage.md", "notes.md",
                ".trotsky/user.json", ".trotsky/index.json", ".trotsky/note.json", ".trotsky/count.json", ".trotsky/dir.json",
                "img/avatar.jpg",
                "js/docsify.min.js",
                "css/vue.css", "css/index.css",
                "webfonts/JetBrainsMono-Regular.ttf",
                "notes/被背叛的革命.md",
                "notes/被背叛的革命/导言-本书的目的.md",
                "notes/被背叛的革命/第一章-已经成就了什么.md",
                "notes/论苏俄革命与国际形势的四篇报导/与美国联合通信社的代表谈话.md",
                "notes/论苏俄革命与国际形势的四篇报导/答纽约泰晤士报编辑部的问题.md"
        };
        String[] dirs = new String[]{
                "img", ".trotsky", "notes", "js", "css", "webfonts", "notes", "notes/被背叛的革命", "notes/论苏俄革命与国际形势的四篇报导"
        };
        for (String dir : dirs) {
            String dirPath = path + "/" + dir;
            fileSystem.mkdirBlocking(dirPath);
            System.out.println("创建✅ " + dirPath);
        }
        for (String fileName : files) {
            String filePath = path + "/" + fileName;
            if (fileSystem.existsBlocking(filePath)) {
                if (force) {
                    fileSystem.copyBlocking("static/" + fileName, path + "/" + fileName);
                    System.out.println("覆盖当前文件✅ " + filePath);
                } else {
                    System.out.println("已存在✅ " + filePath);
                }
            } else {
                fileSystem.copyBlocking("static/" + fileName, path + "/" + fileName);
                System.out.println("创建文件✅ " + filePath);
            }
        }
        return true;
    }

    public boolean copyStaticFile(String path) {
        return copyStaticFile(path, false);
    }

}
