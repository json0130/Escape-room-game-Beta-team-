package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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
import nz.ac.auckland.se206.speech.TextToSpeech;

public class EndController implements Initializable {

  @FXML private Label win;
  @FXML private Label youWin;
  @FXML private Label lose;
  @FXML private Label lose1;
  @FXML private Pane scene;
  @FXML private Button button;
  @FXML private Button s;
  @FXML private Button start;
  @FXML private ImageView e1;
  @FXML private ImageView background;
  @FXML private ImageView spaceship;
  @FXML private ImageView ship;
  @FXML private Rectangle grey;

  @FXML private Rectangle black2;
  @FXML private Rectangle resetBox;
  @FXML private Label resetLabel;
  @FXML private Button resetYes;
  @FXML private Button resetCancel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    if (scene != null) {
      scene
          .sceneProperty()
          .addListener(
              (observable, oldScene, newScene) -> {
                if (newScene != null) {
                  // Schedule the endingAnimation to run after the scene is shown
                  Platform.runLater(this::endingAnimation);
                  youWin.setVisible(false);
                }
              });
    }
  }

  @FXML
  private void endingAnimation() {
    // Create a timeline to continuously increase the scaling factor
    Timeline continuousScaling =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(background.scaleXProperty(), 1.0)),
            new KeyFrame(Duration.ZERO, new KeyValue(background.scaleYProperty(), 1.0)),
            new KeyFrame(Duration.seconds(1), new KeyValue(background.scaleXProperty(), 1.5)),
            new KeyFrame(Duration.seconds(1), new KeyValue(background.scaleYProperty(), 1.5)),
            new KeyFrame(Duration.seconds(1.4), new KeyValue(background.scaleXProperty(), 3)),
            new KeyFrame(Duration.seconds(1.4), new KeyValue(background.scaleYProperty(), 3)),
            new KeyFrame(Duration.seconds(2.4), new KeyValue(background.scaleXProperty(), 6)),
            new KeyFrame(Duration.seconds(2.4), new KeyValue(background.scaleYProperty(), 6)));
    continuousScaling.setCycleCount(1); // Play the animation once

    // Create a translate animation for the r1
    TranslateTransition Translation = new TranslateTransition(Duration.seconds(2.4), background);

    // Set the animation properties

    Translation.setCycleCount(1); // Play the animation once
    Translation.setAutoReverse(false); // Don't reverse the animation

    // Start the animations
    continuousScaling.play();
    Translation.play();
    // Enable the button when the animation is finished
    Translation.setOnFinished(
        event -> {
          // Adjust the background scale to original size
          grey.setVisible(false);
          background.setScaleX(1.0);
          background.setScaleY(1.0);
          FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(2), ship);
          fadeTransition1.setToValue(0.0); // Set the target opacity value (1.0 for fully opaque)
          fadeTransition1.play(); // Start the animation for background1

          FadeTransition fadeTransition2 = new FadeTransition(Duration.seconds(2), grey);
          fadeTransition2.setToValue(1.0); // Set the target opacity value (1.0 for fully opaque)
          fadeTransition2.play(); // Start the animation for background2

          PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2.0));
          pauseTransition.setOnFinished(
              events -> {
                // Adjust the player's position to be right in front of the room
                endAnimation();
              });
          pauseTransition.play();
        });
  }

  @FXML
  private void startAnimation(ActionEvent events) {
    // Create a timeline to continuously increase the scaling factor
    Timeline continuousScaling =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(win.scaleXProperty(), 1.0)),
            new KeyFrame(Duration.ZERO, new KeyValue(win.scaleYProperty(), 1.0)),
            new KeyFrame(Duration.seconds(1), new KeyValue(win.scaleXProperty(), 2)),
            new KeyFrame(Duration.seconds(1), new KeyValue(win.scaleYProperty(), 2)),
            new KeyFrame(Duration.seconds(0.5), new KeyValue(win.scaleXProperty(), 1.7)),
            new KeyFrame(Duration.seconds(0.5), new KeyValue(win.scaleYProperty(), 1.7)));
    continuousScaling.setCycleCount(1); // Play the animation once

    // Create a translate animation for the r1
    TranslateTransition Translation = new TranslateTransition(Duration.seconds(3), win);

    // Set the animation properties

    Translation.setCycleCount(1); // Play the animation once
    Translation.setAutoReverse(false); // Don't reverse the animation

    // Start the animations
    continuousScaling.play();
    Translation.play();
  }

  @FXML
  private void animation() {
    // Create a scale transition
    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2), win);
    scaleTransition.setFromX(1.0); // Starting X scale
    scaleTransition.setFromY(1.0); // Starting Y scale
    scaleTransition.setToX(2.0); // Ending X scale (scale up by a factor of 2)
    scaleTransition.setToY(2.0); // Ending Y scale (scale up by a factor of 2)

    // Create a translate transition
    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), win);
    translateTransition.setByX(100); // Move label 100 units to the right
    translateTransition.setByY(50); // Move label 50 units down

    // Play both transitions in parallel
    scaleTransition.play();
    translateTransition.play();
  }

  @FXML
  private void endAnimation() {
    spaceship.setVisible(true);
    spaceshipSound();

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
          youWin.setVisible(true);
          App.mediaPlayer.setVolume(0.3);
          introTextToSpeech();
        });
  }

  @FXML
  private void spaceshipSound() {
    String soundEffect = "src/main/resources/sounds/spaceship.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setVolume(0.2);
    mediaPlayer.setAutoPlay(true);
  }

  private void introTextToSpeech() {
    Task<Void> introTask =
        new Task<>() {

          @Override
          protected Void call() throws Exception {
            TextToSpeech textToSpeech = new TextToSpeech();
            textToSpeech.speak("Congratulation. You Win!");
            return null;
          }
        };
    Thread introThread = new Thread(introTask);
    introThread.start();
  }
}
