package gep;

import java.util.HashMap;

public class Param {
    /* Gep */
    public static int HEAD_LEN = 15;
    public static HashMap<Character, Integer> FUNC = new HashMap<Character, Integer>() {{
        put('+', 2);
        put('-', 2);
        put('*', 2);
        put('/', 2);
    }};
    public static int FUNC_MAX_PARAM = 2;

    public static char TERM[] = { 'a' }; 


    /* Dc */
    public static int DC_LEN = 10;
    public static double DC_MAX_VALUE = 10.0;
    public static double DC_MIN_VALUE = 1.0;

    /* Population */
    public static int POP_SIZE = 50;
    public static int MAX_GENERATION = 100;

    /* Modify Rate */
    public static double MUTATION_RATE = 0.1;
    public static double CROSSOVER_RATE = 0.7;

    /* File Path */

}

