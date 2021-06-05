package core;

import java.util.ArrayList;
import java.util.List;

public class Main {

	private static ArrayList<double[]> xorTrainList = DataUtil.getXorTrainList();
	private static ArrayList<double[]> xorLabelList = DataUtil.getXorLabeleList();
	private static ArrayList<double[]> sineTrainList = DataUtil.getTrainingData();
	private static ArrayList<double[]> sineLabelList = DataUtil.getTrainingLabels();
	private static ArrayList<Double> sineQueryList = DataUtil.getQueryList();
	private static ArrayList<Double> sineQueryResultList = new ArrayList<>();

	public static void main(String[] args) {
		
		/*
		 * XOR
		 */
		System.out.println("XOR");
		Network network = new Network(2, 2, 1, 0.1);
		network.training(xorTrainList, xorLabelList, 1300, 0.01);
		List<Double> xorErrors = network.getEpochErrors();
		System.out.println("Result 0/0: " + network.query(new double[] { 0.0, 0.0 })[0]);
		System.out.println("Result 1/1: " + network.query(new double[] { 1.0, 1.0 })[0]);
		System.out.println("Result 1/0: " + network.query(new double[] { 1.0, 0.0 })[0]);
		System.out.println("Result 0/1: " + network.query(new double[] { 0.0, 1.0 })[0]);

		/*
		 * Sinus
		 */
		System.out.println("SINUS");
		System.out.println("Sinus training data: " + sineTrainList.size());
		System.out.println("Sinus test data: " + sineQueryList.size());
		network = new Network(1, 3, 1, 0.07);
		network.training(sineTrainList, sineLabelList, 1300, 0.01);
		List<Double> sinErrors = network.getEpochErrors();
		for (Double queryValue : sineQueryList) {
			Double res = network.query(new double[] { queryValue })[0];
			sineQueryResultList.add(res);
		}
		new Chart().showWindow(xorErrors, sinErrors, sineQueryList, sineQueryResultList);
	}

}
