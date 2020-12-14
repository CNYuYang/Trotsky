package run.yuyang.trotsky.service;

import run.yuyang.trotsky.model.conf.IndexConf;
import run.yuyang.trotsky.service.base.SerializableService;

public interface IndexService extends SerializableService {

    IndexConf getIndex();

}
