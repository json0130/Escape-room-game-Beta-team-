package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class IntroController implements Initializable {

  @FXML private Button minB2;
  @FXML private Button minB4;
  @FXML private Button minB6;
  @FXML private Button easyButton;
  @FXML private Button mediumButton;
  @FXML private Button hardButton;
  @FXML private Button startButton;
  @FXML private Button tutorial;
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
  @FXML private Label letter;
  @FXML private Rectangle letterbox;

  private boolean animationStarted = false;
  @FXML private boolean isLevelSelected = false;
  @FXML private boolean isTimeSelected = false;

  @FXML private Button toggleSoundButton;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    buttonHovered(easyButton, easy, easybox);
    buttonHovered(mediumButton, medium, mediumbox);
    buttonHovered(hardButton, hard, hardbox);
    miniuteButtonHovered(minB2);
    miniuteButtonHovered(minB4);
    miniuteButtonHovered(minB6);

    // Add an event handler to the Toggle Sound button
    toggleSoundButton.setOnAction(event -> toggleSound());

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
    letter.setVisible(false);
    letterbox.setVisible(false);
    tutorial.setVisible(false);

    background2.setVisible(false);
    startButton.setVisible(false);
    background3.setVisible(false);
  }

  @FXML
  private void levelButtonClicked(ActionEvent event) {
    soundButttonClick();

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
          "-fx-background-color: rgba(255, 255, 255, 0.5); -fx-text-fill: dark blue;");
    }
    isLevelSelected = true;

    // set the difficulty
    if (event.getSource() == easyButton) {
      GameState.difficulty = "EASY";
    } else if (event.getSource() == mediumButton) {
      GameState.difficulty = "MEDIUM";
    } else if (event.getSource() == hardButton) {
      GameState.difficulty = "HARD";
    }
  }

  @FXML
  private void minBClicked(ActionEvent events) {
    soundButttonClick();

    minB2.setOnMouseEntered(null); // Disable hover effect
    minB4.setOnMouseEntered(null); // Disable hover effect
    minB6.setOnMouseEntered(null); // Disable hover effect

    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.2));
    pauseTransition.setOnFinished(
        event -> {
          startButton.setVisible(true);
          background3.setVisible(true);
        });
    pauseTransition.play();

    Button cButton = (Button) events.getSource();

    switch (cButton.getId()) {
      case "minB2":
        App.timerSeconds = 120;
        App.chosenTimer = 120;
        break;
      case "minB4":
        App.timerSeconds = 240;
        App.chosenTimer = 240;
        break;
      case "minB6":
        App.timerSeconds = 360;
        App.chosenTimer = 360;
        break;
      default:
        break;
    }
    System.out.println(App.timerSeconds);

    // Check if a time is already selected, if so, return
    if (!isTimeSelected) {
      // Reset the style of all buttons to their original state
      minB2.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
      minB4.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
      minB6.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
      cButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-text-fill: dark blue;");
    }
    isTimeSelected = true;
  }

  @FXML
  private void startButtonClicked(ActionEvent event) {
    soundButttonClick();
    if (isLevelSelected && isTimeSelected) {
      startButton.setDisable(true);
      startButton.setVisible(false);
      letter.setVisible(true);
      letterbox.setVisible(true);
      tutorial.setVisible(true);

      minB2.setDisable(false);
      minB4.setDisable(false);
      minB6.setDisable(false);
      minB2.setVisible(false);
      minB4.setVisible(false);
      minB6.setVisible(false);
      easyButton.setVisible(false);
      mediumButton.setVisible(false);
      hardButton.setVisible(false);
    }
  }

  @FXML
  private void startAnimation(ActionEvent events) {
    soundButttonClick();
    if (!animationStarted) {
      spaceship.setVisible(true);
      background2.setVisible(true);
      background3.setVisible(false);
      letter.setVisible(false);
      letterbox.setVisible(false);
      tutorial.setVisible(false);

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

            App.timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), this::updateTimer));
            App.timerTimeline.setCycleCount(App.timerSeconds);
            // App.timerTimeline.play();
          });

      animationStarted = true;
    }
  }

  private void updateTimer(ActionEvent event) {
    App.timerSeconds--;
    if (!GameState.isGameFinished) {
      if (App.timerSeconds <= 0) {
        App.timerTimeline.stop();
        App.setScene(AppUi.LOSE);
        introTextToSpeech();
      }
    } else {
      App.timerTimeline.stop();
    }
  }

  private void introTextToSpeech() {
    Task<Void> introTask =
        new Task<>() {

          @Override
          protected Void call() throws Exception {
            TextToSpeech textToSpeech = new TextToSpeech();
            textToSpeech.speak("TIME OUT. GAME OVER.");
            return null;
          }
        };
    Thread introThread = new Thread(introTask);
    introThread.start();
  }

  @FXML
  private void buttonHovered(Button button, Label label, Rectangle box) {
    button.setOnMouseEntered(
        e -> {
          // Change the style of the button for 0.2 seconds only and back to original style
          button.setStyle(
              "-fx-background-color: rgba(255, 255, 255, 0.5); -fx-text-fill: dark blue;");
          PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
          pauseTransition.setOnFinished(
              event -> {
                button.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.0); -fx-text-fill: white;"
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
          PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5));
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
              "-fx-background-color: rgba(255, 255, 255, 0.5); -fx-text-fill: dark blue;");
          PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
          pauseTransition.setOnFinished(
              event -> {
                button.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.0); -fx-text-fill: white;"
                        + " -fx-border-color: white; -fx-border-width: 2px; -fx-border-radius:"
                        + " 5px");
              });
          pauseTransition.play();
        });

    button.setOnMouseExited(
        e -> {
          // Delay the hiding of elements by 3 seconds
          PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5));
          pauseTransition.setOnFinished(
              event -> {
                // Return the button to its original position
                button.setTranslateY(0);
              });
          pauseTransition.play();
        });
  }

  @FXML
  private void toggleSound() {
    if (GameState.isSoundEnabled) {
      // Disable sound
      if (App.mediaPlayer != null) {
        App.mediaPlayer.setVolume(0.0); // Mute the media player
      }
      toggleSoundButton.setText("Enable Sound");
    } else {
      // Enable sound
      if (App.mediaPlayer != null) {
        App.mediaPlayer.setVolume(0.05); // Set the volume to your desired level
      }
      toggleSoundButton.setText("Disable Sound");
    }

    GameState.isSoundEnabled = !GameState.isSoundEnabled; // Toggle the sound state
  }

  @FXML
  private void soundButttonClick() {
    String soundEffect = "src/main/resources/sounds/button-click.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setAutoPlay(true);
  }
}
