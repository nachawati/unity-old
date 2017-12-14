package unity.cli;

import java.io.IOException;

import com.beust.jcommander.Parameters;

import unity.api.DXException;
import unity.api.DXSession;
import unity.api.DXSystem;

@Parameters(commandNames = "notebook", commandDescription = "Open Jupyter notebook")
public class Notebook extends Command
{
    @Override
    public void action() throws DXException, IOException
    {
        action(null);
    }

    @Override
    public void action(DXSession session) throws DXException, IOException
    {
        final ProcessBuilder processBuilder = new ProcessBuilder("jupyter", "notebook");
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
