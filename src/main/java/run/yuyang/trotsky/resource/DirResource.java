package run.yuyang.trotsky.resource;

import io.vertx.core.Vertx;
import run.yuyang.trotsky.commom.utils.ResUtils;
import run.yuyang.trotsky.model.conf.DirConf;
import run.yuyang.trotsky.model.param.ChangeDirNameParam;
import run.yuyang.trotsky.model.param.dir.NewDirParam;
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
    Vertx vertx;

    @Inject
    ConfService confService;

    @Inject
    DirService dirService;

    @GET
    public Response getAll() {
        return ResUtils.success(dirService.getDirs());
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
                item.add(v.isHave_intro());
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
        if (!dirService.exist(newDirParam.getParent())) {
            ResUtils.failure("未找到父分类");
        }
        String parentName = newDirParam.getParent();
        String childName = newDirParam.getChild();

        DirConf dirConf = new DirConf();
        DirConf parent = dirService.getDir(parentName);
        dirConf.setName(childName);
        dirConf.setFather(parentName);
        dirConf.setPath(parent.getPath() + "/" + newDirParam.getChild());
        boolean status = dirService.addDir(dirConf);
        if (status) {
            parent.addDir();
            dirService.save();
            vertx.fileSystem().mkdir(confService.getWorkerPath() + dirConf.getPath(), res -> {

            });
            return ResUtils.success();
        } else {
            return ResUtils.failure();
        }
    }

    @PUT
    @Path("/name")
    public Response changeName(ChangeDirNameParam changeDirNameParam) {
        String newName = changeDirNameParam.getNewName();
        String oldName = changeDirNameParam.getOldName();
        dirService.changeName(oldName, newName);
        String newPath = dirService.getDir(newName).getPath();
        dirService.getDirs().forEach((k, obj) -> {
            if (obj.getFather().equals(oldName)) {
                obj.setFather(newName);
                obj.setPath(newPath + "/" + obj.getName());
            }
        });
        dirService.save();
        return ResUtils.success();
    }


    @DELETE
    @Path("/name/{name}")
    public Response delByName(@PathParam("name") String name) {
        DirConf dirConf = dirService.getDir(name);
        vertx.fileSystem().delete(confService.getWorkerPath() + dirConf.getPath(), res -> {

        });
        dirService.getDir(dirConf.getFather()).delDir();
        dirService.delDir(name);
        dirService.save();
        return ResUtils.success();
    }

}
