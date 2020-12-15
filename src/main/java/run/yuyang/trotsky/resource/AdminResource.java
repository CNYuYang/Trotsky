package run.yuyang.trotsky.resource;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import run.yuyang.trotsky.commom.utils.ResUtils;
import run.yuyang.trotsky.model.conf.IndexConf;
import run.yuyang.trotsky.model.param.InfoParam;
import run.yuyang.trotsky.model.param.LoginParam;
import run.yuyang.trotsky.model.vo.UserVO;
import run.yuyang.trotsky.service.AuthService;
import run.yuyang.trotsky.service.ConfService;
import run.yuyang.trotsky.service.PageService;
import run.yuyang.trotsky.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.*;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author YuYang
 */
@Path("/admin")
public class AdminResource {

    @Inject
    Vertx vertx;

    @Inject
    ConfService confService;

    @Inject
    PageService pageService;

    @Inject
    AuthService authService;

    @Inject
    UserService userService;

    @GET()
    @Produces(MediaType.TEXT_HTML)
    public Uni<String> home(@CookieParam("uuid") String uuid) {
        if (!authService.auth(uuid)) {
            return vertx.fileSystem().readFile("META-INF/resources/admin/login.html")
                    .onItem().transform(b -> b.toString("UTF-8"));
        }
        return vertx.fileSystem().readFile("META-INF/resources/admin/home.html")
                .onItem().transform(b -> b.toString("UTF-8"));

    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginParam param) {
        if (param.getEmail().equals(userService.getUser().getEmail()) && param.getPassword().equals(userService.getUser().getPassword())) {
            String uuid = authService.token();
            NewCookie cookie = new NewCookie("uuid", uuid);
            return Response.ok(ResUtils.success).cookie(cookie).build();
        } else {
            return ResUtils.failure();
        }
    }

    @GET
    @Path("/info/nickName")
    @Produces(MediaType.APPLICATION_JSON)
    public Response nickName(@CookieParam("uuid") String uuid) throws ParseException {
        if (!authService.auth(uuid)) {
            return ResUtils.failure("No Authenticate");
        }
        return ResUtils.success(userService.getUser().getNickName());

    }

    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response info(@CookieParam("uuid") String uuid) throws ParseException {
        if (!authService.auth(uuid)) {
            return ResUtils.failure("No Authenticate");
        }
        UserVO info = UserVO.builder()
                .nickName(userService.getUser().getNickName())
                .email(userService.getUser().getEmail())
                .build();
        return ResUtils.success(info);
    }


    @PUT
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response info(@CookieParam("uuid") String uuid, InfoParam param) throws ParseException {
        if (!authService.auth(uuid)) {
            return ResUtils.failure("No Authenticate");
        }
        if (!param.getPassword().equals("")) {
            userService.getUser().setPassword(param.getPassword());
        }
        userService.getUser().setNickName(param.getNickName());
        userService.save();
        return ResUtils.success();
    }

    @Path("/setting")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getSetting(@CookieParam("uuid") String uuid) {
        if (!authService.auth(uuid)) {
            return ResUtils.failure("No Authenticate");
        }
        return ResUtils.success(userService.getUser());

    }

    @Path("/setting")
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public Response changeSetting(@CookieParam("uuid") String uuid, IndexConf indexConf) {
        if (!authService.auth(uuid)) {
            return ResUtils.failure("No Authenticate");
        } else {
//            confService.setIndexConf(indexConf);
//            confService.saveIndexConf();
//            pageService.updateCoverPage();
//            pageService.updateIndexPage();
            return ResUtils.success(indexConf);
        }
    }

    @POST
    @Path("/images/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(MultipartFormDataInput input) throws IOException {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        // Get file data to save
        List<InputPart> inputParts = uploadForm.get("file_data");

        for (InputPart inputPart : inputParts) {
            try {
                // convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                OutputStream outputStream = new FileOutputStream(new File(confService.getWorkerPath() + "/img/avatar.jpg"));
                inputStream.transferTo(outputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResUtils.success();
    }

}
