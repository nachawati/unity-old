/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.client;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.User;

import unity.api.DXException;
import unity.api.DXRegistry;
import unity.api.DXSession;
import unity.client.util.GsonJerseyProvider;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDXSession implements DXSession
{
    /**
     *
     */
    protected final GitLabApi      api;

    /**
     *
     */
    protected final Client         client = ClientBuilder.newClient().register(GsonJerseyProvider.class);

    /**
     *
     */
    protected final URI            gitLabHost;

    /**
     *
     */
    protected final URI            host;

    /**
     *
     */
    private final String           privateToken;

    /**
     *
     */
    private final ClientDXRegistry registry;

    /**
     *
     */
    private final ClientDXSystem   system;

    /**
     *
     */
    private ClientDXUser           user;

    /**
     * @param privateToken
     */
    public ClientDXSession(String privateToken)
    {
        this(URI.create("https://dgms.io/"), privateToken);
    }

    /**
     * @param username
     * @param password
     * @throws DXException
     */
    public ClientDXSession(String username, String password) throws DXException
    {
        this(URI.create("https://dgms.io/"), username, password);
    }

    /**
     * @param host
     * @param privateToken
     */
    public ClientDXSession(URI host, String privateToken)
    {
        this(host, resolveGitLabHost(host), privateToken);
    }

    /**
     * @param host
     * @param username
     * @param password
     * @throws DXException
     */
    public ClientDXSession(URI host, String username, String password) throws DXException
    {
        this(host, resolveGitLabHost(host), username, password);
    }

    /**
     * @param host
     * @param gitLabHost
     * @param privateToken
     */
    public ClientDXSession(URI host, URI gitLabHost, String privateToken)
    {
        this.host = host;
        this.gitLabHost = gitLabHost;
        this.privateToken = privateToken;
        api = new GitLabApi(gitLabHost.toString(), privateToken);
        system = new ClientDXSystem(this);
        registry = new ClientDXRegistry(this);
    }

    /**
     * @param host
     * @param gitLabHost
     * @param username
     * @param password
     * @throws DXException
     */
    public ClientDXSession(URI host, URI gitLabHost, String username, String password) throws DXException
    {
        try {
            this.host = host;
            this.gitLabHost = gitLabHost;
            api = GitLabApi.login(gitLabHost.toString(), username, password);
            privateToken = api.getSession().getPrivateToken();
            system = new ClientDXSystem(this);
            registry = new ClientDXRegistry(this);
        } catch (final GitLabApiException e) {
            throw new DXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSession#getGitLabHost()
     */
    @Override
    public URI getGitLabHost()
    {
        return gitLabHost;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSession#getHost()
     */
    @Override
    public URI getHost()
    {
        return host;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSession#getPackageReference(java.lang.String,
     * java.lang.String)
     */
    @Override
    public ClientDXPackageReference getPackageReference(String name, String version)
    {
        return new ClientDXPackageReference(name, version);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSession#getPrivateToken()
     */
    @Override
    public String getPrivateToken()
    {
        return privateToken;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSession#getRegistry()
     */
    @Override
    public DXRegistry getRegistry()
    {
        return registry;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSession#getSystem()
     */
    @Override
    public ClientDXSystem getSystem()
    {
        return system;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSession#getUser()
     */
    @Override
    public ClientDXUser getUser()
    {
        if (user != null)
            return user;
        synchronized (this) {
            try {
                return user = new ClientDXUser(this, api.getUserApi().getCurrentUser());
            } catch (final GitLabApiException e) {
                return user = new ClientDXUser(this, new User());
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSession#getVersion()
     */
    @Override
    public String getVersion()
    {
        return ClientDXSession.class.getPackage().getImplementationVersion();
    }

    /**
     * @param host
     * @return URI
     */
    private static URI resolveGitLabHost(URI host)
    {
        final String hostString = host.toString();
        if (hostString.endsWith("/"))
            return host.resolve("gitlab/");
        return URI.create(hostString + "/gitlab/");
    }
}
