package run.yuyang.trotsky.resource;

import run.yuyang.trotsky.service.impl.PageServiceImpl;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * @author YuYang
 */
@Path("/admin/page")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PageResource {

    @Inject
    PageServiceImpl pageService;

    @GET
    @Path("/notes")
    @Produces(MediaType.TEXT_PLAIN)
    public String randomNotesPage() {
        String notesTree = pageService.updateNotesPage();
        System.out.println(notesTree);
        return notesTree;
    }

    @GET
    @Path("/auto")
    @Produces(MediaType.APPLICATION_JSON)
    public Map auto() {
        return pageService.autoGeneratedConf();
    }

}
