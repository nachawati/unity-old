/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.cli;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.CYAN;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import unity.api.DXException;
import unity.api.DXScriptEngine;
import unity.api.DXSession;
import unity.api.DXSystem;
import unity.system.UnityDXSystem;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "console", commandDescription = "Start Jupyter console", hidden = true)
public class Console extends Command
{
    @Override
    public void action() throws DXException, IOException
    {
        action(null);
    }

    @Override
    public void action(DXSession session) throws DXException, IOException
    {
        final ProcessBuilder processBuilder = new ProcessBuilder("jupyter", "console", "--kernel=basex");
        processBuilder.environment().put("JUPYTER_PATH", DXSystem.getInstallPath().toString());
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);

        final Process p = processBuilder.start();
        try {
            p.waitFor();
        } catch (final InterruptedException e) {
        }
    }
}
