public class NumberMethod implements Trainer {


    public NumberMethod() {
    }

    public void init(Network n) {

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
                   1 0 1
                   1 1 1
                 */

        };

        n.setFitness(0);
        int result;

        for(int i = 0; i<testData.length; i++){
            result = n.predict(testData[i]);
            if(result == i){
                n.setFitness(n.getFitness()+1);
            }
        }

    }
}
