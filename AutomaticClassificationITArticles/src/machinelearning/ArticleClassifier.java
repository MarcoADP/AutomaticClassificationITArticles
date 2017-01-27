package machinelearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author marco
 */
public class ArticleClassifier {

    private static final String NEURALNET_FILENAME = "./neuralNet";
    private static final String TRAININGSET_FILENAME = "./dataset";
    private static final String PDFS_LIST_FILENAME = "./config/pdflist";
    private static final String TRAINING_PDF_LIST_FILENAME = "./config/trainlist";
    private static final String INPUT_PDFS_DIR = "inputPdfs/";
    private static final String TOKENS_AI_FILEPATH = "./config/tokens";

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        NeuralNetwork neuralNetwork = importOrCreateNeuralNetwork(NEURALNET_FILENAME, TRAININGSET_FILENAME);
        DataSet trainingSet = importOrCreateDataSet(TRAININGSET_FILENAME);
        List<DataSetRow> rows = trainingSet.getRows();
        for (DataSetRow row : rows) {
            double[] input = row.getInput();
            neuralNetwork.setInput(input);
            neuralNetwork.calculate();
            double[] output = neuralNetwork.getOutput();
            System.out.println("Input: " + Arrays.toString(input));
            System.out.println("Output: "+ Arrays.toString(output));
            
            for (int i = 0; i < input.length; i++) {
                
            }
        }
    }

    private static NeuralNetwork importOrCreateNeuralNetwork(String neuralNetFilename, String trainingSetFilename) throws IOException {
        NeuralNetwork neuralNet;
        File neuralNetFile = new File(neuralNetFilename);
        if (neuralNetFile.exists()) {
            System.out.println("Importing Neural Network...");
            neuralNet = NeuralNetwork.createFromFile(neuralNetFile);
            System.out.println("Importing Neural Network...OK");
        } else {
            System.out.println("Creating Neural Network...");
            DataSet trainingSet = importOrCreateDataSet(trainingSetFilename);
            neuralNet = new Perceptron(trainingSet.getInputSize(), trainingSet.getOutputSize(), TransferFunctionType.SIGMOID);
            BackPropagation lr = new BackPropagation();
            lr.setMaxError(0.01);
            lr.setLearningRate(0.5);
            System.out.println("Neural Network Learning...");
            neuralNet.learn(trainingSet, lr);
            System.out.println("Neural Network Learning...OK");
            double totalNetworkError = lr.getTotalNetworkError();
            System.out.println("Neural Network Total Error: " + totalNetworkError);
            int totalIterations = lr.getCurrentIteration();
            System.out.println("Neural Network Total Iterations: " + totalIterations);
            // TODO: Descomentar para persistir a rede neural
//            neuralNet.save(neuralNetFilename);
            System.out.println("Creating Neural Network...OK");
        }
        return neuralNet;
    }

    private static DataSet importOrCreateDataSet(String dataSetFilename) throws IOException {
        DataSet trainingSet;
        File trainingSetFile = new File(dataSetFilename);
        if (trainingSetFile.exists()) {
            System.out.println("Importing TrainingSet...");
            trainingSet = DataSet.load(dataSetFilename);
            System.out.println("Importing TrainingSet...OK");
        } else {
            System.out.println("Creating TrainingSet...");
            Object[] expressionsAreas = parseTokens(TOKENS_AI_FILEPATH);
//            TODO: Trocar para TRAINING_PDF_LIST_FILENAME para usar os PDFs classificados
            trainingSet = parsePdfsToTrainingSet(TRAINING_PDF_LIST_FILENAME, expressionsAreas);
//            TODO: Descomentar a linha abaixo para persistir o conjunto de treinamento;
            trainingSet.save(dataSetFilename);
            System.out.println("Creating TrainingSet...OK");
        }
        return trainingSet;
    }

    private static DataSet parsePdfsToTrainingSet(String pdfListFilename, Object[] expressionsAreas) throws IOException {
        System.out.println("Parsing PDFs to Training Set...");
        BufferedReader bf = new BufferedReader(new FileReader(new File(pdfListFilename)));
        DataSet dataSet = new DataSet(expressionsAreas.length, expressionsAreas.length);
        double[] outputs = new double[expressionsAreas.length];
        String line = bf.readLine();
        while (line != null) {
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

    private static Object[] parseTokens(String filename) throws IOException {
        System.out.println("Parsing Tokens...");
        FileReader fr = new FileReader(filename);
        BufferedReader bf = new BufferedReader(fr);
        String line;
        int numAreas = Integer.parseInt(bf.readLine());
        Object expressionsAreas[] = new Object[numAreas];
        for (int i = 0; i < numAreas; i++) {
            bf.readLine();
            List<String[]> expression = new ArrayList<>();
            line = bf.readLine();
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

    private static double[] parsePDF(File file, double[] outputs, Object[] expressionsAreas) throws IOException {
        double[] counts = new double[expressionsAreas.length];
        String text = new PDFTextStripper().getText(PDDocument.load(file));
        String[] words = text.split("\\W+");
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < expressionsAreas.length; j++) {
                List<String[]> expressions = (ArrayList<String[]>) expressionsAreas[j];
                for (String[] expression : expressions) {
                    int k = 0;
                    for (; k < expression.length; k++) {
                        String token = expression[k];
                        int wordIndex = i + k;
                        if (wordIndex < words.length) {
                            String word = words[wordIndex];
                            if (!token.contains(word.toLowerCase())) {
                                break;
                            }
                        }
                    }
                    if (k == expression.length) {
//                            System.out.println(Arrays.toString(expression));
                        counts[j]++;
                    }
                }
            }
        }
        for (int i = 0; i < counts.length; i++) {
            counts[i] = counts[i] / words.length;
        }
        return counts;
    }

}
