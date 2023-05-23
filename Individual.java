package gep;

public class Individual implements Cloneable {
    private Gene gene;
    private double fitness;

    public Individual() {
        this.gene = new Gene();
        this.fitness = 0;
    }

    public Individual(Gene gene) {
        this.gene = gene;
        this.fitness = 0;
    }

    /* Setters and getters */
    public double getFitness() { return this.fitness; }
    public void setFitness(double fitness) { this.fitness = fitness; }

    public String getGeneFragment(int start, int end) {
        return this.gene.getFragment(start, end);
    }
    public String setGeneFragment(int start, int end, String fragment) {
        return this.gene.setFragment(start, end, fragment);
    }


    @Override
    public String toString() { return this.gene.toString(); }

    @Override
    protected Individual clone() throws CloneNotSupportedException {
        Individual indiv = (Individual)super.clone();
        indiv.gene = this.gene.clone();
        return indiv;
    }
}
