package io.dgms.unity.cli;

import java.io.IOException;

import com.beust.jcommander.Parameters;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGSession;
import io.dgms.unity.api.DGSystem;

@Parameters(commandNames = "notebook", commandDescription = "Open Jupyter notebook")
public class Notebook extends Command
{
    @Override
    public void action() throws DGException, IOException
    {
        action(null);
    }

    @Override
    public void action(DGSession session) throws DGException, IOException
    {
        final ProcessBuilder processBuilder = new ProcessBuilder("jupyter", "notebook");
        processBuilder.environment().put("JUPYTER_PATH", DGSystem.getInstallPath().toString());
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
