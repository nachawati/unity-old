/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.cli;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.util.FS;
import org.fusesource.jansi.AnsiConsole;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.JCommander.Builder;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import com.jcraft.jsch.Session;

import io.dgms.unity.UnityDGSession;
import io.dgms.unity.api.DGCommit;
import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGProject;
import io.dgms.unity.api.DGSession;
import io.dgms.unity.client.ClientDGSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandDescription = "Unity Decision Guidance Management System (Unity DGMS)")
public final class Main
{
    /**
     *
     */
    @Parameter(names = { "-h", "--help" }, description = "Print usage and quit", help = true)
    private Boolean help;

    /**
     *
     */
    @Parameter(names = { "-v", "--version" }, description = "Print version information and quit", help = true)
    private Boolean version;

    public static void disableCertificates()
    {
        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
        {

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
            {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
            {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }
        } };

        try {
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (final Exception e) {
        }
    }

    /**
     * @param path
     * @return Path
     */
    public static Path getRepositoryRoot(Path path)
    {
        try {
            path = path.toFile().getCanonicalFile().toPath();
            while (path != null && !Files.exists(path.resolve(".git")))
                path = path.getParent();
            return path;
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * @return Path
     * @throws IOException
     */
    public static Path getSystemPath() throws IOException
    {
        final Path settings = Paths.get(System.getProperty("user.home")).resolve(".dgms").toAbsolutePath();
        if (!Files.exists(settings))
            Files.createDirectories(settings);
        return settings;
    }

    /**
     * @param session
     * @return TransportConfigCallback
     */
    public static TransportConfigCallback getTransportConfigCallback(DGSession session)
    {
        return transport -> {
            final SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(newSshSessionFactory(session));
        };
    }

    /**
     * @param session
     * @param gitLabHost
     * @param jsch
     * @return KeyPair
     */
    public static KeyPair loadKeyPair(DGSession session, URI gitLabHost, JSch jsch)
    {
        final Gson gson = new Gson();
        try (Reader reader = Files.newBufferedReader(getSystemPath().resolve("keys.json"))) {
            final JsonObject keys = gson.fromJson(reader, JsonObject.class);
            final String keyName = session.getUser().getUsername() + "@" + gitLabHost;
            final JsonObject key = keys.getAsJsonObject(keyName);
            if (key == null || !key.isJsonObject())
                return null;
            final String privateKey = key.get("privateKey").getAsString();
            final String publicKey = key.get("publicKey").getAsString();
            return KeyPair.load(jsch, privateKey.getBytes("UTF-8"), publicKey.getBytes("UTF-8"));
        } catch (final Throwable e) {
            return null;
        }
    }

    /**
     * @param args
     */
    public static void main(String... args)
    {
        disableCertificates();
        final Main main = new Main();
        final Builder builder = JCommander.newBuilder().addObject(main);
        final Map<String, Command> commands = new TreeMap<>();
        JCommander jCommander = null;
        try {
            final DGSession session = restore();
            AnsiConsole.systemInstall();
            JSch.setConfig("StrictHostKeyChecking", "no");

            commands.put("init", new Init());
            commands.put("key", new Key());
            commands.put("login", new Login());
            commands.put("logout", new Logout());
            commands.put("projects", new Projects());
            commands.put("run", new Run());
            commands.put("submit", new Submit());
            commands.put("sync", new Sync());
            commands.put("tasks", new Tasks());
            commands.put("whoami", new Whoami());

            for (final Entry<String, Command> command : commands.entrySet())
                builder.addCommand(command.getKey(), command.getValue());
            jCommander = builder.build();
            for (final Entry<String, Command> command : commands.entrySet())
                command.getValue().setJCommander(jCommander);
            jCommander.setProgramName("dgms");
            jCommander.parse(args);

            if (main.help != null && main.help)
                jCommander.usage();
            else if (main.version != null && main.version) {
                final String implementationVersion = Main.class.getPackage().getImplementationVersion();
                final Object version = ansi().fgBright(GREEN).a(implementationVersion).reset();
                System.out.println("Unity DGMS, Version " + version);
            } else if (commands.containsKey(jCommander.getParsedCommand() != null ? jCommander.getParsedCommand() : ""))
                commands.get(jCommander.getParsedCommand()).run(session);
            else
                throw new ParameterException("unknown command: " + jCommander.getParsedCommand());
        } catch (final ParameterException e) {
            System.out.println(ansi().fgBright(RED).a("[ERROR]").reset() + " " + e.getMessage());
            System.out.println();
            if (commands.get(jCommander.getParsedCommand() != null ? jCommander.getParsedCommand() : "") != null)
                commands.get(jCommander.getParsedCommand()).printUsage();
            else
                jCommander.usage();
        } catch (final Throwable e) {
            System.out.println();
            System.out.println(ansi().fgBright(RED).a("[FAILURE]").reset() + " " + e.getMessage());
            e.printStackTrace();
        } finally {
            AnsiConsole.systemUninstall();
        }
    }

    /**
     * @param session
     * @return SshSessionFactory
     */
    public static SshSessionFactory newSshSessionFactory(DGSession session)
    {
        return new JschConfigSessionFactory()
        {
            @Override
            protected void configure(Host host, Session session)
            {
            }

            @Override
            protected JSch createDefaultJSch(FS fs) throws JSchException
            {
                final JSch defaultJSch = super.createDefaultJSch(fs);
                try {
                    final KeyPair keyPair = loadKeyPair(session, session.getGitLabHost(), defaultJSch);
                    final ByteArrayOutputStream privateKey = new ByteArrayOutputStream();
                    keyPair.writePrivateKey(privateKey);
                    final ByteArrayOutputStream publicKey = new ByteArrayOutputStream();
                    keyPair.writePublicKey(publicKey, "");
                    defaultJSch.addIdentity(session.getUser().getUsername(), privateKey.toByteArray(),
                            publicKey.toByteArray(), null);
                    return defaultJSch;
                } catch (final Exception e) {
                    return defaultJSch;
                }
            }
        };
    }

    /**
     * @return DGSession
     * @throws IOException
     */
    public static DGSession restore() throws IOException
    {
        final Gson gson = new Gson();
        try (Reader reader = Files.newBufferedReader(getSystemPath().resolve("session.json"))) {
            final JsonObject sessionProperties = gson.fromJson(reader, JsonObject.class);
            final String privateToken = sessionProperties.get("privateToken").getAsString();
            if (sessionProperties.has("gitLabHost")) {
                final URI gitLabHost = URI.create(sessionProperties.get("gitLabHost").getAsString());
                return new UnityDGSession(gitLabHost, privateToken);
            } else {
                final URI host = URI.create(sessionProperties.get("host").getAsString());
                return new ClientDGSession(host, privateToken);
            }
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * @param session
     * @param gitLabHost
     * @param keyPair
     * @throws DGException
     * @throws IOException
     */
    public static void saveKeyPair(DGSession session, URI gitLabHost, KeyPair keyPair) throws DGException, IOException
    {
        JsonObject keys = new JsonObject();
        final Gson gson = new Gson();
        try (Reader reader = Files.newBufferedReader(getSystemPath().resolve("keys.json"))) {
            keys = gson.fromJson(reader, JsonObject.class);
        } catch (final IOException e) {
        } finally {
            try {
                final String keyName = session.getUser().getUsername() + "@" + gitLabHost;
                keys.getAsJsonObject().remove(keyName);
                final JsonObject key = new JsonObject();
                final ByteArrayOutputStream privateKey = new ByteArrayOutputStream();
                keyPair.writePrivateKey(privateKey);
                key.addProperty("privateKey", privateKey.toString("UTF-8"));
                final ByteArrayOutputStream publicKey = new ByteArrayOutputStream();
                keyPair.writePublicKey(publicKey, "");
                key.addProperty("publicKey", publicKey.toString("UTF-8"));
                keys.getAsJsonObject().add(keyName, key);
                try (JsonWriter writer = gson
                        .newJsonWriter(Files.newBufferedWriter(getSystemPath().resolve("keys.json")))) {
                    gson.toJson(keys, writer);
                }
            } catch (final IOException e) {
                throw e;
            }
        }
    }

    /**
     * @param session
     * @param path
     * @throws DGException
     * @throws IOException
     */
    public static DGCommit synchronize(DGSession session, Path path) throws DGException
    {
        try {
            final Path rootPath = Main.getRepositoryRoot(path);
            if (rootPath == null)
                throw new DGException(
                        ansi().fgBright(YELLOW).a(path.getFileName().toString()).reset() + " is not a repository");

            try (final Git git = Git.wrap(new RepositoryBuilder().findGitDir(rootPath.toFile()).build())) {
                final Path repositoryPath = git.getRepository().getDirectory().toPath();
                if (!repositoryPath.startsWith(Main.getSystemPath().resolve("git")))
                    throw new DGException(ansi().fgBright(YELLOW).a(path.getFileName().toString()).reset()
                            + " is not managed by Unity DGMS");
                final String projectPath = repositoryPath.toString()
                        .substring(Main.getSystemPath().resolve("git").toString().length() + 1).replace("\\", "/");
                final DGProject project = session.getSystem().getProject(projectPath);

                final StoredConfig storedConfig = git.getRepository().getConfig();
                storedConfig.setString("remote", "origin", "url", project.getSshUrlToRepo());
                storedConfig.setString("remote", "origin", "fetch", "+refs/heads/*:refs/remotes/origin/*");
                storedConfig.save();
                git.add().addFilepattern(".").call();
                if (project.getDefaultBranchName() != null) {
                    try {
                        project.getRepository().unprotectBranch(project.getDefaultBranchName());
                    } catch (final DGException e) {
                    }
                    final PullCommand pull = git.pull();
                    pull.setRemote("origin");
                    pull.setRemoteBranchName(project.getDefaultBranchName());
                    pull.setStrategy(MergeStrategy.OURS);
                    pull.setRebase(true);
                    pull.setTransportConfigCallback(getTransportConfigCallback(session));
                    pull.call();
                }
                final RevCommit commit = git.commit().setMessage("Updated file(s)").call();
                final PushCommand push = git.push();
                push.setRemote("origin");
                push.setTransportConfigCallback(getTransportConfigCallback(session));
                push.call();
                final StringWriter reference = new StringWriter();
                commit.getId().copyTo(reference);
                return project.getRepository().getCommit(reference.toString());
            }
        } catch (final Exception e) {
            throw new DGException(e);
        }
    }
}
