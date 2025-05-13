import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NetworkEnvironment {
    private int[][] neighboursMatrix;
    private double detectionProbability;
    private int startNode;
    private int dataNode;
    private int numOfNodes;
    private int valuesPerNode;
    private boolean attackerDetected;
    private boolean dataNodeCracked;
    private boolean attackerAttackSuccessful;
    private List<int[]> attackerNodesWithMaximalValue;
    private List<int[]> defenderNodesWithMaximalValue;
    private boolean attackerMaxedAllValues;
    private boolean defenderMaxedAllValues;
    private Random rng;
    private int initialSecurity;
    private int[][] attackValues;
    private int[][] defenceValues;
    private int[] ports;

    public NetworkEnvironment(int numOfNodes, int valuesPerNode, int[][] neighboursMatrix, double detectionProbability, int dataNode, int startNode, int initialSecurity) {
        this.numOfNodes = numOfNodes;
        this.valuesPerNode = valuesPerNode;
        this.neighboursMatrix = neighboursMatrix;
        this.detectionProbability = detectionProbability;
        this.dataNode = dataNode;
        this.startNode = startNode;
        this.initialSecurity = initialSecurity;
        this.attackerNodesWithMaximalValue = new ArrayList<>();
        this.defenderNodesWithMaximalValue = new ArrayList<>();
        this.rng = new Random(42);
        this.attackValues = new int[numOfNodes][valuesPerNode];
        this.defenceValues = new int[numOfNodes][valuesPerNode + 1];
        this.ports = new int[] {80, 445, 23, 443}; // Ports associés aux nœuds

        reset();
    }

    public void reset() {
        attackerDetected = false;
        dataNodeCracked = false;
        attackerAttackSuccessful = false;
        attackerNodesWithMaximalValue.clear();
        defenderNodesWithMaximalValue.clear();
        attackerMaxedAllValues = false;
        defenderMaxedAllValues = false;

        attackValues = new int[numOfNodes][valuesPerNode];
        defenceValues = new int[numOfNodes][valuesPerNode + 1];
        for (int i = 0; i < numOfNodes; i++) {
            for (int j = 0; j < valuesPerNode + 1; j++) {
                defenceValues[i][j] = initialSecurity;
            }
        }

        for (int i = 1; i < numOfNodes; i++) {
            int rnd = rng.nextInt(valuesPerNode);
            defenceValues[i][rnd] = 0;
            defenceValues[i][valuesPerNode] = (int) detectionProbability;
        }
        for (int j = 0; j < valuesPerNode + 1; j++) {
            defenceValues[startNode][j] = 0;
        }
    }

    public void doDefenderAction(int node, int defenceType) {
        defenceValues[node][defenceType]++;
        if (defenceValues[node][defenceType] == 10) {
            defenderNodesWithMaximalValue.add(new int[]{node, defenceType});
        }
        if (defenderNodesWithMaximalValue.size() == numOfNodes * (valuesPerNode + 1)) {
            defenderMaxedAllValues = true;
        }
    }

    public double[] doAttackerAction(int node, int attackType) {
        attackerAttackSuccessful = false;
        attackValues[node][attackType]++;
        if (attackValues[node][attackType] == 10) {
            attackerNodesWithMaximalValue.add(new int[]{node, attackType});
        }
        if (attackerNodesWithMaximalValue.size() == numOfNodes * valuesPerNode) {
            attackerMaxedAllValues = true;
        }

        if (attackValues[node][attackType] > defenceValues[node][attackType]) {
            if (node == dataNode) {
                dataNodeCracked = true;
                return new double[]{100, -100};
            } else {
                attackerAttackSuccessful = true;
                return new double[]{0, 0};
            }
        } else {
            if (rng.nextDouble() * 10 < defenceValues[node][valuesPerNode]) {
                attackerDetected = true;
                return new double[]{-100, 100};
            }
            return new double[]{0, 0};
        }
    }

    public boolean terminationCondition() {
        return dataNodeCracked || attackerDetected || attackerMaxedAllValues || defenderMaxedAllValues;
    }

    public boolean isAttackerAttackSuccessful() {
        return attackerAttackSuccessful;
    }

    public int[][] getNeighboursMatrix() {
        return neighboursMatrix;
    }

    public int getNumOfNodes() {
        return numOfNodes;
    }

    public int getValuesPerNode() {
        return valuesPerNode;
    }

    public int getStartNode() {
        return startNode;
    }

    public int getDataNode() {
        return dataNode;
    }

    public List<int[]> getAttackerNodesWithMaximalValue() {
        return attackerNodesWithMaximalValue;
    }

    public List<int[]> getDefenderNodesWithMaximalValue() {
        return defenderNodesWithMaximalValue;
    }

    public int[] getPorts() {
        return ports;
    }

    public int[][] getAttackValues() {
        return attackValues;
    }

    public int[][] getDefenceValues() {
        return defenceValues;
    }

    public void displayActivePorts() {
        System.out.println("Active Ports (simulated netstat -an):");
        for (int i = 0; i < numOfNodes; i++) {
            System.out.println("Node " + i + ": Port " + ports[i]);
        }
    }
}