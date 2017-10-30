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
import static org.fusesource.jansi.Ansi.Color.WHITE;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.beust.jcommander.Parameters;

import io.dgms.unity.api.DGSession;
import io.dgms.unity.api.DGTask;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "tasks", commandDescription = "Show task executions")
public class Tasks extends Command
{
    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.cli.Command#action(io.dgms.unity.api.DGSession)
     */
    @Override
    public void action(DGSession session) throws Exception
    {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm");
        printTable(session.getSystem().getTaskExecutions().map(e -> {
            final DGTask t = e.getTask();
            final String submitted = t.getDateSubmitted() != null
                    ? formatter.format(ZonedDateTime.ofInstant(t.getDateSubmitted(), ZoneId.systemDefault()))
                    : null;
            final String initiated = e.getDateInitiated() != null
                    ? formatter.format(ZonedDateTime.ofInstant(e.getDateInitiated(), ZoneId.systemDefault()))
                    : null;
            final String terminated = e.getDateTerminated() != null
                    ? formatter.format(ZonedDateTime.ofInstant(e.getDateTerminated(), ZoneId.systemDefault()))
                    : null;
            final String duration = e.getExecutionDuration() != null ? e.getExecutionDuration().getSeconds() + "s"
                    : null;
            String status;
            switch (e.getStatus()) {
            case ACTIVE:
                status = ansi().fgBright(CYAN).a(e.getStatus()).reset().toString();
                break;
            case FAILED:
                status = ansi().fgBright(RED).a(e.getStatus()).reset().toString();
                break;
            case FINISHED:
                status = ansi().fgBright(GREEN).a(e.getStatus()).reset().toString();
                break;
            case INTERRUPTED:
                status = ansi().fgBright(MAGENTA).a(e.getStatus()).reset().toString();
                break;
            case KILLED:
                status = ansi().fgBright(MAGENTA).a(e.getStatus()).reset().toString();
                break;
            case QUEUED:
                status = ansi().fgBright(YELLOW).a(e.getStatus()).reset().toString();
                break;
            default:
                status = null;
                break;

            }
            return new Object[] { ansi().fgBright(WHITE).a(t.getId()).reset().toString(), t.getName(), t.getUserId(),
                    submitted, initiated, terminated, duration, status };
        }).collect(Collectors.toList()));
    }
}
