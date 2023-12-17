package org.example.visualization.drawer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static org.jfree.ui.RefineryUtilities.centerFrameOnScreen;

public class LinesThemesStatistics extends JFrame {
    private final Map<String, Double> data;

    public LinesThemesStatistics(String title, Map<String, Double> data) {
        super(title);
        this.data = data;
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new Dimension(1000, 600));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset() {
        var sortedData = data.entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed()).toList();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (var entry : sortedData) {
            dataset.addValue(entry.getValue(), "Series 1", entry.getKey());
        }
        return dataset;
    }

    private JFreeChart createChart(final CategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createLineChart(
                getTitle(),       // chart title
                null,                      // domain axis label
                "Проценты",                // range axis label
                dataset,                   // data
                PlotOrientation.HORIZONTAL,  // orientation
                false,                      // include legend
                true,                      // tooltips
                false                      // urls
        );

        chart.setBackgroundPaint(Color.lightGray);

        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.PINK);
        plot.setRangeGridlinePaint(Color.darkGray);


        LineAndShapeRenderer renderer;
        renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesShapesVisible(0, true);

        renderer.setSeriesPaint(0, Color.darkGray);
        renderer.setSeriesStroke(0, new BasicStroke(3f));
        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.CENTER);
        t.setFont(new Font("Arial", Font.BOLD, 16));
        return chart;
    }

    public static void draw(String title, Map<String, Double> data) {
        final LinesThemesStatistics demo = new LinesThemesStatistics(title, data);
        demo.pack();
        centerFrameOnScreen(demo);
        demo.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        demo.setVisible(true);
    }

}
