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

        // TODO: Evaluate population

        generation += 1;
    }

    public void evolve() {
        // TODO: crossover, mutation, selection, evaluate
        // crossover
        Individual[] children = Param.CROSSOVER.crossover(this.indivs, Param.POP_SIZE);

        // mutation
        for (int i = 0; i < children.length; i++) {
            double prob = Math.random();
            if (prob < Param.MUTATION_RATE) {
                children[i] = Param.MUTATOR.mutate(children[i]);
            }
        }
            
        // TODO: evaluate children

        // selection
        Individual[] indivsPool = new Individual[indivs.length + children.length];
        System.arraycopy(indivs, 0, indivsPool, 0, indivs.length);
        System.arraycopy(children, 0, indivsPool, indivs.length, children.length);

        Individual[] newPop = Param.SELECTOR.select(indivsPool, Param.POP_SIZE, Param.ELITISM_RATE);

        // TODO: evaluate newPop


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
	}
        // print pop
}
