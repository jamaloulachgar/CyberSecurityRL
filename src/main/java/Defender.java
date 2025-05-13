import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Defender {
    private NetworkEnvironment environment;
    private boolean learning;
    private String learningAlgorithm;
    private double alpha;
    private double gamma;
    private double epsilon;
    private double[][] QTable;
    private Random rng;

    public Defender(NetworkEnvironment environment, boolean learning, String learningAlgorithm) {
        this.environment = environment;
        this.learning = learning;
        this.learningAlgorithm = learningAlgorithm;
        this.rng = new Random();
        this.QTable = new double[environment.getNumOfNodes()][environment.getValuesPerNode() + 1];

        this.alpha = 0.05;
        this.gamma = 0.91;
        this.epsilon = 0.07;
    }

    // Getter pour le champ learning
    public boolean isLearning() {
        return learning;
    }

    public void reset() {
        // Pas de position à réinitialiser pour le défenseur
    }

    private int[] selectRandomAction() {
        List<int[]> actions = new ArrayList<>();
        for (int node = 0; node < environment.getNumOfNodes(); node++) {
            for (int defenceType = 0; defenceType < environment.getValuesPerNode() + 1; defenceType++) {
                boolean isMaximal = false;
                for (int[] maxAction : environment.getDefenderNodesWithMaximalValue()) {
                    if (maxAction[0] == node && maxAction[1] == defenceType) {
                        isMaximal = true;
                        break;
                    }
                }
                if (!isMaximal) {
                    actions.add(new int[]{node, defenceType});
                }
            }
        }
        return actions.get(rng.nextInt(actions.size()));
    }

    private int[] selectActionEpsilonGreedy() {
        if (rng.nextDouble() < epsilon) {
            return selectRandomAction();
        } else {
            double maxQ = Double.NEGATIVE_INFINITY;
            int bestNode = -1;
            int bestDefenceType = -1;
            for (int node = 0; node < environment.getNumOfNodes(); node++) {
                for (int defenceType = 0; defenceType < environment.getValuesPerNode() + 1; defenceType++) {
                    boolean isMaximal = false;
                    for (int[] maxAction : environment.getDefenderNodesWithMaximalValue()) {
                        if (maxAction[0] == node && maxAction[1] == defenceType) {
                            isMaximal = true;
                            break;
                        }
                    }
                    if (!isMaximal && QTable[node][defenceType] > maxQ) {
                        maxQ = QTable[node][defenceType];
                        bestNode = node;
                        bestDefenceType = defenceType;
                    }
                }
            }
            return new int[]{bestNode, bestDefenceType};
        }
    }

    public int[] selectAction(int episode) {
        if (learning && episode > 5) {
            return selectActionEpsilonGreedy();
        } else {
            return selectRandomAction();
        }
    }

    public void QValuesUpdate(List<int[]> sas, double R) {
        if (!learningAlgorithm.equals("qlearning")) return;

        List<int[]> reversedSas = new ArrayList<>(sas);
        java.util.Collections.reverse(reversedSas);
        double bestQValueNextState = 0;

        for (int i = 0; i < reversedSas.size(); i++) {
            int node = reversedSas.get(i)[1];
            int defenceType = reversedSas.get(i)[2];

            if (i == 0) {
                QTable[node][defenceType] += alpha * (R - QTable[node][defenceType]);
            } else {
                QTable[node][defenceType] += alpha * (gamma * bestQValueNextState - QTable[node][defenceType]);
            }

            double maxQ = Double.NEGATIVE_INFINITY;
            for (int n = 0; n < environment.getNumOfNodes(); n++) {
                for (int d = 0; d < environment.getValuesPerNode() + 1; d++) {
                    if (QTable[n][d] > maxQ) {
                        maxQ = QTable[n][d];
                    }
                }
            }
            bestQValueNextState = maxQ;
        }
    }
}