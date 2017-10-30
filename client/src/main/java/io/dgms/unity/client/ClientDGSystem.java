/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.client;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Stream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGPackageReference;
import io.dgms.unity.api.DGScriptEngine;
import io.dgms.unity.api.DGScriptEngineFactory;
import io.dgms.unity.api.DGSystem;
import io.dgms.unity.api.DGTaskExecution;
import io.dgms.unity.api.DGTaskExecutionStatus;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDGSystem extends ClientDGSessionObject implements DGSystem
{
    /**
     * @param session
     */
    public ClientDGSystem(ClientDGSession session)
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
                            final DGScriptEngine engine = engineFactory.getScriptEngine();
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
                            final DGScriptEngine engine = engineFactory.getScriptEngine();
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
                            final DGScriptEngine engine = engineFactory.getScriptEngine();
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
    public ClientDGProject getProject(Integer projectId) throws DGException
    {
        try {
            final Project object = api().getProjectApi().getProject(projectId);
            return new ClientDGProject(getSession(), object);
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getProject(java.lang.String)
     */
    @Override
    public ClientDGProject getProject(String path) throws DGException
    {
        try {
            final int off = path.lastIndexOf("/");
            final String namespace = path.substring(0, off);
            final String project = path.substring(off + 1);
            final Project object = api().getProjectApi().getProject(namespace, project);
            return new ClientDGProject(getSession(), object);
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getProjects()
     */
    @Override
    public Stream<ClientDGProject> getProjects() throws DGException
    {
        try {
            return api().getProjectApi().getProjects().stream().map(p -> new ClientDGProject(getSession(), p));
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecution(java.lang.Long)
     */
    @Override
    public DGTaskExecution getTaskExecution(Long taskExecutionId) throws DGException
    {
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId);
            final ClientDGTaskExecution taskExecution = target.request(MediaType.APPLICATION_JSON)
                    .get(ClientDGTaskExecution.class);
            taskExecution.session = getSession();
            return taskExecution;
        } catch (final IOException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutionError(java.lang.Long)
     */
    @Override
    public String getTaskExecutionError(Long taskExecutionId) throws DGException
    {
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId + "/error");
            return target.request(MediaType.APPLICATION_JSON).get(String.class);
        } catch (final IOException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutionInput(java.lang.Long)
     */
    @Override
    public String getTaskExecutionInput(Long taskExecutionId) throws DGException
    {
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId + "/input");
            return target.request(MediaType.APPLICATION_JSON).get(String.class);
        } catch (final IOException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutionOutput(java.lang.Long)
     */
    @Override
    public String getTaskExecutionOutput(Long taskExecutionId) throws DGException
    {
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId + "/output");
            return target.request(MediaType.APPLICATION_JSON).get(String.class);
        } catch (final IOException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutionResult(java.lang.Long)
     */
    @Override
    public String getTaskExecutionResult(Long taskExecutionId) throws DGException
    {
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId + "/result");
            return target.request(MediaType.APPLICATION_JSON).get(String.class);
        } catch (final IOException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutions()
     */
    @Override
    public Stream<ClientDGTaskExecution> getTaskExecutions() throws DGException
    {
        try {
            final WebTarget target = getTarget("/api/v1/system/executions");
            return target.request(MediaType.APPLICATION_JSON).get(new GenericType<List<ClientDGTaskExecution>>()
            {
            }).stream().map(t -> {
                t.session = getSession();
                return t;
            });
        } catch (final IOException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutionScript(java.lang.Long)
     */
    @Override
    public String getTaskExecutionScript(Long taskExecutionId) throws DGException
    {
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId + "/script");
            return target.request(MediaType.APPLICATION_JSON).get(String.class);
        } catch (final IOException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getTaskExecutionStatus(java.lang.Long)
     */
    @Override
    public DGTaskExecutionStatus getTaskExecutionStatus(Long taskExecutionId) throws DGException
    {
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId + "/status");
            return target.request(MediaType.APPLICATION_JSON).get(DGTaskExecutionStatus.class);
        } catch (final IOException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getUser(java.lang.Integer)
     */
    @Override
    public ClientDGUser getUser(Integer userId) throws DGException
    {
        try {
            return new ClientDGUser(getSession(), api().getUserApi().getUser(userId));
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getUser(java.lang.String)
     */
    @Override
    public ClientDGUser getUser(String username) throws DGException
    {
        try {
            return new ClientDGUser(getSession(), api().getUserApi().getUser(username));
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getUsers()
     */
    @Override
    public Stream<ClientDGUser> getUsers() throws DGException
    {
        try {
            return api().getUserApi().getUsers().stream().map(u -> new ClientDGUser(getSession(), u));
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getWorkspace(java.lang.Integer)
     */
    @Override
    public ClientDGWorkspace getWorkspace(Integer workspaceId) throws DGException
    {
        try {
            return new ClientDGWorkspace(getSession(), api().getGroupApi().getGroup(workspaceId));
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getWorkspace(java.lang.String)
     */
    @Override
    public ClientDGWorkspace getWorkspace(String path) throws DGException
    {
        try {
            return new ClientDGWorkspace(getSession(), api().getGroupApi().getGroup(path));
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#getWorkspaces()
     */
    @Override
    public Stream<ClientDGWorkspace> getWorkspaces() throws DGException
    {
        try {
            return api().getGroupApi().getGroups().stream().map(g -> new ClientDGWorkspace(getSession(), g));
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
     * @see io.dgms.unity.api.DGSystem#newProject(java.lang.String)
     */
    @Override
    public ClientDGProject newProject(String projectName) throws DGException
    {
        try {
            return new ClientDGProject(getSession(), api().getProjectApi().createProject(projectName));
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#submitTask(java.lang.String, java.io.Reader)
     */
    @Override
    public DGTaskExecution submitTask(String name, Reader reader) throws DGException, IOException
    {
        return submitTask(name, reader, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#submitTask(java.lang.String, java.io.Reader,
     * io.dgms.unity.api.DGPackageReference)
     */
    @Override
    public ClientDGTaskExecution submitTask(String name, Reader reader, DGPackageReference packageReference)
            throws DGException, IOException
    {
        return submitTask(name, IOUtils.toString(reader), null);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSystem#submitTask(java.lang.String,
     * java.lang.String)
     */
    @Override
    public ClientDGTaskExecution submitTask(String name, String script) throws DGException
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
    public ClientDGTaskExecution submitTask(String name, String script, DGPackageReference packageReference)
            throws DGException
    {
        try {
            final WebTarget target = getTarget("/api/v1/system/tasks");
            final ClientDGTask task = new ClientDGTask();
            task.setName(name);
            task.setScript(script);
            task.setPackageReference((ClientDGPackageReference) packageReference);
            final Response response = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(task, MediaType.APPLICATION_JSON));
            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
                return response.readEntity(ClientDGTaskExecution.class);
            throw new DGException(response.getStatusInfo().getReasonPhrase());
        } catch (final IOException e) {
            throw new DGException(e);
        }
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
}
