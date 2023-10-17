package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
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

  public BooleanProperty wKeyPressed = new SimpleBooleanProperty();
  public BooleanProperty aKeyPressed = new SimpleBooleanProperty();
  public BooleanProperty sKeyPressed = new SimpleBooleanProperty();
  public BooleanProperty dKeyPressed = new SimpleBooleanProperty();

  public int movementVariable = 5;
  public double shapesize;

  @FXML public ImageView player;
  @FXML public Pane scene;
  @FXML public Pane alert;

  public double previousX;
  public double previousY;

  @FXML public Label difficultyLabel;
  @FXML public Label hintLabel;
  @FXML public Label hintLabel2;

  @FXML public Rectangle blinkingRectangle;
  @FXML public ImageView soundOn;
  @FXML public ImageView soundOff;

  @FXML public Rectangle black2;
  @FXML public Rectangle resetBox;
  @FXML public Label resetLabel;
  @FXML public Button resetYes;
  @FXML public Button resetCancel;

  public boolean hasHappend = false;

  public MediaPlayer walkingMediaPlayer;

  @FXML
  public Image rightCharacterAnimation =
      new Image(
          new File("src/main/resources/images/walkingRight.gif").toURI().toString(),
          171,
          177,
          false,
          false);

  @FXML
  public Image leftCharacterAnimation =
      new Image(
          new File("src/main/resources/images/walkingLeft.gif").toURI().toString(),
          171,
          177,
          false,
          false);

  @FXML
  public Image leftCharacterIdle =
      new Image(
          new File("src/main/resources/images/gameCharacterArtLeft.png").toURI().toString(),
          171,
          177,
          false,
          false);

  @FXML
  public Image rightCharacterIdle =
      new Image(
          new File("src/main/resources/images/gameCharacterArtRight.png").toURI().toString(),
          171,
          177,
          false,
          false);

  @FXML
  public Image lastPlayedWalk =
      new Image(
          new File("src/main/resources/images/walkingLeft.gif").toURI().toString(),
          171,
          177,
          false,
          false);

  public Boolean walkAnimationPlaying = false;
  public Timeline alertBlinkTimeline;

  @FXML public Button btnRoom1;
  @FXML public Button button;
  @FXML public ImageView exclamationMark;
  @FXML public ImageView gameMaster;

  @FXML public Pane aiWindowController;

  TranslateTransition translate = new TranslateTransition();

  public AnimationTimer timer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {

          previousX = player.getLayoutX(); // Update previousX
          previousY = player.getLayoutY(); // Update previousY

          if (wKeyPressed.get()) {
            player.setLayoutY(player.getLayoutY() - movementVariable);
          }
          if (aKeyPressed.get()) {
            player.setLayoutX(player.getLayoutX() - movementVariable);
          }
          if (sKeyPressed.get()) {
            player.setLayoutY(player.getLayoutY() + movementVariable);
          }
          if (dKeyPressed.get()) {
            player.setLayoutX(player.getLayoutX() + movementVariable);
          }
          squareBorder();
        }
      };

  public void checkCollision2(ImageView player, List<Rectangle> walls) {
    for (Rectangle wall : walls) {
      if (player.getBoundsInParent().intersects(wall.getBoundsInParent())) {
        player.setLayoutX(previousX); // Restore the player's previous X position
        player.setLayoutY(previousY); // Restore the player's previous Y position
        // Exit the loop as soon as a collision is detected
      }
    }
    // Detect if the timer is 30 seconds left and start the alert blinking
    if (App.timerSeconds == 30) {
      if (!hasHappend) {
        System.out.println("30 seconds left");
        hasHappend = true;
        setupAlertBlinking();
      }
    } else if (App.timerSeconds == 0) {
      // Stop the alert blinking when the timer reaches 0
      stopAlertBlinking();
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
              wKeyPressed.get() || aKeyPressed.get() || sKeyPressed.get() || dKeyPressed.get();
          // when w key is pressed, player moves up
          if (e.getCode() == KeyCode.W) {
            if (walkAnimationPlaying == false) {
              player.setImage(lastPlayedWalk);
              walkAnimationPlaying = true;
            }
            wKeyPressed.set(true);
          }
          // when a key is pressed, player moves left
          if (e.getCode() == KeyCode.A) {
            if (player.getImage() != leftCharacterAnimation) {
              player.setImage(leftCharacterAnimation);
              walkAnimationPlaying = true;
              lastPlayedWalk = player.getImage();
            }
            aKeyPressed.set(true);
          }
          // when s key is pressed, player moves down
          if (e.getCode() == KeyCode.S) {
            if (walkAnimationPlaying == false) {
              player.setImage(lastPlayedWalk);
              walkAnimationPlaying = true;
            }
            sKeyPressed.set(true);
          }
          // when d key is pressed, player moves right
          if (e.getCode() == KeyCode.D) {
            if (player.getImage() != rightCharacterAnimation) {
              player.setImage(rightCharacterAnimation);
              walkAnimationPlaying = true;
              lastPlayedWalk = player.getImage();
            }
            dKeyPressed.set(true);
          }

          boolean isMoving =
              wKeyPressed.get() || aKeyPressed.get() || sKeyPressed.get() || dKeyPressed.get();

          // If we started moving and weren't before, start the sound.
          if (isMoving && !wasMoving) {
            walkingMediaPlayer.play();
          }
        });

    scene.setOnKeyReleased(
        e -> {
          boolean wasMoving =
              wKeyPressed.get() || aKeyPressed.get() || sKeyPressed.get() || dKeyPressed.get();
          // when w key is released, the player stops at its current position
          if (e.getCode() == KeyCode.W) {
            if (player.getImage() == leftCharacterAnimation
                && sKeyPressed.get() == false
                && aKeyPressed.get() == false) {
              player.setImage(leftCharacterIdle);
              walkAnimationPlaying = false;
            } else if (sKeyPressed.get() == true) {
              player.setImage(lastPlayedWalk);
            } else if (aKeyPressed.get() == false
                && dKeyPressed.get() == false
                && sKeyPressed.get() == false) {
              player.setImage(rightCharacterIdle);
              walkAnimationPlaying = false;
            }
            wKeyPressed.set(false);
          }
          // when a key is released, the player stops at its current position
          if (e.getCode() == KeyCode.A) {
            if (dKeyPressed.get() == false
                && wKeyPressed.get() == false
                && sKeyPressed.get() == false) {
              player.setImage(leftCharacterIdle);
              walkAnimationPlaying = false;
            } else if (dKeyPressed.get() == true) {
              player.setImage(rightCharacterAnimation);
            }

            aKeyPressed.set(false);
          }
          // when s key is released, the player stops at its current position
          if (e.getCode() == KeyCode.S) {
            if (player.getImage() == leftCharacterAnimation
                && wKeyPressed.get() == false
                && aKeyPressed.get() == false) {
              player.setImage(leftCharacterIdle);
              walkAnimationPlaying = false;
            } else if (wKeyPressed.get() == true) {
              player.setImage(lastPlayedWalk);
            } else if (aKeyPressed.get() == false
                && dKeyPressed.get() == false
                && wKeyPressed.get() == false) {
              player.setImage(rightCharacterIdle);
              walkAnimationPlaying = false;
            }
            sKeyPressed.set(false);
          }
          // when d key is released, the player stops at its current position
          if (e.getCode() == KeyCode.D) {
            if (aKeyPressed.get() == false
                && wKeyPressed.get() == false
                && sKeyPressed.get() == false) {
              player.setImage(rightCharacterIdle);
              walkAnimationPlaying = false;
            } else if (aKeyPressed.get() == true) {
              player.setImage(leftCharacterAnimation);
            }

            dKeyPressed.set(false);
          }

          boolean isMovinng =
              wKeyPressed.get() || aKeyPressed.get() || sKeyPressed.get() || dKeyPressed.get();

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
