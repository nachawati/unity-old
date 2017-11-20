package io.dgms.unity.server.controllers;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.PathSegment;

import org.glassfish.jersey.server.mvc.Viewable;

import io.dgms.unity.api.DGCommit;
import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGProject;
import io.dgms.unity.api.DGWorkspace;
import io.dgms.unity.server.Controller;

public class WorkspaceController extends Controller
{
    private final DGWorkspace workspace;

    public WorkspaceController(Controller parent, DGWorkspace workspace)
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
    public Controller getResource(@PathParam("path") final PathSegment path) throws DGException
    {
        final DGWorkspace subWorkspace = workspace.getSubWorkspace(path.getPath());
        if (subWorkspace != null)
            return new WorkspaceController(this, subWorkspace);
        final DGProject project = workspace.getProject(path.getPath());
        if (project == null)
            throw new WebApplicationException(404);
        final Set<String> params = path.getMatrixParameters().keySet();
        if (params.isEmpty())
            return new ProjectController(this, project);
        final String ref = params.iterator().next();
        final DGCommit commit = project.getRepository().getCommit(ref);
        if (commit == null)
            throw new WebApplicationException(404);
        return new ProjectController(this, project, commit, ref);
    }

    public DGWorkspace getWorkspace()
    {
        return workspace;
    }
}
