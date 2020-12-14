package run.yuyang.trotsky;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import run.yuyang.trotsky.commom.exception.TrotskyException;
import run.yuyang.trotsky.service.ConfService;
import run.yuyang.trotsky.service.FileService;
import run.yuyang.trotsky.service.InitService;

import javax.inject.Inject;


/**
 * @author YuYang
 */
@QuarkusMain
public class Trotsky implements QuarkusApplication {


    private String banner = "_________ _______  _______ _________ _______  _                \n" +
            "\\__   __/(  ____ )(  ___  )\\__   __/(  ____ \\| \\    /\\|\\     /|\n" +
            "   ) (   | (    )|| (   ) |   ) (   | (    \\/|  \\  / /( \\   / )\n" +
            "   | |   | (____)|| |   | |   | |   | (_____ |  (_/ /  \\ (_) / \n" +
            "   | |   |     __)| |   | |   | |   (_____  )|   _ (    \\   /  \n" +
            "   | |   | (\\ (   | |   | |   | |         ) ||  ( \\ \\    ) (   \n" +
            "   | |   | ) \\ \\__| (___) |   | |   /\\____) ||  /  \\ \\   | |   \n" +
            "   )_(   |/   \\__/(_______)   )_(   \\_______)|_/    \\/   \\_/   \n" +
            "                                                               ";

    @Inject
    InitService initService;

    @Inject
    ConfService confService;

    @Inject
    FileService fileService;

    @ConfigProperty(name = "trotsky.version", defaultValue = "unknown")
    private String version;

    @ConfigProperty(name = "quarkus.http.port", defaultValue = "1917")
    private String port;

    @Override
    public int run(String... args) {
        if (args.length == 0) {
            return help();
        }
        String cmd = args[0];
        switch (cmd) {
            case "-v":
            case "--version":
                return version();
            case "init": {
                String path = "";
                if (args.length == 2) {
                    path = args[1];
                } else if (args.length > 2) {
                    return help();
                }
                try {
                    initService.init(path);
                    return 0;
                } catch (TrotskyException e) {
                    System.out.println(e.getMessage());
                    return -1;
                }
            }
            case "serve": {
                String path = "";
                if (args.length == 2) {
                    path = args[1];
                } else if (args.length > 2) {
                    return help();
                }
                confService.readConfFromFile(fileService.getRelPath(path));
                System.out.println(banner);
                System.out.println("\n\n    trotsky is listening port : " + port + " \n    the URL is http://127.0.0.1:" + port);
                System.out.println("\n\n    trotsky`s dashboard is worker on  http://127.0.0.1:" + port + "/admin");
                Quarkus.waitForExit();
                return 0;
            }
            default:
                return help();
        }
    }

    public int help() {
        System.out.println("例子：trotsky <init|serve> <path>");
        System.out.println("");
        System.out.println("常用命令:");
        System.out.println("\ttrotsky init [path]");
        System.out.println("\ttrotsky serve [path]");
        System.out.println("\ttrotsky start <path>");
        System.out.println("");
        System.out.println("其他命令:");
        System.out.println("\ttrotsky --help,-h");
        System.out.println("\ttrotsky --version,-v");
        return 0;
    }

    public int version() {
        System.out.println("\ntrotsky version : \n\t\t" + version + "\n");
        return 0;
    }

}
