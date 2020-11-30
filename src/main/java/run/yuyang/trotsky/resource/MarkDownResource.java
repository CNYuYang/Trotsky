package run.yuyang.trotsky.resource;

import com.sun.source.tree.Tree;
import io.smallrye.mutiny.Uni;
import run.yuyang.trotsky.commom.utils.ResUtils;
import run.yuyang.trotsky.model.conf.DirConf;
import run.yuyang.trotsky.model.conf.NoteConf;
import run.yuyang.trotsky.model.request.MDParam;
import run.yuyang.trotsky.model.response.TreeInfo;
import run.yuyang.trotsky.service.AsyncFileService;
import run.yuyang.trotsky.service.ConfService;
import run.yuyang.trotsky.service.FileService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

/**
 * @author YuYang
 */
@Path("/admin/md")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MarkDownResource {

    @Inject
    ConfService confService;

    @Inject
    FileService fileService;

    @Inject
    AsyncFileService asyncFileService;

    @GET
    public Response getAllInfo() {
        return ResUtils.success(confService.getNoteConfs());
    }

    @GET
    @Path("/{name}/info")
    public Response getInfoByName(@PathParam("name") String name) {
        if (confService.existNoteConf(name)) {
            return ResUtils.success(confService.getNoteConf(name));
        } else {
            return ResUtils.failure("未找到该文件信息");
        }
    }

    @GET
    @Path("/{name}/async")
    public Uni<String> getTextAsync(@PathParam("name") String name) {
        if (confService.existNoteConf(name) && fileService.existFile(confService.getNotePath(name))) {
            return asyncFileService.getFileAsync(confService.getNotePath(name));
        }
        return null;
    }

    @GET
    @Path("/{name}/sync")
    public Response getTextSync(@PathParam("name") String name) {
        if (confService.existNoteConf(name) && fileService.existFile(confService.getNotePath(name))) {
            return ResUtils.success(fileService.getFileSync(confService.getNotePath(name)));
        }
        return null;
    }


    @POST
    public Response newFile(MDParam param) {
        if (confService.existDirConf(param.getFather())) {
            fileService.saveNewFile(confService.getRelPath(param), param.getText(), NoteConf.map(param, confService.getNotePath(param.getFather()), confService.getCountConf().getNextNoteId(), confService.getDirConf(param.getFather()).getDepth() + 1));
            return ResUtils.success();
        } else {
            return ResUtils.failure();
        }
    }

    @DELETE
    @Path("/{name}")
    public Response delNotes(@PathParam("name") String name) {
        if (confService.existNoteConf(name)) {
            fileService.delFile(confService.getNotePath(name), name);
            return ResUtils.success();
        } else {
            return ResUtils.failure("未找到该文件信息");
        }
    }

    @GET
    @Path("/dir/type/{type}")
    public Response getDirByType(@PathParam("type") Integer type) {
        List<TreeInfo> list = new LinkedList<>();
        confService.getNoteDirs().forEach((k, v) -> {
            if (v.getType().equals(type)) {
                list.add(TreeInfo.builder()
                        .name(v.getName())
                        .path(v.getPath())
                        .build());
            }
        });
        return ResUtils.success(list);
    }

    @GET
    @Path("/info/all")
    public Response getAllMdInfo() {
        List<TreeInfo> list = new LinkedList<>();
        confService.getNoteConfs().forEach((k, v) -> {
            list.add(TreeInfo.builder()
                    .name(v.getName())
                    .path(v.getPath())
                    .build());
        });
        return ResUtils.success(list);
    }

}
