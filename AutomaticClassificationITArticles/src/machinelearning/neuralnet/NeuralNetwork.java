package machinelearning.neuralnet;


import machinelearning.readers.PDFReader;
import machinelearning.readers.TextReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class NeuralNetwork {

    private List<String> keyWords;

    public NeuralNetwork(File keyWordsFile) {
        keyWords = TextReader.getKeyWords(keyWordsFile.getAbsolutePath());
    }

    public String classificarTexto(File file) throws IOException {
        List<String> pdfWords = PDFReader.getPDFWords(file);

        String filename = file.getName().replace("pdf", "txt");

        int count = PDFReader.compareTexts(keyWords, pdfWords, "lista2/"+filename);

        if(count > 300){
            return "Inteligência Artificial";
        } else {
            return "NÃO É IA";
        }
    }
}
