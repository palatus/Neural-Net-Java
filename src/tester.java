public class tester {

    public static void main(String[] args){

        System.out.println("\n");

        double[][] testData = {

                {1,1,1,1,0,1,1,0,1,1,0,1,1,1,1}, // 0
                {0,1,0,0,1,0,0,1,0,0,1,0,0,1,0}, // 1
                {1,1,1,0,0,1,1,1,1,1,0,0,1,1,1}, // 2
                {1,1,1,0,0,1,1,1,1,0,0,1,1,1,1}, // 3
                {1,0,1,1,0,1,1,1,1,0,0,1,0,0,1}, // 4
                {1,1,1,1,0,0,1,1,1,0,0,1,1,1,1}, // 5
                {1,1,1,1,0,0,1,1,1,1,0,1,1,1,1}, // 6

                /* 1 1 1
                   1 0 0
                   1 1 1
                   0 0 1
                   1 1 1
                 */

        };

        int generations = 5000;
        Population pop = new Population("AlbÖt");
        pop.populate(new int[][]{{100,40,100},{100,10,100}}, testData[2]);
        pop.detailedDescriptions = true;
        pop.NewGeneration(generations);


        pop.prune();
        pop.trim();
        //pop.Describe();

        System.out.println(pop.name+" Testing Data | "+generations+" Generations\n");

        int i = 0, score = 0;
        for(double[] d : testData){
            int dd = pop.Fittest().predict(d);
            System.out.print(dd);
            if(dd == i){
                score++;
                System.out.println("| +1");
            } else {
                System.out.println("|  X");
            }
            i++;
        }
        System.out.println("\n Prediction Score: "+score);

    }



}
