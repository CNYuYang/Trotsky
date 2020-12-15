package run.yuyang.trotsky.service.impl;

import run.yuyang.trotsky.service.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ConfServiceImpl implements ConfService {

    private String workerPath;

    @Override
    public String getWorkerPath() {
        return workerPath;
    }

    @Override
    public void setWorkerPath(String workerPath) {
        this.workerPath = workerPath;
    }

    @Inject
    DirService dirService;

    @Inject
    IndexService indexService;

    @Inject
    NoteService noteService;

    @Inject
    UserService userService;

    @Inject
    IntroService introService;

    @Override
    public void load() {
        dirService.load();
        indexService.load();
        noteService.load();
        userService.load();
        introService.load();
    }
}
