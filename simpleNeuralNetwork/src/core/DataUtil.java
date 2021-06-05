package core;

import java.util.ArrayList;
import java.util.Random;

public class DataUtil {

	static final int NUM_DATA = 300;
	static final double SINE_MIN = 0;
	static final double SINE_MAX = 6.29;

	private static ArrayList<double[]> xorTrainList = new ArrayList<>();
	private static ArrayList<double[]> xorLabelList = new ArrayList<>();
	private static ArrayList<double[]> sineTrainList = new ArrayList<>();
	private static ArrayList<double[]> sineLabelList = new ArrayList<>();
	private static ArrayList<Double> sineQueryList = new ArrayList<>();
	private static Random random = new Random();

	static void createXorData() {
		xorTrainList.add(new double[] { 0.0, 0.0 });
		xorLabelList.add(new double[] { 0.0 });
		xorTrainList.add(new double[] { 1.0, 0.0 });
		xorLabelList.add(new double[] { 1.0 });
		xorTrainList.add(new double[] { 1.0, 1.0 });
		xorLabelList.add(new double[] { 0.0 });
		xorTrainList.add(new double[] { 0.0, 1.0 });
		xorLabelList.add(new double[] { 1.0 });
	}

	static ArrayList<double[]> getXorTrainList() {
		if (xorTrainList.isEmpty()) {
			createXorData();
		}
		return xorTrainList;
	}

	static ArrayList<double[]> getXorLabeleList() {
		if (xorLabelList.isEmpty()) {
			createXorData();
		}
		return xorLabelList;
	}

	static ArrayList<double[]> getTrainingData() {
		if (sineTrainList.isEmpty()) {
			createRandomSineData();
		}
		return sineTrainList;
	}

	static ArrayList<double[]> getTrainingLabels() {
		if (sineLabelList.isEmpty()) {
			createRandomSineData();
		}
		return sineLabelList;
	}

	static ArrayList<Double> getQueryList() {
		if (sineQueryList.isEmpty()) {
			createRandomSineData();
		}
		return sineQueryList;
	}

	private static void createRandomSineData() {

		for (int i = 0; i < NUM_DATA; i++) {
			double x = randomInRange(SINE_MIN, SINE_MAX);
			sineTrainList.add(new double[] { x });
			sineLabelList.add(new double[] { Math.sin(x) });
			sineQueryList.add(randomInRange(SINE_MIN, SINE_MAX));
		}
	}

	private static double randomInRange(double min, double max) {
		double range = max - min;
		double scaled = random.nextDouble() * range;
		double shifted = scaled + min;
		return shifted;
	}

}
