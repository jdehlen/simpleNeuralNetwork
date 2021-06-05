package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Network {

	private static final double BIAS = 1.0;
	private int hiddenNodes;
	private int outputNodes;
	private double[][] weightsInputToHidden;
	private double[][] weightsHiddenToOutput;
	private double lerningRate;
	private Random rand = new Random();
	private double[] epochErrors;

	/**
	 * @param inputNodes  number of input nodes
	 * @param hiddenNodes number of hidden nodes
	 * @param outputNodes number of output nodes
	 * @param lerningRate learning rate
	 * 
	 */
	public Network(int inputNodes, int hiddenNodes, int outputNodes, double lerningRate) {
		this.lerningRate = lerningRate;
		this.hiddenNodes = hiddenNodes;
		this.outputNodes = outputNodes;
		this.weightsInputToHidden = new double[hiddenNodes][inputNodes + 1];
		this.weightsHiddenToOutput = new double[outputNodes][hiddenNodes + 1];
		Arrays.stream(weightsInputToHidden).forEach(array -> Arrays.setAll(array, weight -> (rand.nextDouble() - 0.5)));
		Arrays.stream(weightsHiddenToOutput)
				.forEach(array -> Arrays.setAll(array, weight -> (rand.nextDouble() - 0.5)));
	}

	/**
	 * @param input   list of training datasets
	 * @param labels  list of labels
	 * @param epochs  number of epochs
	 * @param epsilon accepted error rate
	 * 
	 */
	public void training(ArrayList<double[]> input, ArrayList<double[]> labels, int epochs, double epsilon) {

		epochErrors = new double[epochs];
		for (int i = 0; i < epochs; i++) {
			double error = 0.0;
			ArrayList<double[]> epochInput = new ArrayList<>(input);
			ArrayList<double[]> epochLabel = new ArrayList<>(labels);
			ArrayList<double[]> inputShuffled = new ArrayList<double[]>();
			ArrayList<double[]> labelShuffled = new ArrayList<double[]>();
			for (int s = 0; s < input.size(); s++) {
				int rnd = rand.nextInt(epochInput.size());
				inputShuffled.add(epochInput.get(rnd));
				labelShuffled.add(epochLabel.get(rnd));
				epochInput.remove(rnd);
				epochLabel.remove(rnd);
			}
			for (int j = 0; j < inputShuffled.size(); j++) {
				error += train(inputShuffled.get(j), labelShuffled.get(j));
			}
			epochErrors[i] = error / input.size();
			if (error / input.size() < epsilon) {
				System.out.println("Success at Epoch: " + i);
				break;
			}
		}
	}

	public double train(double[] input, double[] labels) {

		// adding bias
		double[] inputValues = Arrays.copyOf(input, input.length + 1);
		inputValues[inputValues.length - 1] = BIAS;
		double[] hiddenValues = new double[this.hiddenNodes];

		// calculate hidden from input and weights one
		for (int i = 0; i < hiddenValues.length; i++) {
			for (int j = 0; j < inputValues.length; j++) {
				hiddenValues[i] += weightsInputToHidden[i][j] * inputValues[j];
			}
			hiddenValues[i] = sigmoid(hiddenValues[i]);
		}

		// adding bias
		hiddenValues = Arrays.copyOf(hiddenValues, hiddenValues.length + 1);
		hiddenValues[hiddenValues.length - 1] = BIAS;

		// calculate output from hidden and weightsTwo
		double[] outputValues = new double[outputNodes];
		for (int i = 0; i < outputNodes; i++) {
			for (int j = 0; j < hiddenValues.length; j++) {
				outputValues[i] += weightsHiddenToOutput[i][j] * hiddenValues[j];
			}
		}

		double[] outputErrors = new double[outputValues.length];
		for (int i = 0; i < outputErrors.length; i++) {
			outputErrors[i] = labels[i] - outputValues[i];
		}

		// backpropagation
		// update weights hidden to output
		for (int i = 0; i < outputValues.length; i++) {
			for (int j = 0; j < hiddenValues.length; j++) {
				weightsHiddenToOutput[i][j] += 2 * lerningRate * outputErrors[i] * hiddenValues[j];
			}
		}

		// calc errors hidden to output
		double[] errorsHidden = new double[hiddenValues.length];
		for (int i = 0; i < hiddenValues.length; i++) {
			for (int j = 0; j < outputValues.length; j++) {
				errorsHidden[i] += weightsHiddenToOutput[j][i] * outputErrors[j];
			}
		}

		// update weights input to hidden
		for (int i = 0; i < hiddenNodes; i++) {
			for (int j = 0; j < inputValues.length; j++) {
				weightsInputToHidden[i][j] += lerningRate * errorsHidden[i] * inputValues[j] * 2 * hiddenValues[i] * (1 - hiddenValues[i]);
			}
		}

		double squareErrorSum = 0.0;
		for (double d : outputErrors) {
			squareErrorSum += Math.pow(d, 2);
		}
		return Math.sqrt(squareErrorSum);
	}

	double[] query(double[] input) {

		double[] inputValues = input;
		inputValues = Arrays.copyOf(inputValues, inputValues.length + 1);
		inputValues[inputValues.length - 1] = 1.0;
		double[] hiddenValues = new double[this.hiddenNodes];

		for (int i = 0; i < hiddenValues.length; i++) {
			for (int j = 0; j < inputValues.length; j++) {
				hiddenValues[i] += weightsInputToHidden[i][j] * inputValues[j];
			}
			hiddenValues[i] = sigmoid(hiddenValues[i]);
		}

		// adding bias
		hiddenValues = Arrays.copyOf(hiddenValues, hiddenValues.length + 1);
		hiddenValues[hiddenValues.length - 1] = 1.0;

		// calc output from hidden and weightsTwo
		double[] outputValues = new double[outputNodes];
		for (int i = 0; i < outputNodes; i++) {
			for (int j = 0; j < hiddenValues.length; j++) {
				outputValues[i] += weightsHiddenToOutput[i][j] * hiddenValues[j];
			}
		}
		return outputValues;
	}

	List<Double> getEpochErrors() {
		return DoubleStream.of(this.epochErrors).boxed().collect(Collectors.toList());
	}

	private double sigmoid(double input) {
		return 1 / (1 + Math.pow(Math.E, (-1) * input));
	}

}
