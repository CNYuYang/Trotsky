package run.yuyang.trotsky.service;

import run.yuyang.trotsky.model.conf.IntroConf;
import run.yuyang.trotsky.service.base.SerializableService;

public interface IntroService extends SerializableService {

    boolean exist(String name);

    IntroConf getIntro(String name);

    boolean delIntro(String name);

    boolean delIntroAndSave(String name);

    boolean addIntro(IntroConf conf);

    boolean addIntroAndSave(IntroConf conf);

}
