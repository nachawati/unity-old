/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.server;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.TracingConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

import io.dgms.unity.server.system.ServerDGSystem;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class Application extends ResourceConfig
{
    /**
     *
     */
    public Application()
    {
        packages(ServerDGSystem.class.getPackage().getName());
        this.register(JspMvcFeature.class);
        register(LoggingFeature.class);
        property(ServerProperties.TRACING, TracingConfig.ON_DEMAND.name());
    }
}
