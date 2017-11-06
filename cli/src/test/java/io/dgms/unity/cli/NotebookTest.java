package io.dgms.unity.cli;

import java.io.IOException;

import org.junit.Test;

import io.dgms.unity.api.DGSystem;

public class NotebookTest
{
    @Test
    public void test() throws IOException
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
