package gep;

public interface Mutator {
    public Individual mutate(Individual indiv);
}

class SimpleRandomMutator implements Mutator {
    public Individual mutate(Individual indiv) {
        // random point
        int mutatePoint = (int) (Math.random() * indiv.getGeneExpLenth());

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
        indiv.setFitness(-1);

        return indiv;
    }
}

class RandomTransposMutator implements Mutator {
    public Individual mutate(Individual indiv) {
        // radom transpos length, start point, and replace point
        int tpLen = (int) (Math.random() * Param.HEAD_LEN);
        int tpPoint = (int) (Math.random() * (Param.HEAD_LEN + Param.TAIL_LEN - tpLen));
        int replacePoint = (int) (Math.random() * (Param.HEAD_LEN - tpLen - 1)) + 1;

        // transpos
        Individual newIndiv = null;
        try {
            newIndiv = indiv.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        newIndiv.setFitness(-1);

        String fragment = newIndiv.getGeneFragment(tpPoint, tpPoint + tpLen);
        newIndiv.setGeneFragment(replacePoint, replacePoint + tpLen, fragment);

        return newIndiv;
    }
}

class RandomInverseMutator implements Mutator {
    public Individual mutate(Individual indiv) {
        // random inverse point
        int start = 0, end = 0;
        while (start >= end) {
            start = (int) (Math.random() * Param.HEAD_LEN) + 1;
            end = (int) (Math.random() * Param.HEAD_LEN);
        }

        // inverse
        Individual newIndiv = null;
        try {
            newIndiv = indiv.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        newIndiv.setFitness(-1);
        String fragment = newIndiv.getGeneFragment(start, end);
        newIndiv.setGeneFragment(start, end, new StringBuilder(fragment).reverse().toString());

        return newIndiv;
    }
}
