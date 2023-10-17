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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

/** Controller for the intro scene */
public class IntroController implements Initializable {
  @FXML private Button minB2;
  @FXML private Button minB4;
  @FXML private Button minB6;
  @FXML private Button easyButton;
  @FXML private Button mediumButton;
  @FXML private Button hardButton;
  @FXML private Button startButton;
  @FXML private Button tutorial;
  @FXML private Button close;

  @FXML private ImageView spaceship;
  @FXML private ImageView soundOn;
  @FXML private ImageView soundOff;
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

  @FXML private Rectangle black2;
  @FXML private Rectangle resetBox;
  @FXML private Label resetLabel;
  @FXML private Button resetYes;
  @FXML private Button resetCancel;

  @FXML private Rectangle letterbox;

  private Timeline animationTimeline;
  private boolean animationStarted = false;
  @FXML private boolean isLevelSelected = false;
  @FXML private boolean isTimeSelected = false;

  @FXML private Rectangle grey2;
  @FXML private Button resetButton1;

  @FXML private Button toggleSoundButton;
  @FXML private ImageView title;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    buttonHovered(easyButton, easy, easybox);
    buttonHovered(mediumButton, medium, mediumbox);
    buttonHovered(hardButton, hard, hardbox);
    miniuteButtonHovered(minB2);
    miniuteButtonHovered(minB4);
    miniuteButtonHovered(minB6);

    // Add an event handler to the Toggle Sound button
    toggleSoundButton.setOnMouseClicked(this::toggleSound);

    easybox.setVisible(false);
    mediumbox.setVisible(false);
    hardbox.setVisible(false);
    easy.setVisible(false);
    medium.setVisible(false);
    hard.setVisible(false);
    spaceship.setVisible(false);
    letter.setVisible(false);
    letterbox.setVisible(false);
    tutorial.setVisible(false);
    close.setVisible(false);

    background2.setVisible(false);
    startButton.setVisible(false);
    background3.setVisible(false);

    animationTimeline =
        new Timeline(
            new KeyFrame(
                Duration.millis(100),
                new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                    // Update animation based on the current time
                    checkCollision2();
                  }
                }));
    animationTimeline.setCycleCount(Timeline.INDEFINITE);
    animationTimeline.play();
  }

  /**
   * Check if the sound is enabled or disabled
   *
   * @param soundOn the soundOn image
   * @param soundOff the soundOff image
   * @return true if the sound is enabled
   */
  @FXML
  public void checkCollision2() {
    // Initialize sound images based on the initial isSoundEnabled state
    if (GameState.isSoundEnabled) {
      soundOn.setVisible(true);
      soundOff.setVisible(false);
    } else {
      soundOn.setVisible(false);
      soundOff.setVisible(true);
    }
  }

  @FXML
  private void onClickLevel(ActionEvent events) {
    // If the user clicks the level button then the time buttons will be visible.
    soundButttonClick();

    easybox.setVisible(false);
    mediumbox.setVisible(false);
    hardbox.setVisible(false);
    easy.setVisible(false);
    medium.setVisible(false);
    hard.setVisible(false);
    minB2.setVisible(true);
    minB4.setVisible(true);
    minB6.setVisible(true);

    Button clickedButton = (Button) events.getSource();

    // Reset the style of all buttons to their original state
    easyButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
    mediumButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
    hardButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
    clickedButton.setStyle(
        "-fx-background-color: rgba(255, 255, 255, 0.5); -fx-text-fill: dark blue;");
    isLevelSelected = true;

    // set the difficulty
    if (events.getSource() == easyButton) {
      GameState.difficulty = "EASY";
      GameState.clickedLevelButton = "easyButton";
      System.out.println(GameState.difficulty);
    } else if (events.getSource() == mediumButton) {
      GameState.difficulty = "MEDIUM";
      GameState.clickedLevelButton = "mediumButton";
      System.out.println(GameState.difficulty);
    } else if (events.getSource() == hardButton) {
      GameState.difficulty = "HARD";
      GameState.clickedLevelButton = "hardButton";
      System.out.println(GameState.difficulty);
    }

    if (isLevelSelected && isTimeSelected) {
      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.2));
      pauseTransition.setOnFinished(
          event -> {
            startButton.setVisible(true);
            background3.setVisible(true);
            title.setVisible(false);
          });
      pauseTransition.play();
    }
  }

  @FXML
  private void onClickTime(ActionEvent events) {
    // If the user clicks the minute button then the start button will be visible.
    soundButttonClick();
    Button clickedButton = (Button) events.getSource();

    switch (clickedButton.getId()) {
      case "minB2":
        App.timerSeconds = 120;
        App.chosenTimer = 120;
        GameState.clickedButton = "minB2";
        break;
      case "minB4":
        App.timerSeconds = 240;
        App.chosenTimer = 240;
        GameState.clickedButton = "minB4";
        break;
      case "minB6":
        App.timerSeconds = 360;
        App.chosenTimer = 360;
        GameState.clickedButton = "minB6";
        break;
      default:
        break;
    }
    System.out.println(App.timerSeconds);
    // Reset the style of all buttons to their original state
    minB2.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
    minB4.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
    minB6.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
    clickedButton.setStyle("-fx-background-color: grey; -fx-text-fill: dark blue;");
    isTimeSelected = true;

    if (isLevelSelected && isTimeSelected) {
      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.2));
      pauseTransition.setOnFinished(
          event -> {
            startButton.setVisible(true);
            background3.setVisible(true);
            title.setVisible(false);
          });
      pauseTransition.play();
    }
  }

  @FXML
  private void onClickStart(ActionEvent event) {
    // If level and time is selected then the start button will be visible and allow the user to
    // start the game.
    soundButttonClick();
    if (isLevelSelected && isTimeSelected) {
      startButton.setDisable(true);
      startButton.setVisible(false);
      letter.setVisible(true);
      letterbox.setVisible(true);
      tutorial.setVisible(true);
      close.setVisible(true);

      minB2.setDisable(true);
      minB4.setDisable(true);
      minB6.setDisable(true);
      minB2.setVisible(false);
      minB4.setVisible(false);
      minB6.setVisible(false);
      easyButton.setVisible(false);
      mediumButton.setVisible(false);
      hardButton.setVisible(false);
    }
  }

  @FXML
  private void onClickClose() {
    // If the user clicks the close button then the start button will be visible again.
    startButton.setDisable(false);
    startButton.setVisible(true);
    letter.setVisible(false);
    letterbox.setVisible(false);
    tutorial.setVisible(false);
    close.setVisible(false);

    minB2.setDisable(false);
    minB4.setDisable(false);
    minB6.setDisable(false);
    minB2.setVisible(true);
    minB4.setVisible(true);
    minB6.setVisible(true);
    easyButton.setVisible(true);
    mediumButton.setVisible(true);
    hardButton.setVisible(true);
  }

  @FXML
  private void clickAnimations(ActionEvent events) {
    GameState.isGameStarted = true;
    soundButttonClick();
    if (!animationStarted) {
      spaceship.setVisible(true);
      background2.setVisible(true);
      background3.setVisible(false);
      letter.setVisible(false);
      letterbox.setVisible(false);
      tutorial.setVisible(false);
      close.setVisible(false);

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
            startButton.setDisable(false);
            App.setScene(AppUi.TUTORIAL);
            App.timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), this::updateTimer));
            App.timerTimeline.setCycleCount(App.timerSeconds);
          });

      animationStarted = true;
    }
  }

  @FXML
  private void spaceshipSound() {
    String soundEffect = "src/main/resources/sounds/spaceship.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setVolume(0.2);
    mediaPlayer.setAutoPlay(true);
  }

  private void updateTimer(ActionEvent event) {
    // This update the timer by decreasing the timerSeconds by 1 every second.
    App.timerSeconds--;
    if (!GameState.isGameFinished) {
      if (App.timerSeconds <= 0) {
        App.timerTimeline.stop();
        App.setScene(AppUi.LOSE);
        System.out.println("GAME OVER");
        String musicFile;
        musicFile = "src/main/resources/sounds/final.mp3";
        App.musicType = "final";
        Media media = new Media(new File(musicFile).toURI().toString());
        // Stop current playing media
        App.mediaPlayer.stop();
        // Create new media player for alert sound
        App.mediaPlayer = new MediaPlayer(media);

        // Check if sound is enabled before setting volume and playing.
        if (GameState.isSoundEnabled) {
          App.mediaPlayer.setVolume(0.2);
          App.mediaPlayer.setAutoPlay(true);
          App.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        loseTextToSpeech();
        GameState.isGameFinished = true;
      }
    } else {
      App.timerTimeline.stop();
    }
  }

  @FXML
  private void loseTextToSpeech() {
    // This is the text to speech for the lose scene.
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
          // Make label and box visible
          label.setVisible(true);
          box.setVisible(true);
        });

    button.setOnMouseExited(
        e -> {
          if (!isLevelSelected && !isTimeSelected) {
            button.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.0); -fx-text-fill: white;"
                    + " -fx-border-color: white; -fx-border-width: 2px; -fx-border-radius:"
                    + " 5px");
          } else {
            if (button.getId().equals(GameState.clickedLevelButton)) {
              button.setStyle(
                  "-fx-background-color: rgba(255, 255, 255, 0.0); -fx-text-fill: white;"
                      + " -fx-border-color: white; -fx-border-width: 2px; -fx-border-radius:"
                      + " 5px");
            } else {
              button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
            }
          }
          label.setVisible(false);
          box.setVisible(false);
        });
  }

  @FXML
  private void miniuteButtonHovered(Button button) {
    // This is for the minute button hovering effect.
    button.setOnMouseEntered(
        e -> {
          // Change the style of the button for 0.2 seconds only and back to original style
          button.setStyle(
              "-fx-background-color: rgba(255, 255, 255, 0.5); -fx-text-fill: dark blue;");
        });

    button.setOnMouseExited(
        e -> {
          if (!isLevelSelected && !isTimeSelected) {
            button.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.0); -fx-text-fill: white;"
                    + " -fx-border-color: white; -fx-border-width: 2px; -fx-border-radius:"
                    + " 5px");
          } else {
            if (button.getId().equals(GameState.clickedButton)) {
              button.setStyle(
                  "-fx-background-color: rgba(255, 255, 255, 0.0); -fx-text-fill: white;"
                      + " -fx-border-color: white; -fx-border-width: 2px; -fx-border-radius:"
                      + " 5px");
            } else {
              button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
            }
          }
          button.setTranslateY(0);
        });
  }

  @FXML
  private void toggleSound(MouseEvent event) {
    if (GameState.isSoundEnabled) {
      // Disable sound
      if (App.mediaPlayer != null) {
        App.mediaPlayer.setVolume(0.0); // Mute the media player
      }
      soundOff.setVisible(true);
      soundOn.setVisible(false);
    } else {
      // Enable sound
      if (App.mediaPlayer != null) {
        App.mediaPlayer.setVolume(0.05); // Set the volume to your desired level
      }
      soundOn.setVisible(true);
      soundOff.setVisible(false);
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
