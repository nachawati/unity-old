package unity.cli;

import java.io.IOException;

import org.junit.Test;

import unity.api.DXSystem;

public class NotebookTest
{
    @Test
    public void test() throws IOException, InterruptedException
    {
        Runtime.getRuntime().exec("taskkill /F /IM java.exe").waitFor();
        Runtime.getRuntime().exec("taskkill /F /IM jupyter.exe").waitFor();
        Runtime.getRuntime().exec("taskkill /F /IM jupyter-notebook.exe").waitFor();

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
