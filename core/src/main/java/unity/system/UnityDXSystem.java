/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.system;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Stream;

import javax.persistence.Query;

import org.apache.commons.io.IOUtils;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;

import unity.UnityDXSession;
import unity.UnityDXSessionObject;
import unity.api.DXException;
import unity.api.DXPackageReference;
import unity.api.DXScriptEngine;
import unity.api.DXScriptEngineFactory;
import unity.api.DXSystem;
import unity.api.DXTaskExecutionStatus;
import unity.persistence.UnityDXEntityManager;
import unity.registry.UnityDXPackageReference;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXSystem extends UnityDXSessionObject implements DXSystem
{
    /**
     * @param session
     */
    public UnityDXSystem(UnityDXSession session)
    {
        super(session);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getEngineByExtension(java.lang.String)
     */
    @Override
    public DXScriptEngine getEngineByExtension(String extension)
    {
        for (final DXScriptEngineFactory engineFactory : engineFactories) {
            List<String> extensions = null;
            try {
                extensions = engineFactory.getExtensions();
            } catch (final Exception e) {
            } finally {
                if (extensions == null)
                    continue;
                for (final String ext : extensions)
                    if (extension.equals(ext))
                        try {
                            final DXScriptEngine engine = engineFactory.getScriptEngine(session);
                            // engine.setBindings(getBindings(), ScriptContext.GLOBAL_SCOPE);
                            return engine;
                        } catch (final Exception e) {
                        }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getEngineByMimeType(java.lang.String)
     */
    @Override
    public DXScriptEngine getEngineByMimeType(String mimeType)
    {
        for (final DXScriptEngineFactory engineFactory : engineFactories) {
            List<String> mimeTypes = null;
            try {
                mimeTypes = engineFactory.getMimeTypes();
            } catch (final Exception e) {
            } finally {
                if (mimeTypes == null)
                    continue;
                for (final String mime : mimeTypes)
                    if (mimeType.equals(mime))
                        try {
                            final DXScriptEngine engine = engineFactory.getScriptEngine(session);
                            // engine.setBindings(getBindings(), ScriptContext.GLOBAL_SCOPE);
                            return engine;
                        } catch (final Exception e) {
                        }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getEngineByName(java.lang.String)
     */
    @Override
    public DXScriptEngine getEngineByName(String shortName)
    {
        for (final DXScriptEngineFactory engineFactory : engineFactories) {
            List<String> names = null;
            try {
                names = engineFactory.getNames();
            } catch (final Exception e) {
            } finally {
                if (names == null)
                    continue;
                for (final String name : names)
                    if (shortName.equals(name))
                        try {
                            final DXScriptEngine engine = engineFactory.getScriptEngine(session);
                            // engine.setBindings(getBindings(), ScriptContext.GLOBAL_SCOPE);
                            return engine;
                        } catch (final Exception e) {
                        }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getEngineFactories()
     */
    @Override
    public List<DXScriptEngineFactory> getEngineFactories()
    {
        return Collections.unmodifiableList(engineFactories);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getProject(java.lang.Integer)
     */
    @Override
    public UnityDXProject getProject(Integer projectId)
    {
        try {
            final Project object = api().getProjectApi().getProject(projectId);
            return new UnityDXProject(getSession(), object);
        } catch (final GitLabApiException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getProject(java.lang.String)
     */
    @Override
    public UnityDXProject getProject(String path)
    {
        try {
            final int off = path.lastIndexOf("/");
            final String namespace = path.substring(0, off);
            final String project = path.substring(off + 1);
            final Project object = api().getProjectApi().getProject(namespace, project);
            return new UnityDXProject(getSession(), object);
        } catch (final GitLabApiException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getProjects()
     */
    @Override
    public Stream<UnityDXProject> getProjects()
    {
        try {
            return api().getProjectApi().getProjects().stream().map(p -> new UnityDXProject(getSession(), p));
        } catch (final GitLabApiException e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getTaskExecution(java.lang.Long)
     */
    @Override
    public UnityDXTaskExecution getTaskExecution(Long taskExecutionId)
    {
        try (UnityDXEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDXTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return (UnityDXTaskExecution) query.getSingleResult();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getTaskExecutionError(java.lang.Long)
     */
    @Override
    public String getTaskExecutionError(Long taskExecutionId)
    {
        try (UnityDXEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDXTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return ((UnityDXTaskExecution) query.getSingleResult()).getStandardError();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getTaskExecutionInput(java.lang.Long)
     */
    @Override
    public String getTaskExecutionInput(Long taskExecutionId)
    {
        try (UnityDXEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDXTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return ((UnityDXTask) query.getSingleResult()).getStandardInput();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getTaskExecutionOutput(java.lang.Long)
     */
    @Override
    public String getTaskExecutionOutput(Long taskExecutionId)
    {
        try (UnityDXEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDXTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return ((UnityDXTaskExecution) query.getSingleResult()).getStandardOutput();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getTaskExecutionResult(java.lang.Long)
     */
    @Override
    public String getTaskExecutionResult(Long taskExecutionId)
    {
        try (UnityDXEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDXTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return ((UnityDXTaskExecution) query.getSingleResult()).getResult();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getTaskExecutions()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Stream<UnityDXTaskExecution> getTaskExecutions()
    {
        try (UnityDXEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDXTaskExecution e");
            return query.getResultStream();
        } catch (final Exception e) {
            return Stream.empty();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Stream<UnityDXTaskExecution> getTaskExecutions(String path)
    {
        try (UnityDXEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDXTaskExecution e WHERE e.task.taskName = :path");
            query.setParameter("path", path);
            return query.getResultStream();
        } catch (final Exception e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
<<<<<<< HEAD:core/src/main/java/io/dgms/unity/system/UnityDGSystem.java
     * @see io.dgms.unity.api.DGSystem#getTaskExecutions()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Stream<UnityDGTaskExecution> getTaskExecutions(String path)
    {
        try (UnityDGEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDGTaskExecution e WHERE e.task.taskName = :path");
            query.setParameter("path", path);
            return query.getResultStream();
        } catch (final Exception e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutionScript(java.lang.Long)
=======
     * @see unity.api.DXSystem#getTaskExecutionScript(java.lang.Long)
>>>>>>> 5f276a37a85e21b845cc9ede283e805ba8685565:core/src/main/java/unity/system/UnityDXSystem.java
     */
    @Override
    public String getTaskExecutionScript(Long taskExecutionId)
    {
        try (UnityDXEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDXTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return ((UnityDXTask) query.getSingleResult()).getScript();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getTaskExecutionStatus(java.lang.Long)
     */
    @Override
    public DXTaskExecutionStatus getTaskExecutionStatus(Long taskExecutionId)
    {
        try (UnityDXEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDXTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return ((UnityDXTaskExecution) query.getSingleResult()).getStatus();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getUser(java.lang.Integer)
     */
    @Override
    public UnityDXUser getUser(Integer userId)
    {
        try {
            return new UnityDXUser(getSession(), api().getUserApi().getUser(userId));
        } catch (final GitLabApiException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getUser(java.lang.String)
     */
    @Override
    public UnityDXUser getUser(String username)
    {
        try {
            return new UnityDXUser(getSession(), api().getUserApi().getUser(username));
        } catch (final GitLabApiException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getUsers()
     */
    @Override
    public Stream<UnityDXUser> getUsers()
    {
        try {
            return api().getUserApi().getUsers().stream().map(u -> new UnityDXUser(getSession(), u));
        } catch (final GitLabApiException e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getWorkspace(java.lang.Integer)
     */
    @Override
    public UnityDXWorkspace getWorkspace(Integer workspaceId)
    {
        try {
            return new UnityDXWorkspace(getSession(), api().getGroupApi().getGroup(workspaceId));
        } catch (final GitLabApiException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getWorkspace(java.lang.String)
     */
    @Override
    public UnityDXWorkspace getWorkspace(String path)
    {
        try {
            return new UnityDXWorkspace(getSession(), api().getGroupApi().getGroup(path));
        } catch (final GitLabApiException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getWorkspaces()
     */
    @Override
    public Stream<UnityDXWorkspace> getWorkspaces()
    {
        try {
            return api().getGroupApi().getGroups().stream().map(g -> new UnityDXWorkspace(getSession(), g));
        } catch (final GitLabApiException e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#newProject(java.lang.String)
     */
    @Override
    public UnityDXProject instantiateProject(String projectName) throws DXException
    {
        try {
            return new UnityDXProject(getSession(), api().getProjectApi().createProject(projectName));
        } catch (final GitLabApiException e) {
            throw new DXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#killTask(java.lang.Long)
     */
    @Override
    public void killTask(Long taskId) throws DXException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#submitTask(java.lang.String, java.io.Reader)
     */
    @Override
    public UnityDXTaskExecution submitTask(String name, Reader reader) throws DXException, IOException
    {
        return submitTask(name, IOUtils.toString(reader));
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#submitTask(java.lang.String, java.io.Reader,
     * unity.api.DXPackageReference)
     */
    @Override
    public UnityDXTaskExecution submitTask(String name, Reader reader, DXPackageReference packageReference)
            throws DXException, IOException
    {
        return submitTask(name, IOUtils.toString(reader), packageReference);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#submitTask(java.lang.String, java.lang.String)
     */
    @Override
    public UnityDXTaskExecution submitTask(String name, String script) throws DXException
    {
        return submitTask(name, script, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#submitTask(java.lang.String, java.lang.String,
     * unity.api.DXPackageReference)
     */
    @Override
    public UnityDXTaskExecution submitTask(String name, String script, DXPackageReference packageReference)
            throws DXException
    {
        return UnityDXSession.runner.submit(getSession().getUser().getId(), getSession().getPrivateToken(), name,
                script, (UnityDXPackageReference) packageReference);
    }

    /**
     *
     */
    private static final List<DXScriptEngineFactory> engineFactories = new LinkedList<>();

    static {
        final ServiceLoader<DXScriptEngineFactory> sl = ServiceLoader.load(DXScriptEngineFactory.class);
        final Iterator<DXScriptEngineFactory> factories = sl.iterator();
        for (; factories.hasNext();)
            engineFactories.add(factories.next());
    }

    /**
     * @param extension
     * @return
     */
    public static DXScriptEngine getLocalEngineByExtension(String extension)
    {
        for (final DXScriptEngineFactory engineFactory : engineFactories) {
            List<String> extensions = null;
            try {
                extensions = engineFactory.getExtensions();
            } catch (final Exception e) {
            } finally {
                if (extensions == null)
                    continue;
                for (final String ext : extensions)
                    if (extension.equals(ext))
                        try {
                            final DXScriptEngine engine = engineFactory.getScriptEngine();
                            // engine.setBindings(getBindings(), ScriptContext.GLOBAL_SCOPE);
                            return engine;
                        } catch (final Exception e) {
                        }
            }
        }
        return null;
    }

    /**
     * @param mimeType
     * @return
     */
    public static DXScriptEngine getLocalEngineByMimeType(String mimeType)
    {
        for (final DXScriptEngineFactory engineFactory : engineFactories) {
            List<String> mimeTypes = null;
            try {
                mimeTypes = engineFactory.getMimeTypes();
            } catch (final Exception e) {
            } finally {
                if (mimeTypes == null)
                    continue;
                for (final String mime : mimeTypes)
                    if (mimeType.equals(mime))
                        try {
                            final DXScriptEngine engine = engineFactory.getScriptEngine();
                            // engine.setBindings(getBindings(), ScriptContext.GLOBAL_SCOPE);
                            return engine;
                        } catch (final Exception e) {
                        }
            }
        }
        return null;
    }

    /**
     * @param shortName
     * @return
     */
    public static DXScriptEngine getLocalEngineByName(String shortName)
    {
        for (final DXScriptEngineFactory engineFactory : engineFactories) {
            List<String> names = null;
            try {
                names = engineFactory.getNames();
            } catch (final Exception e) {
            } finally {
                if (names == null)
                    continue;
                for (final String name : names)
                    if (shortName.equals(name))
                        try {
                            final DXScriptEngine engine = engineFactory.getScriptEngine();
                            // engine.setBindings(getBindings(), ScriptContext.GLOBAL_SCOPE);
                            return engine;
                        } catch (final Exception e) {
                        }
            }
        }
        return null;
    }
}
