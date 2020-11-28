package run.yuyang.trotsky.resource;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import run.yuyang.trotsky.commom.utils.ResUtils;
import run.yuyang.trotsky.model.conf.IndexConf;
import run.yuyang.trotsky.model.conf.UserConf;
import run.yuyang.trotsky.model.request.InfoParam;
import run.yuyang.trotsky.model.request.LoginParam;
import run.yuyang.trotsky.model.response.UserInfo;
import run.yuyang.trotsky.service.ConfService;
import run.yuyang.trotsky.service.PageService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.*;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
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

    @Inject
    PageService pageService;

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
    @Path("/info/nickName")
    @Produces(MediaType.APPLICATION_JSON)
    public Response nickName(@CookieParam("uuid") String uuid) throws ParseException {
        if (null == uuid || "".equals(uuid) || !uuid.equals(confService.getUUID())) {
            return ResUtils.failure("No Authenticate");
        } else {
            return ResUtils.success(confService.getUserConf().getNickName());
        }
    }

    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response info(@CookieParam("uuid") String uuid) throws ParseException {
        if (null == uuid || "".equals(uuid) || !uuid.equals(confService.getUUID())) {
            return ResUtils.failure("No Authenticate");
        } else {
            UserInfo info = UserInfo.builder()
                    .nickName(confService.getUserConf().getNickName())
                    .email(confService.getUserConf().getEmail())
                    .build();
            return ResUtils.success(info);
        }
    }


    @PUT
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response info(@CookieParam("uuid") String uuid, InfoParam param) throws ParseException {
        if (null == uuid || "".equals(uuid) || !uuid.equals(confService.getUUID())) {
            return ResUtils.failure("No Authenticate");
        } else {
            if (!param.getPassword().equals("")) {
                confService.getUserConf().setPassword(param.getPassword());
            }
            confService.getUserConf().setNickName(param.getNickName());
            confService.saveUserConf();
            return ResUtils.success();
        }
    }

    @Path("/setting")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getSetting(@CookieParam("uuid") String uuid) {
        if (null == uuid || "".equals(uuid) || !uuid.equals(confService.getUUID())) {
            return ResUtils.failure("No Authenticate");
        } else {
            return ResUtils.success(confService.getIndexConf());
        }
    }

    @Path("/setting")
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public Response changeSetting(@CookieParam("uuid") String uuid, IndexConf indexConf) {
        if (null == uuid || "".equals(uuid) || !uuid.equals(confService.getUUID())) {
            return ResUtils.failure("No Authenticate");
        } else {
            confService.setIndexConf(indexConf);
            confService.saveIndexConf();
            pageService.updateCoverPage();
            pageService.updateIndexPage();
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
