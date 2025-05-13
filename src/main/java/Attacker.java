import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Attacker {
    private NetworkEnvironment environment;
    private boolean learning;
    private String learningAlgorithm;
    private double alpha;
    private double gamma;
    private double epsilon;
    private int currentNode;
    private double[][][] QTable;
    private Random rng;

    public Attacker(NetworkEnvironment environment, boolean learning, String learningAlgorithm) {
        this.environment = environment;
        this.learning = learning;
        this.learningAlgorithm = learningAlgorithm;
        this.currentNode = environment.getStartNode();
        this.rng = new Random();
        this.QTable = new double[environment.getNumOfNodes() - 1][environment.getNumOfNodes()][environment.getValuesPerNode()];

        this.alpha = 0.1;
        this.gamma = 0.95;
        this.epsilon = 0.04;
    }

    // Ajout de la méthode isLearning()
    public boolean isLearning() {
        return learning;
    }

    public void reset() {
        this.currentNode = environment.getStartNode();
    }

    private int[] selectRandomAction() {
        List<int[]> actions = new ArrayList<>();
        int[] neighbours = environment.getNeighboursMatrix()[currentNode];
        for (int neighbour : neighbours) {
            for (int attackType = 0; attackType < environment.getValuesPerNode(); attackType++) {
                boolean isMaximal = false;
                for (int[] maxAction : environment.getAttackerNodesWithMaximalValue()) {
                    if (maxAction[0] == neighbour && maxAction[1] == attackType) {
                        isMaximal = true;
                        break;
                    }
                }
                if (!isMaximal) {
                    actions.add(new int[]{neighbour, attackType});
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
            int bestAttackType = -1;
            int[] neighbours = environment.getNeighboursMatrix()[currentNode];
            for (int neighbour : neighbours) {
                for (int attackType = 0; attackType < environment.getValuesPerNode(); attackType++) {
                    boolean isMaximal = false;
                    for (int[] maxAction : environment.getAttackerNodesWithMaximalValue()) {
                        if (maxAction[0] == neighbour && maxAction[1] == attackType) {
                            isMaximal = true;
                            break;
                        }
                    }
                    if (!isMaximal && QTable[currentNode][neighbour][attackType] > maxQ) {
                        maxQ = QTable[currentNode][neighbour][attackType];
                        bestNode = neighbour;
                        bestAttackType = attackType;
                    }
                }
            }
            return new int[]{bestNode, bestAttackType};
        }
    }

    public int[] selectAction(int episode) {
        if (learning && episode > 5) {
            // 10% de chance de lancer une attaque DDoS sur le port 80 (Node 0)
            if (rng.nextDouble() < 0.1) {
                return simulateDDoSAttack();
            }
            return selectActionEpsilonGreedy();
        } else {
            return selectRandomAction();
        }
    }

    private int[] simulateDDoSAttack() {
        System.out.println("DDoS Attack launched on Node 0 (Port 80)!");
        return new int[]{0, 0};
    }

    public void updatePosition(int node) {
        this.currentNode = node;
    }

    public void QValuesUpdate(List<int[]> sas, double R) {
        if (!learningAlgorithm.equals("qlearning")) return;

        List<int[]> reversedSas = new ArrayList<>(sas);
        java.util.Collections.reverse(reversedSas);
        double bestQValueNextState = 0;

        for (int i = 0; i < reversedSas.size(); i++) {
            int s = reversedSas.get(i)[0];
            int node = reversedSas.get(i)[1];
            int attackType = reversedSas.get(i)[2];

            if (i == 0) {
                QTable[s][node][attackType] += alpha * (R - QTable[s][node][attackType]);
            } else {
                QTable[s][node][attackType] += alpha * (gamma * bestQValueNextState - QTable[s][node][attackType]);
            }

            double maxQ = Double.NEGATIVE_INFINITY;
            for (int n = 0; n < environment.getNumOfNodes(); n++) {
                for (int a = 0; a < environment.getValuesPerNode(); a++) {
                    if (QTable[s][n][a] > maxQ) {
                        maxQ = QTable[s][n][a];
                    }
                }
            }
            bestQValueNextState = maxQ;
        }
    }

    public int getCurrentNode() {
        return currentNode;
    }
}