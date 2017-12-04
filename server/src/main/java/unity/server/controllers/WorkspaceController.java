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
import unity.api.DXWorkspace;
import unity.server.Controller;

public class WorkspaceController extends Controller
{
    private final DXWorkspace workspace;

    public WorkspaceController(Controller parent, DXWorkspace workspace)
    {
        super(parent);
        this.workspace = workspace;
    }

    @GET
    public Viewable getIndexPage()
    {
        return new Viewable("index", this);
    }

    @Override
    public String getPath()
    {
        return workspace.getFullPath();
    }

    @Path("{path}")
    public Controller getResource(@PathParam("path") final PathSegment path) throws DXException
    {
        final DXWorkspace subWorkspace = workspace.getSubWorkspace(path.getPath());
        if (subWorkspace != null)
            return new WorkspaceController(this, subWorkspace);
        final DXProject project = workspace.getProject(path.getPath());
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

    public DXWorkspace getWorkspace()
    {
        return workspace;
    }
}
