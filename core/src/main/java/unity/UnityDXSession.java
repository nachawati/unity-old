/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity;

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

import unity.api.DXException;
import unity.api.DXSession;
import unity.registry.UnityDXPackageReference;
import unity.registry.UnityDXRegistry;
import unity.runner.UnityDXTaskRunner;
import unity.system.UnityDXSystem;
import unity.system.UnityDXUser;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXSession implements DXSession
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
    private final UnityDXRegistry registry;

    /**
     *
     */
    private final UnityDXSystem   system;

    /**
     *
     */
    private UnityDXUser           user;

    /**
     * @param privateToken
     */
    public UnityDXSession(String privateToken)
    {
        this(URI.create("https://git.dgms.io/"), privateToken);
    }

    /**
     * @param username
     * @param password
     * @throws DXException
     */
    public UnityDXSession(String username, String password) throws DXException
    {
        this(URI.create("https://git.dgms.io/"), username, password);
    }

    /**
     * @param gitLabHost
     * @param privateToken
     */
    public UnityDXSession(URI gitLabHost, String privateToken)
    {
        this.gitLabHost = gitLabHost;
        this.privateToken = privateToken;
        api = new GitLabApi(gitLabHost.toString(), privateToken);
        system = new UnityDXSystem(this);
        registry = new UnityDXRegistry(this);
    }

    /**
     * @param gitLabHost
     * @param username
     * @param password
     * @throws DXException
     */
    public UnityDXSession(URI gitLabHost, String username, String password) throws DXException
    {
        try {
            this.gitLabHost = gitLabHost;
            api = GitLabApi.login(gitLabHost.toString(), username, password);
            privateToken = api.getSession().getPrivateToken();
            system = new UnityDXSystem(this);
            registry = new UnityDXRegistry(this);
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
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSession#getPackageReference(java.lang.String,
     * java.lang.String)
     */
    @Override
    public UnityDXPackageReference getPackageReference(String name, String version)
    {
        return new UnityDXPackageReference(name, version);
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
    public UnityDXRegistry getRegistry()
    {
        return registry;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSession#getSystem()
     */
    @Override
    public UnityDXSystem getSystem()
    {
        return system;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSession#getUser()
     */
    @Override
    public UnityDXUser getUser()
    {
        if (user != null)
            return user;
        synchronized (this) {
            try {
                return user = new UnityDXUser(this, api.getUserApi().getCurrentUser());
            } catch (final GitLabApiException e) {
                return user = new UnityDXUser(this, new User());
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
        return UnityDXSession.class.getPackage().getImplementationVersion();
    }

    /**
     *
     */
    public static final EntityManagerFactory factory;
    /**
     *
     */
    public static final UnityDXTaskRunner    runner;

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
            runner = new UnityDXTaskRunner(factory);
        }
    }

    /**
     * @return
     * @throws IOException
     */
    public static Path getLocalSystemPath() throws IOException
    {
        final Path settings = Paths.get(System.getProperty("user.home")).resolve(".unity").toAbsolutePath();
        if (!Files.exists(settings))
            Files.createDirectories(settings);
        return settings;
    }
}
