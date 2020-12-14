package run.yuyang.trotsky.resource;

import run.yuyang.trotsky.commom.utils.ResUtils;
import run.yuyang.trotsky.model.conf.NoteConf;
import run.yuyang.trotsky.model.param.MDParam;
import run.yuyang.trotsky.model.vo.TreeVO;
import run.yuyang.trotsky.service.ConfServiceOld;
import run.yuyang.trotsky.service.impl.FileServiceImpl;

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
    ConfServiceOld confService;

    @Inject
    FileServiceImpl fileService;

    @GET
    public Response getAllInfo() {
        return ResUtils.success(confService.getNoteConfs());
    }

    @GET
    @Path("/info/{name}")
    public Response getInfoByName(@PathParam("name") String name) {
        if (confService.existNoteConf(name)) {
            return ResUtils.success(confService.getNoteConf(name));
        } else {
            return ResUtils.failure("未找到该文件信息");
        }
    }

    @GET
    @Path("/info/all")
    public Response getAllMdInfo() {
        List<TreeVO> list = new LinkedList<>();
        confService.getNoteConfs().forEach((k, v) -> {
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


}
