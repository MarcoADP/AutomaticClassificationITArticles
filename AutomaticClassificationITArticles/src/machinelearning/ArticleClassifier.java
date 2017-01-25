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
import org.neuroph.nnet.Perceptron;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author marco
 */
public class ArticleClassifier {

    private static final String NEURALNET_FILENAME = "./neuralNet";
    private static final String TRAININGSET_FILENAME = "./dataset";
    private static final String PDFS_DIR = "./inputPdfs";
    private static final String TOKENS_AI_FILEPATH = "./config/tokens";

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        NeuralNetwork neuralNetwork = importOrCreateNeuralNetwork(NEURALNET_FILENAME, TRAININGSET_FILENAME);
//        save trained network for future use
//        neuralNetwork.save(NEURALNET_FILENAME);
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
            neuralNet = new Perceptron(trainingSet.getInputSize(), trainingSet.getOutputSize(), TransferFunctionType.LINEAR);
            // TODO: Verificar se necessita mais configurações
//            System.out.println("Neural Network Learning...");
//            neuralNet.learn(trainingSet);
//            System.out.println("Neural Network Learning...OK");
//            neuralNet.save(neuralNetFilename);
//            System.out.println("Creating Neural Network...OK");
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
            trainingSet = parsePdfsToTrainingSet(PDFS_DIR, expressionsAreas);
            trainingSet.save(dataSetFilename);
            System.out.println("Creating TrainingSet...OK");
        }
        return trainingSet;
    }

    private static DataSet parsePdfsToTrainingSet(String dirPath, Object[] expressionsAreas) throws IOException {
        System.out.println("Parsing PDFs to Training Set...");
        File[] dataSetFiles = new File(dirPath).listFiles();
        DataSet dataSet = new DataSet(expressionsAreas.length, expressionsAreas.length);
        double[] outputs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (File file : dataSetFiles) {
            System.out.println(file.getName());
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
                            int workIndex = i + k;
                            if (workIndex < words.length) {
                                String word = words[workIndex];
                                if (!token.equals(word.toLowerCase())) {
                                    break;
                                }
                            }
                        }
                        if (k == expression.length) {
                            counts[j]++;
                        }
                    }
                }
            }
//            Por enquanto, xi = Area(i) Tokens Frequency
//            Sugestão do Igarashi:
//            xi = SUM(Area(i) Tokens Frequency)/Total Words
            dataSet.addRow(counts, outputs);
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

}