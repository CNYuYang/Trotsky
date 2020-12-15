package run.yuyang.trotsky.resource;

import io.vertx.core.Vertx;
import run.yuyang.trotsky.commom.utils.ResUtils;
import run.yuyang.trotsky.model.conf.DirConf;
import run.yuyang.trotsky.model.conf.IntroConf;
import run.yuyang.trotsky.model.conf.NoteConf;
import run.yuyang.trotsky.service.ConfService;
import run.yuyang.trotsky.service.DirService;
import run.yuyang.trotsky.service.IntroService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author YuYang
 */
@Path("/admin/intro")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IntroResource {

    @Inject
    Vertx vertx;

    @Inject
    ConfService confService;

    @Inject
    IntroService introService;

    @Inject
    DirService dirService;


    @DELETE
    @Path("/name/{name}")
    public Response delDirIntro(@PathParam("name") String name) {
        DirConf dirConf = dirService.getDir(name);
        if (introService.delIntroAndSave(name)) {
            vertx.fileSystem().delete(confService.getWorkerPath() + "/" + dirConf.getPath(), res -> {
            });
            dirConf.setHave_intro(false);
            dirService.save();
        }
        return ResUtils.success();
    }

    @POST
    @Path("/name/{name}")
    public Response newDirIntro(@PathParam("name") String name) {
        DirConf dirConf = dirService.getDir(name);
        IntroConf introConf = new IntroConf();
        introConf.setName(name);
        introConf.setFather(dirConf.getFather());
        introConf.setPath(dirConf.getPath() + ".md");
        if (introService.addIntroAndSave(introConf)) {
            vertx.fileSystem().createFile(confService.getWorkerPath() + introConf.getPath(), res -> {
            });
            dirConf.setHave_intro(true);
            dirService.save();
        }
        return ResUtils.success();
    }

}
