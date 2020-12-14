package run.yuyang.trotsky.service.impl;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import run.yuyang.trotsky.model.conf.IndexConf;
import run.yuyang.trotsky.service.ConfService;
import run.yuyang.trotsky.service.IndexService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class IndexServiceImpl implements IndexService {

    @Inject
    Vertx vertx;

    @Inject
    ConfService confService;

    private static final String FILE_NAME = "/.trotsky/index.json";
    private IndexConf indexConf = new IndexConf();

    @Override
    public void save() {
        vertx.fileSystem().writeFile(confService.getWorkerPath() + FILE_NAME, Buffer.buffer(JsonObject.mapFrom(indexConf).toString() + "\n"), res -> {

        });
    }

    @Override
    public void load() {
        JsonObject object = vertx.fileSystem().readFileBlocking(confService.getWorkerPath() + FILE_NAME).toJsonObject();
        indexConf = object.mapTo(IndexConf.class);
    }

    @Override
    public void saveBlocking() {
        vertx.fileSystem().writeFileBlocking(confService.getWorkerPath() + FILE_NAME, Buffer.buffer(JsonObject.mapFrom(indexConf).toString() + "\n"));
    }

    @Override
    public IndexConf getIndex() {
        return indexConf;
    }
}
