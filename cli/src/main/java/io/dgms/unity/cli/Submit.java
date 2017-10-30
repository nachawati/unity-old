/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.cli;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import io.dgms.unity.api.DGCommit;
import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGPackageReference;
import io.dgms.unity.api.DGSession;
import io.dgms.unity.api.DGTaskExecution;
import io.dgms.unity.api.DGTaskExecutionStatus;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "submit", commandDescription = "Submit task to Unity DGMS")
public class Submit extends Command
{
    /**
     *
     */
    @Parameter(description = "<path or expression>", required = true)
    private List<String> expressions;

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.cli.Command#action(io.dgms.unity.api.DGSession)
     */
    @Override
    public void action(DGSession session) throws Exception
    {
        String expression = null;
        if (expressions != null && !expressions.isEmpty())
            expression = expressions.stream().collect(Collectors.joining(" "));
        DGPackageReference packageReference = null;
        DGTaskExecution execution = null;
        try {
            final Path path = Paths.get(expression);
            if (Files.exists(path)) {
                final Path rootPath = Main.getRepositoryRoot(path);
                if (rootPath != null) {
                    System.out.print("Attemping to synchronize "
                            + ansi().fgBright(YELLOW).a(rootPath.getFileName()).reset() + "... ");
                    System.out.flush();
                    final DGCommit commit = Main.synchronize(session, rootPath);
                    packageReference = commit.getAsPackageReference();
                    System.out.println(ansi().fgBright(GREEN).a("DONE").reset());
                }
                System.out.print("Attempting to submit task... ");
                try (Reader reader = Files.newBufferedReader(path)) {
                    execution = session.getSystem().submitTask(path.getFileName().toString(), reader, packageReference);
                }
            }
        } catch (final IOException e) {
        }

        if (execution == null) {
            final Path rootPath = Main.getRepositoryRoot(new File(".").toPath());
            if (rootPath != null) {
                System.out.print("Attemping to synchronize " + ansi().fgBright(YELLOW).a(rootPath.getFileName()).reset()
                        + "... ");
                System.out.flush();
                final DGCommit commit = Main.synchronize(session, rootPath);
                packageReference = commit.getAsPackageReference();
                System.out.println(ansi().fgBright(GREEN).a("DONE").reset());
            }
            System.out.print("Attempting to submit task... ");
            execution = session.getSystem().submitTask(
                    expression.substring(0, expression.length() > 16 ? 16 : expression.length()), expression,
                    packageReference);
        }

        System.out.println(ansi().fgBright(GREEN).a("DONE").reset());
        System.out.print("Waiting for task execution " + execution.getId() + " to complete... ");
        System.out.flush();

        DGTaskExecutionStatus status = null;
        while (true) {
            status = session.getSystem().getTaskExecutionStatus(execution.getId());
            if (!(status == DGTaskExecutionStatus.ACTIVE || status == DGTaskExecutionStatus.QUEUED))
                break;
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
            }
        }
        switch (status) {
        case FAILED:
            System.out.println(ansi().fgBright(RED).a("FAILED").reset() + ":");
            final String err = session.getSystem().getTaskExecutionError(execution.getId());
            if (err != null)
                System.err.println(err);
            break;
        case FINISHED:
            System.out.println(ansi().fgBright(GREEN).a("FINISHED").reset() + ":");
            final String result = session.getSystem().getTaskExecutionResult(execution.getId());
            if (result != null)
                System.err.println(result);
            break;
        case INTERRUPTED:
            System.out.println(ansi().fgBright(MAGENTA).a("INTERRUPTED").reset());
            break;
        case KILLED:
            System.out.println(ansi().fgBright(MAGENTA).a("KILLED").reset());
            break;
        default:
            throw new DGException("invalid task execution status returned: " + status);
        }

        execution = session.getSystem().getTaskExecution(execution.getId());
        if (execution != null) {
            final Duration duration = execution.getExecutionDuration();
            if (duration != null) {
                System.out.println();
                System.out.println("---");
                System.out.println(
                        "Execution time: " + ansi().fgBright(CYAN).a(duration.getSeconds() + " seconds").reset());
            }
        }
    }
}
