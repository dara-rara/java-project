package org.example.visualization.drawer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static org.jfree.ui.RefineryUtilities.centerFrameOnScreen;

public class BarCitiesStatistics extends JFrame {
    private final Map<String, Integer> data;

    public BarCitiesStatistics(String title, Map<String, Integer> data) {
        super(title);
        this.data = data;
        setContentPane(createPanel());
    }

    public JPanel createPanel() {
        CategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(1000, 600));
        return chartPanel;
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (var i : data.keySet()) {
            dataset.addValue(data.get(i), i, i);
        }
        return dataset;
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Статистика городов студентов, записанных на курс",
                null,                    // x-axis label
                "Количество студентов",                 // y-axis label
                dataset, PlotOrientation.HORIZONTAL, true, true, false);
        chart.setBackgroundPaint(Color.white);
        final CategoryPlot plot = chart.getCategoryPlot();
        ((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(-3);
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);
        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.CENTER);
        t.setFont(new Font("Arial", Font.BOLD, 16));
        plot.setBackgroundPaint(Color.darkGray);
        return chart;
    }

    public static void draw(String title, Map<String, Integer> data) {
        BarCitiesStatistics statistics = new BarCitiesStatistics(title, data);
        statistics.pack();
        statistics.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        centerFrameOnScreen(statistics);
        statistics.setVisible(true);
    }
}

