package application;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
import java.util.Optional;
import java.util.stream.Collectors;

public class HomePage extends Application {
    private User user;
    private HostServices hostServices;
    private Stage primaryStage;
    private GridPane candidateGrid;

    public HomePage(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) {
        Locale.setDefault(new Locale("tr", "TR"));

        this.primaryStage = primaryStage;
        this.hostServices = getHostServices();

        primaryStage.setTitle("Voting System - Home");
        primaryStage.setMaximized(true);

        BorderPane root = new BorderPane();
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#87CEEB")),
                new Stop(1, Color.web("#1E90FF")));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #1E90FF);");

        Label headerLabel = new Label("Vote for a Candidate in " + user.getDepartment());
        headerLabel.setFont(new Font("Arial", 28));
        headerLabel.setTextFill(Color.WHITE);
        root.setTop(headerLabel);
        BorderPane.setAlignment(headerLabel, Pos.CENTER);
        BorderPane.setMargin(headerLabel, new Insets(30));

        candidateGrid = new GridPane();
        candidateGrid.setHgap(15);
        candidateGrid.setVgap(15);
        candidateGrid.setPadding(new Insets(20));
        candidateGrid.setAlignment(Pos.CENTER);

        List<Candidate> candidates = VotingManager.getInstance().getCandidatesByDepartment(user.getDepartment());
        int row = 0, col = 0;
        int columns = 3;
        for (Candidate candidate : candidates) {
            VBox candidatePane = createCandidatePane(candidate);
            candidateGrid.add(candidatePane, col, row);
            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }

        candidateGrid.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        for (int i = 0; i < columns; i++) {
            javafx.scene.layout.ColumnConstraints colConstraints = new javafx.scene.layout.ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / columns);
            colConstraints.setHgrow(javafx.scene.layout.Priority.ALWAYS);
            candidateGrid.getColumnConstraints().add(colConstraints);
        }
        for (int i = 0; i < (candidates.size() + columns - 1) / columns; i++) {
            javafx.scene.layout.RowConstraints rowConstraints = new javafx.scene.layout.RowConstraints();
            rowConstraints.setPercentHeight(100.0 / ((candidates.size() + columns - 1) / columns));
            rowConstraints.setVgrow(javafx.scene.layout.Priority.ALWAYS);
            candidateGrid.getRowConstraints().add(rowConstraints);
        }

        root.setCenter(candidateGrid);

        Button displayButton = new Button("View Statistics");
        displayButton.setFont(new Font("Arial", 20));
        displayButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-padding: 12 25;");
        displayButton.setOnMouseEntered(e -> displayButton.setStyle("-fx-background-color: #5A9BD5; -fx-text-fill: white; -fx-padding: 12 25;"));
        displayButton.setOnMouseExited(e -> displayButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-padding: 12 25;"));
        displayButton.setDisable(false);
        displayButton.setVisible(true);
        displayButton.setOnAction(e -> {
            System.out.println("View Statistics button clicked for department: " + user.getDepartment());
            try {
                Stage statsStage = new Stage();
                DisplayPage displayPage = new DisplayPage(user.getDepartment());
                System.out.println("Attempting to start DisplayPage for department: " + user.getDepartment());
                displayPage.start(statsStage);
                System.out.println("DisplayPage started successfully, closing HomePage");
                primaryStage.close();
            } catch (Exception ex) {
                System.err.println("Error opening DisplayPage: " + ex.getMessage());
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to open the statistics page: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        Button finishButton = new Button("Finish");
        finishButton.setFont(new Font("Arial", 20));
        finishButton.setStyle("-fx-background-color: #00008B; -fx-text-fill: white; -fx-padding: 12 25;");
        finishButton.setOnMouseEntered(e -> finishButton.setStyle("-fx-background-color: #0000CD; -fx-text-fill: white; -fx-padding: 12 25;"));
        finishButton.setOnMouseExited(e -> finishButton.setStyle("-fx-background-color: #00008B; -fx-text-fill: white; -fx-padding: 12 25;"));
        finishButton.setOnAction(e -> {
            System.out.println("Finish button clicked, returning to WelcomePage");
            new WelcomePage().start(new Stage());
            primaryStage.close();
        });

        VBox buttonBox = new VBox(20, displayButton, finishButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30));
        root.setBottom(buttonBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        if (VotingManager.getInstance().haveAllUsersInDepartmentVoted(user.getDepartment())) {
            System.out.println("All users in " + user.getDepartment() + " have voted at startup, showing statistics");
            showStatisticsAndWinner();
        }
    }

    private VBox createCandidatePane(Candidate candidate) {
        VBox pane = new VBox(10);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(15));
        pane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-border-color: #4682B4; -fx-border-width: 2;");

        ImageView imageView;
        try {
            Image image = new Image(candidate.getImagePath(), 100, 100, true, true);
            imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            Label errorLabel = new Label("Image not available");
            errorLabel.setFont(new Font("Arial", 12));
            errorLabel.setTextFill(Color.RED);
            imageView = new ImageView();
            pane.getChildren().add(errorLabel);
        }
        pane.getChildren().add(imageView);

        Label nameLabel = new Label(candidate.getName());
        nameLabel.setFont(new Font("Arial", 18));
        nameLabel.setTextFill(Color.BLACK);
        pane.getChildren().add(nameLabel);

        Button detailsButton = new Button("Details");
        detailsButton.setFont(new Font("Arial", 14));
        detailsButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-padding: 6 12;");
        detailsButton.setOnMouseEntered(e -> detailsButton.setStyle("-fx-background-color: #5A9BD5; -fx-text-fill: white; -fx-padding: 6 12;"));
        detailsButton.setOnMouseExited(e -> detailsButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-padding: 6 12;"));
        detailsButton.setOnAction(e -> openDetailsLink(candidate));
        pane.getChildren().add(detailsButton);

        Button voteButton = new Button("Vote");
        voteButton.setFont(new Font("Arial", 14));
        voteButton.setStyle("-fx-background-color: #00008B; -fx-text-fill: white; -fx-padding: 6 12;");
        voteButton.setOnMouseEntered(e -> voteButton.setStyle("-fx-background-color: #0000CD; -fx-text-fill: white; -fx-padding: 6 12;"));
        voteButton.setOnMouseExited(e -> voteButton.setStyle("-fx-background-color: #00008B; -fx-text-fill: white; -fx-padding: 6 12;"));
        voteButton.setDisable(user.hasVoted());
        voteButton.setOnAction(e -> {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Vote");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Are you sure you want to vote for " + candidate.getName() + "?");
            
            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            confirmation.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                System.out.println("User confirmed vote for " + candidate.getName());
                vote(candidate);
                disableAllVoteButtons();
                if (VotingManager.getInstance().haveAllUsersInDepartmentVoted(user.getDepartment())) {
                    System.out.println("All users in " + user.getDepartment() + " have voted, showing statistics");
                    showStatisticsAndWinner();
                }
            } else {
                System.out.println("User canceled vote for " + candidate.getName());
            }
        });
        pane.getChildren().add(voteButton);

        return pane;
    }

    private void disableAllVoteButtons() {
        System.out.println("Disabling all vote buttons");
        for (javafx.scene.Node node : candidateGrid.getChildren()) {
            if (node instanceof VBox) {
                VBox candidatePane = (VBox) node;
                javafx.scene.Node lastChild = candidatePane.getChildren().get(candidatePane.getChildren().size() - 1);
                if (lastChild instanceof Button) {
                    Button voteButton = (Button) lastChild;
                    voteButton.setDisable(true);
                }
            }
        }
    }

    private void openDetailsLink(Candidate candidate) {
        String url = candidate.getDetailsUrl();
        if (url != null && !url.isEmpty()) {
            try {
                System.out.println("Opening details link for " + candidate.getName() + ": " + url);
                hostServices.showDocument(url);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to open the link: " + url);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No link available for this candidate.");
            alert.showAndWait();
        }
    }

    private void vote(Candidate candidate) {
        VotingManager.getInstance().vote(user, candidate);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Vote cast for " + candidate.getName());
        alert.showAndWait();
        System.out.println("Vote cast for " + candidate.getName() + " by " + user.getName());
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

    private void showStatisticsAndWinner() {
        Stage statsStage = new Stage();
        DisplayPage displayPage = new DisplayPage(user.getDepartment());
        displayPage.start(statsStage);
        primaryStage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}