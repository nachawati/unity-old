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
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "sync", commandDescription = "Synchronize local repository")
public class Sync extends Command
{
    /**
     *
     */
    @Parameter(description = "[path,...]", required = false)
    protected List<String> paths;

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.cli.Command#action(io.dgms.unity.api.DGSession)
     */
    @Override
    public void action(DGSession session) throws Exception
    {
        if (paths == null)
            paths = new LinkedList<>();
        if (paths.isEmpty())
            paths.add(".");
        for (final String path : paths)
            try {
                final Path canonicalPath = new File(path).getCanonicalFile().toPath();
                final Path rootPath = Main.getRepositoryRoot(canonicalPath);
                System.out.print("Attemping to synchronize "
                        + ansi().fgBright(YELLOW)
                                .a(rootPath != null ? rootPath.getFileName() : canonicalPath.getFileName()).reset()
                        + "... ");
                if (rootPath == null)
                    throw new DGException(ansi().fgBright(YELLOW).a(canonicalPath.getFileName().toString()).reset()
                            + " is not a repository");
                System.out.flush();
                Main.synchronize(session, rootPath);
                System.out.println(ansi().fgBright(GREEN).a("DONE").reset());
            } catch (final Exception e) {
                System.out.println();
                System.out.println(ansi().fgBright(RED).a("[FAILURE]").reset() + " " + "unable to synchronize " + path
                        + " : " + e.getMessage());
            }
    }
}
