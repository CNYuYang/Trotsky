package run.yuyang.trotsky.service.impl;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import run.yuyang.trotsky.model.conf.DirConf;
import run.yuyang.trotsky.model.conf.IntroConf;
import run.yuyang.trotsky.service.ConfService;
import run.yuyang.trotsky.service.IntroService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class IntroServiceImpl implements IntroService {


    @Inject
    Vertx vertx;

    @Inject
    ConfService confService;

    private static final String FILE_NAME = "/.trotsky/intro.json";

    private Map<String, IntroConf> intros = new ConcurrentHashMap<>();

    @Override
    public boolean exist(String name) {
        return intros.containsKey(name);
    }

    @Override
    public IntroConf getIntro(String name) {
        if (exist(name)) {
            return intros.get(name);
        }
        return null;
    }

    @Override
    public boolean delIntro(String name) {
        if (exist(name)) {
            intros.remove(name);
            return true;
        }
        return false;
    }

    @Override
    public boolean delIntroAndSave(String name) {
        boolean status = delIntro(name);
        if (status) {
            save();
        }
        return status;
    }

    @Override
    public boolean addIntro(IntroConf conf) {
        if (!exist(conf.getName())) {
            intros.put(conf.getName(), conf);
            return true;
        }
        return false;
    }

    @Override
    public boolean addIntroAndSave(IntroConf conf) {
        boolean status = addIntro(conf);
        if (status){
            save();
        }
        return status;
    }

    @Override
    public void save() {
        JsonArray array = new JsonArray();
        intros.forEach((k, into) -> {
            array.add(JsonObject.mapFrom(into));
        });
        vertx.fileSystem().writeFile(confService.getWorkerPath() + FILE_NAME, Buffer.buffer(array.toString() + "\n"), res -> {

        });
    }

    @Override
    public void load() {
        JsonArray array = vertx.fileSystem().readFileBlocking(confService.getWorkerPath() + FILE_NAME).toJsonArray();
        array.forEach(obj -> {
            if (obj instanceof JsonObject) {
                IntroConf intro = ((JsonObject) obj).mapTo(IntroConf.class);
                intros.put(intro.getName(), intro);
            }
        });
    }

    @Override
    public void saveBlocking() {
        JsonArray array = new JsonArray();
        intros.forEach((k, into) -> {
            array.add(JsonObject.mapFrom(into));
        });
        vertx.fileSystem().writeFileBlocking(confService.getWorkerPath() + FILE_NAME, Buffer.buffer(array.toString() + "\n"));
    }
}
