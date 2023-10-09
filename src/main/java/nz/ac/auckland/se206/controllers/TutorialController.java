package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class TutorialController implements Initializable {

  private BooleanProperty wPressed = new SimpleBooleanProperty();
  private BooleanProperty aPressed = new SimpleBooleanProperty();
  private BooleanProperty sPressed = new SimpleBooleanProperty();
  private BooleanProperty dPressed = new SimpleBooleanProperty();

  private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);

  private int movementVariable = 5;
  private double shapesize;

  List<ImageView> rocks = new ArrayList<>();

  @FXML private Button button;
  @FXML private Button skipButton;
  @FXML private ImageView player;
  @FXML private Pane scene;
  @FXML private Circle c1;
  @FXML private Circle c2;
  @FXML private Circle c3;
  @FXML private ImageView r1;
  @FXML private ImageView r2;
  @FXML private ImageView r3;
  @FXML private ImageView r4;
  @FXML private ImageView soundOn;
  @FXML private ImageView soundOff;
  @FXML private Rectangle box;

  // sound for rocket movement
  String soundEffect = "src/main/resources/sounds/rocket.mp3";
  Media media = new Media(new File(soundEffect).toURI().toString());
  MediaPlayer mediaPlayer = new MediaPlayer(media);

  @FXML private Button toggleSoundButton;

  @FXML private ProgressBar progressBar;
  private PauseTransition collisionPause = new PauseTransition(Duration.seconds(1));

  private double health = 1.0;

  private double previousX;
  private double previousY;

  private boolean collisionDetected = false;
  private boolean isInstructionDone = false;

  @FXML private Label sentenceLabel;
  private List<String> sentences = new ArrayList<>();
  private int currentSentenceIndex = 0;

  AnimationTimer collisionTimer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          checkCollision1(player, box);
          if (isInstructionDone) {
            checkFinish(player, c3);
            checkCollision(player, rocks);
          }
        }
      };

  //  code for character movement using wasd movement
  AnimationTimer timer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {

          previousX = player.getLayoutX(); // Update previousX
          previousY = player.getLayoutY(); // Update previousY

          if (wPressed.get()) {
            player.setLayoutY(player.getLayoutY() - movementVariable);
          }
          if (aPressed.get()) {
            player.setLayoutX(player.getLayoutX() - movementVariable);
          }
          if (sPressed.get()) {
            player.setLayoutY(player.getLayoutY() + movementVariable);
          }
          if (dPressed.get()) {
            player.setLayoutX(player.getLayoutX() + movementVariable);
          }
          squareBorder();
        }
      };

  @FXML
  private void skipTutorial(ActionEvent event) {
    soundButttonClick();
    App.setScene(AppUi.ANIMATION);
    collisionTimer.stop();
    timer.stop();
  }

  @FXML
  private void play(ActionEvent event) {
    setRotate(c1, true, 360, 10);
    setRotate(c2, true, 180, 18);
    setRotate(c3, true, 145, 24);

    // Set the movement of the images and repeat it forever
    setMovement(r1, false, 2, -900, 0, 2);
    setMovement(r2, false, 3, -900, 0, 4);
    setMovement(r3, false, 4, -900, -0, 3);
    setMovement(r4, false, 2, -900, 0, 5);
  }

  private List<String> parseSentences(String paragraph) {
    String[] sentenceArray = paragraph.split("[.!?]"); // Split the paragraph into sentences
    // Add each sentence to the List
    List<String> sentences = new ArrayList<>();
    for (String sentence : sentenceArray) {
      sentences.add(sentence.trim());
    }
    return sentences;
  }

  private void displayNextSentence() {
    if (currentSentenceIndex < sentences.size()) {
      String nextSentence = sentences.get(currentSentenceIndex);
      sentenceLabel.setText(nextSentence);
      currentSentenceIndex++;
    } else {
      // All sentences are displayed, so call playRock
      playRock();
      box.setVisible(false);
      isInstructionDone = true;
    }
  }

  private void setRotate(Circle c, boolean reverse, int angle, int duration) {
    RotateTransition rt = new RotateTransition(Duration.seconds(duration), c);
    rt.setAutoReverse(reverse);
    rt.setByAngle(angle);
    rt.setDelay(Duration.seconds(0));
    rt.setRate(3);
    rt.setCycleCount(18);
    rt.play();
  }

  private void setMovement(
      ImageView r, boolean reverse, int duration, double endX, double endY, int delaySeconds) {
    double startX = r.getTranslateX();
    double startY = r.getTranslateY();

    Path path = new Path();
    path.getElements().add(new MoveTo(startX, startY));
    path.getElements().add(new LineTo(endX, endY));

    PathTransition pathTransition = new PathTransition();
    pathTransition.setNode(r);
    pathTransition.setPath(path);
    pathTransition.setDuration(Duration.seconds(duration));
    pathTransition.setCycleCount(reverse ? Animation.INDEFINITE : 1);
    pathTransition.setAutoReverse(reverse);

    SequentialTransition sequentialTransition = new SequentialTransition();
    sequentialTransition
        .getChildren()
        .addAll(new PauseTransition(Duration.seconds(delaySeconds)), pathTransition);

    sequentialTransition.setOnFinished(
        event -> {
          // Reset the position of the ImageView to its original location
          r.setTranslateX(startX);
          r.setTranslateY(startY);

          // Start the animation again
          sequentialTransition.playFromStart();
        });
    sequentialTransition.play();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Initialise the position of the images
    r1.setLayoutX(950);
    r1.setLayoutY(100);
    r2.setLayoutX(950);
    r2.setLayoutY(250);
    r3.setLayoutX(950);
    r3.setLayoutY(400);
    r4.setLayoutX(950);
    r4.setLayoutY(550);

    toggleSoundButton.setOnMouseClicked(this::toggleSound);

    String paragraph =
        "Hello, I am the Game master of this game. This is a simple tutorial"
            + " game. It will help you to get used to keyboard control. Start with 'W' to move"
            + " up. 'S' to move down. 'A' to move left. 'D' to move right. Try to avoid the rocks"
            + " and reach the finish line. Good Luck!";
    sentences = parseSentences(paragraph);

    shapesize = player.getFitHeight();
    movementSetup();

    rocks.add(r1);
    rocks.add(r2);
    rocks.add(r3);
    rocks.add(r4);

    collisionTimer.start();

    previousX = player.getLayoutX();
    previousY = player.getLayoutY();

    // Initialize the sentenceLabel
    sentenceLabel.setWrapText(true);
    scene
        .sceneProperty()
        .addListener(
            (observable, oldScene, newScene) -> {
              if (newScene != null) {
                PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
                pauseTransition.setOnFinished(
                    event -> {
                      // Create a Timeline to display sentences
                      Timeline timeline =
                          new Timeline(
                              new KeyFrame(Duration.ZERO, events -> displayNextSentence()),
                              new KeyFrame(Duration.seconds(2)));
                      timeline.setCycleCount(sentences.size()); // Repeat for each sentence
                      // Start displaying sentences
                      displayNextSentence();
                      timeline.play();
                    });
                pauseTransition.play();
              }
            });

    keyPressed.addListener(
        ((observableValue, aBoolean, t1) -> {
          if (!aBoolean) {
            timer.start();
          } else {
            timer.stop();
          }
        }));
  }

  @FXML
  private void playRock() {
    setRotate(c1, true, 360, 10);
    setRotate(c2, true, 180, 18);
    setRotate(c3, true, 145, 24);

    // Set the movement of the images and repeat it forever
    setMovement(r1, false, 3, -900, 0, 3);
    setMovement(r2, false, 3, -900, 0, 0);
    setMovement(r3, false, 3, -900, -0, 2);
    setMovement(r4, false, 3, -900, 0, 5);
  }

  public void checkCollision(ImageView player, List<ImageView> rocks) {
    if (!collisionDetected) {
      for (ImageView rock : rocks) {
        if (player.getBoundsInParent().intersects(rock.getBoundsInParent())) {
          // Collision detected, move the player back a bit
          player.setLayoutX(previousX - 10);
          player.setLayoutY(previousY);
          collisionDetected = true;
          progressBar.setProgress(health -= 0.25); // Decrease the progress bar
          if (health == 0) {
            App.setScene(AppUi.ANIMATION);
          }
          collisionPause.setOnFinished(
              event -> collisionDetected = false); // Re-enable collision detection after 1 second
          collisionPause.play();
        }
      }
    }
  }

  public void checkFinish(ImageView player, Circle c3) {
    if (player.getBoundsInParent().intersects(c3.getBoundsInParent())) {
      App.setScene(AppUi.ANIMATION);
      collisionTimer.stop();
      timer.stop();
    }
  }

  public void checkCollision1(ImageView player, Rectangle box) {
    if(!isInstructionDone){
      if (player.getBoundsInParent().intersects(box.getBoundsInParent())) {
      player.setLayoutX(previousX);
      player.setLayoutY(previousY);
      }
    }
    
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
  public void movementSetup() {
    scene.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.W) {

            wPressed.set(true);
          }

          if (e.getCode() == KeyCode.A) {

            aPressed.set(true);
          }

          if (e.getCode() == KeyCode.S) {

            sPressed.set(true);
          }

          if (e.getCode() == KeyCode.D) {

            dPressed.set(true);
          }
        });

    scene.setOnKeyReleased(
        e -> {
          if (e.getCode() == KeyCode.W) {

            wPressed.set(false);
          }

          if (e.getCode() == KeyCode.A) {

            aPressed.set(false);
          }

          if (e.getCode() == KeyCode.S) {

            sPressed.set(false);
          }

          if (e.getCode() == KeyCode.D) {

            dPressed.set(false);
          }
        });
  }

  public void squareBorder() {
    double left = 0;
    double right = scene.getWidth() - shapesize;
    double top = 0;
    double bottom = scene.getHeight() - shapesize;

    if (player.getLayoutX() < left) {
      player.setLayoutX(left);
    }

    if (player.getLayoutX() > right) {
      player.setLayoutX(right);
    }

    if (player.getLayoutY() < top) {
      player.setLayoutY(top);
    }

    if (player.getLayoutY() > bottom) {
      player.setLayoutY(bottom);
    }
  }

  @FXML
  private void playSoundRocket() {
    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    mediaPlayer.play();
  }

  @FXML
  private void soundButttonClick() {
    String soundEffect = "src/main/resources/sounds/button-click.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setAutoPlay(true);
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

}
