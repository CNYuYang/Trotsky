package run.yuyang.trotsky.resource;

import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import run.yuyang.trotsky.commom.utils.ResUtils;
import run.yuyang.trotsky.model.conf.NoteConf;
import run.yuyang.trotsky.model.param.MDParam;
import run.yuyang.trotsky.model.vo.TreeVO;
import run.yuyang.trotsky.service.ConfService;
import run.yuyang.trotsky.service.DirService;
import run.yuyang.trotsky.service.NoteService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

/**
 * @author YuYang
 */
@Path("/admin/note")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NoteResource {

    @Inject
    Vertx vertx;

    @Inject
    ConfService confService;

    @Inject
    NoteService noteService;

    @Inject
    DirService dirService;

    @GET
    public Response getAllInfo() {
        return ResUtils.success(noteService.getNotes());
    }

    @GET
    @Path("/info/{name}")
    public Response getInfoByName(@PathParam("name") String name) {
        if (noteService.existNote(name)) {
            return ResUtils.success(noteService.getNote(name));
        } else {
            return ResUtils.failure("未找到该文件信息");
        }
    }

    @GET
    @Path("/info/all")
    public Response getAllMdInfo() {
        List<TreeVO> list = new LinkedList<>();
        noteService.getNotes().forEach((k, v) -> {
            list.add(TreeVO.builder()
                    .name(v.getName())
                    .path(v.getPath())
                    .build());
        });
        return ResUtils.success(list);
    }

    @GET
    @Path("/{name}")
    public Response getText(@PathParam("name") String name) {
        FileSystem fileSystem = vertx.fileSystem();
        if (noteService.existNote(name) && fileSystem.existsBlocking(confService.getWorkerPath() + "/" + name)) {
            return ResUtils.success(fileSystem.readFileBlocking(confService.getWorkerPath() + "/" + name).toString());
        }
        return null;
    }


    @POST
    public Response newFile(MDParam param) {
        if (dirService.exist(param.getFather())) {
            //TODO fixbug
            //fileService.saveNewFile(confService.getRelPath(param), param.getText(), NoteConf.map(param, confService.getNotePath(param.getFather()), confService.getCountConf().getNextNoteId(), confService.getDirConf(param.getFather()).getDepth() + 1));
            return ResUtils.success();
        } else {
            return ResUtils.failure();
        }
    }

    @DELETE
    @Path("/{name}")
    public Response delNotes(@PathParam("name") String name) {
        if (noteService.existNote(name)) {
            noteService.delNoteAndSave(name);
            return ResUtils.success();
        } else {
            return ResUtils.failure("未找到该文件信息");
        }
    }


}
