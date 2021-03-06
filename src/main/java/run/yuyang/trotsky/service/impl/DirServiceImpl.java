package run.yuyang.trotsky.service.impl;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import run.yuyang.trotsky.model.conf.DirConf;
import run.yuyang.trotsky.service.ConfService;
import run.yuyang.trotsky.service.DirService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class DirServiceImpl implements DirService {


    @Inject
    Vertx vertx;

    @Inject
    ConfService confService;

    private final static String FILE_NAME = "/.trotsky/dir.json";

    private Map<String, DirConf> dirs = new ConcurrentHashMap<>();

    @Override
    public void save() {
        JsonArray array = new JsonArray();
        dirs.forEach((k, note) -> {
            array.add(JsonObject.mapFrom(note));
        });
        vertx.fileSystem().writeFile(confService.getWorkerPath() + FILE_NAME, Buffer.buffer(array.toString() + "\n"), res -> {

        });
    }

    @Override
    public void load() {
        JsonArray array = vertx.fileSystem().readFileBlocking(confService.getWorkerPath() + FILE_NAME).toJsonArray();
        array.forEach(obj -> {
            if (obj instanceof JsonObject) {
                DirConf conf = ((JsonObject) obj).mapTo(DirConf.class);
                dirs.put(conf.getName(), conf);
            }
        });
    }

    @Override
    public void saveBlocking() {
        JsonArray array = new JsonArray();
        dirs.forEach((k, note) -> {
            array.add(JsonObject.mapFrom(note));
        });
        vertx.fileSystem().writeFileBlocking(confService.getWorkerPath() + FILE_NAME, Buffer.buffer(array.toString() + "\n"));
    }


    @Override
    public boolean exist(String name) {
        return dirs.containsKey(name);
    }

    @Override
    public boolean addDir(DirConf conf) {
        if (exist(conf.getName())) {
            return false;
        }
        dirs.put(conf.getName(), conf);
        return true;
    }

    @Override
    public boolean addDirAndSave(DirConf conf) {
        boolean status = addDir(conf);
        if (status) {
            save();
        }
        return status;
    }

    @Override
    public Map<String, DirConf> getDirs() {
        return dirs;
    }

    @Override
    public DirConf getDir(String name) {
        if (exist(name)) {
            return dirs.get(name);
        }
        return null;
    }

    @Override
    public boolean changeName(String oldName, String newName) {
        if (exist(oldName) && !exist(newName)) {
            DirConf dirConf = getDir(oldName);
            String oldPath = dirConf.getPath();
            String newPath = getDir(dirConf.getFather()).getPath() + "/" + newName;
            delDir(oldName);
            dirConf.setName(newName);
            dirConf.setPath(newPath);
            addDir(dirConf);
            vertx.fileSystem().move(confService.getWorkerPath() + oldPath, confService.getWorkerPath() + newPath, res -> {

            });
            return true;
        }
        return false;
    }

    @Override
    public boolean delDir(String name) {
        if (exist(name)) {
            dirs.remove(name);
            return true;
        }
        return false;
    }


}
