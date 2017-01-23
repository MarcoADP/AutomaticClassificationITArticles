package machinelearning.neuralnet;


import machinelearning.readers.PDFReader;
import machinelearning.readers.TextReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

    private List<String> keyWords;
    
    private List<List<Neuron>> network;
    private int numInputs;
    private int numOutputs;
    private int depth;
    private Double learningRate;
    
    public NeuralNetwork(){
        this.network = new ArrayList<>();
        this.numInputs = 0;
        this.numOutputs = 0;
        this.learningRate = 0.0;
    }
    
    public void linkNeurons(){
        for(int i = 1; i < this.network.size(); i++){
            for(Neuron n : this.network.get(i)){
                n.setNumInputs(this.network.get(i-1).size());
            }
        }
    }
    
    public void print(){
        for(int i = 0; i < this.network.size(); i++){
            System.out.println("LAYER #" + i);
            for(Neuron n : this.network.get(i)){
                n.print();
            }
        }
    }
    
    public void reset(){
        for(List<Neuron> list : this.network){
            for(Neuron n : list){
                n.resetWeights(1);
            }
        }
    }
    
    List<Double> query(List<Double> inputs){
        List<Double> lInp = new ArrayList<>();;
        for(int i = 0; i < inputs.size(); i++){
            for(int j = 0; j < this.network.get(i).size(); j++){
                lInp.set(j, this.network.get(i).get(j).evaluate(inputs));
            }
        }
        return lInp;
    }
    
    public void setLearningRate(Double rate){
        for(List<Neuron> list : this.network){
            for(Neuron neuron : list){
                neuron.setLearningRate(rate);
            }
        }
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public List<List<Neuron>> getNetwork() {
        return network;
    }

    public void setNetwork(List<List<Neuron>> network) {
        this.network = network;
    }

    public int getNumInputs() {
        return numInputs;
    }

    public void setNumInputs(int numInputs) {
        this.numInputs = numInputs;
    }

    public int getNumOutputs() {
        return numOutputs;
    }

    public void setNumOutputs(int numOutputs) {
        this.numOutputs = numOutputs;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    
    
    
    
    
    
    //Provavelmente irá usar em outra classe
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
