package View;

import Model.DbManager;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author apeyt
 */
public class ViewPollution extends JPanel {

    private ChartPanel chartPanel;
    private final DbManager dbManager;

    public ViewPollution(DbManager dbManager) {
        this.dbManager = dbManager;
        initChart();
        validate();
        repaint();
    }

    private void initChart() {
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Pollution Chart",
                "Date (hh:mm)",
                "Air Quality Index (AQI)",
                createDataset(),
                PlotOrientation.VERTICAL,
                false, true, false);

        chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        this.add(chartPanel);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Récupération des données depuis la base
        String query = "SELECT dt, aqi FROM pollution ORDER BY dt ASC";
        try (ResultSet resultSet = dbManager.getCon().createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                int dt = resultSet.getInt("dt");
                int aqi = resultSet.getInt("aqi");

                String time = new SimpleDateFormat("dd-MM-yyyy").format(new Date((long) dt * 1000));
                dataset.addValue(aqi, "AQI", time);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des données : " + e.getMessage());
        }

        return dataset;
    }


    public void updateChart() {
        this.remove(chartPanel);
        initChart();
        validate();
        repaint();
    }
}
