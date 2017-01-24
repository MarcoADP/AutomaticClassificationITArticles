package machinelearning.neuralnet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Neuron {
    
    private int numInputs;
    private Double learningRate;
    private Double zWeigth;
    private Double result;
    private Double error;
    private List<Double> lastInp;
    private List<Double> weights;
    
    public Neuron(){
        this.learningRate = 0.5;
        this.zWeigth = 0.0;
        this.result = 0.0;
        this.error = 0.0;
        this.lastInp = new ArrayList<>();
        this.weights = new ArrayList<>();
        this.numInputs = 0;     
    }

    public Neuron(Double rate, int numInput){
        this.learningRate = rate;
        this.numInputs = numInput;
        this.zWeigth = 0.0;
        this.result = 0.0;
        this.error = 0.0;
        this.lastInp = new ArrayList<>();
        this.weights = new ArrayList<>();
        this.numInputs = 0;   
    }
    
    Double evaluate(List<Double> input){
        this.lastInp = input;
        this.result = 0.0;
        for(int i = 0; i < input.size(); i++){
            this.result += input.get(i) * this.weights.get(i);
        }
        result = this.sigmoid(this.result + this.zWeigth);
        return this.result;
    }
    
    Double evaluate_without_save(List<Double> input){
        Double result1 = 0.0;
        for(int i = 0; i < input.size(); i++){
            result1 += input.get(i) * this.weights.get(i);
        }
        result1 = sigmoid(result1 + this.zWeigth);
        return result1;
    }
    
    void addWeight(int id, Double w){
        this.weights.set(id, this.weights.get(id) + w);
    }
    
    void resetWeights(int range){
        Random ran = new Random();
        for(int i = 0; i < this.weights.size(); i++){
            Double a = (ran.nextDouble() % (range * 200.0) - (100 * range)) / 100.0;
            this.weights.set(i, a);
        }
        this.zWeigth = (ran.nextDouble() % (range * 200.0) - (100 * range)) / 100.0;
    }
    
    void errorAdjust(){
        for(int i = 0; i < this.weights.size(); i++){
            this.weights.set(i, this.learningRate * error * this.lastInp.get(i));
        }
        this.zWeigth += this.learningRate * this.error;
    }
    
    void print(){
        System.out.println("Neuron: ");
        for(int i = 0; i < this.weights.size(); i++){
            System.out.println("WEIGHT #" + i + " => " + this.weights.get(i));
        }
    }
    
    Double sigmoid(Double num){
        return 1.0 / (1.0 + Math.pow(2.71828, -num));
    }
    
    public Double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(Double learningRate) {
        this.learningRate = learningRate;
    }

    public int getNumInputs() {
        return numInputs;
    }

    public void setNumInputs(int numInputs) {
        this.numInputs = numInputs;
    }

    public Double getzWeigth() {
        return zWeigth;
    }

    public void setzWeigth(Double zWeigth) {
        this.zWeigth = zWeigth;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public Double getError() {
        return error;
    }

    public void setError(Double error) {
        this.error = error;
    }

    public List<Double> getLastInp() {
        return lastInp;
    }

    public void setLastInp(List<Double> lastInp) {
        this.lastInp = lastInp;
    }

    public List<Double> getWeights() {
        return weights;
    }

    public void setWeights(List<Double> weights) {
        this.weights = weights;
    }
    
    
    
}
