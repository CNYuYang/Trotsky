package run.yuyang.trotsky.resource;

import run.yuyang.trotsky.commom.utils.ResUtils;
import run.yuyang.trotsky.model.vo.TreeVO;
import run.yuyang.trotsky.service.ConfService;

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

}
