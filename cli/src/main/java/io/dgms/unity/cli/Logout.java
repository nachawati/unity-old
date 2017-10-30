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

import java.io.IOException;
import java.nio.file.Files;

import com.beust.jcommander.Parameters;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "logout", commandDescription = "Log out of Unity DGMS")
public class Logout extends Command
{
    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.cli.Command#action(io.dgms.unity.api.DGSession)
     */
    @Override
    public void action(DGSession session) throws DGException
    {
        try {
            System.out.print("Logging out of Unity DGMS... ");
            System.out.flush();
            Files.delete(Main.getSystemPath().resolve("session.json"));
            System.out.println(ansi().fgBright(GREEN).a("DONE").reset());
        } catch (final IOException e) {
            throw new DGException("unable to delete session");
        }
    }
}
