package run.yuyang.trotsky.service;

public interface FileService {

    boolean copyStaticFile(String path, boolean force);

    boolean copyStaticFile(String path);

}
