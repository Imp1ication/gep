package gep;

import java.util.Random;

public interface Crossover {
    public Individual[] crossover(Individual[] indivs, int childNum);
}

class RouletteDoublePointCrossover implements Crossover {
    public Individual[] crossover(Individual[] indivs, int childNum) {
        // Calculate relative fitness
        double totalFitness = 0;
        double[] ragularizedFitness = new double[indivs.length];
        double[] relativeFitness = new double[indivs.length];

        for(int i=0; i<indivs.length; ++i) ragularizedFitness[i] = indivs[i].getFitness() + 1;
        for(int i=0; i<indivs.length; ++i) totalFitness += ragularizedFitness[i]; 
        for(int i=0; i<indivs.length; ++i) relativeFitness[i] = ragularizedFitness[i] / totalFitness;

        // Create Roulette
        double[] wheel = new double[indivs.length];

        wheel[0] = relativeFitness[0];
        wheel[indivs.length-1] = 1;
        for(int i=1; i<indivs.length-1; ++i) wheel[i] = relativeFitness[i] + wheel[i-1];

        // Create child
        Individual[] children = new Individual[childNum];
        for (int i = 0; i < childNum; ++i) {
            // Select parents by roulette
            Individual parent1 = null;
            Individual parent2 = null;

            double dart = Math.random();
            for(int j=0; j<wheel.length; ++j) {
                if( dart < wheel[j] ) {
                    parent1 = indivs[j];
                    break;
                }
            }

            dart = Math.random();
            for(int j=0; j<wheel.length; ++j) {
                if( dart < wheel[j] ) {
                    parent2 = indivs[j];
                    break;
                }
            }

            // Crossover
            try {
				children[i] = parent1.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
            children[i].setFitness(-1);

            // Randomly select crossover point
            Random random = new Random();
            int cp1 = random.nextInt(children[i].getGeneExpLenth()) + 1; // 1 ~ geneExpLenth
            int cp2 = random.nextInt(Param.GENE_LEN - cp1 + 1) + cp1;    // cp1 ~ geneLen

            // Swap genes
            children[i].setGeneFragment(cp1, cp2, parent2.getGeneFragment(cp1, cp2));
        }

        return children;
    }
}
