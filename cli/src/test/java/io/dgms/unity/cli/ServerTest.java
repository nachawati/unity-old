package io.dgms.unity.cli;

import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Test;

public class ServerTest
{
    @Test
    public void test() throws Exception
    {
        final org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(8080);

        // Configure webapp provided as external WAR
        final WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setExtraClasspath("C:\\Users\\Omar\\Documents\\GitHub\\unity\\target\\windows-msvc-x86_64\\lib");
        webapp.setWar("C:\\Users\\Omar\\Documents\\GitHub\\unity\\target\\windows-msvc-x86_64\\server");
        server.setHandler(webapp);

        // Start the server
        server.start();
        server.join();
    }
}
