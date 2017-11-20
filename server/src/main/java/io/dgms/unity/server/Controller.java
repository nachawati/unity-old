package io.dgms.unity.server;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import io.dgms.unity.api.DGSession;
import io.dgms.unity.api.DGSystem;

public abstract class Controller
{
    private final Controller    parent;
    @Context
    private HttpServletRequest  request;
    @Context
    private HttpServletResponse response;

    public Controller()
    {
        parent = null;
    }

    public Controller(final Controller parent)
    {
        this.parent = parent;
        request = parent.request;
        response = parent.response;
    }

    public Controller getParent()
    {
        return parent;
    }

    public String getPath()
    {
        return null;
    }

    public LinkedHashMap<String, String> getPathComponents()
    {
        if (getPath() == null)
            return null;
        final StringBuilder sb = new StringBuilder();
        final LinkedHashMap<String, String> pathComponents = new LinkedHashMap<>();
        sb.append(getRequest().getContextPath());
        for (final String pathComponent : getPath().split("/")) {
            sb.append("/" + pathComponent);
            pathComponents.put(pathComponent, sb.toString());
        }
        return pathComponents;
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }

    public HttpServletResponse getResponse()
    {
        return response;
    }

    public DGSession getSession()
    {
        return (DGSession) request.getAttribute("session");
    }

    public DGSystem getSystem()
    {
        final DGSession session = getSession();
        if (session != null)
            return session.getSystem();
        return null;
    }

    public String getVersion()
    {
        return "0.1.0-SNAPSHOT";
    }
}
