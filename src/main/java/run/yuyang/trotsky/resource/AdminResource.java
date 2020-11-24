package run.yuyang.trotsky.resource;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import run.yuyang.trotsky.commom.utils.DateUtils;
import run.yuyang.trotsky.commom.utils.ResUtils;
import run.yuyang.trotsky.model.request.LoginParam;
import run.yuyang.trotsky.model.response.HomeInfo;
import run.yuyang.trotsky.service.ConfService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author YuYang
 */
@Path("/admin")
public class AdminResource {

    @Inject
    Vertx vertx;

    @Inject
    ConfService confService;

    @GET()
    @Produces(MediaType.TEXT_HTML)
    public Uni<String> home(@CookieParam("uuid") String uuid) {
        if (null == uuid || "".equals(uuid) || !uuid.equals(confService.getUUID())) {
            return vertx.fileSystem().readFile("META-INF/resources/admin/login.html")
                    .onItem().transform(b -> b.toString("UTF-8"));
        } else {
            return vertx.fileSystem().readFile("META-INF/resources/admin/home.html")
                    .onItem().transform(b -> b.toString("UTF-8"));
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginParam param) {
        if (param.getEmail().equals(confService.getUserConf().getEmail()) && param.getPassword().equals(confService.getUserConf().getPassword())) {
            String uuid = UUID.randomUUID().toString();
            NewCookie cookie = new NewCookie("uuid", uuid);
            confService.setUUID(uuid);
            return Response.ok(ResUtils.success).cookie(cookie).build();
        } else {
            return ResUtils.failure();
        }
    }

    @GET
    @Path("/info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response info(@CookieParam("uuid") String uuid) throws ParseException {
        if (null == uuid || "".equals(uuid) || !uuid.equals(confService.getUUID())) {
            return ResUtils.failure("No Authenticate");
        } else {
            HomeInfo info = new HomeInfo();
            info.setNickName(confService.getUserConf().getNickName());
            info.setNoteCount(confService.getCountConf().getNoteCount());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date buildTime = format.parse(confService.getUserConf().getBuildTime());
            info.setDay(DateUtils.differentDays(buildTime, new Date()));
            return ResUtils.success(info);
        }
    }

}
