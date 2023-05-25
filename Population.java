package gep;

public class Population implements Cloneable {
    private int generation;
    private Individual[] indivs = new Individual[Param.POP_SIZE];

    public Population() { this.generation = 0; }

    /* Setters and getters */
    public int getGeneration() { return generation; }

    /* Evolve methods */
    public void initialize() {
        for (int i = 0; i < Param.POP_SIZE; i++) {
            this.indivs[i] = new Individual();
        }

        for (int i = 0; i < Param.POP_SIZE; i++) {
            this.indivs[i].setFitness(Param.EVALUATOR.evaluate(this.indivs[i]));
        }

        generation += 1;
    }

    public void evolve() {
        // crossover
        Individual[] children = Param.CROSSOVER.crossover(this.indivs, Param.POP_SIZE);

        // mutation
        for (int i = 0; i < children.length; i++) {
            double prob = Math.random();
            if (prob < Param.MUTATION_RATE) {
                // random choose mutator
                int mutatorIndex = (int)(Math.random() * Param.MUTATOR.length);
                children[i] = Param.MUTATOR[mutatorIndex].mutate(children[i]);
            }
        }
            
        // evaluate children
        for (int i = 0; i < children.length; i++) {
            children[i].setFitness(Param.EVALUATOR.evaluate(children[i]));
        }

        // selection
        Individual[] indivsPool = new Individual[indivs.length + children.length];
        System.arraycopy(indivs, 0, indivsPool, 0, indivs.length);
        System.arraycopy(children, 0, indivsPool, indivs.length, children.length);

        Individual[] newPop = Param.SELECTOR.select(indivsPool, Param.POP_SIZE, Param.ELITISM_RATE);

        // evaluate new population
        System.out.println("Evaluate new population:");
        for (int i = 0; i < newPop.length; i++) {
            newPop[i].setFitness(Param.EVALUATOR.evaluate(newPop[i]));
            System.out.println( i + "/" + newPop.length + " " + newPop[i].getFitness());
        }

        // update population
        this.indivs = newPop;
        generation += 1;
    }

    @Override
    protected Population clone() throws CloneNotSupportedException {
        Population pop = (Population)super.clone();
        pop.indivs = this.indivs.clone();
        for (int i = 0; i < Param.POP_SIZE; i++) {
            pop.indivs[i] = this.indivs[i].clone();
        }
        return pop;
    }

	public static void main(String[] args) {
        


        Population pop = new Population();
        pop.initialize();

        for (int i = 0; i < 10; i++) {
            System.out.println("Generation: " + pop.getGeneration());
            for (int j = 0; j < Param.POP_SIZE; j++) {
                System.out.println(pop.indivs[j] + " " + pop.indivs[j].getFitness());
            }
            System.out.println();
            pop.evolve();
        }
            
        // Population pop = new Population();
        // pop.initialize();
        // System.out.println("Generation: " + pop.getGeneration());
        // for (int i = 0; i < Param.POP_SIZE; i++) {
        //     System.out.println(pop.indivs[i] + " " + pop.indivs[i].getFitness());
        // }
        //
        // //print dc
        // System.out.println("DC:");
        // for (int i = 0; i < Param.DC_LEN; i++) {
        //     System.out.println(Param.DC_ARRAY[i]);
        // }
	}
}
