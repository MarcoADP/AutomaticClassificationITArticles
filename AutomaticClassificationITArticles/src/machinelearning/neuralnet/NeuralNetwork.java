package machinelearning.neuralnet;


import machinelearning.readers.PDFReader;
import machinelearning.readers.TextReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

    private List<String> keyWords;
    private List<String> classes;
    private List<List<String>> tokens;
    
    //private List<List<Neuron>> network;
    //private int numInputs;
    //private int numOutputs;
    //private int depth;
    //private Double learningRate;
    //private List<Integer> countLevel;
    
     public NeuralNetwork(File keyWordsFile) {
        keyWords = TextReader.getKeyWords(keyWordsFile.getAbsolutePath());
        this.classes = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.preencheClassesAndTokens();
        this.mostraClasses();
        this.mostraTokens();
        
    }
     
    public void mostraClasses(){
        System.out.println("CLASSES: ");
        for(String s : this.classes){
            System.out.println(s);
        }
    }
    
    public void mostraTokens(){
        for(int i = 0; i < this.tokens.size(); i++){
            System.out.println("Classe => " + this.classes.get(i));
            System.out.println("Tokens");
            for(String s : this.tokens.get(i)){
                System.out.println(s);
            }
            System.out.println("============================================================");
        }
    }
    public void preencheClassesAndTokens(){
        int cont = 0;
        int k = Integer.parseInt(this.keyWords.get(cont++));
        while(k != 0){
            this.classes.add(this.keyWords.get(cont++));
            List<String> list = new ArrayList<>();
            for(int i = cont; i < cont + k; i++){
                list.add(this.keyWords.get(i));
            }
            this.tokens.add(list);
            cont += k;
            k = Integer.parseInt(this.keyWords.get(cont++));
        }
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
    
   /* public NeuralNetwork(){
        this.network = new ArrayList<>();
        this.numInputs = 0;
        this.numOutputs = 0;
        this.learningRate = 0.0;
        this.countLevel = new ArrayList<>();
    }
    
    public void linkNeurons(){
        for(int i = 1; i < this.depth; i++){
            for(int j = 0; j < this.countLevel.get(i); j++){
                this.network.get(i).get(j).setNumInputs(this.network.get(i-1).size());
                //n.setNumInputs(this.network.get(i-1).size());
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
            inputs = lInp;
        }
        return inputs;
    }
    
    void train(List<Double> inputs, List<Double> expected){
        
        List<Double> results = this.query(inputs);
        
        for(int i = 0; i <= results.size(); i++){
            this.network.get(this.network.size()-1).get(i).setError(results.get(i) * (1 - results.get(i)) * (expected.get(i) - results.get(i)));
            this.network.get(this.network.size()-1).get(i).errorAdjust();
        }
        
        for(int i = this.network.size()-2; i >= 0; i--){
            for(int j = 0; j < this.network.size(); j++){
                Double backPropVal = 0.0;
                for(int k = 0; k < this.network.get(i+1).size(); k++){
                    backPropVal += this.network.get(i+1).get(k).getWeights().get(j) * this.network.get(i+1).get(k).getError();
                }
                
                this.network.get(i).get(j).setError(this.network.get(i).get(j).getResult() * (1 - this.network.get(i).get(j).getResult())*backPropVal);
                this.network.get(i).get(j).errorAdjust();
            }
        }
        
    }
    
    
    //==========================================================//
    //                      GETs e SETs                         //  
    //==========================================================//
    
    public void setNodeCountAtLevel(int count, int level){
        //#level = count
        this.countLevel.set(level, count);
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

    */
    
    
    
    
    
    //Provavelmente irá usar em outra classe
   
}
