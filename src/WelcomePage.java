package application;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WelcomePage extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Voting System - Login");
        primaryStage.setMaximized(true);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#87CEEB")), // Light blue (SkyBlue)
                new Stop(1, Color.web("#1E90FF"))); // Darker blue (DodgerBlue)
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #1E90FF);");

        
        ImageView imageView;
        try {
            Image image = new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_cWU6FDzyZgOCNBG7J6roldWMufynrHhVOA&s", 300, 300, true, true);
            imageView = new ImageView(image);
            imageView.setFitWidth(300);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            Label errorLabel = new Label("Image not available");
            errorLabel.setFont(new Font("Arial", 20));
            errorLabel.setTextFill(Color.WHITE);
            imageView = new ImageView();
            root.getChildren().add(errorLabel);
        }

        Label titleLabel = new Label("Welcome To Bahcesehir University Voting System");
        titleLabel.setFont(new Font("Arial", 36));
        titleLabel.setTextFill(Color.WHITE);

        Label subtitleLabel = new Label("Your voice matters!");
        subtitleLabel.setFont(new Font("Arial", 24));
        subtitleLabel.setTextFill(Color.WHITE);

        Label idLabel = new Label("Enter Your ID:");
        idLabel.setFont(new Font("Arial", 20));
        idLabel.setTextFill(Color.WHITE);

        TextField idField = new TextField();
        idField.setPromptText("e.g., 1001");
        idField.setMaxWidth(300);
        idField.setFont(new Font("Arial", 16));
        idField.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");

        Label nameLabel = new Label("Enter Your Name:");
        nameLabel.setFont(new Font("Arial", 20));
        nameLabel.setTextFill(Color.WHITE);

        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Alice");
        nameField.setMaxWidth(300);
        nameField.setFont(new Font("Arial", 16));
        nameField.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");

        Button loginButton = new Button("Login");
        loginButton.setFont(new Font("Arial", 18));
        loginButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-padding: 12 25;"); // SteelBlue for Login button
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #5A9BD5; -fx-text-fill: white; -fx-padding: 12 25;")); // Lighter blue on hover
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-padding: 12 25;"));
        loginButton.setPrefWidth(300);
        loginButton.setOnAction(e -> {
            String id = idField.getText();
            String name = nameField.getText();
            User user = VotingManager.getInstance().authenticate(id);
            if (user != null && user.getName().equalsIgnoreCase(name)) {
                if (VotingManager.getInstance().haveAllUsersInDepartmentVoted(user.getDepartment())) {
                    System.out.println("All users in " + user.getDepartment() + " have voted, showing statistics");
                    showStatisticsAndWinner(user.getDepartment(), primaryStage);
                } else {
                    new HomePage(user).start(new Stage());
                    primaryStage.close();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid ID or Name. Please try again.");
                alert.showAndWait();
            }
        });

        Button helpButton = new Button("Help");
        helpButton.setFont(new Font("Arial", 18));
        helpButton.setStyle("-fx-background-color: #00008B; -fx-text-fill: white; -fx-padding: 6 12;"); // DarkBlue
        helpButton.setOnMouseEntered(e -> helpButton.setStyle("-fx-background-color: #0000CD; -fx-text-fill: white; -fx-padding: 6 12;")); // MediumBlue on hover
        helpButton.setOnMouseExited(e -> helpButton.setStyle("-fx-background-color: #00008B; -fx-text-fill: white; -fx-padding: 6 12;"));
        helpButton.setPrefWidth(300);
        helpButton.setOnAction(e -> showHelpDialog());

        root.getChildren().addAll(imageView, titleLabel, subtitleLabel, idLabel, idField, nameLabel, nameField, loginButton, helpButton);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
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

    private void showStatisticsAndWinner(String department, Stage primaryStage) {
        Stage statsStage = new Stage();
        DisplayPage displayPage = new DisplayPage(department);
        displayPage.start(statsStage);
        primaryStage.close();
    }

    private void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help - How to Use the Voting System");
        alert.setHeaderText("Instructions for Using the Bahcesehir University Voting System");
        alert.setContentText(
                "1. **Login**: Enter your ID (e.g., 1001) and your name (e.g., Alice) in the provided fields, then click 'Login'.\n" +
                "2. **Vote for a Candidate**: On the Home Page, you’ll see a list of candidates from your department. Click 'Details' to learn more about a candidate (opens a webpage), or click 'Vote' to cast your vote.\n" +
                "3. **Voting Rules**: You can only vote for one candidate per round. After voting, the 'Vote' button will be disabled until all users in your department have voted.\n" +
                "4. **View Statistics and Winner**: Once all users in your department have voted, the statistics for your department will be displayed. Click 'View the Winner' to see the winner(s).\n" +
                "5. **Independent Voting**: Each department votes independently. You can vote at any time, and your department’s voting process does not depend on other departments.\n" +
                "6. **New Voting Round**: After viewing the winner, your department’s voting will reset, and you can vote again in a new round.\n" +
                "7. **Persistence**: Your voting progress is saved, so you can close and reopen the program without losing data."
        );
        alert.getDialogPane().setPrefWidth(600);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}