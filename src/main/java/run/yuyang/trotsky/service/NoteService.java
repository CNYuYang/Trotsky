package run.yuyang.trotsky.service;

import run.yuyang.trotsky.model.conf.NoteConf;
import run.yuyang.trotsky.service.base.SerializableService;

import java.util.Map;

public interface NoteService extends SerializableService {

    boolean addNote(NoteConf noteConf);

    boolean existNote(String name);

    Map<String, NoteConf> getNotes();

    NoteConf getNote(String name);

    boolean delNote(String name);

    boolean addNoteAndSave(NoteConf noteConf);

    boolean delNoteAndSave(String name);

}
