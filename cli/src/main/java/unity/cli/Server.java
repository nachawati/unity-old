/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.cli;

import java.net.URI;

import com.beust.jcommander.Parameters;

import unity.api.DXSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "server", commandDescription = "Unity Server")
public class Server extends Command
{
    /*
     * (non-Javadoc)
     *
     * @see unity.cli.Command#action()
     */
    @Override
    public void action() throws Exception
    {
        final URI path = Server.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        if (path.toString().endsWith("/classes/")) {

        } else
            System.out.println(path);
        // final org.eclipse.jetty.runner.Runner.main(new String[] { "--lib",
        // path.resolve("lib").toString(),
        // path.resolve("server").toString() });
        // "--classes", path.resolve("server/WEB-INF/classess")
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.cli.Command#action(unity.api.DXSession)
     */
    @Override
    public void action(DXSession session) throws Exception
    {
        action();
    }
}
