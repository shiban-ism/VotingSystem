package application;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DisplayPage extends Application implements Observer {
    private Label totalVotesLabel;
    private BarChart<String, Number> barChart;
    private PieChart pieChart;
    private LineChart<String, Number> lineChart;
    private XYChart.Series<String, Number> barSeries;
    private XYChart.Series<String, Number> lineSeries;
    private ObservableList<PieChart.Data> pieChartData;
    private String department;
    private Button viewWinnerButton;

    public DisplayPage() {
        this.department = null;
    }

    public DisplayPage(String department) {
        this.department = department;
    }

    @Override
    public void start(Stage primaryStage) {
        Locale.setDefault(new Locale("tr", "TR"));

        primaryStage.setTitle("Voting System - Statistics");
        primaryStage.setMaximized(true);

        BorderPane root = new BorderPane();
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#87CEEB")), // Light blue (SkyBlue)
                new Stop(1, Color.web("#1E90FF"))); // Darker blue (DodgerBlue)
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #1E90FF);");

        Label titleLabel = new Label(department != null ? department + " Department Voting Statistics" : "Voting Statistics");
        titleLabel.setFont(new Font("Arial", 36));
        titleLabel.setTextFill(Color.WHITE);

        totalVotesLabel = new Label("Total Votes: 0");
        totalVotesLabel.setFont(new Font("Arial", 24));
        totalVotesLabel.setTextFill(Color.WHITE);

        VBox topBox = new VBox(15, titleLabel, totalVotesLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(30));
        root.setTop(topBox);

        pieChart = new PieChart();
        pieChart.setTitle("Vote Distribution (%)");
        pieChartData = FXCollections.observableArrayList();
        pieChart.setData(pieChartData);
        pieChart.setPrefSize(450, 450);
        pieChart.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-border-color: #1C2526; -fx-border-width: 1; -fx-font-family: 'Arial';");
        VBox pieChartBox = new VBox(pieChart);
        pieChartBox.setAlignment(Pos.TOP_LEFT);
        pieChartBox.setPadding(new Insets(20));
        root.setLeft(pieChartBox);

        CategoryAxis barXAxis = new CategoryAxis();
        barXAxis.setLabel("Candidate");
        NumberAxis barYAxis = new NumberAxis();
        barYAxis.setLabel("Votes");
        barChart = new BarChart<>(barXAxis, barYAxis);
        barChart.setTitle("Vote Counts");
        barSeries = new XYChart.Series<>();
        barChart.getData().add(barSeries);
        barChart.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-border-color: #1C2526; -fx-border-width: 1; -fx-font-family: 'Arial';");
        barChart.setLegendVisible(false);
        barChart.setPrefSize(400, 300);
        barChart.setPadding(new Insets(20));
        barChart.setBarGap(8);
        barChart.setCategoryGap(25);
        barChart.lookupAll(".default-color0.chart-bar").forEach(node -> 
            node.setStyle("-fx-bar-fill: #0000FF; -fx-pref-width: 5px;"));
        root.setCenter(barChart);

        CategoryAxis lineXAxis = new CategoryAxis();
        lineXAxis.setLabel("Candidate");
        NumberAxis lineYAxis = new NumberAxis();
        lineYAxis.setLabel("Votes");
        lineChart = new LineChart<>(lineXAxis, lineYAxis);
        lineChart.setTitle("Vote Trend");
        lineSeries = new XYChart.Series<>();
        lineChart.getData().add(lineSeries);
        lineChart.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-border-color: #1C2526; -fx-border-width: 1; -fx-font-family: 'Arial';");
        lineChart.setLegendVisible(false);
        lineChart.setPrefSize(450, 450);
        lineChart.setPadding(new Insets(20));
        VBox lineChartBox = new VBox(lineChart);
        lineChartBox.setAlignment(Pos.TOP_RIGHT);
        lineChartBox.setPadding(new Insets(20));
        root.setRight(lineChartBox);

        Button exitButton = new Button("Exit");
        exitButton.setFont(new Font("Arial", 20));
        exitButton.setStyle("-fx-background-color: #00008B; -fx-text-fill: white; -fx-padding: 12 25;"); // DarkBlue
        exitButton.setOnMouseEntered(e -> exitButton.setStyle("-fx-background-color: #0000CD; -fx-text-fill: white; -fx-padding: 12 25;")); // MediumBlue on hover
        exitButton.setOnMouseExited(e -> exitButton.setStyle("-fx-background-color: #00008B; -fx-text-fill: white; -fx-padding: 12 25;"));
        exitButton.setOnAction(e -> {
            System.out.println("Exit button clicked, returning to WelcomePage");
            new WelcomePage().start(new Stage());
            primaryStage.close();
        });

        viewWinnerButton = new Button("View the Winner");
        viewWinnerButton.setFont(new Font("Arial", 20));
        viewWinnerButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-padding: 12 25;"); // SteelBlue
        viewWinnerButton.setOnMouseEntered(e -> viewWinnerButton.setStyle("-fx-background-color: #5A9BD5; -fx-text-fill: white; -fx-padding: 12 25;")); // Lighter blue on hover
        viewWinnerButton.setOnMouseExited(e -> viewWinnerButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-padding: 12 25;"));
        viewWinnerButton.setOnAction(e -> {
            List<Candidate> candidates = VotingManager.getInstance().getCandidatesByDepartment(department);
            List<Candidate> tiedWinners = determineTiedWinners(candidates);
            if (!tiedWinners.isEmpty()) {
                System.out.println("View the Winner button clicked, showing tied winners: " + tiedWinners.stream().map(Candidate::getName).collect(Collectors.joining(", ")));
                Stage winnerStage = new Stage();
                new WinnerPage(tiedWinners).start(winnerStage);
                winnerStage.setOnHidden(e2 -> {
                    System.out.println("Winner page closed, resetting department and moving to next");
                    VotingManager.getInstance().resetDepartmentVoting(department);
                    VotingManager.getInstance().moveToNextDepartment();
                    new WelcomePage().start(new Stage());
                });
                primaryStage.close();
            } else {
                System.out.println("No winners to display");
            }
        });

        HBox buttonBox = new HBox(20, viewWinnerButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30));
        root.setBottom(buttonBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        List<Candidate> candidates = department != null
                ? VotingManager.getInstance().getCandidatesByDepartment(department)
                : VotingManager.getInstance().getAllCandidates();
        for (Candidate c : candidates) {
            c.addObserver(this);
        }
        update();
        primaryStage.show();
    }

    private List<Candidate> determineTiedWinners(List<Candidate> candidates) {
        if (candidates.isEmpty()) return List.of();

        int maxVoteCount = candidates.stream()
                .mapToInt(Candidate::getVoteCount)
                .max()
                .orElse(0);

        if (maxVoteCount == 0) return List.of();

        return candidates.stream()
                .filter(c -> c.getVoteCount() == maxVoteCount)
                .collect(Collectors.toList());
    }

    @Override
    public void update() {
        List<Candidate> candidates = department != null
                ? VotingManager.getInstance().getCandidatesByDepartment(department)
                : VotingManager.getInstance().getAllCandidates();
        int totalVotes = candidates.stream()
                .mapToInt(Candidate::getVoteCount)
                .sum();

        System.out.println("Updating statistics for " + (department != null ? department : "all departments"));
        System.out.println("Total votes: " + totalVotes);
        candidates.forEach(c -> System.out.println("Candidate " + c.getName() + ": " + c.getVoteCount() + " votes"));

        totalVotesLabel.setText("Total Votes: " + totalVotes);

        ObservableList<XYChart.Data<String, Number>> barData = FXCollections.observableArrayList();
        for (int i = 0; i < candidates.size(); i++) {
            Candidate c = candidates.get(i);
            int voteCount = c.getVoteCount();
            XYChart.Data<String, Number> data = new XYChart.Data<>(c.getName(), voteCount);
            barData.add(data);

            String color;
            switch (i % 5) {
                case 0: color = "#0000FF"; break;
                case 1: color = "#FF4500"; break;
                case 2: color = "#008000"; break;
                case 3: color = "#800080"; break;
                case 4: color = "#FFFF00"; break;
                default: color = "#0000FF";
            }
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: " + color + "; -fx-pref-width: 5px;");
                }
            });
        }
        barSeries.setData(barData);

        pieChartData.clear();
        for (int i = 0; i < candidates.size(); i++) {
            Candidate c = candidates.get(i);
            int voteCount = c.getVoteCount();
            double percentage = totalVotes > 0 ? (voteCount * 100.0 / totalVotes) : 0;
            PieChart.Data data = new PieChart.Data(c.getName() + " (" + String.format("%.2f%%", percentage) + ")", percentage);
            pieChartData.add(data);

            String color;
            switch (i % 5) {
                case 0: color = "#0000FF"; break;
                case 1: color = "#FF4500"; break;
                case 2: color = "#008000"; break;
                case 3: color = "#800080"; break;
                case 4: color = "#FFFF00"; break;
                default: color = "#0000FF";
            }
            data.getNode().setStyle("-fx-pie-color: " + color + ";");
        }

        ObservableList<XYChart.Data<String, Number>> lineData = FXCollections.observableArrayList();
        for (int i = 0; i < candidates.size(); i++) {
            Candidate c = candidates.get(i);
            int voteCount = c.getVoteCount();
            XYChart.Data<String, Number> data = new XYChart.Data<>(c.getName(), voteCount);
            lineData.add(data);

            String color;
            switch (i % 5) {
                case 0: color = "#0000FF"; break;
                case 1: color = "#FF4500"; break;
                case 2: color = "#008000"; break;
                case 3: color = "#800080"; break;
                case 4: color = "#FFFF00"; break;
                default: color = "#0000FF";
            }
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-background-color: " + color + ";");
                }
            });
        }
        lineSeries.setData(lineData);

        boolean allUsersVoted = department != null && VotingManager.getInstance().haveAllUsersInDepartmentVoted(department);
        List<Candidate> tiedWinners = determineTiedWinners(candidates);
        viewWinnerButton.setDisable(!(allUsersVoted && !tiedWinners.isEmpty()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}