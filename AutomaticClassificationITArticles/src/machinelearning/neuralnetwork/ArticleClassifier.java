package machinelearning.neuralnetwork;

import javafx.util.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.util.TransferFunctionType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;


public class ArticleClassifier {

    private static final String NEURALNET_FILENAME = "./neuralNet";
    private static final String TRAININGSET_FILENAME = "./dataset";
    private static final String PDFS_LIST_FILENAME = "./config/pdflist";
    private static final String TRAINING_PDF_LIST_FILENAME = "./config/trainlist";
    private static final String INPUT_PDFS_DIR = "inputPdfs/";
    private static final String TOKENS_AI_FILEPATH = "./config/tokens";
    private static final int TOTAL_HIDDEN_LAYERS = 15;

    private NeuralNetwork neuralNetwork;

    private Map<Integer, String> categories;
    private Object[] expressionsAreas;

    public ArticleClassifier() throws IOException {
        expressionsAreas = parseTokens(TOKENS_AI_FILEPATH);
        neuralNetwork = importOrCreateNeuralNetwork(NEURALNET_FILENAME, TRAININGSET_FILENAME);
    }

    public Pair<String, double[]> classifyText(File file) throws IOException {
        double[] input = parsePDF(file, null, expressionsAreas);
        neuralNetwork.setInput(input);

        System.out.println("Calculating neural network for: " + file.getName());
        neuralNetwork.calculate();

        double[] output = neuralNetwork.getOutput();

        System.out.println("Input: " + Arrays.toString(input));
        System.out.println("Output: " + Arrays.toString(output));

        if (true) {
            return new Pair<>("Inteligência Artificial", output);
        }

        return new Pair<>("Outros", output);
    }

    private NeuralNetwork importOrCreateNeuralNetwork(String neuralNetFilename, String trainingSetFilename) throws IOException {
        NeuralNetwork neuralNet;
        File neuralNetFile = new File(neuralNetFilename);

        if (neuralNetFile.exists()) {
            System.out.println("Importing Neural Network...");
            neuralNet = NeuralNetwork.createFromFile(neuralNetFile);
            System.out.println("Importing Neural Network...OK");
        } else {
            System.out.println("Creating Neural Network...");
            DataSet trainingSet = importOrCreateDataSet(trainingSetFilename);

            neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, trainingSet.getInputSize(), TOTAL_HIDDEN_LAYERS, trainingSet.getOutputSize());

            MomentumBackpropagation lr = new MomentumBackpropagation();
            lr.setMomentum(0.5);
            lr.setMaxError(0.05);
            lr.setLearningRate(0.25);

            System.out.println("Neural Network Learning...");
            neuralNet.learn(trainingSet, lr);
            System.out.println("Neural Network Learning...OK");

            double totalNetworkError = lr.getTotalNetworkError();
            System.out.println("Neural Network Total Error: " + totalNetworkError);

            int totalIterations = lr.getCurrentIteration();
            System.out.println("Neural Network Total Iterations: " + totalIterations);

            // TODO: Descomentar para persistir a rede neural
            neuralNet.save(neuralNetFilename);
            System.out.println("Creating Neural Network...OK");
        }

        return neuralNet;
    }

    private DataSet importOrCreateDataSet(String dataSetFilename) throws IOException {
        DataSet trainingSet;
        File trainingSetFile = new File(dataSetFilename);

        if (trainingSetFile.exists()) {
            System.out.println("Importing TrainingSet...");
            trainingSet = DataSet.load(dataSetFilename);
            System.out.println("Importing TrainingSet...OK");
        } else {
            System.out.println("Creating TrainingSet...");
            trainingSet = parsePdfsToTrainingSet(TRAINING_PDF_LIST_FILENAME, expressionsAreas);
            trainingSet.save(dataSetFilename);
            trainingSet.saveAsTxt(dataSetFilename + ".csv", ",");
            System.out.println("Creating TrainingSet...OK");
        }

        return trainingSet;
    }

    private DataSet parsePdfsToTrainingSet(String pdfListFilename, Object[] expressionsAreas) throws IOException {
        System.out.println("Parsing PDFs to Training Set...");

        DataSet dataSet = new DataSet(expressionsAreas.length, expressionsAreas.length);

        BufferedReader bf = new BufferedReader(new FileReader(new File(pdfListFilename)));
        String line = bf.readLine();

        while (line != null) {
            double[] outputs = new double[expressionsAreas.length];
            String[] splitted = line.split(" ");

            String filename = splitted[0];

            for (int i = 1; i < splitted.length; i++) {
                outputs[i - 1] = Double.parseDouble(splitted[i]);
            }

            File file = new File(INPUT_PDFS_DIR + filename);
            System.out.println(filename);
            
            double[] inputs = parsePDF(file, outputs, expressionsAreas);
            dataSet.addRow(inputs, outputs);
            line = bf.readLine();
        }
        System.out.println("Parsing PDFs to Training Set...OK");
        return dataSet;
    }

    private Object[] parseTokens(String filename) throws IOException {
        System.out.println("Parsing Tokens...");

        FileReader fr = new FileReader(filename);
        BufferedReader bf = new BufferedReader(fr);

        int numAreas = Integer.parseInt(bf.readLine());
        Object expressionsAreas[] = new Object[numAreas];

        categories = new HashMap<>();

        for (int i = 0; i < numAreas; i++) {
            String category = bf.readLine();
            if (category != null) {
                categories.put(i, category.substring(1, category.lastIndexOf(']')));
            }

            List<String[]> expression = new ArrayList<>();

            String line = bf.readLine();
            while (line != null && !line.isEmpty()) {
                String[] tokens = line.split(" ");
                expression.add(tokens);
                line = bf.readLine();
            }

            System.out.println(Arrays.deepToString(expression.toArray()));
            expressionsAreas[i] = expression;
        }

        System.out.println("Parsing Tokens...OK");
        return expressionsAreas;
    }

    private double[] parsePDF(File file, double[] outputs, Object[] expressionsAreas) throws IOException {
        double[] counts = new double[expressionsAreas.length];
        String text = new PDFTextStripper().getText(PDDocument.load(file));
        String[] words = text.split("\\W+");
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < expressionsAreas.length; j++) {
                List<String[]> expressions = (ArrayList<String[]>) expressionsAreas[j];
                for (String[] expression : expressions) {
                    if (i + expression.length < words.length) {
                        String[] expressionFromWords = Arrays.copyOfRange(words, i, i + expression.length);
                        counts[j] += countOrNot(expression, expressionFromWords);
                    }
                }
            }
        }

        for (int i = 0; i < counts.length; i++) {
            counts[i] = counts[i] / words.length;
        }

        return counts;
    }

    private int countOrNot(String[] expression, String[] expressionFromWords) {
        for (int i = 0; i < expression.length; i++) {
            String token = expression[i];
            String word = expressionFromWords[i];
            if (!word.toLowerCase().equals(token) && !word.toLowerCase().equals(token + 's')) {
                return 0;
            }
        }
        return 1;
    }

    public Map<Integer, String> getCategories() {
        return categories;
    }
}
