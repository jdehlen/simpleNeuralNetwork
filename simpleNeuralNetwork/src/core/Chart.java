package core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart {

	void showWindow(List<Double> errorXor, List<Double> errorSin, ArrayList<Double> query, ArrayList<Double> output) {
		
		/*
		 * Window
		 */
		JFrame window = new JFrame();
		window.setTitle("Simple Neural Network for XOR and SINE");
		window.setSize(800, 600);
		window.setLayout(new FlowLayout());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*
		 * Error
		 */
		XYSeries errorXorSeries = new XYSeries("ErrorXOR");
		XYSeries errorSinSeries = new XYSeries("ErrorSine");
		for (int i = 0; i < errorXor.size(); i++) {
			errorXorSeries.add(i, errorXor.get(i).doubleValue());
		}
		for (int i = 0; i < errorSin.size(); i++) {
			errorSinSeries.add(i, errorSin.get(i).doubleValue());
		}
		XYSeriesCollection errors = new XYSeriesCollection();
		errors.addSeries(errorXorSeries);
		errors.addSeries(errorSinSeries);
		JFreeChart errorChart = ChartFactory.createXYLineChart("Error per Epoch", "Epoch", "Error", errors);

		/*
		 * sine and output
		 */
		XYSeries sineReferenceSeries = new XYSeries("Sine");
		XYSeries outputSeries = new XYSeries("Output");
		
		for (int i = 0; i < query.size(); i++) {
			sineReferenceSeries.add(query.get(i).doubleValue(), Math.sin(query.get(i).doubleValue()));
			outputSeries.add(query.get(i).doubleValue(), output.get(i).doubleValue());
		}
		
		XYSeriesCollection sineSeriesCollection = new XYSeriesCollection();
		sineSeriesCollection.addSeries(outputSeries);
		sineSeriesCollection.addSeries(sineReferenceSeries);
		//JFreeChart chart = ChartFactory.createXYLineChart("Sine Output", "x", "Sin(x)", sineSeriesCollection);
		JFreeChart chart = ChartFactory.createScatterPlot("Sine Output", "x", "Sin(x)", sineSeriesCollection);
		XYItemRenderer renderer = chart.getXYPlot().getRenderer();
		renderer.setDefaultStroke(new BasicStroke(1.5f));
		renderer.setSeriesPaint(1, Color.magenta);
		renderer.setSeriesPaint(0, Color.blue);
		((AbstractRenderer) renderer).setAutoPopulateSeriesStroke(false);
		
		/*
		 * display
		 */
		window.getContentPane().add(new ChartPanel(errorChart));
		window.getContentPane().add(new ChartPanel(chart));
		window.pack();
		window.setVisible(true);
	}

}
