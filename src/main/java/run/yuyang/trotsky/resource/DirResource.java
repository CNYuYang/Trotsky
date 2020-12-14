package run.yuyang.trotsky.resource;

import run.yuyang.trotsky.commom.utils.ResUtils;
import run.yuyang.trotsky.model.param.ChangeDirNameParam;
import run.yuyang.trotsky.model.param.NewDirParam;
import run.yuyang.trotsky.model.vo.TreeVO;
import run.yuyang.trotsky.service.ConfServiceOld;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

/**
 * @author YuYang
 */
@Path("/admin/dir")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DirResource {

    @Inject
    ConfServiceOld confService;

    @GET
    public Response getAll() {
        return ResUtils.success(confService.getNoteDirs());
    }

    @GET
    @Path("/type/{type}")
    public Response getDirByType(@PathParam("type") Integer type) {
        List<TreeVO> list = new LinkedList<>();
        confService.getNoteDirs().forEach((k, v) -> {
            if (v.getType().equals(type)) {
                list.add(TreeVO.builder()
                        .name(v.getName())
                        .path(v.getPath())
                        .build());
            }
        });
        return ResUtils.success(list);
    }

    @GET
    @Path("/parent/{parent}")
    public Response getDirByParent(@PathParam("parent") String parent) {
        List<List<Object>> list = new LinkedList<>();
        confService.getNoteDirs().forEach((k, v) -> {
            if (v.getFather().equals(parent)) {
                List<Object> item = new LinkedList<>();
                item.add(v.getName());
                item.add(v.getPath());
                item.add(v.getNote_nums());
                item.add(v.getDir_nums());
                item.add(v.getType());
                list.add(item);
            }
        });
        return ResUtils.success(list);
    }

    @POST
    public Response newDir(NewDirParam newDirParam) {
        confService.newDir(newDirParam.getParent(), newDirParam.getChild());
        return ResUtils.success();
    }

    @PUT
    @Path("/change/name")
    public Response changeName(ChangeDirNameParam changeDirNameParam) {


        return ResUtils.success();
    }


    @DELETE
    @Path("/name/{name}")
    public Response delByName(@PathParam("name") String name) {

        return ResUtils.success();
    }

    @DELETE
    @Path("/intro/{name}")
    public Response delDirIntro(@PathParam("name") String name) {

        return ResUtils.success();
    }

    @POST
    @Path("/intro/{name}")
    public Response newDirIntro(@PathParam("name") String name) {

        return ResUtils.success();
    }

}
