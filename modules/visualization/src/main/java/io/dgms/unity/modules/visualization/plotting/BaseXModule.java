package io.dgms.unity.modules.visualization.plotting;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Base64;

import org.basex.query.QueryModule.Permission;
import org.basex.query.QueryModule.Requires;
import org.jzy3d.chart.AWTScreenshotUtils;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.AWTRenderer3d;

public class BaseXModule
{
    @Requires(Permission.READ)
    public Object plot()
    {
        // Define a function to plot
        final Mapper mapper = new Mapper()
        {
            @Override
            public double f(double x, double y)
            {
                return 10 * Math.sin(x / 10) * Math.cos(y / 20);
            }
        };

        // Define range and precision for the function to plot
        final Range range = new Range(-150, 150);
        final int steps = 50;

        // Create a surface drawing that function
        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getXRange()));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);
        surface.setWireframeColor(Color.BLACK);

        // Create a chart and add the surface
        final Chart chart = AWTChartComponentFactory.chart(Quality.Advanced);
        chart.add(surface);
        // chart.screenshot();
        final AWTRenderer3d renderer = (AWTRenderer3d) chart.getCanvas().getRenderer();
        return renderer.getLastScreenshotImage();
    }
}
