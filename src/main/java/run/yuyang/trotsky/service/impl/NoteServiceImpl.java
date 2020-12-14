package run.yuyang.trotsky.service.impl;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jboss.logging.Logger;
import run.yuyang.trotsky.model.conf.NoteConf;
import run.yuyang.trotsky.service.ConfService;
import run.yuyang.trotsky.service.NoteService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class NoteServiceImpl implements NoteService {

    //TODO 打印Logger信息
    private static final Logger logger = Logger.getLogger(NoteServiceImpl.class);

    private static final String FILE_NAME = "/.trotsky/note.json";

    private final Map<String, NoteConf> notes = new ConcurrentHashMap<>();

    @Inject
    Vertx vertx;

    @Inject
    ConfService confService;

    @Override
    public boolean addNote(NoteConf noteConf) {
        if (existNote(noteConf.getName())) {
            return false;
        }
        notes.put(noteConf.getName(), noteConf);
        return true;
    }

    @Override
    public boolean existNote(String name) {
        return notes.containsKey(name);
    }

    @Override
    public Map<String, NoteConf> getNotes() {
        return notes;
    }

    @Override
    public NoteConf getNote(String name) {
        if (existNote(name)) {
            return notes.get(name);
        }
        return null;
    }

    @Override
    public boolean delNote(String name) {
        if (existNote(name)) {
            notes.remove(name);
        }
        return false;
    }

    @Override
    public void load() {
        JsonArray array = vertx.fileSystem().readFileBlocking(confService.getWorkerPath() + FILE_NAME).toJsonArray();
        array.forEach(obj -> {
            if (obj instanceof JsonObject) {
                NoteConf conf = ((JsonObject) obj).mapTo(NoteConf.class);
                addNote(conf);
            }
        });
    }

    @Override
    public void saveBlocking() {
        JsonArray array = new JsonArray();
        notes.forEach((k, note) -> {
            array.add(JsonObject.mapFrom(note));
        });
        vertx.fileSystem().writeFileBlocking(confService.getWorkerPath() + FILE_NAME, Buffer.buffer(array.toString() + "\n"));
    }

    @Override
    public void save() {
        JsonArray array = new JsonArray();
        notes.forEach((k, note) -> {
            array.add(JsonObject.mapFrom(note));
        });
        vertx.fileSystem().writeFile(confService.getWorkerPath() + FILE_NAME, Buffer.buffer(array.toString() + "\n"), res -> {
            if (res.succeeded()) {

            } else {

            }
        });
    }

    @Override
    public boolean addNoteAndSave(NoteConf noteConf) {
        boolean status = addNote(noteConf);
        if (status) {
            save();
        }
        return status;
    }

    @Override
    public boolean delNoteAndSave(String name) {
        boolean status = delNote(name);
        if (status) {
            save();
        }
        return status;
    }

}
