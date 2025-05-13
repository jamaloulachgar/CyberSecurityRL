import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private NetworkEnvironment environment;
    private Attacker attacker;
    private Defender defender;
    private VulnerabilityScanner vulnerabilityScanner;

    public Simulation(NetworkEnvironment environment, Attacker attacker, Defender defender) {
        this.environment = environment;
        this.attacker = attacker;
        this.defender = defender;
        this.vulnerabilityScanner = new VulnerabilityScanner(environment);
    }

    public double[] episode(int episodeNumber) {
        environment.reset();
        attacker.reset();
        defender.reset();

        List<int[]> attackerSas = new ArrayList<>();
        List<int[]> defenderSas = new ArrayList<>();
        int durationControlled = 0;

        while (true) {
            int[] defenderAction = defender.selectAction(episodeNumber);
            int[] attackerAction = attacker.selectAction(episodeNumber);

            environment.doDefenderAction(defenderAction[0], defenderAction[1]);
            double[] rewards = environment.doAttackerAction(attackerAction[0], attackerAction[1]);

            attackerSas.add(new int[]{attacker.getCurrentNode(), attackerAction[0], attackerAction[1]});
            defenderSas.add(new int[]{0, defenderAction[0], defenderAction[1]});

            if (environment.isAttackerAttackSuccessful()) {
                attacker.updatePosition(attackerAction[0]);
                durationControlled++; // Incrément de la durée de contrôle
                vulnerabilityScanner.updateVulnerability(attacker.getCurrentNode(), durationControlled);
            } else {
                durationControlled = 0; // Réinitialisation si l’attaque échoue
            }

            if (environment.terminationCondition()) {
                double attackerReward = 0;
                double defenderReward = 0;

                if (environment.getAttackerNodesWithMaximalValue().size() == environment.getNumOfNodes() * environment.getValuesPerNode() ||
                    environment.getDefenderNodesWithMaximalValue().size() == environment.getNumOfNodes() * (environment.getValuesPerNode() + 1)) {
                    return new double[]{0, 0};
                }

                if (environment.getDataNode() == attackerAction[0] && rewards[0] == 100) {
                    attackerReward = 100;
                    defenderReward = -100;
                } else if (rewards[1] == 100) {
                    attackerReward = -100;
                    defenderReward = 100;
                }

                if (attacker.isLearning()) {
                    attacker.QValuesUpdate(attackerSas, attackerReward);
                }
                if (defender.isLearning()) {
                    defender.QValuesUpdate(defenderSas, defenderReward);
                }

                return new double[]{attackerReward, defenderReward};
            }
        }
    }
}