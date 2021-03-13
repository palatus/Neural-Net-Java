import java.util.ArrayList;

public class NGroup {

    private NGroup previous, next;
    private int level;
    private ArrayList<Neuron> neurons = new ArrayList<>();

    public NGroup(int level){
        this.level = level;
    }

    public void linkNext(NGroup gOther){
        next = gOther;
        gOther.setPrevious(this);
    }
    public void linkPrevious(NGroup gOther){
        previous = gOther;
        gOther.setNext(this);
    }

    public NGroup clone(){

        NGroup ng = new NGroup(this.level);

        for(Neuron n: neurons){
            ng.appendNeuron(n.init(n.getWeights().length));
        }
        return ng;

    }

    /*  Gets array of all neuron fire returns within the group
     */
    public double[] fire(double[] inputs){

        double[] all = new double[neurons.size()];

        int i = 0;
        for (Neuron n: neurons){
            all[i] = n.fire(inputs);
            i++;
        }

        /*
        System.out.println("Group Returns");
        for(double d: all){
            System.out.print(d+" ");
        }
        System.out.println("");
        */

        return all;

    }

    public void appendNeuron(Neuron n){
        neurons.add(n);
    }

    public NGroup getNext() {
        return next;
    }

    public void setNext(NGroup next) {
        this.next = next;
    }
    public NGroup getPrevious() {
        return previous;
    }

    public void setPrevious(NGroup previous) {
        this.previous = previous;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<Neuron> getNeurons(){
        return neurons;
    }
    public void setNeurons(ArrayList an){
        neurons = an;
    }
}
