/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.cli;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.GREEN;

import com.beust.jcommander.Parameters;

import unity.api.DXException;
import unity.api.DXSession;
import unity.api.DXUser;

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
     * @see unity.cli.Command#action(unity.api.DXSession)
     */
    @Override
    public void action(DXSession session) throws DXException
    {
        final DXUser user = session.getUser();
        final Object username = ansi().fgBright(GREEN).a(user.getUsername()).reset();
        System.out.println(username + ": " + user.getName() + " (" + user.getEmail() + ")");
    }
}
