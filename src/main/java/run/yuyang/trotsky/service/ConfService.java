package run.yuyang.trotsky.service;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import run.yuyang.trotsky.model.conf.*;
import run.yuyang.trotsky.model.request.MDParam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YuYang
 */
@ApplicationScoped
@Setter
@Getter
public class ConfService {

    @Inject
    Vertx vertx;

    @ConfigProperty(name = "trotsky.version", defaultValue = "unkown")
    private String version;

    private IndexConf indexConf;

    private UserConf userConf;

    private Map<String, NoteConf> noteConfs;

    private CountConf countConf;

    private Map<String, DirConf> noteDirs;

    private String workerPath;

    private String UUID;

    public void readConfFromFile(String path) {
        workerPath = path;
        JsonObject object = vertx.fileSystem().readFileBlocking(path + "/.trotsky/index.json").toJsonObject();
        indexConf = object.mapTo(IndexConf.class);
        object = vertx.fileSystem().readFileBlocking(path + "/.trotsky/user.json").toJsonObject();
        userConf = object.mapTo(UserConf.class);
        JsonArray array = vertx.fileSystem().readFileBlocking(path + "/.trotsky/note.json").toJsonArray();
        noteConfs = new HashMap<>();
        array.forEach(obj -> {
            if (obj instanceof JsonObject) {
                NoteConf conf = ((JsonObject) obj).mapTo(NoteConf.class);
                noteConfs.put(conf.getName(), conf);
            }
        });
        array = vertx.fileSystem().readFileBlocking(path + "/.trotsky/dir.json").toJsonArray();
        noteDirs = new HashMap<>();
        array.forEach(obj -> {
            if (obj instanceof JsonObject) {
                DirConf conf = ((JsonObject) obj).mapTo(DirConf.class);
                noteDirs.put(conf.getName(), conf);
            }
        });
        object = vertx.fileSystem().readFileBlocking(path + "/.trotsky/count.json").toJsonObject();
        countConf = object.mapTo(CountConf.class);
    }

    public void saveUserConf() {
        vertx.fileSystem().writeFileBlocking(workerPath + "/.trotsky/user.json", Buffer.buffer(JsonObject.mapFrom(userConf).toString() + "\n"));
    }

    public void saveUserConf(UserConf userConf) {
        vertx.fileSystem().writeFileBlocking(workerPath + "/.trotsky/user.json", Buffer.buffer(JsonObject.mapFrom(userConf).toString() + "\n"));
    }


    public boolean existNoteConf(String name) {
        return noteConfs.containsKey(name);
    }

    public NoteConf getNoteConf(String name) {
        return noteConfs.get(name);
    }

    public boolean existDirConf(String name) {
        return noteDirs.containsKey(name);
    }

    public DirConf getDirConf(String name) {
        return noteDirs.get(name);
    }

    public String getNotePath(String name) {
        return noteConfs.get(name).getPath();
    }

    public String getDirPath(String name) {
        return noteDirs.get(name).getPath();
    }

    public String getRelPath(MDParam param) {
        return workerPath + "/" + noteDirs.get(param.getFather()).getPath() + "/" + param.getName();
    }

    public String getRelPath(String name) {
        return workerPath + noteConfs.get(name).getPath();
    }

    private void addNoteConf(NoteConf noteConf) {
        noteConfs.put(noteConf.getName(), noteConf);
        countConf.addNote();
    }

    private void saveNoteConf() {
        JsonArray array = new JsonArray();
        noteConfs.forEach((k, note) -> {
            array.add(JsonObject.mapFrom(note));
        });
        vertx.fileSystem().writeFileBlocking(workerPath + "/.trotsky/note.json", Buffer.buffer(array.toString() + "\n"));
        vertx.fileSystem().writeFileBlocking(workerPath + "/.trotsky/count.json", Buffer.buffer(JsonObject.mapFrom(countConf).toString() + "\n"));
    }

    public void saveIndexConf() {
        vertx.fileSystem().writeFileBlocking(workerPath + "/.trotsky/index.json", Buffer.buffer(JsonObject.mapFrom(indexConf).toString() + "\n"));
    }

    public void addNoteConfAndSave(NoteConf noteConf) {
        addNoteConf(noteConf);
        saveNoteConf();
    }

    private void delNoteConf(String name) {
        noteConfs.remove(name);
        countConf.delNote();
    }

    public void delNoteConfAndSave(String name) {
        delNoteConf(name);
        saveNoteConf();
    }

}