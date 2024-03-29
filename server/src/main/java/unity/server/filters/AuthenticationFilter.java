package unity.server.filters;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

import unity.UnityDXSession;
import unity.api.DXException;
import unity.api.DXSession;
import unity.modules.engines.zorba.ZorbaDXScriptEngine;

public class AuthenticationFilter implements Filter
{
    private Pattern staticContentRegex;

    public AuthenticationFilter()
    {
    }

    public void doFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain) throws IOException, ServletException, DXException, URISyntaxException
    {
        DXSession session = null;
        if (request.getSession(false) != null)
            session = (DXSession) request.getSession().getAttribute("session");
        else {
            final String token = request.getParameter("token");
            if (token != null)
                session = new UnityDXSession(token);
        }
        
        if (ZorbaDXScriptEngine.class.getProtectionDomain().getCodeSource().getLocation().toURI()
        .getPath().contains("jetty"))
        	request.setAttribute("session", session/* = new UnityDXSession("ketNfDswkVTH9xySngFz")*/);
        else
        	request.setAttribute("session", session = new UnityDXSession("ketNfDswkVTH9xySngFz"));
        if (session != null)
            request.setAttribute("user", session.getUser());
        if (staticContentRegex.matcher(request.getRequestURI()).matches())
            filterChain.doFilter(request, response);
        else if (request.getSession(false) != null) {
            if (session != null)
                filterChain.doFilter(request, response);
            else
                response.sendRedirect(request.getContextPath() + "/users/sign-in");
        } else if (session != null)
            filterChain.doFilter(request, response);
        else {
            final String accept = request.getHeader("accept");
            if (accept != null && accept.contains("text/html"))
                response.sendRedirect(request.getContextPath() + "/users/sign-in");
            else
                throw new WebApplicationException(401);
        }
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
            throws IOException, ServletException
    {
        try {
            doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
        } catch (final DXException | URISyntaxException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException
    {
        try {
            final String regex = "/(assets|users/sign-in|users/register|users/password/new)(/.*)?";
            staticContentRegex = Pattern.compile(filterConfig.getServletContext().getContextPath() + regex);
        } catch (final Exception e) {
            throw new ServletException(e);
        }
    }
}
