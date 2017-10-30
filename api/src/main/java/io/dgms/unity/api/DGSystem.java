/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

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
public interface DGSystem extends DGSessionObject
{
    /**
     * @param extension
     * @return DGScriptEngine
     */
    DGScriptEngine getEngineByExtension(String extension);

    /**
     * @param mimeType
     * @return DGScriptEngine
     */
    DGScriptEngine getEngineByMimeType(String mimeType);

    /**
     * @param shortName
     * @return DGScriptEngine
     */
    DGScriptEngine getEngineByName(String shortName);

    /**
     * @return List<DGScriptEngineFactory>
     */
    List<DGScriptEngineFactory> getEngineFactories();

    /**
     * @param projectId
     * @return DGProject
     * @throws DGException
     */
    DGProject getProject(Integer projectId) throws DGException;

    /**
     * @param path
     * @return DGProject
     * @throws DGException
     */
    DGProject getProject(String path) throws DGException;

    /**
     * @return Stream<? extends DGProject>
     * @throws DGException
     */
    Stream<? extends DGProject> getProjects() throws DGException;

    /**
     * @param taskExecutionId
     * @return DGTaskExecution
     * @throws DGException
     */
    DGTaskExecution getTaskExecution(Long taskExecutionId) throws DGException;

    /**
     * @param taskExecutionId
     * @return String
     * @throws DGException
     */
    String getTaskExecutionError(Long taskExecutionId) throws DGException;

    /**
     * @param taskExecutionId
     * @return String
     * @throws DGException
     */
    String getTaskExecutionInput(Long taskExecutionId) throws DGException;

    /**
     * @param taskExecutionId
     * @return String
     * @throws DGException
     */
    String getTaskExecutionOutput(Long taskExecutionId) throws DGException;

    /**
     * @param taskExecutionId
     * @return String
     * @throws DGException
     */
    String getTaskExecutionResult(Long taskExecutionId) throws DGException;

    /**
     * @return Stream<? extends DGTaskExecution>
     * @throws DGException
     */
    Stream<? extends DGTaskExecution> getTaskExecutions() throws DGException;

    /**
     * @param taskExecutionId
     * @return String
     * @throws DGException
     */
    String getTaskExecutionScript(Long taskExecutionId) throws DGException;

    /**
     * @param taskExecutionId
     * @return DGTaskExecutionStatus
     * @throws DGException
     */
    DGTaskExecutionStatus getTaskExecutionStatus(Long taskExecutionId) throws DGException;

    /**
     * @param userId
     * @return DGUser
     * @throws DGException
     */
    DGUser getUser(Integer userId) throws DGException;

    /**
     * @param username
     * @return DGUser
     * @throws DGException
     */
    DGUser getUser(String username) throws DGException;

    /**
     * @return Stream<? extends DGUser>
     * @throws DGException
     */
    Stream<? extends DGUser> getUsers() throws DGException;

    /**
     * @param workspaceId
     * @return DGWorkspace
     * @throws DGException
     */
    DGWorkspace getWorkspace(Integer workspaceId) throws DGException;

    /**
     * @param path
     * @return DGWorkspace
     * @throws DGException
     */
    DGWorkspace getWorkspace(String path) throws DGException;

    /**
     * @return Stream<? extends DGWorkspace>
     * @throws DGException
     */
    Stream<? extends DGWorkspace> getWorkspaces() throws DGException;

    /**
     * @param taskId
     * @throws DGException
     */
    void killTask(Long taskId) throws DGException;

    /**
     * @param projectName
     * @return DGProject
     * @throws DGException
     */
    DGProject newProject(String projectName) throws DGException;

    /**
     * @param name
     * @param reader
     * @return DGTaskExecution
     * @throws DGException
     * @throws IOException
     */
    DGTaskExecution submitTask(String name, Reader reader) throws DGException, IOException;

    /**
     * @param name
     * @param reader
     * @param packageReference
     * @return DGTaskExecution
     * @throws DGException
     * @throws IOException
     */
    DGTaskExecution submitTask(String name, Reader reader, DGPackageReference packageReference)
            throws DGException, IOException;

    /**
     * @param name
     * @param script
     * @return DGTaskExecution
     * @throws DGException
     */
    DGTaskExecution submitTask(String name, String script) throws DGException;

    /**
     * @param name
     * @param script
     * @param packageReference
     * @return DGTaskExecution
     * @throws DGException
     */
    DGTaskExecution submitTask(String name, String script, DGPackageReference packageReference) throws DGException;

    /**
     * @return Path
     */
    static Path getInstallPath()
    {
        try {
            final String path = DGSystem.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
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
