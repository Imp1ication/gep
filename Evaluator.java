package gep;

import java.io.FileWriter;
import java.io.IOException;

import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.simulate.GameResult;

public interface Evaluator {
    public double evaluate(Individual indiv);
}

class BossWinScoreEvaluator implements Evaluator {
    public double evaluate(Individual indiv) {
        // write chromosome file
        try {
            FileWriter fw = new FileWriter(Param.CHROMOSOME_FILE_PATH);

            // chromosome
            fw.write(indiv.toString() + "\n");

            // dc
            for (int i = 0; i < Param.DC_LEN; ++i) {
                fw.write(Double.toString(Param.DC_ARRAY[i])+ "\n");
            }

            fw.flush();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // run game
        double score = 0;

        for (int i = 0; i < Param.FIGHT_NUM; ++i) {
            MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
            gameRunner.setLeagueLevel(3);

            // set seed
            if (Param.IS_GAME_RANDOM) {
                gameRunner.setSeed((long) (Math.random() * 1000000));
            } else {
                gameRunner.setSeed(Param.GAME_SEED);
            }

            // add agents
            gameRunner.addAgent(Param.PLAYER_AGENT);
            gameRunner.addAgent(Param.BOSS_AGENT);

            GameResult result = gameRunner.simulate();

            // parse result
            int turn = (result.views.size() + 1) / 2;
            int winner, losser;
            double winScoreCal = 0;

            // get winner and losser
            if (result.scores.get(0) > result.scores.get(1)) {
                winner = 0;
                losser = 1;
            } else {
                winner = 1;
                losser = 0;
            }

            // calculate winner score
            switch (result.scores.get(winner)) {
                case 1:
                    // Complete win: win turns + 220
                    winScoreCal = (220 - turn) + 220;
                    break;
                case 3:
                case 2:
                    // Health win: win health + 220
                    winScoreCal = result.scores.get(winner) - result.scores.get(losser) + 220;
                    break;
                default:
                    // Mana win: win mana
                    winScoreCal = result.scores.get(winner) - result.scores.get(losser);
                    break;
            }

            // if player win, score is positive, else negative
            winScoreCal /= 440;
            winScoreCal = (winner == 0) ? winScoreCal : winScoreCal * -1;

            System.out.println("  Fight " + (i + 1) + "/" + Param.FIGHT_NUM + ":\t" + winScoreCal);
            score += winScoreCal;
        }

        // calculate fitness
        double fitness = (score + indiv.getFitness()) / (Param.FIGHT_NUM + 1);
        fitness = Math.round(fitness * 1000.0) / 1000.0;

        return fitness;
    }
}
