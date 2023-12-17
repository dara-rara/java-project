package org.example.visualization.drawer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static org.jfree.ui.RefineryUtilities.centerFrameOnScreen;

public class PieTaskStatistics extends JFrame {

    private  final Map<Integer, Integer> data;

    public PieTaskStatistics(String title, Map<Integer, Integer> data) {
        super(title);
        this.data = data;
        setContentPane(createPanel());
    }

    public JPanel createPanel() {
        JFreeChart chart = createChart(createDataset());
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(1000, 600));
        return panel;
    }

    private PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (var i : data.keySet()) {
            dataset.setValue(String.format("Баллы: %s", i), data.get(i));
        }
        return dataset;
    }

    private JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Статистика по баллам для задачи",  // chart title
                dataset,             // data
                false,               // no legend
                true,                // tooltips
                false                // no URL generation
        );

        // Определение фона графического изображения
        chart.setBackgroundPaint(new GradientPaint(new Point(0, 0), new Color(20, 20, 20),
                new Point(400, 200), Color.DARK_GRAY));
        // Определение заголовка
        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.CENTER);
        t.setPaint(new Color(240, 240, 240));
        t.setFont(new Font("Arial", Font.BOLD, 16));

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.04);
        plot.setOutlineVisible(false);

        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(2.0f));

        // Настройка меток названий секций
        plot.setLabelFont(new Font("Arial", Font.BOLD, 20));
        plot.setLabelLinkPaint(Color.WHITE);
        plot.setLabelLinkStroke(new BasicStroke(2.0f));
        plot.setLabelOutlineStroke(null);
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelBackgroundPaint(null);

        return chart;
    }

    public static void draw(String title, Map<Integer, Integer> data) {
        PieTaskStatistics statistics = new PieTaskStatistics(title, data);
        statistics.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        statistics.pack();
        centerFrameOnScreen(statistics);
        statistics.setVisible(true);
    }
}