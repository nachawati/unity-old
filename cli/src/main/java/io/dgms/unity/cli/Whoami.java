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

import com.beust.jcommander.Parameters;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGSession;
import io.dgms.unity.api.DGUser;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "whoami", commandDescription = "Identify current user")
public class Whoami extends Command
{
    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.cli.Command#action(io.dgms.unity.api.DGSession)
     */
    @Override
    public void action(DGSession session) throws DGException
    {
        final DGUser user = session.getUser();
        final Object username = ansi().fgBright(GREEN).a(user.getUsername()).reset();
        System.out.println(username + ": " + user.getName() + " (" + user.getEmail() + ")");
    }
}
