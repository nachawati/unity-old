package unity.cli;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import unity.api.DXException;
import unity.api.DXSession;
import unity.kernel.UnityDXKernel;

@Parameters(commandNames = "kernel", commandDescription = "Start kernel for Jupyter", hidden = true)
public class Kernel extends Command
{
    @Parameter(description = "[connection_file]", required = true)
    private List<String> connectionFiles;

    @Parameter(names = { "-e", "--engine" }, description = "Engine")
    protected String     engine = "basex";

    @Override
    public void action() throws DXException, IOException
    {
        action(null);
    }

    @Override
    public void action(DXSession session) throws DXException, IOException
    {
        String connectionFile = null;
        if (connectionFiles != null && !connectionFiles.isEmpty())
            connectionFile = connectionFiles.stream().collect(Collectors.joining(" "));
        while (true)
            try (UnityDXKernel kernel = new UnityDXKernel(Paths.get(connectionFile))) {
                while (true)
                    try {
                        Thread.sleep(1000);
                    } catch (final InterruptedException e) {
                    }
            }
    }
}
