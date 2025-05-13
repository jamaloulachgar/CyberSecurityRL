import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.Map;

public class Main extends Application {
    private static final int NUM_OF_NODES = 4;
    private static final int VALUES_PER_NODE = 10;
    private static final int[][] NEIGHBOURS_MATRIX = {
        {1, 2}, {0, 3}, {0, 3}, {1, 2}
    };
    private static final int SIMULATIONS = 10;
    private static final int EPISODES = 2000;

    private Circle[] nodes;
    private Label statsLabel;
    private Label percentageLabel;
    private VBox[] nodeContainers; // Conteneurs pour organiser les informations de chaque nœud
    private Text[] vulnerabilityTexts; // Utilisation de Text pour gérer les sauts de ligne
    private VulnerabilityScanner vulnerabilityScanner;

    @Override
    public void start(Stage primaryStage) {
        NetworkEnvironment environment = new NetworkEnvironment(NUM_OF_NODES, VALUES_PER_NODE, NEIGHBOURS_MATRIX, 1, 3, 0, 2);
        Attacker attacker = new Attacker(environment, true, "qlearning");
        Defender defender = new Defender(environment, true, "qlearning");
        Simulation simulation = new Simulation(environment, attacker, defender);
        vulnerabilityScanner = new VulnerabilityScanner(environment);

        // Interface graphique
        Pane pane = new Pane();
        nodes = new Circle[NUM_OF_NODES];
        nodeContainers = new VBox[NUM_OF_NODES];
        vulnerabilityTexts = new Text[NUM_OF_NODES];
        int[] ports = environment.getPorts();

        // Création des conteneurs pour chaque nœud
        for (int i = 0; i < NUM_OF_NODES; i++) {
            nodes[i] = new Circle(50 + i * 150, 100, 20, Color.GRAY); // Espacement accru entre les nœuds
            Label nodeLabel = new Label("Node " + i + "\nPort: " + ports[i]);
            nodeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");

            vulnerabilityTexts[i] = new Text("Vuln: 0%");
            vulnerabilityTexts[i].setWrappingWidth(120); // Largeur maximale pour le wrapping
            vulnerabilityTexts[i].setStyle("-fx-font-size: 11px;");

            // Création d’un VBox pour organiser les informations du nœud
            nodeContainers[i] = new VBox(5); // Espacement de 5 entre les éléments
            nodeContainers[i].getChildren().addAll(nodeLabel, vulnerabilityTexts[i]);
            nodeContainers[i].setLayoutX(30 + i * 150);
            nodeContainers[i].setLayoutY(130);

            pane.getChildren().addAll(nodes[i], nodeContainers[i]);
        }

        // Ajout des lignes entre les nœuds
        for (int i = 0; i < NUM_OF_NODES; i++) {
            for (int j : NEIGHBOURS_MATRIX[i]) {
                Line line = new Line(nodes[i].getCenterX(), nodes[i].getCenterY(), nodes[j].getCenterX(), nodes[j].getCenterY());
                line.setStroke(Color.BLACK);
                pane.getChildren().add(line);
            }
        }

        statsLabel = new Label("Wins: Attacker 0 | Defender 0");
        statsLabel.setLayoutX(10);
        statsLabel.setLayoutY(10);
        statsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        pane.getChildren().add(statsLabel);

        percentageLabel = new Label("Percentages: Attacker 0% | Defender 0%");
        percentageLabel.setLayoutX(10);
        percentageLabel.setLayoutY(30);
        percentageLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        pane.getChildren().add(percentageLabel);

        Scene scene = new Scene(pane, 650, 300); // Augmentation de la taille de la fenêtre
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cyber Security Simulation");
        primaryStage.show();

        // Données pour le graphique
        XYSeries attackerSeries = new XYSeries("Attacker Wins");
        XYSeries defenderSeries = new XYSeries("Defender Wins");
        XYSeries riskSeries = new XYSeries("Global Risk");
        double[] attackerWins = new double[SIMULATIONS];
        double[] defenderWins = new double[SIMULATIONS];
        double[] riskValues = new double[SIMULATIONS];

        for (int sim = 0; sim < SIMULATIONS; sim++) {
            int attackerWinCount = 0;
            int defenderWinCount = 0;
            int ep = 0;
            int durationControlled = 0;

            while (ep < EPISODES) {
                environment.displayActivePorts();
                double[] rewards = simulation.episode(ep);
                if (rewards[0] == 0) continue;

                if (rewards[0] > rewards[1]) {
                    attackerWinCount++;
                    updateNodeColor(environment.getDataNode(), Color.RED);
                    durationControlled++;
                } else {
                    defenderWinCount++;
                    updateNodeColor(attacker.getCurrentNode(), Color.GREEN);
                    durationControlled = 0;
                }
                statsLabel.setText("Wins: Attacker " + attackerWinCount + " | Defender " + defenderWinCount);

                // Mise à jour des vulnérabilités
                vulnerabilityScanner.updateVulnerability(attacker.getCurrentNode(), durationControlled);
                updateVulnerabilityDisplay();

                ep++;
            }

            attackerWins[sim] = (double) attackerWinCount / EPISODES;
            defenderWins[sim] = (double) defenderWinCount / EPISODES;
            riskValues[sim] = calculateGlobalRisk();

            attackerSeries.add(sim + 1, attackerWins[sim]);
            defenderSeries.add(sim + 1, defenderWins[sim]);
            riskSeries.add(sim + 1, riskValues[sim]);
        }

        double meanAttackerWin = calculateMean(attackerWins);
        double meanDefenderWin = calculateMean(defenderWins);
        percentageLabel.setText(String.format("Percentages: Attacker %.2f%% | Defender %.2f%%", 
            meanAttackerWin * 100, meanDefenderWin * 100));

        // Création du graphique avec la courbe de risque
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(attackerSeries);
        dataset.addSeries(defenderSeries);
        dataset.addSeries(riskSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
            "Wins and Risk Over Simulations",
            "Simulation",
            "Percentage",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );

        ChartFrame chartFrame = new ChartFrame("Simulation Results", chart);
        chartFrame.setSize(600, 400);
        chartFrame.setVisible(true);
    }

    private void updateNodeColor(int nodeIndex, Color color) {
        nodes[nodeIndex].setFill(color);
    }

    private void updateVulnerabilityDisplay() {
        Map<Integer, Double> scores = vulnerabilityScanner.getVulnerabilityScores();
        for (int i = 0; i < NUM_OF_NODES; i++) {
            double score = scores.getOrDefault(i, 0.0) * 100;
            String color = score < 20 ? "green" : (score <= 50 ? "orange" : "red");
            vulnerabilityTexts[i].setText(vulnerabilityScanner.getCountermeasureSuggestion(i));
            vulnerabilityTexts[i].setFill(Color.web(color));
        }
    }

    private double calculateGlobalRisk() {
        return vulnerabilityScanner.getVulnerabilityScores().values().stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }

    private double calculateMean(double[] values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    public static void main(String[] args) {
        launch(args);
    }
}