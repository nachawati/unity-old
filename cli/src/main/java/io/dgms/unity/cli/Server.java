/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.cli;

import com.beust.jcommander.Parameters;

import io.dgms.unity.api.DGSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "server", commandDescription = "Unity DGMS Server")
public class Server extends Command
{
    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.cli.Command#action()
     */
    @Override
    public void action() throws Exception
    {
        // final URI path =
        // Server.class.getProtectionDomain().getCodeSource().getLocation().toURI().resolve("..");
        // org.eclipse.jetty.runner.Runner
        // .main(new String[] { "--lib", path.resolve("lib").toString(),
        // path.resolve("server").toString() });
        // "--classes", path.resolve("server/WEB-INF/classess")
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.cli.Command#action(io.dgms.unity.api.DGSession)
     */
    @Override
    public void action(DGSession session) throws Exception
    {
        action();
    }
}
