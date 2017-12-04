/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.client;

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

import unity.api.DXException;
import unity.api.DXPackageReference;
import unity.api.DXScriptEngine;
import unity.api.DXScriptEngineFactory;
import unity.api.DXSystem;
import unity.api.DXTaskExecution;
import unity.api.DXTaskExecutionStatus;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDXSystem extends ClientDXSessionObject implements DXSystem
{
    /**
     * @param session
     */
    public ClientDXSystem(ClientDXSession session)
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
                            final DXScriptEngine engine = engineFactory.getScriptEngine();
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
                            final DXScriptEngine engine = engineFactory.getScriptEngine();
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
                            final DXScriptEngine engine = engineFactory.getScriptEngine();
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
    public ClientDXProject getProject(Integer projectId)
    {
        try {
            final Project object = api().getProjectApi().getProject(projectId);
            return new ClientDXProject(getSession(), object);
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
    public ClientDXProject getProject(String path)
    {
        try {
            final int off = path.lastIndexOf("/");
            final String namespace = path.substring(0, off);
            final String project = path.substring(off + 1);
            final Project object = api().getProjectApi().getProject(namespace, project);
            return new ClientDXProject(getSession(), object);
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
    public Stream<ClientDXProject> getProjects()
    {
        try {
            return api().getProjectApi().getProjects().stream().map(p -> new ClientDXProject(getSession(), p));
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
    public DXTaskExecution getTaskExecution(Long taskExecutionId)
    {
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId);
            final ClientDXTaskExecution taskExecution = target.request(MediaType.APPLICATION_JSON)
                    .get(ClientDXTaskExecution.class);
            taskExecution.session = getSession();
            return taskExecution;
        } catch (final IOException e) {
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
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId + "/error");
            return target.request(MediaType.APPLICATION_JSON).get(String.class);
        } catch (final IOException e) {
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
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId + "/input");
            return target.request(MediaType.APPLICATION_JSON).get(String.class);
        } catch (final IOException e) {
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
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId + "/output");
            return target.request(MediaType.APPLICATION_JSON).get(String.class);
        } catch (final IOException e) {
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
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId + "/result");
            return target.request(MediaType.APPLICATION_JSON).get(String.class);
        } catch (final IOException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getTaskExecutions()
     */
    @Override
    public Stream<ClientDXTaskExecution> getTaskExecutions()
    {
        try {
            final WebTarget target = getTarget("/api/v1/system/executions");
            return target.request(MediaType.APPLICATION_JSON).get(new GenericType<List<ClientDXTaskExecution>>()
            {
            }).stream().map(t -> {
                t.session = getSession();
                return t;
            });
        } catch (final IOException e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getTaskExecutionScript(java.lang.Long)
     */
    @Override
    public String getTaskExecutionScript(Long taskExecutionId)
    {
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId + "/script");
            return target.request(MediaType.APPLICATION_JSON).get(String.class);
        } catch (final IOException e) {
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
        try {
            final WebTarget target = getTarget("/api/v1/system/executions/" + taskExecutionId + "/status");
            return target.request(MediaType.APPLICATION_JSON).get(DXTaskExecutionStatus.class);
        } catch (final IOException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#getUser(java.lang.Integer)
     */
    @Override
    public ClientDXUser getUser(Integer userId)
    {
        try {
            return new ClientDXUser(getSession(), api().getUserApi().getUser(userId));
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
    public ClientDXUser getUser(String username)
    {
        try {
            return new ClientDXUser(getSession(), api().getUserApi().getUser(username));
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
    public Stream<ClientDXUser> getUsers()
    {
        try {
            return api().getUserApi().getUsers().stream().map(u -> new ClientDXUser(getSession(), u));
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
    public ClientDXWorkspace getWorkspace(Integer workspaceId)
    {
        try {
            return new ClientDXWorkspace(getSession(), api().getGroupApi().getGroup(workspaceId));
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
    public ClientDXWorkspace getWorkspace(String path)
    {
        try {
            return new ClientDXWorkspace(getSession(), api().getGroupApi().getGroup(path));
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
    public Stream<ClientDXWorkspace> getWorkspaces()
    {
        try {
            return api().getGroupApi().getGroups().stream().map(g -> new ClientDXWorkspace(getSession(), g));
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
    public ClientDXProject instantiateProject(String projectName) throws DXException
    {
        try {
            return new ClientDXProject(getSession(), api().getProjectApi().createProject(projectName));
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
    public DXTaskExecution submitTask(String name, Reader reader) throws DXException, IOException
    {
        return submitTask(name, reader, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#submitTask(java.lang.String, java.io.Reader,
     * unity.api.DXPackageReference)
     */
    @Override
    public ClientDXTaskExecution submitTask(String name, Reader reader, DXPackageReference packageReference)
            throws DXException, IOException
    {
        return submitTask(name, IOUtils.toString(reader), null);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSystem#submitTask(java.lang.String, java.lang.String)
     */
    @Override
    public ClientDXTaskExecution submitTask(String name, String script) throws DXException
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
    public ClientDXTaskExecution submitTask(String name, String script, DXPackageReference packageReference)
            throws DXException
    {
        try {
            final WebTarget target = getTarget("/api/v1/system/tasks");
            final ClientDXTask task = new ClientDXTask();
            task.setName(name);
            task.setScript(script);
            task.setPackageReference((ClientDXPackageReference) packageReference);
            final Response response = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(task, MediaType.APPLICATION_JSON));
            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
                return response.readEntity(ClientDXTaskExecution.class);
            throw new DXException(response.getStatusInfo().getReasonPhrase());
        } catch (final IOException e) {
            throw new DXException(e);
        }
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
}
