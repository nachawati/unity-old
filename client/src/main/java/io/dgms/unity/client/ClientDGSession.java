/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.client;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.User;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGRegistry;
import io.dgms.unity.api.DGSession;
import io.dgms.unity.client.util.GsonJerseyProvider;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDGSession implements DGSession
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
    private final ClientDGRegistry registry;

    /**
     *
     */
    private final ClientDGSystem   system;

    /**
     *
     */
    private ClientDGUser           user;

    /**
     * @param privateToken
     */
    public ClientDGSession(String privateToken)
    {
        this(URI.create("https://dgms.io/"), privateToken);
    }

    /**
     * @param username
     * @param password
     * @throws DGException
     */
    public ClientDGSession(String username, String password) throws DGException
    {
        this(URI.create("https://dgms.io/"), username, password);
    }

    /**
     * @param host
     * @param privateToken
     */
    public ClientDGSession(URI host, String privateToken)
    {
        this(host, resolveGitLabHost(host), privateToken);
    }

    /**
     * @param host
     * @param username
     * @param password
     * @throws DGException
     */
    public ClientDGSession(URI host, String username, String password) throws DGException
    {
        this(host, resolveGitLabHost(host), username, password);
    }

    /**
     * @param host
     * @param gitLabHost
     * @param privateToken
     */
    public ClientDGSession(URI host, URI gitLabHost, String privateToken)
    {
        this.host = host;
        this.gitLabHost = gitLabHost;
        this.privateToken = privateToken;
        api = new GitLabApi(gitLabHost.toString(), privateToken);
        system = new ClientDGSystem(this);
        registry = new ClientDGRegistry(this);
    }

    /**
     * @param host
     * @param gitLabHost
     * @param username
     * @param password
     * @throws DGException
     */
    public ClientDGSession(URI host, URI gitLabHost, String username, String password) throws DGException
    {
        try {
            this.host = host;
            this.gitLabHost = gitLabHost;
            api = GitLabApi.login(gitLabHost.toString(), username, password);
            privateToken = api.getSession().getPrivateToken();
            system = new ClientDGSystem(this);
            registry = new ClientDGRegistry(this);
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSession#getGitLabHost()
     */
    @Override
    public URI getGitLabHost()
    {
        return gitLabHost;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSession#getHost()
     */
    @Override
    public URI getHost()
    {
        return host;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSession#getPackageReference(java.lang.String,
     * java.lang.String)
     */
    @Override
    public ClientDGPackageReference getPackageReference(String name, String version)
    {
        return new ClientDGPackageReference(name, version);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSession#getPrivateToken()
     */
    @Override
    public String getPrivateToken()
    {
        return privateToken;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSession#getRegistry()
     */
    @Override
    public DGRegistry getRegistry()
    {
        return registry;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSession#getSystem()
     */
    @Override
    public ClientDGSystem getSystem()
    {
        return system;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSession#getUser()
     */
    @Override
    public ClientDGUser getUser()
    {
        if (user != null)
            return user;
        synchronized (this) {
            try {
                return user = new ClientDGUser(this, api.getUserApi().getCurrentUser());
            } catch (final GitLabApiException e) {
                return user = new ClientDGUser(this, new User());
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSession#getVersion()
     */
    @Override
    public String getVersion()
    {
        return ClientDGSession.class.getPackage().getImplementationVersion();
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
