package run.yuyang.trotsky.service.impl;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import run.yuyang.trotsky.model.conf.CountConf;
import run.yuyang.trotsky.model.conf.UserConf;
import run.yuyang.trotsky.service.ConfService;
import run.yuyang.trotsky.service.CountService;

import javax.inject.Inject;

public class CountServiceImpl implements CountService {

    private static final String FILE_NAME = "/.trotsky/count.json";

    CountConf countConf = new CountConf();

    @Inject
    Vertx vertx;

    @Inject
    ConfService confService;

    @Override
    public void save() {
        vertx.fileSystem().writeFile(confService.getWorkerPath() + FILE_NAME, Buffer.buffer(JsonObject.mapFrom(countConf).toString() + "\n"), res -> {

        });
    }

    @Override
    public void load() {
        JsonObject object = vertx.fileSystem().readFileBlocking(confService.getWorkerPath() + FILE_NAME).toJsonObject();
        countConf = object.mapTo(CountConf.class);
    }

    @Override
    public void saveBlocking() {
        vertx.fileSystem().writeFileBlocking(confService.getWorkerPath() + FILE_NAME, Buffer.buffer(JsonObject.mapFrom(countConf).toString() + "\n"));
    }

    @Override
    public CountConf getCount() {
        return countConf;
    }
}
