import java.util.Random;

public class Neuron {

    private double[] weights;
    private double threshold;

    public Neuron(int inputs){

        weights = generateWeightSet(inputs);

    }

    public double[] generateWeightSet(int size){

        Random rand = new Random();
        threshold = rand.nextDouble() * 2 -1;
        weights = new double[size];
        for (int i = 0; i<weights.length; i++){
            double dd = rand.nextDouble() * 2 -1;
            weights[i] = dd;
        }
        return weights;

    }

    public void setWeights(double[]w){
        weights = w;
    }

    public void reportWeights(){
        for(int i = 0; i<weights.length; i++){
            System.out.println(weights[i]);
        }
        System.out.println("");
    }
    public Neuron init(int s){
        Neuron copy = new Neuron(s);
        copy.generateWeightSet(s);
        return copy;
    }
    public Neuron clone() {
        Neuron copy = new Neuron(this.weights.length);
        copy.threshold = this.threshold;

        for(int i=0; i < weights.length; i++)
            copy.weights[i] = this.weights[i];

        return copy;
    }
    public void reflect(Neuron n){
        this.weights = n.weights;
        this.threshold = n.threshold;
    }

    public double fire(double[] inputs){

        double sum = 0;
        for(int i =0; i<inputs.length; i++){
            sum += weights[i] * inputs[i];
        }
        if(sum >= threshold){
            return 1;
        } else{
            return 0;
        }

    }

    public int size(){
        return this.weights.length;
    }

    public double[] getWeights(){
        return this.weights;
    }

    public double getThreshhold() {
        return threshold;
    }

    public void setThreshhold(double threshhold) {
        this.threshold = threshhold;
    }
}
