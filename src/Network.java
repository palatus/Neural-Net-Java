import java.util.HashMap;
import java.util.UUID;

public class Network implements Comparable<Network> {

    enum Type{

        FORWARD_LINK,

    }
    enum TrainingMethod{

        NUMBER_RECOGNITION(new NumberMethod());

        public Trainer controller;
        TrainingMethod(Trainer con){
            controller = con;
        }
        public void run(Network n){
            controller.init(n);
        }

    }

    private HashMap<Integer, NGroup> neurons = new HashMap<>();
    private Type buildType;
    private String id;
    private int fitness;
    private TrainingMethod trainType;

    public Network(){
        this(Type.FORWARD_LINK,TrainingMethod.NUMBER_RECOGNITION);
    }
    public Network(TrainingMethod tm){ this(Type.FORWARD_LINK,tm); }
    public Network(Type t, TrainingMethod tm){

        addGroup(new NGroup(0));
        buildType = t;
        id = UUID.randomUUID().toString();
        trainType = tm;

    }

    public Network clone(){

        Network nn = new Network(this.buildType,this.trainType);

        for(NGroup n: this.neurons.values()){
            nn.addGroup(n.clone());
        }

        return nn;

    }
    public void dump(){
        for(NGroup n: neurons.values()){
            n.setNext(null);
            n.setPrevious(null);
            n.setNeurons(null);
        }
        neurons = null;
    }

    /*

        Will add a neuron using the specified linking type

        -- On Forward linking

          Add a neuron at a specific depth
          Will create NGroups for neurons as long as they are no more than 1 level higher than the current depth



     */
    public void addNeuron(Neuron n, int level){

        switch (this.buildType){

            case FORWARD_LINK:
                forwardAdd(n,level);
                break;

        }

    }

    public void forwardAdd(Neuron n, int level){

        int h = getHeight()+1;

        if(level > h){
            System.err.println("Specified level is greater than network height ("+h+")");
            return;
        }

        // The given neuron needs a new empty group to live in
        if(level == h-1){
            NGroup nnew = new NGroup(h-1);
            addGroup(nnew);
        }

        for(NGroup ng : neurons.values()){
            if(ng.getLevel() == level)
                ng.appendNeuron(n);
        }

    }

    public void populate(int count, int level, double[] inputs){
        populate(count,level,inputs.length);
    }
    public void populate(int count, int level, int inputs){

        for(int i = 0; i<count; i++){
            Neuron n = new Neuron(inputs);
            n.generateWeightSet(inputs);
            addNeuron(n,level);
        }
        Train();

    }
    public int groupSize(int level){
        return getGroup(level).getNeurons().size();
    }

    public void addGroup(NGroup ng){

        for (int i = 0; i<ng.getLevel(); i++){
            Integer iint = Integer.valueOf(i);
            NGroup cGroup = neurons.get(iint);
            if(cGroup != null && cGroup.getLevel() == ng.getLevel()-1){
                cGroup.linkNext(ng);
            }
        }

        neurons.put(ng.getLevel(),ng);
    }

    public void Train()
    {

        trainType.run(this);

    }

    public NGroup getGroup(Integer level){
        return neurons.get(level);
    }

    public int getHeight(){
        return neurons.keySet().size();
    }

    public double[] fireGroup(int level, double[] inputs){

        NGroup group = getGroup(level);
        double[] r = new double[group.getNeurons().size()];

        int i = 0;
        for(Neuron n: group.getNeurons()){
            r[i] = n.fire(inputs);
            i++;
        }

        return r;

    }

    public int compareTo(Network other)
    {
        if(this.fitness == other.fitness)
            return 0;
        else if(this.fitness > other.fitness)
            return 1;
        else
            return -1;
    }

    public int predict(double[] input){

        double[] previous = input;

        for(NGroup n: neurons.values()){

            previous = n.fire(previous);

        }

        // Previous should currently be the final neural group's output
        int eval = evaluate(previous);

        return eval;

    }

    // Evaluate the output from the final layer of the network
    public int evaluate(double[] predictions){

        for(int i = 0; i<predictions.length; i++){
            if(predictions[i] == 1)
                return i;
        }

        return -1;

    }

    /*

        Output a useful description of data about the network.

     */
    public void Describe(){

        System.out.println("Network Type: "+buildType);
        System.out.println("Group Size: "+this.neurons.values().size()+"\n");
        for(NGroup ng : neurons.values()){
            System.out.println("Group Level: "+ng.getLevel()+" - "+"Next: "+ng.getNext());
            for(Neuron n: ng.getNeurons()){
                System.out.println(n.toString());
            }
            System.out.println("--------------");
        }
        System.out.println("Done\n");

    }

    public int getFitness(){
        return this.fitness;
    }

    public HashMap<Integer, NGroup> getNeurons(){
        return this.neurons;
    }

    public boolean compatible(Network other){

        // requires same number of groups
        if(getNeurons().values().size() != other.getNeurons().values().size()){
            return false;
        }
        // requires same number of neurons in each group
        int i = 0;
        for(NGroup ng : this.getNeurons().values()){
            if(ng.getNeurons().size() != other.getNeurons().get(i).getNeurons().size()){
                return false;
            }
            i++;
        }

        return true;

    }

    public String getId() {
        return id;
    }
    public void setFitness(int f){
        fitness = f;
    }

}
