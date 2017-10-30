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

import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public abstract class Command
{
    /**
     *
     */
    @Parameter(names = { "--help" }, description = "Print usage and quit", help = true, order = -2)
    protected Boolean    help;
    /**
     *
     */
    protected JCommander jCommander;

    /**
     * @throws Exception
     */
    public void action() throws Exception
    {
        throw new DGException("you are not logged in");
    }

    /**
     * @param session
     * @throws Exception
     */
    public void action(DGSession session) throws Exception
    {
    }

    /**
     * @return String
     */
    public String getCommandName()
    {
        for (final Parameters parameter : this.getClass().getAnnotationsByType(Parameters.class))
            for (final String commandName : parameter.commandNames())
                return commandName;
        return null;
    }

    /**
     * @return JCommander
     */
    protected JCommander getJCommander()
    {
        return jCommander;
    }

    /**
     * @param rows
     */
    protected void printTable(List<Object[]> rows)
    {
        int columns = 0;
        for (final Object[] row : rows)
            columns = Math.max(row.length, columns);
        final int[] widths = new int[columns];
        for (final Object[] row : rows)
            for (int j = 0; j < row.length; j++)
                widths[j] = Math.max(row[j] != null ? row[j].toString().length() : 4, widths[j]);
        final String[] formats = new String[widths.length];
        for (int i = 0; i < widths.length; i++)
            formats[i] = "%1$-" + widths[i] + "s" + (i < widths.length ? " " : "");
        for (final Object[] row : rows) {
            for (int j = 0; j < row.length; j++)
                System.out.printf(formats[j], row[j] != null ? row[j] : "null");
            System.out.println();
        }
        System.out.println("Count: " + ansi().fgBright(GREEN).a(rows.size()).reset());
    }

    /**
     *
     */
    public void printUsage()
    {
        jCommander.usage(getCommandName());
    }

    /**
     * @param session
     * @throws Exception
     */
    public void run(DGSession session) throws Exception
    {
        if (help != null && help)
            printUsage();
        else if (session != null)
            action(session);
        else
            action();
    }

    /**
     * @param jCommander
     * @return Command
     */
    public Command setJCommander(JCommander jCommander)
    {
        this.jCommander = jCommander;
        return this;
    }
}
