package gep;

import java.util.HashMap;

public class Param {
    /* Gep */
    public static char TERM[] = { 'a', '?' }; // ? is the placeholder for Dc
    public static final HashMap<Character, Integer> FUNC = new HashMap<Character, Integer>() {{
        put('+', 2);
        put('-', 2);
        put('*', 2);
        put('/', 2);
    }};
    public static final int FUNC_MAX_PARAM = 2;
    public static final int HEAD_LEN = 7;
    public static final int TAIL_LEN = HEAD_LEN * (FUNC_MAX_PARAM - 1) + 1;
    public static final int GENE_LEN = HEAD_LEN + TAIL_LEN + TAIL_LEN;

    /* Dc */
    public static final int DC_LEN = 10;
    public static final double DC_MAX_VALUE = 10.0;
    public static final double DC_MIN_VALUE = 1.0;
    public static final double[] DC_ARRAY = new double[DC_LEN];
    
    static {
        // random dc
        for (int i = 0; i < DC_LEN; i++) {
            DC_ARRAY[i] = Math.random() * (DC_MAX_VALUE - DC_MIN_VALUE) + DC_MIN_VALUE;
        }
    }

    /* Population */
    public static final int POP_SIZE = 10;
    public static final int MAX_GENERATION = 10;

    public static final Crossover CROSSOVER = new RouletteDoublePointCrossover();
    public static final Mutator MUTATOR = new SimpleRandomMutator();
    public static final Selector SELECTOR = new RouletteWheelSelector();

    /* Modify Rate */
    public static final double ELITISM_RATE = 0.3;
    public static final double MUTATION_RATE = 1;

    /* File Path */
    public static final String CHROMOSOME_FILE_PATH = "src/test/java/gep/chromosome.txt";


    /* Public methods */
    public static final char getRandomFunc() {
        int index = (int)(Math.random() * FUNC.size());
        return (char)FUNC.keySet().toArray()[index];
    }

    public static final char getRandomTerm() {
        int index = (int)(Math.random() * TERM.length);
        return TERM[index];
    }

    public static final char getRandomDc() {
        int index = (int)(Math.random() * DC_LEN);
        return (char)('0' + index);
    }

    public static final char getRandomElem() {
        int index = (int)(Math.random() * (FUNC.size() + TERM.length));
        if (index < FUNC.size()) {
            return (char)FUNC.keySet().toArray()[index];
        } else {
            return TERM[index - FUNC.size()];
        }
    }

}

