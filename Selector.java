package gep;

public interface Selector {
    public Individual[] select(Individual[] indivs, int selectNum, double elitismRate);
}

class RouletteWheelSelector implements Selector {
    public Individual[] select(Individual[] indivs, int selectNum, double elitismRate) {
        // Sort indivs by fitness
        for (int i=0; i<indivs.length-1; ++i) {
            for (int j=0; j<indivs.length-i-1; ++j) {
                if (indivs[j].getFitness() < indivs[j+1].getFitness()) {
                    Individual temp = indivs[j];
                    indivs[j] = indivs[j+1];
                    indivs[j+1] = temp;
                }
            }
        }

        // Create roulette wheel
        double totalFitness = 0;
        double[] regularizedFitness = new double[indivs.length];
        double[] relativeFitness = new double[indivs.length];

        for (int i=0; i<indivs.length; ++i) regularizedFitness[i] = indivs[i].getFitness() + 1;
        for (int i=0; i<indivs.length; ++i) totalFitness += regularizedFitness[i];
        for (int i=0; i<indivs.length; ++i) relativeFitness[i] = regularizedFitness[i] / totalFitness;
        
        double[] wheel = new double[indivs.length];

        wheel[0] = relativeFitness[0];
        wheel[indivs.length-1] = 1;
        for (int i=1; i<indivs.length-1; i++) wheel[i] = relativeFitness[i] + wheel[i-1];

        // Select elite
        Individual[] selected = new Individual[selectNum];
        int eliteNum = (int)(selectNum * elitismRate);

        for (int i=0; i<eliteNum; ++i) {
            try {
                selected[i] = indivs[i].clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        // Select the rest by roulette
        for (int i=eliteNum; i<selectNum; ++i) {
            double dart = Math.random();
            for (int j=0; j<indivs.length; ++j) {
                if (dart < wheel[j]) {
                    try {
                        selected[i] = indivs[j].clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        return selected;
    }
}
