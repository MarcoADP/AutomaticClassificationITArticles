package machinelearning.neuralnet;

import java.util.ArrayList;
import java.util.List;

public class Synapse {
    Neuron parent;
    List<Neuron> outputs;
    Double value;
    
    public Synapse(Neuron n){
        this.parent = n;
        this.outputs = new ArrayList<>();
    }
    
    public void addOutput(Neuron n){
        this.outputs.add(n);
    }
    
    public void setValue(Double Value){
        this.value = Value;
        for(int i = 0; i < outputs.size(); i++){
            //Saida do Neuronio -> entrada do outro
        }
    }
}
