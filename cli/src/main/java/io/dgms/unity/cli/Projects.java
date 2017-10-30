/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.cli;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.beust.jcommander.Parameters;

import io.dgms.unity.api.DGSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "projects", commandDescription = "Show available projects")
public class Projects extends Command
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
        printTable(session.getSystem().getProjects().map(p -> {
            final ZonedDateTime updated = ZonedDateTime.ofInstant(p.getLastActivityDate(), ZoneId.systemDefault());
            return new Object[] { p.getId(), p.getVisibility(), formatter.format(updated), p.getPathWithNamespace() };
        }).collect(Collectors.toList()));
    }
}
