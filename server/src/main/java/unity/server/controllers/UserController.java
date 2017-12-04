package unity.server.controllers;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.PathSegment;

import org.glassfish.jersey.server.mvc.Viewable;

import unity.api.DXCommit;
import unity.api.DXException;
import unity.api.DXProject;
import unity.api.DXUser;
import unity.server.Controller;

public class UserController extends Controller
{
    private final DXUser user;

    public UserController(Controller parent, DXUser user)
    {
        super(parent);
        this.user = user;
    }

    @GET
    public Viewable getIndex()
    {
        return new Viewable("index", this);
    }

    @Override
    public String getPath()
    {
        return user.getUsername();
    }

    @Path("{path}")
    public Controller getResource(@PathParam("path") final PathSegment path) throws DXException
    {
        final DXProject project = user.getProject(path.getPath());
        if (project == null)
            throw new WebApplicationException(404);
        final Set<String> params = path.getMatrixParameters().keySet();
        if (params.isEmpty())
            return new ProjectController(this, project);
        final String ref = params.iterator().next();
        final DXCommit commit = project.getRepository().getCommit(ref);
        if (commit == null)
            throw new WebApplicationException(404);
        return new ProjectController(this, project, commit, ref);
    }

    public DXUser getUser()
    {
        return user;
    }
}
