package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatBubble;
import nz.ac.auckland.se206.GameState;

/** Controller class for the room view. */
abstract class RoomController implements javafx.fxml.Initializable {
  public static ObservableList<ChatBubble> chatBubbleListTileRoom =
      FXCollections.observableArrayList();

  public BooleanProperty directionUp = new SimpleBooleanProperty();
  public BooleanProperty directionLeft = new SimpleBooleanProperty();
  public BooleanProperty directionDown = new SimpleBooleanProperty();
  public BooleanProperty directionRight = new SimpleBooleanProperty();

  public int movementVariable = 5;
  public double shapesize;

  @FXML private ImageView player;
  @FXML private Pane scene;
  @FXML private Pane alert;

  public double previousX;
  public double previousY;

  @FXML private Label difficultyLabel;
  @FXML private Label hintLabel;
  @FXML private Label hintLabel2;

  @FXML private Rectangle blinkingRectangle;
  @FXML private ImageView soundOn;
  @FXML private ImageView soundOff;

  @FXML private Rectangle black2;
  @FXML private Rectangle resetBox;
  @FXML private Label resetLabel;
  @FXML private Button resetYes;
  @FXML private Button resetCancel;

  private MediaPlayer walkingMediaPlayer;

  @FXML
  private Image rightCharacterAnimation =
      new Image(
          new File("src/main/resources/images/walkingRight.gif").toURI().toString(),
          171,
          177,
          false,
          false);

  @FXML
  private Image leftCharacterAnimation =
      new Image(
          new File("src/main/resources/images/walkingLeft.gif").toURI().toString(),
          171,
          177,
          false,
          false);

  @FXML
  private Image leftCharacterIdle =
      new Image(
          new File("src/main/resources/images/gameCharacterArtLeft.png").toURI().toString(),
          171,
          177,
          false,
          false);

  @FXML
  private Image rightCharacterIdle =
      new Image(
          new File("src/main/resources/images/gameCharacterArtRight.png").toURI().toString(),
          171,
          177,
          false,
          false);

  @FXML
  private Image lastPlayedWalk =
      new Image(
          new File("src/main/resources/images/walkingLeft.gif").toURI().toString(),
          171,
          177,
          false,
          false);

  public Boolean walkAnimationPlaying = false;
  public Timeline alertBlinkTimeline;

  @FXML private Button btnRoom1;
  @FXML private Button button;
  @FXML private ImageView exclamationMark;
  @FXML private ImageView gameMaster;

  @FXML private Pane aiWindowController;

  public AnimationTimer timer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {

          previousX = player.getLayoutX(); // Update previousX
          previousY = player.getLayoutY(); // Update previousY

          if (directionUp.get()) {
            player.setLayoutY(player.getLayoutY() - movementVariable);
          }
          if (directionLeft.get()) {
            player.setLayoutX(player.getLayoutX() - movementVariable);
          }
          if (directionDown.get()) {
            player.setLayoutY(player.getLayoutY() + movementVariable);
          }
          if (directionRight.get()) {
            player.setLayoutX(player.getLayoutX() + movementVariable);
          }
          squareBorder();
        }
      };

  // Modify your setupAlertBlinking method as follows
  public void setupAlertBlinking() {
    alert.setVisible(true); // Initially show the alert label
    App.mediaPlayer.stop();
    // Check if sound is enabled before setting volume and playing.
    if (GameState.isSoundEnabled) {
      App.alertSoundPlayer.setVolume(0.03);
    } else {
      App.alertSoundPlayer.setVolume(0.0);
    }
    App.alertSoundPlayer.setAutoPlay(true);
    App.alertSoundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    App.alertSoundPlayer.play();

    // Set up the blinking animation for the alert label
    alertBlinkTimeline =
        new Timeline(
            new KeyFrame(Duration.seconds(0.5), e -> alert.setVisible(true)),
            new KeyFrame(Duration.seconds(1), e -> alert.setVisible(false)));

    alertBlinkTimeline.setCycleCount(Timeline.INDEFINITE);
    alertBlinkTimeline.play();
  }

  // Add a method to stop the alert blinking
  public void stopAlertBlinking() {
    if (alertBlinkTimeline != null) {
      // Stop timeline and hide label
      alertBlinkTimeline.stop();
      alert.setVisible(false);
      App.alertSoundPlayer.stop();
    }
  }

  // code for player movement using wasd keys
  @FXML
  public void movingSetup() {
    scene.setOnKeyPressed(
        e -> {
          boolean wasMoving =
              directionUp.get() || directionLeft.get() || directionDown.get() || directionRight.get();
          // when w key is pressed, player moves up
          if (e.getCode() == KeyCode.W) {
            if (walkAnimationPlaying == false) {
              player.setImage(lastPlayedWalk);
              walkAnimationPlaying = true;
            }
            directionUp.set(true);
          }
          // when a key is pressed, player moves left
          if (e.getCode() == KeyCode.A) {
            if (player.getImage() != leftCharacterAnimation) {
              player.setImage(leftCharacterAnimation);
              walkAnimationPlaying = true;
              lastPlayedWalk = player.getImage();
            }
            directionLeft.set(true);
          }
          // when s key is pressed, player moves down
          if (e.getCode() == KeyCode.S) {
            if (walkAnimationPlaying == false) {
              player.setImage(lastPlayedWalk);
              walkAnimationPlaying = true;
            }
            directionDown.set(true);
          }
          // when d key is pressed, player moves right
          if (e.getCode() == KeyCode.D) {
            if (player.getImage() != rightCharacterAnimation) {
              player.setImage(rightCharacterAnimation);
              walkAnimationPlaying = true;
              lastPlayedWalk = player.getImage();
            }
            directionRight.set(true);
          }

          boolean isMoving =
              directionUp.get() || directionLeft.get() || directionDown.get() || directionRight.get();

          // If we started moving and weren't before, start the sound.
          if (isMoving && !wasMoving) {
            walkingMediaPlayer.play();
          }
        });

    scene.setOnKeyReleased(
        e -> {
          boolean wasMoving =
              directionUp.get() || directionLeft.get() || directionDown.get() || directionRight.get();
          // when w key is released, the player stops at its current position
          if (e.getCode() == KeyCode.W) {
            if (player.getImage() == leftCharacterAnimation
                && directionDown.get() == false
                && directionLeft.get() == false) {
              player.setImage(leftCharacterIdle);
              walkAnimationPlaying = false;
            } else if (directionDown.get() == true) {
              player.setImage(lastPlayedWalk);
            } else if (directionLeft.get() == false
                && directionRight.get() == false
                && directionDown.get() == false) {
              player.setImage(rightCharacterIdle);
              walkAnimationPlaying = false;
            }
            directionUp.set(false);
          }
          // when a key is released, the player stops at its current position
          if (e.getCode() == KeyCode.A) {
            if (directionRight.get() == false
                && directionUp.get() == false
                && directionDown.get() == false) {
              player.setImage(leftCharacterIdle);
              walkAnimationPlaying = false;
            } else if (directionRight.get() == true) {
              player.setImage(rightCharacterAnimation);
            }

            directionLeft.set(false);
          }
          // when s key is released, the player stops at its current position
          if (e.getCode() == KeyCode.S) {
            if (player.getImage() == leftCharacterAnimation
                && directionUp.get() == false
                && directionLeft.get() == false) {
              player.setImage(leftCharacterIdle);
              walkAnimationPlaying = false;
            } else if (directionUp.get() == true) {
              player.setImage(lastPlayedWalk);
            } else if (directionLeft.get() == false
                && directionRight.get() == false
                && directionUp.get() == false) {
              player.setImage(rightCharacterIdle);
              walkAnimationPlaying = false;
            }
            directionDown.set(false);
          }
          // when d key is released, the player stops at its current position
          if (e.getCode() == KeyCode.D) {
            if (directionLeft.get() == false
                && directionUp.get() == false
                && directionDown.get() == false) {
              player.setImage(rightCharacterIdle);
              walkAnimationPlaying = false;
            } else if (directionLeft.get() == true) {
              player.setImage(leftCharacterAnimation);
            }

            directionRight.set(false);
          }

          boolean isMovinng =
              directionUp.get() || directionLeft.get() || directionDown.get() || directionRight.get();

          // If we stopped moving and were before, stop the sound.
          if (!isMovinng && wasMoving) {
            walkingMediaPlayer.stop();
            try {
              // This line will reset audio clip from start when stopped
              walkingMediaPlayer.seek(Duration.ZERO);
            } catch (Exception ex) {
              System.out.println("Error resetting audio: " + ex.getMessage());
            }
          }
        });
  }

  public void squareBorder() {
    // prevent the player moves out of the window
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

  // detect change in game state difficulty which is selected in the intro scene
  public void detectDifficulty() {
    Timer labelTimer = new Timer(true);
    labelTimer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            if (GameState.difficulty != null) {
              // if medium, need to turn on the timer long for hint reduction
              if (GameState.difficulty == "MEDIUM") {
                Platform.runLater(() -> updateLabels());
                if (GameState.numOfHints == 0) {
                  labelTimer.cancel();
                }
                // easy and hard, turn off just after difficulty selection
              } else {
                Platform.runLater(() -> updateLabels());
              }
            }
          }
        },
        0,
        500);
  }

  // labels for hint and difficulty updates as the game progress
  public void updateLabels() {
    difficultyLabel.setText(GameState.difficulty);
    // depending on the difficulty chosen in the intro, change the label in the header
    if (GameState.difficulty == "EASY") {
      hintLabel.setText("UNLIMITED");
      // for the medium difficulty, number of hints is reflected in the header
    } else if (GameState.difficulty == "MEDIUM") {
      hintLabel.setText(String.valueOf(GameState.numOfHints));
      hintLabel2.setText("HINTS");
      if (GameState.numOfHints == 1) {
        hintLabel2.setText("HINT");
      }
    } else {
      hintLabel.setText("NO");
    }
  }

  @FXML
  public void toggleSound(MouseEvent event) {
    GameState.isSoundEnabled = !GameState.isSoundEnabled;

    double volume = GameState.isSoundEnabled ? 0.03 : 0;
    if (App.mediaPlayer != null) {
      App.mediaPlayer.setVolume(volume);
    }

    if (App.alertSoundPlayer != null) {
      // If an Alert Sound Player exists, adjust its volume as well.
      App.alertSoundPlayer.setVolume(volume);
    }

    soundOn.setVisible(GameState.isSoundEnabled);
    soundOff.setVisible(!GameState.isSoundEnabled);
  }

  @FXML
  public void enterRoom() {
    String soundEffect = "src/main/resources/sounds/enterReal.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setAutoPlay(true);
  }

  @FXML
  public void simulateKeyPressAfterDelay() {
    // It released the key pressed when the player is leaving the scene
    Thread thread =
        new Thread(
            () -> {
              try {
                Thread.sleep(50);
                KeyEvent keyReleaseEventS =
                    new KeyEvent(
                        KeyEvent.KEY_RELEASED, "S", "S", KeyCode.S, false, false, false, false);
                // S key is released when the scene changes
                KeyEvent keyReleaseEventA =
                    new KeyEvent(
                        KeyEvent.KEY_RELEASED, "A", "A", KeyCode.A, false, false, false, false);
                // A key is released when the scene changes
                KeyEvent keyReleaseEventW =
                    new KeyEvent(
                        KeyEvent.KEY_RELEASED, "W", "W", KeyCode.W, false, false, false, false);
                // W key is released when the scene changes
                KeyEvent keyReleaseEventD =
                    new KeyEvent(
                        KeyEvent.KEY_RELEASED, "D", "D", KeyCode.D, false, false, false, false);
                // D key is released when the scene changes
                scene.fireEvent(keyReleaseEventA);
                scene.fireEvent(keyReleaseEventD);
                scene.fireEvent(keyReleaseEventW);
                scene.fireEvent(keyReleaseEventS);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    thread.start();
  }

  /**
   * Show buttons to restart the game or cancel.
   *
   * @param event mouse is clicked
   * @throws IOException if the objects don't exist
   */
  @FXML
  public void handleRestartButtonClick(ActionEvent event) throws IOException {
    black2.setVisible(true);
    resetBox.setVisible(true);
    resetLabel.setVisible(true);
    resetYes.setVisible(true);
    resetCancel.setVisible(true);
  }

  @FXML
  public void handleRestartButtonCanceled(ActionEvent event) throws IOException {
    black2.setVisible(false);
    resetBox.setVisible(false);
    resetLabel.setVisible(false);
    resetYes.setVisible(false);
    resetCancel.setVisible(false);
  }

  @FXML
  public void handleResetEvent(ActionEvent event) throws IOException {
    try {
      GameState.resetGames();
    } catch (Exception e) {
      // TODO: handle exception
    }
  }
}
