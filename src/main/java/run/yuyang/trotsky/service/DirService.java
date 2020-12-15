package run.yuyang.trotsky.service;

import run.yuyang.trotsky.model.conf.DirConf;
import run.yuyang.trotsky.service.base.SerializableService;

import java.util.Map;

public interface DirService extends SerializableService {

    boolean exist(String name);

    boolean addDir(DirConf conf);

    Map<String, DirConf> getDirs();

    DirConf getDir(String name);

    boolean changeName(String oldName, String newName);

    boolean delDir(String name);


}
