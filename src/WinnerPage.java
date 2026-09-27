package application;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class WinnerPage extends Application {
    private List<Candidate> tiedCandidates;

    public WinnerPage(List<Candidate> tiedCandidates) {
        this.tiedCandidates = tiedCandidates;
    }

    @Override
    public void start(Stage primaryStage) {
        Locale.setDefault(new Locale("tr", "TR"));

        primaryStage.setTitle("Voting System - Winner");
        primaryStage.setMaximized(true);

        BorderPane root = new BorderPane();
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#87CEEB")), // Light blue (SkyBlue)
                new Stop(1, Color.web("#1E90FF"))); // Darker blue (DodgerBlue)
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #1E90FF);");

        Label titleLabel = new Label("Winner");
        titleLabel.setFont(new Font("Arial", 48));
        titleLabel.setTextFill(Color.WHITE);
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(30));

        Pane centerPane = new Pane();
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));
        centerBox.setStyle("-fx-border-color: #4682B4; -fx-border-width: 4; -fx-background-color: rgba(0, 0, 0, 0.3); -fx-border-radius: 10;"); // SteelBlue border

        Label congratsLabel = new Label("Congratulations");
        congratsLabel.setFont(new Font("Arial", 60));
        congratsLabel.setTextFill(Color.WHITE);
        centerBox.getChildren().add(congratsLabel);

        HBox imageBox = new HBox(20);
        imageBox.setAlignment(Pos.CENTER);
        for (Candidate candidate : tiedCandidates) {
            ImageView imageView;
            try {
                Image image = new Image(candidate.getImagePath(), 250, 250, true, true);
                imageView = new ImageView(image);
                imageView.setFitWidth(250);
                imageView.setFitHeight(250);
                imageView.setPreserveRatio(true);
            } catch (Exception e) {
                Label errorLabel = new Label("Image not available");
                errorLabel.setFont(new Font("Arial", 16));
                errorLabel.setTextFill(Color.WHITE);
                imageView = new ImageView();
                centerBox.getChildren().add(errorLabel);
            }
            imageBox.getChildren().add(imageView);
        }
        centerBox.getChildren().add(imageBox);

        Label resultLabel;
        if (tiedCandidates.size() > 1) {
            resultLabel = new Label("The candidates have the same number of votes");
        } else {
            resultLabel = new Label("The Winner is:");
        }
        resultLabel.setFont(new Font("Arial", 28));
        resultLabel.setTextFill(Color.WHITE);
        centerBox.getChildren().add(resultLabel);

        HBox nameBox = new HBox(20);
        nameBox.setAlignment(Pos.CENTER);
        for (Candidate candidate : tiedCandidates) {
            Label winnerLabel = new Label(candidate.getName());
            winnerLabel.setFont(new Font("Arial", 36));
            winnerLabel.setTextFill(Color.WHITE);
            nameBox.getChildren().add(winnerLabel);
        }
        centerBox.getChildren().add(nameBox);

        int totalVotes = VotingManager.getInstance()
                .getCandidatesByDepartment(tiedCandidates.get(0).getDepartment())
                .stream()
                .mapToInt(Candidate::getVoteCount)
                .sum();
        int winnerVotes = tiedCandidates.get(0).getVoteCount();
        double percentage = totalVotes > 0 ? (winnerVotes * 100.0 / totalVotes) : 0.0;

        Label votesLabel = new Label("Number of Votes: " + winnerVotes);
        votesLabel.setFont(new Font("Arial", 20));
        votesLabel.setTextFill(Color.WHITE);
        centerBox.getChildren().add(votesLabel);

        Label percentageLabel = new Label("Percentage: " + String.format("%.2f%%", percentage));
        percentageLabel.setFont(new Font("Arial", 20));
        percentageLabel.setTextFill(Color.WHITE);
        centerBox.getChildren().add(percentageLabel);

        centerPane.getChildren().add(centerBox);

        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            Circle confetti = new Circle(5);
            double x = random.nextDouble() * primaryStage.getWidth();
            double y = random.nextDouble() * primaryStage.getHeight();
            confetti.setCenterX(x);
            confetti.setCenterY(y);
            Color[] colors = {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.PURPLE};
            confetti.setFill(colors[random.nextInt(colors.length)]);
            centerPane.getChildren().add(confetti);
        }

        centerBox.layoutXProperty().bind(centerPane.widthProperty().subtract(centerBox.widthProperty()).divide(2));
        centerBox.layoutYProperty().bind(centerPane.heightProperty().subtract(centerBox.heightProperty()).divide(2));
        root.setCenter(centerPane);

        Button exitButton = new Button("Exit");
        exitButton.setFont(new Font("Arial", 20));
        exitButton.setStyle("-fx-background-color: #00008B; -fx-text-fill: white; -fx-padding: 12 25;"); // DarkBlue
        exitButton.setOnMouseEntered(e -> exitButton.setStyle("-fx-background-color: #0000CD; -fx-text-fill: white; -fx-padding: 12 25;")); // MediumBlue on hover
        exitButton.setOnMouseExited(e -> exitButton.setStyle("-fx-background-color: #00008B; -fx-text-fill: white; -fx-padding: 12 25;"));
        exitButton.setOnAction(e -> primaryStage.close());

        Button savePrintButton = new Button("Save/Print");
        savePrintButton.setFont(new Font("Arial", 20));
        savePrintButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-padding: 12 25;"); // SteelBlue
        savePrintButton.setOnMouseEntered(e -> savePrintButton.setStyle("-fx-background-color: #5A9BD5; -fx-text-fill: white; -fx-padding: 12 25;")); // Lighter blue on hover
        savePrintButton.setOnMouseExited(e -> savePrintButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-padding: 12 25;"));
        savePrintButton.setOnAction(e -> savePageAsImage(primaryStage));

        HBox buttonBox = new HBox(20, savePrintButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30));
        root.setBottom(buttonBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void savePageAsImage(Stage stage) {
        try {
            WritableImage snapshot = stage.getScene().snapshot(null);
            int width = (int) snapshot.getWidth();
            int height = (int) snapshot.getHeight();
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int argb = snapshot.getPixelReader().getArgb(x, y);
                    bufferedImage.setRGB(x, y, argb);
                }
            }
            File outputFile = new File("winner_page.png");
            ImageIO.write(bufferedImage, "png", outputFile);
            Label successLabel = new Label("Page saved as winner_page.png in project directory.\nYou can open and print the file.");
            successLabel.setFont(new Font("Arial", 16));
            successLabel.setTextFill(Color.GREEN);
            VBox successBox = new VBox(successLabel);
            successBox.setAlignment(Pos.CENTER);
            successBox.setPadding(new Insets(20));
            Scene successScene = new Scene(successBox, 400, 150);
            Stage successStage = new Stage();
            successStage.setTitle("Success");
            successStage.setScene(successScene);
            successStage.show();
        } catch (Exception e) {
            Label errorLabel = new Label("Failed to save the page: " + e.getMessage());
            errorLabel.setFont(new Font("Arial", 16));
            errorLabel.setTextFill(Color.RED);
            VBox errorBox = new VBox(errorLabel);
            errorBox.setAlignment(Pos.CENTER);
            errorBox.setPadding(new Insets(20));
            Scene errorScene = new Scene(errorBox, 400, 150);
            Stage errorStage = new Stage();
            errorStage.setTitle("Error");
            errorStage.setScene(errorScene);
            errorStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}