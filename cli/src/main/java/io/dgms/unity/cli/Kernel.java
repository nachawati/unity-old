package io.dgms.unity.cli;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGSession;
import io.dgms.unity.kernel.UnityDGKernel;

@Parameters(commandNames = "kernel", commandDescription = "Start kernel for Jupyter", hidden = true)
public class Kernel extends Command
{
    @Parameter(description = "[connection_file]", required = true)
    private List<String> connectionFiles;

    @Override
    public void action() throws DGException, IOException
    {
        action(null);
    }

    @Override
    public void action(DGSession session) throws DGException, IOException
    {
        String connectionFile = null;
        if (connectionFiles != null && !connectionFiles.isEmpty())
            connectionFile = connectionFiles.stream().collect(Collectors.joining(" "));
        while (true)
            try (UnityDGKernel kernel = new UnityDGKernel(Paths.get(connectionFile))) {
                while (true)
                    try {
                        Thread.sleep(1000);
                    } catch (final InterruptedException e) {
                    }
            }
    }
}
