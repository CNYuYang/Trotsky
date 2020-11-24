package run.yuyang.trotsky.resource;

import run.yuyang.trotsky.commom.utils.ResUtils;
import run.yuyang.trotsky.service.ConfService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author YuYang
 */
@Path("/admin/category")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Inject
    ConfService confService;

    @GET
    public Response getAll() {
        return ResUtils.success(confService.getNoteDirs());
    }

}
