package io.dgms.unity.server.system;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.server.mvc.Template;

@Path("/")
@Singleton
@Template
@Produces(MediaType.TEXT_HTML)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerDGSystem
{
    private final String name;

    public ServerDGSystem()
    {
        name = "Test123";
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    public ServerDGSystem doGet()
    {
        return this;
    }

    public String getName()
    {
        return name;
    }
}
