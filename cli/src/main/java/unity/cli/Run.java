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

import unity.api.DXScriptEngine;
import unity.api.DXSession;
import unity.system.UnityDXSystem;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "run", commandDescription = "Execute local script or query", hidden = true)
public class Run extends Command
{
    /**
     *
     */
    @Parameter(description = "<path or expression>", required = true)
    private List<String> expressions;

    /*
     * (non-Javadoc)
     *
     * @see unity.cli.Command#action()
     */
    @Override
    public void action() throws Exception
    {
        String expression = null;
        if (expressions != null && !expressions.isEmpty())
            expression = expressions.stream().collect(Collectors.joining(" "));
        String script = null;
        try {
            final Path path = Paths.get(expression);
            if (Files.exists(path))
                try (Reader reader = Files.newBufferedReader(path)) {
                    script = IOUtils.toString(reader);
                }
        } catch (final IOException e) {
        }

        if (script == null)
            script = expression;

        final DXScriptEngine engine = UnityDXSystem.getLocalEngineByName("zorba");
        final long startNanos = System.nanoTime();
        System.out.println(engine.eval(script));
        System.out.println();
        System.out.println("---");
        System.out.println("Execution time: "
                + ansi().fgBright(CYAN).a((System.nanoTime() - startNanos) / 1000000000 + " seconds").reset());
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
