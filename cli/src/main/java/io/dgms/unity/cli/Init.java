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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.merge.MergeStrategy;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGProject;
import io.dgms.unity.api.DGSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "init", commandDescription = "Initialize new repository")
public class Init extends Command
{
    /**
     *
     */
    @Parameter(description = "[path,...]", required = false)
    private List<String> paths;

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.cli.Command#action(io.dgms.unity.api.DGSession)
     */
    @Override
    public void action(DGSession session) throws DGException, IOException
    {
        if (paths == null)
            paths = new LinkedList<>();
        if (paths.isEmpty())
            paths.add(".");
        for (final String path : paths)
            try {
                final Path canonicalPath = new File(path).getCanonicalFile().toPath();
                System.out.print("Attempting to initialize repository "
                        + ansi().fgBright(YELLOW).a(canonicalPath.getFileName()).reset() + "... ");
                System.out.flush();
                init(session, canonicalPath);
                System.out.println(ansi().fgBright(GREEN).a("DONE").reset());
            } catch (final Exception e) {
                System.out.println();
                System.out.println(ansi().fgBright(RED).a("[FAILURE]").reset() + " " + "unable to initialize " + path
                        + " (check if name has been taken) : " + e.getMessage());
            }
    }

    /**
     * @param session
     * @param path
     * @throws DGException
     * @throws IOException
     */
    private void init(DGSession session, Path path) throws DGException, IOException
    {
        try {
            File directory = path.toFile();
            if (!directory.exists())
                directory.mkdirs();
            directory = directory.getCanonicalFile();
            if (!directory.isDirectory())
                throw new DGException("cannot init '" + directory + "'");
            final DGProject project = session.getSystem().instantiateProject(directory.getName());

            final InitCommand init = Git.init();
            init.setGitDir(Main.getSystemPath().resolve("git").resolve(project.getPathWithNamespace()).toFile());
            init.setDirectory(directory);
            final Git git = init.call();

            final StoredConfig storedConfig = git.getRepository().getConfig();
            storedConfig.setString("remote", "origin", "url", project.getSshUrlToRepo());
            storedConfig.setString("remote", "origin", "fetch", "+refs/heads/*:refs/remotes/origin/*");
            storedConfig.save();

            if (project.getDefaultBranchName() != null)
                try {
                    project.getRepository().unprotectBranch(project.getDefaultBranchName());
                } catch (final DGException e) {
                } finally {
                    final PullCommand pull = git.pull();
                    pull.setRemote("origin");
                    pull.setRemoteBranchName(project.getDefaultBranchName());
                    pull.setStrategy(MergeStrategy.OURS);
                    pull.setTransportConfigCallback(Main.getTransportConfigCallback(session));
                    pull.call();
                }
        } catch (final DGException | IOException e) {
            throw e;
        } catch (final Exception e) {
            throw new DGException(e);
        }
    }
}
