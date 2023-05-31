package gep;

public interface Crossover {
    public Individual[] crossover(Individual[] indivs, int childNum);
}

class RouletteDoublePointCrossover implements Crossover {
    public Individual[] crossover(Individual[] indivs, int childNum) {
        // Calculate relative fitness
        double totalFitness = 0;
        double[] relativeFitness = new double[indivs.length];

        for(int i=0; i<indivs.length; ++i) totalFitness += indivs[i].getFitness();
        for(int i=0; i<indivs.length; ++i) relativeFitness[i] = indivs[i].getFitness() / totalFitness;

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
            int crossoverPoint1 = 0;
            int crossoverPoint2 = 0;

            while(crossoverPoint1 == crossoverPoint2) {
                crossoverPoint1 = (int)(Math.random() * Param.GENE_LEN);
                crossoverPoint2 = (int)(Math.random() * (Param.GENE_LEN - crossoverPoint1)) + crossoverPoint1;
            }

            // Swap genes
            children[i].setGeneFragment(crossoverPoint1, crossoverPoint2, parent2.getGeneFragment(crossoverPoint1, crossoverPoint2));
        }

        return children;
    }
}
