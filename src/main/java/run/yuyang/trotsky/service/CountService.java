package run.yuyang.trotsky.service;

import run.yuyang.trotsky.model.conf.CountConf;
import run.yuyang.trotsky.service.base.SerializableService;

public interface CountService extends SerializableService {

    CountConf getCount();


}
