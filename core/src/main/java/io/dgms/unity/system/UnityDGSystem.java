/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.system;

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

import io.dgms.unity.UnityDGSession;
import io.dgms.unity.UnityDGSessionObject;
import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGPackageReference;
import io.dgms.unity.api.DGScriptEngine;
import io.dgms.unity.api.DGScriptEngineFactory;
import io.dgms.unity.api.DGSystem;
import io.dgms.unity.api.DGTaskExecutionStatus;
import io.dgms.unity.persistence.UnityDGEntityManager;
import io.dgms.unity.registry.UnityDGPackageReference;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDGSystem extends UnityDGSessionObject implements DGSystem
{
    /**
     * @param session
     */
    public UnityDGSystem(UnityDGSession session)
    {
        super(session);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getEngineByExtension(java.lang.String)
     */
    @Override
    public DGScriptEngine getEngineByExtension(String extension)
    {
        for (final DGScriptEngineFactory engineFactory : engineFactories) {
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
                            final DGScriptEngine engine = engineFactory.getScriptEngine(session);
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
     * @see io.dgms.unity.api.DGSystem#getEngineByMimeType(java.lang.String)
     */
    @Override
    public DGScriptEngine getEngineByMimeType(String mimeType)
    {
        for (final DGScriptEngineFactory engineFactory : engineFactories) {
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
                            final DGScriptEngine engine = engineFactory.getScriptEngine(session);
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
     * @see io.dgms.unity.api.DGSystem#getEngineByName(java.lang.String)
     */
    @Override
    public DGScriptEngine getEngineByName(String shortName)
    {
        for (final DGScriptEngineFactory engineFactory : engineFactories) {
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
                            final DGScriptEngine engine = engineFactory.getScriptEngine(session);
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
     * @see io.dgms.unity.api.DGSystem#getEngineFactories()
     */
    @Override
    public List<DGScriptEngineFactory> getEngineFactories()
    {
        return Collections.unmodifiableList(engineFactories);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getProject(java.lang.Integer)
     */
    @Override
    public UnityDGProject getProject(Integer projectId)
    {
        try {
            final Project object = api().getProjectApi().getProject(projectId);
            return new UnityDGProject(getSession(), object);
        } catch (final GitLabApiException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getProject(java.lang.String)
     */
    @Override
    public UnityDGProject getProject(String path)
    {
        try {
            final int off = path.lastIndexOf("/");
            final String namespace = path.substring(0, off);
            final String project = path.substring(off + 1);
            final Project object = api().getProjectApi().getProject(namespace, project);
            return new UnityDGProject(getSession(), object);
        } catch (final GitLabApiException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getProjects()
     */
    @Override
    public Stream<UnityDGProject> getProjects()
    {
        try {
            return api().getProjectApi().getProjects().stream().map(p -> new UnityDGProject(getSession(), p));
        } catch (final GitLabApiException e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecution(java.lang.Long)
     */
    @Override
    public UnityDGTaskExecution getTaskExecution(Long taskExecutionId)
    {
        try (UnityDGEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDGTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return (UnityDGTaskExecution) query.getSingleResult();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutionError(java.lang.Long)
     */
    @Override
    public String getTaskExecutionError(Long taskExecutionId)
    {
        try (UnityDGEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDGTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return ((UnityDGTaskExecution) query.getSingleResult()).getStandardError();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutionInput(java.lang.Long)
     */
    @Override
    public String getTaskExecutionInput(Long taskExecutionId)
    {
        try (UnityDGEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDGTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return ((UnityDGTask) query.getSingleResult()).getStandardInput();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutionOutput(java.lang.Long)
     */
    @Override
    public String getTaskExecutionOutput(Long taskExecutionId)
    {
        try (UnityDGEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDGTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return ((UnityDGTaskExecution) query.getSingleResult()).getStandardOutput();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutionResult(java.lang.Long)
     */
    @Override
    public String getTaskExecutionResult(Long taskExecutionId)
    {
        try (UnityDGEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDGTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return ((UnityDGTaskExecution) query.getSingleResult()).getResult();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutions()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Stream<UnityDGTaskExecution> getTaskExecutions()
    {
        try (UnityDGEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDGTaskExecution e");
            return query.getResultStream();
        } catch (final Exception e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
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
     */
    @Override
    public String getTaskExecutionScript(Long taskExecutionId)
    {
        try (UnityDGEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDGTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return ((UnityDGTask) query.getSingleResult()).getScript();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutionStatus(java.lang.Long)
     */
    @Override
    public DGTaskExecutionStatus getTaskExecutionStatus(Long taskExecutionId)
    {
        try (UnityDGEntityManager em = newEntityManager()) {
            final Query query = em.createQuery("SELECT e FROM UnityDGTaskExecution e WHERE e.id = :taskExecutionId");
            query.setParameter("taskExecutionId", taskExecutionId);
            return ((UnityDGTaskExecution) query.getSingleResult()).getStatus();
        } catch (final Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getUser(java.lang.Integer)
     */
    @Override
    public UnityDGUser getUser(Integer userId)
    {
        try {
            return new UnityDGUser(getSession(), api().getUserApi().getUser(userId));
        } catch (final GitLabApiException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getUser(java.lang.String)
     */
    @Override
    public UnityDGUser getUser(String username)
    {
        try {
            return new UnityDGUser(getSession(), api().getUserApi().getUser(username));
        } catch (final GitLabApiException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getUsers()
     */
    @Override
    public Stream<UnityDGUser> getUsers()
    {
        try {
            return api().getUserApi().getUsers().stream().map(u -> new UnityDGUser(getSession(), u));
        } catch (final GitLabApiException e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getWorkspace(java.lang.Integer)
     */
    @Override
    public UnityDGWorkspace getWorkspace(Integer workspaceId)
    {
        try {
            return new UnityDGWorkspace(getSession(), api().getGroupApi().getGroup(workspaceId));
        } catch (final GitLabApiException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getWorkspace(java.lang.String)
     */
    @Override
    public UnityDGWorkspace getWorkspace(String path)
    {
        try {
            return new UnityDGWorkspace(getSession(), api().getGroupApi().getGroup(path));
        } catch (final GitLabApiException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getWorkspaces()
     */
    @Override
    public Stream<UnityDGWorkspace> getWorkspaces()
    {
        try {
            return api().getGroupApi().getGroups().stream().map(g -> new UnityDGWorkspace(getSession(), g));
        } catch (final GitLabApiException e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#newProject(java.lang.String)
     */
    @Override
    public UnityDGProject instantiateProject(String projectName) throws DGException
    {
        try {
            return new UnityDGProject(getSession(), api().getProjectApi().createProject(projectName));
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#killTask(java.lang.Long)
     */
    @Override
    public void killTask(Long taskId) throws DGException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#submitTask(java.lang.String, java.io.Reader)
     */
    @Override
    public UnityDGTaskExecution submitTask(String name, Reader reader) throws DGException, IOException
    {
        return submitTask(name, IOUtils.toString(reader));
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#submitTask(java.lang.String, java.io.Reader,
     * io.dgms.unity.api.DGPackageReference)
     */
    @Override
    public UnityDGTaskExecution submitTask(String name, Reader reader, DGPackageReference packageReference)
            throws DGException, IOException
    {
        return submitTask(name, IOUtils.toString(reader), packageReference);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#submitTask(java.lang.String,
     * java.lang.String)
     */
    @Override
    public UnityDGTaskExecution submitTask(String name, String script) throws DGException
    {
        return submitTask(name, script, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#submitTask(java.lang.String,
     * java.lang.String, io.dgms.unity.api.DGPackageReference)
     */
    @Override
    public UnityDGTaskExecution submitTask(String name, String script, DGPackageReference packageReference)
            throws DGException
    {
        return UnityDGSession.runner.submit(getSession().getUser().getId(), getSession().getPrivateToken(), name,
                script, (UnityDGPackageReference) packageReference);
    }

    /**
     *
     */
    private static final List<DGScriptEngineFactory> engineFactories = new LinkedList<>();

    static {
        final ServiceLoader<DGScriptEngineFactory> sl = ServiceLoader.load(DGScriptEngineFactory.class);
        final Iterator<DGScriptEngineFactory> factories = sl.iterator();
        for (; factories.hasNext();)
            engineFactories.add(factories.next());
    }

    /**
     * @param extension
     * @return
     */
    public static DGScriptEngine getLocalEngineByExtension(String extension)
    {
        for (final DGScriptEngineFactory engineFactory : engineFactories) {
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
                            final DGScriptEngine engine = engineFactory.getScriptEngine();
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
    public static DGScriptEngine getLocalEngineByMimeType(String mimeType)
    {
        for (final DGScriptEngineFactory engineFactory : engineFactories) {
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
                            final DGScriptEngine engine = engineFactory.getScriptEngine();
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
    public static DGScriptEngine getLocalEngineByName(String shortName)
    {
        for (final DGScriptEngineFactory engineFactory : engineFactories) {
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
                            final DGScriptEngine engine = engineFactory.getScriptEngine();
                            // engine.setBindings(getBindings(), ScriptContext.GLOBAL_SCOPE);
                            return engine;
                        } catch (final Exception e) {
                        }
            }
        }
        return null;
    }
}
