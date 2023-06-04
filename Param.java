package gep;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Param {
    /* File Path */
    // NOTE: change file path
    public static final String BOSS_FILE_PATH = "bot/morris_agent.cpp";
    public static final String PLAYER_FILE_PATH = "gep/bot/main.cpp"; // "bot/jerry_agent.cpp"
    public static final String CHROMOSOME_FILE_PATH = "src/test/java/gep/chromosome.txt";
    public static final String GEP_FILE_PATH = "src/test/java/gep/gep_param.txt";
    public static final String GEP_LOG_FILE_PATH = "gep/log.txt";

    public static String BOSS_AGENT, PLAYER_AGENT;
    static {
        // compile agent
        BOSS_AGENT = compileCpp(BOSS_FILE_PATH, "Boss");
        PLAYER_AGENT = compileCpp(PLAYER_FILE_PATH, "Player");
    }

    /* Gep */
    public static ArrayList<Character> TERM = new ArrayList<>(); // ? is the placeholder for Dc
    public static HashMap<Character, Integer> FUNC = new HashMap<>();
    public static int FUNC_MAX_PARAM = 2;
    public static int HEAD_LEN = 0;
    public static int TAIL_LEN = HEAD_LEN * (FUNC_MAX_PARAM - 1) + 1;
    public static int GENE_LEN = HEAD_LEN + TAIL_LEN + TAIL_LEN;

    // read gep parameters from file if file exists
    static {
        String filePath = GEP_FILE_PATH;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // read term
            line = br.readLine();
            line = br.readLine();

            // split "a,b,c,d,e,?" to array
            String[] termArray = line.split(",");
            for (String term : termArray) {
                TERM.add(term.charAt(0));
            }

            // read func
            line = br.readLine();
            line = br.readLine();

            int funcNum = Integer.parseInt(line);
            FUNC_MAX_PARAM = 0;
            while (funcNum-- > 0) {
                line = br.readLine();
                String[] funcArray = line.split(",");
                FUNC.put(funcArray[0].charAt(0), Integer.parseInt(funcArray[1]));
                FUNC_MAX_PARAM = Math.max(FUNC_MAX_PARAM, Integer.parseInt(funcArray[1]));
            }

            // read head len
            line = br.readLine();
            line = br.readLine();
            HEAD_LEN = Integer.parseInt(line);
            TAIL_LEN = HEAD_LEN * (FUNC_MAX_PARAM - 1) + 1;
            GENE_LEN = HEAD_LEN + TAIL_LEN + TAIL_LEN;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Dc */
    public static final int DC_LEN = 10;
    public static final double DC_MAX_VALUE = 10.0;
    public static final double DC_MIN_VALUE = 0.0;
    public static final double[] DC_ARRAY = new double[DC_LEN];
    
    static {
        // random dc
        for (int i = 0; i < DC_LEN; i++) {
            DC_ARRAY[i] = Math.random() * (DC_MAX_VALUE - DC_MIN_VALUE) + DC_MIN_VALUE;
            DC_ARRAY[i] = Math.round(DC_ARRAY[i] * 1000.0) / 1000.0;
        }
    }

    /* Population */
    public static final int POP_SIZE = 5;
    public static final int MAX_GENERATION = 5;

    public static final Crossover CROSSOVER = new RouletteDoublePointCrossover();
    public static final Mutator MUTATOR[] = {
        new SimpleRandomMutator(),
        new RandomTransposMutator(),
        new RandomInverseMutator(),
    };
    public static final Selector SELECTOR = new RouletteWheelSelector();
    public static final Evaluator EVALUATOR = new BossWinScoreEvaluator();

    /* Modify Rate */
    public static final double ELITISM_RATE = 0.3;
    public static final double MUTATION_RATE = 0.1;

    /* Evaluate Fitness Setting */
    public static final int FIGHT_NUM = 5;
    public static final long GAME_SEED = 53295539L;
    public static final boolean IS_GAME_RANDOM = false;

    /* Public methods */
    public static final int getTermNeed(Character func) {
        return FUNC.containsKey(func) ? FUNC.get(func) : 0;
    }

    public static final char getRandomFunc() {
        int index = (int)(Math.random() * FUNC.size());
        return (char)FUNC.keySet().toArray()[index];
    }

    public static final char getRandomTerm() {
        int index = (int)(Math.random() * TERM.size());
        return TERM.get(index);
    }

    public static final char getRandomDc() {
        int index = (int)(Math.random() * DC_LEN);
        return (char)('0' + index);
    }

    public static final char getRandomElem() {
        int index = (int)(Math.random() * (FUNC.size() + TERM.size()));
        if (index < FUNC.size()) {
            return (char)FUNC.keySet().toArray()[index];
        } else {
            return TERM.get(index - FUNC.size());
        }
    }

	public static String compileCpp(String botFile, String playerName) {
	    System.out.println("Compiling Cpp " + botFile);
	    Process compileProcess;
		try {
			compileProcess = Runtime.getRuntime().exec(
			    "g++ " + botFile + " -o " + playerName
			);
            compileProcess.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	    System.out.println("./"+playerName);
	    return "./" + playerName;
	}
}

