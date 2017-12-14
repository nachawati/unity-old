package unity.server.controllers;

import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Viewable;

import unity.UnityDXSession;
import unity.api.DXUser;
import unity.api.DXWorkspace;
import unity.server.Controller;

@Path("/")
@Produces({ MediaType.TEXT_HTML })
public class SystemController extends Controller
{
    public SystemController()
    {
    }

    @POST
    @Path("users/sign-in")
    public void doSignIn(@FormParam("username") final String username, @FormParam("password") final String password)
    {
        try {
            final HttpSession session = getRequest().getSession();
            session.setAttribute("session", new UnityDXSession(username, password));
            getResponse().sendRedirect(getRequest().getContextPath() + "/" + username);
        } catch (final Exception e) {
            throw new WebApplicationException(500);
        }
    }

    @GET
    @Path("users/sign-out")
    public void doSignOut()
    {
        try {
            final HttpSession session = getRequest().getSession(false);
            if (session != null)
                session.invalidate();
            getResponse().sendRedirect(getRequest().getContextPath());
        } catch (final Exception e) {
            throw new WebApplicationException(500);
        }
    }

    @GET
    public Viewable getIndex()
    {
        return new Viewable("index", this);
    }

    @Path("{path}")
    public Controller getResource(@PathParam("path") final String path)
    {
        final DXWorkspace workspace = getSystem().getWorkspace(path);
        if (workspace != null)
            return new WorkspaceController(this, workspace);
        final DXUser user = getSystem().getUser(path);
        if (user != null)
            return new UserController(this, user);
        throw new WebApplicationException(404);
    }

    @GET
    @Path("users/sign-in")
    public Viewable getSignIn()
    {
        return new Viewable("sign-in", this);
    }
}
