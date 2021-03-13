import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Population {

    public ArrayList<Network> population = new ArrayList<>();
    public float mutationRate = 0.01f;
    public boolean prune = true, detailedDescriptions;
    public String name;

    /*  A Population is a collection of networks

            Here is where we manage the Networks as a list
            A Population is responsible for generation breeding

     */

    public  Population(){
        this("AI "+new Random().nextInt(9001));
    }
    public Population(String name){
        this.name = name;
    }

    public Network Fittest(){
        Sort();
        return population.get(0);
    }

    /*  Removes any networks that are behind the current max fitness level

            - Aims to keep the population at the two best candidates.
                - if there are too many networks of the same fitness, pruning will not be beneficial.
                - Pruned networks are cleared of reference data

            - Will likely log pruned data

     */
    public void prune(){

        if(prune){

            int max = Fittest().getFitness();
            int size = population.size()-1;
            int c = population.get(size).getFitness();

            if(c < max){
                Integer[] remove = getByFitness(c);
                int rm = 0;
                for(Integer i : remove){
                    if(population.size() < 3)
                        return;
                    population.get(i.intValue()-rm).dump();
                    population.remove(i.intValue()-rm);
                    rm++;
                }
                prune();
            }
        }
    }

    /*  Trim groups in population that share the same fitness level */
    public void trim(){
        trim(3,0);
    }
    public void trim(int lowerLimit, int upperCatch){

        int upperLimit = lowerLimit+upperCatch;
        int[] fitcount = new int[Fittest().getFitness()+1];
        for(Network n : population){
            fitcount[n.getFitness()] = fitcount[n.getFitness()]+1;
        }
        for(int i = 0; i<fitcount.length; i++){

            if(fitcount[i] > lowerLimit){
                int target = fitcount[i]-upperLimit;
                for(int j = 0; j<target; j++){
                    removeAtLevel(i);
                }
                break;
            }

        }

    }
    public void removeAtLevel(int level){
        Random r = new Random();
        Integer[] oflevel = getByFitness(level);
        int target = oflevel[r.nextInt(oflevel.length)];
        population.remove(target);
    }



    /*  Get an array of the indices at which a network is of a specified fitness level, within this population */
    public Integer[] getByFitness(int fit){

        ArrayList<Integer> found = new ArrayList<>();

        int i = 0;
        for(Network n : population){
            if(n.getFitness() == fit){
                found.add(i);
            }
            i++;
        }
        return found.toArray(Integer[]::new);

    }

    /*  Breed a new generation n times

            - Will sort the population before breeding
            - Will prune networks of low fitness levels

     */
    public void NewGeneration(int attempts){

        Sort();
        for(int i = 0; i<attempts; i++){
            NewGeneration();
        }
        prune();

    }

    //  Can be tested for failure
    public boolean NewGeneration(){

        if(population.size() < 2){
            return false;
        }

        int p1 = randomParent(), p2 = randomParent();
        if(p1 == -1 || p2 == -1){
            return false;
        }

        Network net1 = population.get(p1).clone();
        Network net2 = population.get(p2).clone();

        Network spawn = breed(net1,net2);

        if(spawn != null)
            population.add(0,spawn);
        else
            return false;

        return true;

    }

    public Network breed(Network one, Network two){

        if(!one.compatible(two)){
            return null;
        }

        Network child = one.clone();

        int i = 0,j;
        for(NGroup ng : child.getNeurons().values()){
            j = 0;
            for(Neuron n : ng.getNeurons()){
                Neuron mixed = mixNeurons(n,two.getGroup(i).getNeurons().get(j));
                n.reflect(mixed);
                j++;
            }
            i++;
        }

        child.Train();

        return child;

    }
    public Neuron mixNeurons(Neuron one, Neuron two){

        Random rand = new Random();
        Neuron child = one.clone();

        for(int i=0; i < child.getWeights().length; i++)
        {
            if(rand.nextBoolean()) {
                child.getWeights()[i] = two.getWeights()[i];
            }

            if(rand.nextDouble() <= mutationRate) {
                child.getWeights()[i] = rand.nextDouble();
            }
        }

        if(rand.nextBoolean()) {
            child.setThreshhold(two.getThreshhold());
        }

        if(rand.nextDouble() <= mutationRate) {
            child.setThreshhold(rand.nextDouble());
        }

        return child;

    }
    public int randomParent() {

        Random rand = new Random();
        int fitnessSum = 0;
        for (Network net : population) {
            fitnessSum += net.getFitness();
        }

        if(fitnessSum < 1){
            return -1;
        }
        int randomSelected = rand.nextInt(fitnessSum);

        int selectedParent = 0;
        for (int tempSum = 0; tempSum <= randomSelected; ) {
            tempSum += population.get(selectedParent).getFitness();

            if (tempSum >= randomSelected) {
                break;
            } else
                selectedParent++;
        }

        return selectedParent;
    }
    public void Sort(){
        population.sort(Collections.reverseOrder());
    }

    /*  Quickly create a network structure from levelCounts

        populate(new int[]{10,5,2},inputs);
            10 neurons -> 5 neurons -> 2 neurons

        OR

        populate(new int[][]{{2,3},{4,5,6}},inputs);
            2 neurons -> 3 neurons
            4 neurons -> 5 neurons -> 6 neurons

     */

    public void populate(int[][] levelCounts, double[] inputs) {

        for(int i = 0; i<levelCounts.length; i++){
            populate(levelCounts[i],inputs);
        }

    }
    public void populate(int[] levelCounts, double[] inputs){

        Network n = new Network();
        for(int i = 0; i<levelCounts.length; i++){
            if(i == 0){
                n.populate(levelCounts[i],i,inputs);
            } else {
                NGroup ng = n.getGroup(i-1);
                n.populate(levelCounts[i], i, ng.getNeurons().size());
            }
        }

        population.add(n);

    }

    public void Describe(){

        Sort();
        System.out.println("POPULATION DESCRIPTION\n");
        int nn = 1;
        for(Network net : population){
            System.out.print("\t|| "+name+" Neural Net "+nn+"\n");
            if(net != null) {
                System.out.print("\t||");
                System.out.print(" "+net + ": Fitness " + net.getFitness()+"\n ");
                if(detailedDescriptions)
                    System.out.print("\t  \\ \n");
                else
                    System.out.println("\t  \\");

                int j = 0;
                for (NGroup group : net.getNeurons().values()) {
                    if(j==0)
                        System.out.print("\t\t\\");
                    else
                        System.out.print("\t\t  |");
                    System.out.println("\t " + group);
                    if(detailedDescriptions)
                        for (Neuron n : group.getNeurons()) {
                            System.out.println("\t\t  |\t\t" + n + " (" + n.getWeights() + ")");
                        }
                    System.out.println("\t\t  |");
                    j++;
                }

            }
            System.out.print("\n");
            nn++;
        }

    }

}
