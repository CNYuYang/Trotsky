package run.yuyang.trotsky.service;

import run.yuyang.trotsky.model.conf.UserConf;
import run.yuyang.trotsky.service.base.SerializableService;

public interface UserService extends SerializableService {

    UserConf getUser();

}
