package run.yuyang.trotsky.service.impl;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import run.yuyang.trotsky.model.conf.UserConf;
import run.yuyang.trotsky.service.ConfService;
import run.yuyang.trotsky.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    private static final String FILE_NAME = "/.trotsky/user.json";

    UserConf userConf = new UserConf();

    @Inject
    Vertx vertx;

    @Inject
    ConfService confService;

    @Override
    public void save() {
        vertx.fileSystem().writeFile(confService.getWorkerPath() + FILE_NAME, Buffer.buffer(JsonObject.mapFrom(userConf).toString() + "\n"), res -> {

        });
    }

    @Override
    public void load() {
        JsonObject object = vertx.fileSystem().readFileBlocking(confService.getWorkerPath() + FILE_NAME).toJsonObject();
        userConf = object.mapTo(UserConf.class);
    }

    @Override
    public void saveBlocking() {
        vertx.fileSystem().writeFileBlocking(confService.getWorkerPath() + FILE_NAME, Buffer.buffer(JsonObject.mapFrom(userConf).toString() + "\n"));
    }

    @Override
    public UserConf getUser() {
        return userConf;
    }

}
