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

import java.io.IOException;
import java.nio.file.Files;

import com.beust.jcommander.Parameters;

import unity.api.DXException;
import unity.api.DXSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "logout", commandDescription = "Log out of Unity")
public class Logout extends Command
{
    /*
     * (non-Javadoc)
     *
     * @see unity.cli.Command#action(unity.api.DXSession)
     */
    @Override
    public void action(DXSession session) throws DXException
    {
        try {
            System.out.print("Logging out of Unity... ");
            System.out.flush();
            Files.delete(Main.getSystemPath().resolve("session.json"));
            System.out.println(ansi().fgBright(GREEN).a("DONE").reset());
        } catch (final IOException e) {
            throw new DXException("unable to delete session");
        }
    }
}
