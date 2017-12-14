/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.SystemUtils;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXSystem extends DXSessionObject
{
    /**
     * @param extension
     * @return DXScriptEngine
     */
    DXScriptEngine getEngineByExtension(String extension);

    /**
     * @param mimeType
     * @return DXScriptEngine
     */
    DXScriptEngine getEngineByMimeType(String mimeType);

    /**
     * @param shortName
     * @return DXScriptEngine
     */
    DXScriptEngine getEngineByName(String shortName);

    /**
     * @return List<DXScriptEngineFactory>
     */
    List<DXScriptEngineFactory> getEngineFactories();

    /**
     * @param projectId
     * @return DXProject
     * @throws DXException
     */
    DXProject getProject(Integer projectId);

    /**
     * @param path
     * @return DXProject
     * @throws DXException
     */
    DXProject getProject(String path);

    /**
     * @return Stream<? extends DXProject>
     * @throws DXException
     */
    Stream<? extends DXProject> getProjects();

    /**
     * @param taskExecutionId
     * @return DXTaskExecution
     * @throws DXException
     */
    DXTaskExecution getTaskExecution(Long taskExecutionId);

    /**
     * @param taskExecutionId
     * @return String
     * @throws DXException
     */
    String getTaskExecutionError(Long taskExecutionId);

    /**
     * @param taskExecutionId
     * @return String
     * @throws DXException
     */
    String getTaskExecutionInput(Long taskExecutionId);

    /**
     * @param taskExecutionId
     * @return String
     * @throws DXException
     */
    String getTaskExecutionOutput(Long taskExecutionId);

    /**
     * @param taskExecutionId
     * @return String
     * @throws DXException
     */
    String getTaskExecutionResult(Long taskExecutionId);

    /**
     * @return Stream<? extends DXTaskExecution>
     * @throws DXException
     */
    Stream<? extends DXTaskExecution> getTaskExecutions();

    Stream<? extends DXTaskExecution> getTaskExecutions(String path);

    /**
     * @return Stream<? extends DGTaskExecution>
     * @throws DGException
     */
    Stream<? extends DGTaskExecution> getTaskExecutions(String path);

    /**
     * @param taskExecutionId
     * @return String
     * @throws DXException
     */
    String getTaskExecutionScript(Long taskExecutionId);

    /**
     * @param taskExecutionId
     * @return DXTaskExecutionStatus
     * @throws DXException
     */
    DXTaskExecutionStatus getTaskExecutionStatus(Long taskExecutionId);

    /**
     * @param userId
     * @return DXUser
     * @throws DXException
     */
    DXUser getUser(Integer userId);

    /**
     * @param username
     * @return DXUser
     * @throws DXException
     */
    DXUser getUser(String username);

    /**
     * @return Stream<? extends DXUser>
     * @throws DXException
     */
    Stream<? extends DXUser> getUsers();

    /**
     * @param workspaceId
     * @return DXWorkspace
     * @throws DXException
     */
    DXWorkspace getWorkspace(Integer workspaceId);

    /**
     * @param path
     * @return DXWorkspace
     * @throws DXException
     */
    DXWorkspace getWorkspace(String path);

    /**
     * @return Stream<? extends DXWorkspace>
     * @throws DXException
     */
    Stream<? extends DXWorkspace> getWorkspaces();

    /**
     * @param projectName
     * @return DXProject
     * @throws DXException
     */
    DXProject instantiateProject(String projectName) throws DXException;

    /**
     * @param taskId
     * @throws DXException
     */
    void killTask(Long taskId) throws DXException;

    /**
     * @param name
     * @param reader
     * @return DXTaskExecution
     * @throws DXException
     * @throws IOException
     */
    DXTaskExecution submitTask(String name, Reader reader) throws DXException, IOException;

    /**
     * @param name
     * @param reader
     * @param packageReference
     * @return DXTaskExecution
     * @throws DXException
     * @throws IOException
     */
    DXTaskExecution submitTask(String name, Reader reader, DXPackageReference packageReference)
            throws DXException, IOException;

    /**
     * @param name
     * @param script
     * @return DXTaskExecution
     * @throws DXException
     */
    DXTaskExecution submitTask(String name, String script) throws DXException;

    /**
     * @param name
     * @param script
     * @param packageReference
     * @return DXTaskExecution
     * @throws DXException
     */
    DXTaskExecution submitTask(String name, String script, DXPackageReference packageReference) throws DXException;

    /**
     * @return Path
     */
    static Path getInstallPath()
    {
        try {
            final String path = DXSystem.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if (path.endsWith("classes/")) {
                final File location = new File(path).getParentFile().getParentFile().getParentFile();
                if (SystemUtils.IS_OS_WINDOWS)
                    switch (SystemUtils.OS_ARCH) {
                    case "amd64":
                    case "x86_64":
                        return new File(location, "target/windows-msvc-x86_64").toPath();
                    }
                else if (SystemUtils.IS_OS_LINUX)
                    switch (SystemUtils.OS_ARCH) {
                    case "amd64":
                    case "x86_64":
                        return new File(location, "target/linux-gcc-x86_64").toPath();
                    }
            }
            if (path.contains("WEB-INF")) {
                final File location = new File("C:\\Users\\Omar\\Documents\\GitHub\\unity");
                if (SystemUtils.IS_OS_WINDOWS)
                    switch (SystemUtils.OS_ARCH) {
                    case "amd64":
                    case "x86_64":
                        return new File(location, "target/windows-msvc-x86_64").toPath();
                    }
                else if (SystemUtils.IS_OS_LINUX)
                    switch (SystemUtils.OS_ARCH) {
                    case "amd64":
                    case "x86_64":
                        return new File(location, "target/linux-gcc-x86_64").toPath();
                    }
            }

            return new File(path).getParentFile().getParentFile().toPath();
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param uri
     * @return String
     */
    static String toPathString(final String uri)
    {
        return toPathString(URI.create(uri));
    }

    /**
     * @param uri
     * @return String
     */
    static String toPathString(URI uri)
    {
        uri = uri.normalize();
        final StringBuilder sb = new StringBuilder();
        if (uri.getHost() != null) {
            final String host = uri.getHost();
            final String[] hostParts = host.split("\\.");
            for (int i = hostParts.length - 1; i >= 0; i--) {
                sb.append(hostParts[i]);
                if (i > 0)
                    sb.append("/");
            }
            sb.append("/");
        }
        final String path = uri.getPath();
        final String[] pathParts = path.split("/");
        for (int i = 0; i < pathParts.length; i++) {
            if (pathParts[i].isEmpty())
                continue;
            sb.append(pathParts[i]);
            if (i < pathParts.length - 1)
                sb.append("/");
        }
        return sb.toString();
    }

    /**
     * @param uri
     * @return String
     */
    static String toQualifiedNameString(final String uri)
    {
        return toQualifiedNameString(URI.create(uri));
    }

    /**
     * @param uri
     * @return String
     */
    static String toQualifiedNameString(URI uri)
    {
        uri = uri.normalize();
        final StringBuilder sb = new StringBuilder();
        if (uri.getHost() != null) {
            final String host = uri.getHost();
            final String[] hostParts = host.split("\\.");
            for (int i = hostParts.length - 1; i >= 0; i--) {
                sb.append(hostParts[i]);
                if (i > 0)
                    sb.append(".");
            }
            sb.append(".");
        }
        final String path = uri.getPath();
        final String[] pathParts = path.split("/");
        for (int i = 0; i < pathParts.length; i++) {
            if (pathParts[i].isEmpty())
                continue;
            sb.append(pathParts[i]);
            if (i < pathParts.length - 1)
                sb.append(".");
        }
        return sb.toString();
    }
}
