package run.yuyang.trotsky.resource;

import run.yuyang.trotsky.commom.utils.ResUtils;
import run.yuyang.trotsky.model.conf.DirConf;
import run.yuyang.trotsky.model.param.ChangeDirNameParam;
import run.yuyang.trotsky.model.param.NewDirParam;
import run.yuyang.trotsky.model.vo.TreeVO;
import run.yuyang.trotsky.service.ConfService;
import run.yuyang.trotsky.service.DirService;

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
    ConfService confService;

    @Inject
    DirService dirService;

    @GET
    public Response getAll() {
        return ResUtils.success(dirService.getDirs());
    }

    @GET
    @Path("/type/{type}")
    public Response getDirByType(@PathParam("type") Integer type) {
        List<TreeVO> list = new LinkedList<>();
        dirService.getDirs().forEach((k, v) -> {
            if (v.getType() == type) {
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
        dirService.getDirs().forEach((k, v) -> {
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
        if (dirService.exist(newDirParam.getChild())) {
            ResUtils.failure("已存在该名称的分类");
        }
        if (!dirService.exist(newDirParam.getParent())){
            ResUtils.failure("未找到父分类");
        }
        DirConf dirConf = DirConf.defaultConf();
        DirConf parent = dirService.getDir(newDirParam.getParent());

        dirConf.setName(newDirParam.getChild());
        dirConf.setFather(newDirParam.getParent());
        dirConf.setPath(parent.getPath() + "/" + newDirParam.getChild());
        dirService.addDir(dirConf);
        parent.setDir_nums(parent.getDir_nums() + 1);
        dirService.save();

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
