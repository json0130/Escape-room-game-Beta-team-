package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class IntroController implements Initializable {

  @FXML private Button minB2;
  @FXML private Button minB4;
  @FXML private Button minB6;
  @FXML private Button easyButton;
  @FXML private Button mediumButton;
  @FXML private Button hardButton;
  @FXML private Button startButton;
  @FXML private ImageView spaceship;
  @FXML private ImageView background;
  @FXML private ImageView background2;
  @FXML private Pane background3;
  @FXML private Rectangle easybox;
  @FXML private Rectangle mediumbox;
  @FXML private Rectangle hardbox;
  @FXML private Label easy;
  @FXML private Label medium;
  @FXML private Label hard;
  @FXML private Label title;

  private boolean animationStarted = false;
  @FXML private boolean isLevelSelected = false;
  @FXML private boolean isTimeSelected = false;

  @FXML
  private void levelButtonClicked(ActionEvent event) {

    easyButton.setOnMouseEntered(null); // Disable hover effect
    mediumButton.setOnMouseEntered(null); // Disable hover effect
    hardButton.setOnMouseEntered(null); // Disable hover effect

    easybox.setVisible(false);
    mediumbox.setVisible(false);
    hardbox.setVisible(false);
    easy.setVisible(false);
    medium.setVisible(false);
    hard.setVisible(false);
    minB2.setVisible(true);
    minB4.setVisible(true);
    minB6.setVisible(true);

    Button clickedButton = (Button) event.getSource();

    // Change the style of the clicked button
    if (!isLevelSelected) {
      // Reset the style of all buttons to their original state
      easyButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
      mediumButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
      hardButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
      clickedButton.setStyle(
          "-fx-background-color: rgba(255, 255, 255, 0.5); -fx-text-fill: drak blue;");
    }
    isLevelSelected = true;
  }

  @FXML
  private void minBClicked(ActionEvent events) {

    minB2.setOnMouseEntered(null); // Disable hover effect
    minB4.setOnMouseEntered(null); // Disable hover effect
    minB6.setOnMouseEntered(null); // Disable hover effect

    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.2));
    pauseTransition.setOnFinished(
        event -> {
          startButton.setVisible(true);
          background3.setVisible(true);
          title.setVisible(false);
        });
    pauseTransition.play();

    Button cButton = (Button) events.getSource();

    switch (cButton.getId()) {
      case "minB2":
        App.timerSeconds = 12;
        App.chosenTimer = 12;
        break;
      case "minB4":
        App.timerSeconds = 24;
        App.chosenTimer = 24;
        break;
      case "minB6":
        App.timerSeconds = 36;
        App.chosenTimer = 36;
        break;
      default:
        App.timerSeconds = 12;
        App.chosenTimer = 12;
    }
    System.out.println(App.timerSeconds);

    // Check if a time is already selected, if so, return
    if (!isTimeSelected) {
      // Reset the style of all buttons to their original state
      minB2.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
      minB4.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
      minB6.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
      cButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-text-fill: drak blue;");
    }
    isTimeSelected = true;
  }

  @FXML
  private void startAnimation() {
    if (!animationStarted) {
      spaceship.setVisible(true);
      background2.setVisible(true);
      minB2.setDisable(false);
      minB4.setDisable(false);
      minB6.setDisable(false);
      background3.setVisible(false);

      // Create a timeline to continuously increase the scaling factor
      Timeline continuousScaling =
          new Timeline(
              new KeyFrame(Duration.ZERO, new KeyValue(spaceship.scaleXProperty(), 1.0)),
              new KeyFrame(Duration.ZERO, new KeyValue(spaceship.scaleYProperty(), 1.0)),
              new KeyFrame(Duration.seconds(3), new KeyValue(spaceship.scaleXProperty(), 5.0)),
              new KeyFrame(Duration.seconds(3), new KeyValue(spaceship.scaleYProperty(), 5.0)));
      continuousScaling.setCycleCount(1); // Play the animation once

      // Create a path for the spaceship to follow (a curve)
      Path path = new Path();
      path.getElements()
          .addAll(
              new MoveTo(400, 300), // Start at the left corner
              new CubicCurveTo(
                  -200, -100, // Control point 1
                  1000, -700, // Control point 2
                  1300, -1000 // End at the right end of the scene
                  ));

      // Create a path transition
      PathTransition spaceshipPathTransition =
          new PathTransition(Duration.seconds(3), path, spaceship);
      spaceshipPathTransition.setCycleCount(1); // Play the animation once

      // Start the animations
      continuousScaling.play();
      spaceshipPathTransition.play();

      // Enable the button when the animation is finished
      spaceshipPathTransition.setOnFinished(
          event -> {
            startButton.setDisable(false);
            App.setScene(AppUi.TUTORIAL);

            try {
              SceneManager.addScene(AppUi.ROOM3, App.loadFxml("room3"));
              SceneManager.addScene(AppUi.ROOM1, App.loadFxml("room1"));
              SceneManager.addScene(AppUi.PLAYER, App.loadFxml("player"));
              SceneManager.addScene(AppUi.TILEPUZZLE, App.loadFxml("tilegamedesk"));
              SceneManager.addScene(AppUi.TILEROOM, App.loadFxml("tilegameroom"));
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }

            App.timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), this::updateTimer));
            App.timerTimeline.setCycleCount(App.timerSeconds);
            // App.timerTimeline.play();
          });

      animationStarted = true;
    }
  }

  private void updateTimer(ActionEvent event) {
    App.timerSeconds--;
    System.out.println("Actual timer: " + App.timerSeconds);
  }

  @FXML
  private void buttonHovered(Button button, Label label, Rectangle box) {
    button.setOnMouseEntered(
        e -> {
          // Change the style of the button for 0.2 seconds only and back to original style
          button.setStyle(
              "-fx-background-color: rgba(255, 255, 255, 0.5); -fx-text-fill: drak blue;");
          PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.9));
          pauseTransition.setOnFinished(
              event -> {
                button.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.0); -fx-text-fill: whilte;"
                        + " -fx-border-color: white; -fx-border-width: 2px; -fx-border-radius:"
                        + " 5px");
              });
          pauseTransition.play();

          // Make label and box visible
          label.setVisible(true);
          box.setVisible(true);
        });

    button.setOnMouseExited(
        e -> {

          // Delay the hiding of elements by 3 seconds
          PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2.5));
          pauseTransition.setOnFinished(
              event -> {
                // Hide label and box
                label.setVisible(false);
                box.setVisible(false);
              });
          pauseTransition.play();
        });
  }

  @FXML
  private void miniuteButtonHovered(Button button) {
    button.setOnMouseEntered(
        e -> {
          // Change the style of the button for 0.2 seconds only and back to original style
          button.setStyle(
              "-fx-background-color: rgba(255, 255, 255, 0.5); -fx-text-fill: drak blue;");
          PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
          pauseTransition.setOnFinished(
              event -> {
                button.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.0); -fx-text-fill: whilte;"
                        + " -fx-border-color: white; -fx-border-width: 2px; -fx-border-radius:"
                        + " 5px");
              });
          pauseTransition.play();
        });

    button.setOnMouseExited(
        e -> {
          // Delay the hiding of elements by 3 seconds
          PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2.5));
          pauseTransition.setOnFinished(
              event -> {
                // Return the button to its original position
                button.setTranslateY(0);
              });
          pauseTransition.play();
        });
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    buttonHovered(easyButton, easy, easybox);
    buttonHovered(mediumButton, medium, mediumbox);
    buttonHovered(hardButton, hard, hardbox);
    miniuteButtonHovered(minB2);
    miniuteButtonHovered(minB4);
    miniuteButtonHovered(minB6);

    easybox.setVisible(false);
    mediumbox.setVisible(false);
    hardbox.setVisible(false);
    easy.setVisible(false);
    medium.setVisible(false);
    hard.setVisible(false);
    spaceship.setVisible(false);
    minB2.setVisible(false);
    minB4.setVisible(false);
    minB6.setVisible(false);
    background2.setVisible(false);
    startButton.setVisible(false);
    background3.setVisible(false);
  }
}
