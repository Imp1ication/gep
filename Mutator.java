package gep;

public interface Mutator {
    public Individual mutate(Individual indiv);
}

class SimpleRandomMutator implements Mutator {
    public Individual mutate(Individual indiv){
        // random point
        int mutatePoint = (int)(Math.random() * Param.GENE_LEN);

        String newElement = "";
        if (mutatePoint == 0) {
            newElement += Param.getRandomFunc();
        } else if (mutatePoint < Param.HEAD_LEN) {
            newElement += Param.getRandomElem();
        } else if (mutatePoint < Param.HEAD_LEN + Param.TAIL_LEN) {
            newElement += Param.getRandomTerm();
        } else {
            newElement += Param.getRandomDc();
        }

        // mutate
        indiv.setGeneFragment(mutatePoint, mutatePoint + 1, newElement);

        return indiv;
    }
}
