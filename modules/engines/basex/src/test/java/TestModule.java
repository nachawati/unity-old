import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.script.ScriptException;

import org.basex.core.Context;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryProcessor;
import org.basex.query.value.Value;
import org.basex.query.value.array.Array;
import org.basex.query.value.item.Dbl;
import org.basex.query.value.item.FuncItem;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.query.value.map.Map;
import org.basex.util.InputInfo;
import org.junit.Test;

import aliceinnets.python.jyplot.JyPlot;
import aliceinnets.util.OneLiners;
import unity.api.DXScriptEngine;
import unity.modules.engines.basex.BaseXDXScriptEngineFactory;

public class TestModule
{
    @Test
    public void test() throws QueryException, IOException, ScriptException

    {
        final Context context = new Context();
        try (final DXScriptEngine processor = new BaseXDXScriptEngineFactory().getScriptEngine()) {

            final Object o = processor.eval(
                    "import module namespace java = \"java:unity.modules.visualization.plotting.BaseXModule\";\n" +

                            "java:plot2d( {\n" + "    \n" + "    \"plots\": [\n" + "        {\n" + "\n"
                            + "            \"data\":\n" + "            [\n" + "                {\n"
                            + "                    \"style\": \"--\",\n"
                            + "                    \"mapper\": function($x) {$x}\n" + "                }\n"
                            + "            ]\n" + "        }\n" + "    ]\n" + "})");
            System.out.println(o);

        }
    }
}
