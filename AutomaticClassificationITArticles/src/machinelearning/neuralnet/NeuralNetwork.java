package machinelearning.neuralnet;


import java.io.File;

public class NeuralNetwork {

    private File file;

    public NeuralNetwork(File file) {
        this.file = file;
    }

    public String classificarTexto() {
        return "Inteligência Artificial";
    }
}
