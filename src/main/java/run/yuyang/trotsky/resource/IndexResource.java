package run.yuyang.trotsky.resource;

import run.yuyang.trotsky.service.ConfService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

/**
 * @author YuYang
 */
@Path("")
public class IndexResource {

    @Inject
    ConfService confService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response home() {
        File file = new File(confService.getWorkerPath() + "/index.html");
        return Response.ok(file).build();
    }

    @GET
    @Path("/css/{css}")
    @Produces("text/css")
    public Response getCss(@PathParam("css") String css) {
        File file = new File(confService.getWorkerPath() + "/css/" + css);
        return Response.ok(file).build();
    }

    @GET
    @Path("/js/{js}")
    @Produces("application/javascript")
    public Response getJs(@PathParam("js") String js) {
        File file = new File(confService.getWorkerPath() + "/js/" + js);
        return Response.ok(file).build();
    }

    @GET
    @Path("/img/avatar.jpg")
    @Produces("image/png")
    public Response getAvatar() {
        File file = new File(confService.getWorkerPath() + "/img/avatar.jpg");
        return Response.ok(file).build();
    }

    @GET
    @Path("/webfonts/{webfonts}")
    @Produces("font/tff")
    public Response getWebFont(@PathParam("webfonts") String webfonts) {
        File file = new File(confService.getWorkerPath() + "/webfonts/" + webfonts);
        return Response.ok(file).build();
    }

    @GET
    @Path("/{md:.*md$}")
    @Produces("text/markdown")
    public Response getMd(@PathParam("md") String md) {
        File file = new File(confService.getWorkerPath() + "/" + md);
        return Response.ok(file).build();
    }

}
