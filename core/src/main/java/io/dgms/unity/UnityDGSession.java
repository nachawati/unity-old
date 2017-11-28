/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.User;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGSession;
import io.dgms.unity.registry.UnityDGPackageReference;
import io.dgms.unity.registry.UnityDGRegistry;
import io.dgms.unity.runner.UnityDGTaskRunner;
import io.dgms.unity.system.UnityDGSystem;
import io.dgms.unity.system.UnityDGUser;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDGSession implements DGSession
{
    /**
     *
     */
    protected final GitLabApi     api;

    /**
     *
     */
    protected final URI           gitLabHost;

    /**
     *
     */
    private final String          privateToken;

    /**
     *
     */
    private final UnityDGRegistry registry;

    /**
     *
     */
    private final UnityDGSystem   system;

    /**
     *
     */
    private UnityDGUser           user;

    /**
     * @param privateToken
     */
    public UnityDGSession(String privateToken)
    {
        this(URI.create("https://git.dgms.io/"), privateToken);
    }

    /**
     * @param username
     * @param password
     * @throws DGException
     */
    public UnityDGSession(String username, String password) throws DGException
    {
        this(URI.create("https://git.dgms.io/"), username, password);
    }

    /**
     * @param gitLabHost
     * @param privateToken
     */
    public UnityDGSession(URI gitLabHost, String privateToken)
    {
        this.gitLabHost = gitLabHost;
        this.privateToken = privateToken;
        api = new GitLabApi(gitLabHost.toString(), privateToken);
        system = new UnityDGSystem(this);
        registry = new UnityDGRegistry(this);
    }

    /**
     * @param gitLabHost
     * @param username
     * @param password
     * @throws DGException
     */
    public UnityDGSession(URI gitLabHost, String username, String password) throws DGException
    {
        try {
            this.gitLabHost = gitLabHost;
            api = GitLabApi.login(gitLabHost.toString(), username, password);
            privateToken = api.getSession().getPrivateToken();
            system = new UnityDGSystem(this);
            registry = new UnityDGRegistry(this);
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
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSession#getPackageReference(java.lang.String,
     * java.lang.String)
     */
    @Override
    public UnityDGPackageReference getPackageReference(String name, String version)
    {
        return new UnityDGPackageReference(name, version);
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
    public UnityDGRegistry getRegistry()
    {
        return registry;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSession#getSystem()
     */
    @Override
    public UnityDGSystem getSystem()
    {
        return system;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSession#getUser()
     */
    @Override
    public UnityDGUser getUser()
    {
        if (user != null)
            return user;
        synchronized (this) {
            try {
                return user = new UnityDGUser(this, api.getUserApi().getCurrentUser());
            } catch (final GitLabApiException e) {
                return user = new UnityDGUser(this, new User());
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
        return UnityDGSession.class.getPackage().getImplementationVersion();
    }

    /**
     *
     */
    public static final EntityManagerFactory factory;
    /**
     *
     */
    public static final UnityDGTaskRunner    runner;

    static {
        try {
            System.setProperty("derby.system.home", getLocalSystemPath().toString());
        } catch (final IOException e) {
        } finally {
            EntityManagerFactory emf = null;
            try {
                DriverManager.getConnection("jdbc:derby;shutdown=true;deregister=false");
            } catch (final Exception e) {
            }
            try {
                emf = Persistence.createEntityManagerFactory("unity");
            } catch (final Exception e) {
            } finally {
                factory = emf;
            }
            runner = new UnityDGTaskRunner(factory);
        }
    }

    /**
     * @return
     * @throws IOException
     */
    public static Path getLocalSystemPath() throws IOException
    {
        final Path settings = Paths.get(System.getProperty("user.home")).resolve(".dgms").toAbsolutePath();
        if (!Files.exists(settings))
            Files.createDirectories(settings);
        return settings;
    }
}
