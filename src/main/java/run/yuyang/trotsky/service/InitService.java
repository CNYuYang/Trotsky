package run.yuyang.trotsky.service;

import run.yuyang.trotsky.commom.exception.TrotskyException;

public interface InitService {

    void init(String path) throws TrotskyException;

}
