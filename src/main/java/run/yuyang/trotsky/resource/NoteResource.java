package run.yuyang.trotsky.resource;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import run.yuyang.trotsky.commom.utils.ResUtils;
import run.yuyang.trotsky.model.conf.NoteConf;
import run.yuyang.trotsky.model.param.MDParam;
import run.yuyang.trotsky.model.param.base.TextParam;
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
        if (noteService.existNoteAndDisk(name)) {
            return ResUtils.success(fileSystem.readFileBlocking(confService.getWorkerPath() + noteService.getNote(name).getPath()).toString());
        } else {
            return ResUtils.failure("数据库或者磁盘上未找到该笔记。");
        }
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
    @Path("/name/{name}")
    public Response delNotes(@PathParam("name") String name) {
        if (noteService.existNote(name)) {
            NoteConf noteConf = noteService.getNote(name);
            String father = noteConf.getFather();
            noteService.delNoteAndSave(name);
            vertx.fileSystem().delete(confService.getWorkerPath() + noteConf.getPath(), res -> {

            });
            dirService.getDir(father).delNote();
            dirService.save();
            return ResUtils.success();
        } else {
            return ResUtils.failure("未找到该文件信息");
        }
    }

    @GET
    @Path("/table/all")
    public Response getNotes() {
        List<List<Object>> list = new LinkedList<>();
        noteService.getNotes().forEach((k, obj) -> {
            List<Object> item = new LinkedList<>();
            item.add(obj.getName());
            item.add(obj.getPath());
            item.add(obj.getFather());
            item.add(obj.getShow());
            item.add(0);
            list.add(item);
        });
        return ResUtils.success(list);
    }

    @GET
    @Path("/name/all")
    public Response getAllName() {
        return ResUtils.success(noteService.getNotes().keySet());
    }

    @PUT
    @Path("/{name}")
    public Response updateText(@PathParam("name") String name, TextParam textParam) {
        if (noteService.existNoteAndDisk(name)) {
            NoteConf noteConf = noteService.getNote(name);
            vertx.fileSystem().writeFile(confService.getWorkerPath() + noteConf.getPath(), Buffer.buffer(textParam.getText()), res -> {
            });
            return ResUtils.success();
        } else {
            return ResUtils.failure("数据库或者磁盘上未找到该笔记。");
        }
    }
}
