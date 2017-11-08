package io.dgms.unity.modules.visualization.plotting;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryModule;
import org.basex.query.value.Value;
import org.basex.query.value.array.Array;
import org.basex.query.value.item.Dbl;
import org.basex.query.value.item.FuncItem;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.query.value.map.Map;
import org.basex.util.InputInfo;

import aliceinnets.python.jyplot.JyPlot;
import aliceinnets.util.OneLiners;

public class BaseXModule extends QueryModule
{
    @Requires(Permission.READ)
    public Object plot2d(Map input) throws IOException, QueryException
    {
        final Map obj = input;
        final QueryContext qc = queryContext;
        final InputInfo ii = qc.root.info;

        final Value columnsValue = obj.get(Str.get("columns"), ii);
        final Value rowsValue = obj.get(Str.get("rows"), ii);
        final int columns = (int) (columnsValue instanceof Item ? ((Item) columnsValue).dbl(ii) : 1);
        final int rows = (int) (rowsValue instanceof Item ? ((Item) rowsValue).itr(ii) : 1);

        final JyPlot plt = new JyPlot();
        plt.write("from matplotlib import cm");
        plt.figure();

        int i = 1;
        for (final Value plotValue : ((Array) obj.get(Str.get("plots"), ii)).members()) {
            if (columns * rows > 1)
                plt.subplot(100 * rows + 10 * columns + i);
            if (!(plotValue instanceof Map))
                continue;
            final Map plotMap = (Map) plotValue;
            final Value xminValue = plotMap.get(Str.get("xmin"), ii);
            final Value xmaxValue = plotMap.get(Str.get("xmax"), ii);
            final Value yminValue = plotMap.get(Str.get("ymin"), ii);
            final Value ymaxValue = plotMap.get(Str.get("ymax"), ii);
            final Value pointsValue = plotMap.get(Str.get("points"), ii);

            final double xmin = xminValue instanceof Item ? ((Item) xminValue).dbl(ii) : 0;
            final double xmax = xmaxValue instanceof Item ? ((Item) xmaxValue).dbl(ii) : 10;
            final double ymin = yminValue instanceof Item ? ((Item) yminValue).dbl(ii) : 0;
            final double ymax = ymaxValue instanceof Item ? ((Item) ymaxValue).dbl(ii) : 10;
            final int points = (int) (pointsValue instanceof Item ? ((Item) pointsValue).itr(ii) : 100);

            final double[] x = OneLiners.linspace(xmin, xmax, points);
            final double[] y = new double[x.length];

            for (final Value dataValue : ((Array) plotMap.get(Str.get("data"), ii)).members()) {
                if (!(dataValue instanceof Map))
                    continue;
                final Map dataMap = (Map) dataValue;

                final String style = dataMap.get(Str.get("style"), ii).toString();
                final FuncItem mapper = (FuncItem) dataMap.get(Str.get("mapper"), ii);
                for (int j = 0; j < y.length; j++)
                    try {
                        y[j] = mapper.invokeItem(qc, ii, Dbl.get(x[j])).dbl(ii);
                    } catch (final Exception e) {
                        y[j] = Double.NaN;
                    }
                plt.plot(x, y, style);
            }

            plt.grid();
            plt.xlabel("$X Axis$");
            plt.ylabel("$Y Axis$");
            plt.xlim(xmin, xmax);
            plt.ylim(ymin, ymax);
            plt.legend("loc='lower left'");

            i++;
        }

        final File temp = File.createTempFile("plot", ".png").getAbsoluteFile();
        plt.savefig("r'" + temp.getPath() + "', format=r'png'");
        // plt.saveAndExec();
        plt.tight_layout();
        plt.exec();
        final BufferedImage image = ImageIO.read(temp);
        temp.delete();
        return image;
    }
}
