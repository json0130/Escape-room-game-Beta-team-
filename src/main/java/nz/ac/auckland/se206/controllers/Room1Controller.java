package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class Room1Controller implements Initializable {

  private BooleanProperty wPressed = new SimpleBooleanProperty();
  private BooleanProperty aPressed = new SimpleBooleanProperty();
  private BooleanProperty sPressed = new SimpleBooleanProperty();
  private BooleanProperty dPressed = new SimpleBooleanProperty();

  private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);
  private int movementVariable = 5;
  private double shapesize;

  List<Rectangle> walls = new ArrayList<>();

  @FXML private ImageView player;
  @FXML private ImageView soundOn;
  @FXML private ImageView soundOff;
  @FXML private Pane scene;
  @FXML private Pane alert;

  private double previousX;
  private double previousY;

  @FXML private Rectangle exit;
  @FXML private Rectangle wall1;
  @FXML private Rectangle wall2;
  @FXML private Rectangle wall3;
  @FXML private Rectangle blinkingRectangle;
  @FXML private Rectangle crew1Collision;
  @FXML private Rectangle crew2Collision;
  @FXML private Rectangle crew3Collision;
  @FXML private Rectangle crew4Collision;

  @FXML private Button btnCollect1;
  @FXML private Button btnCollect2;
  @FXML private Button btnCollect3;
  @FXML private Button btnCollect4;
  @FXML private Button btnRiddle;

  @FXML private ImageView crew1;
  @FXML private ImageView crew2;
  @FXML private ImageView crew3;
  @FXML private ImageView crew4;

  @FXML private ImageView idCaptain;
  @FXML private ImageView idChef;
  @FXML private ImageView idDoctor;
  @FXML private ImageView idEngineer;

  @FXML private ImageView crew1Indicator;
  @FXML private ImageView crew2Indicator;
  @FXML private ImageView crew3Indicator;
  @FXML private ImageView crew4Indicator;

  @FXML private ImageView gameMaster;

  @FXML private Label difficultyLabel;
  @FXML private Label hintLabel;
  @FXML private Label hintLabel2;
  @FXML private Label clickLabel;

  @FXML private Button btnSend;
  @FXML private Button btnClose;
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;

  @FXML public Pane aiWindowController;

  private FadeTransition fadeTransition;
  public static String riddleAnswer;
  public boolean isCrew1Colliding = false;
  public boolean isCrew2Colliding = false;
  public boolean isCrew3Colliding = false;
  public boolean isCrew4Colliding = false;

  private MediaPlayer walkingMediaPlayer;

  @FXML private Button toggleSoundButton;

  // Add this variable to your class
  private Timeline alertBlinkTimeline;

  private boolean nextToButton = false;
  private boolean hasHappend = false;

  // timer for collsion check between monitor and walls
  AnimationTimer collisionTimer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          checkCollision2(player, walls);
          checkExit(player, exit);
          checkMonitor(player, wall2);
        }
      };

  // Prevent the character moves outside of the window
  AnimationTimer timer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {

          previousX = player.getLayoutX(); // Update previousX
          previousY = player.getLayoutY(); // Update previousY

          if (wPressed.get()) {
            player.setLayoutY(player.getLayoutY() - movementVariable);
            System.out.println("w");
          }
          if (aPressed.get()) {
            player.setLayoutX(player.getLayoutX() - movementVariable);
            System.out.println("a");
          }
          if (sPressed.get()) {
            player.setLayoutY(player.getLayoutY() + movementVariable);
            System.out.println("s");
          }
          if (dPressed.get()) {
            player.setLayoutX(player.getLayoutX() + movementVariable);
            System.out.println("d");
          }
          squareBorder();
        }
      };

  public void initialize(URL url, ResourceBundle resource) {
    animateRobot();
    shapesize = player.getFitWidth();
    movementSetup();
    alert.setVisible(false);

    aiWindowController.setVisible(true);

    collisionTimer.start();

    walls.add(wall1);
    walls.add(wall3);

    // Add an event handler to the Toggle Sound button
    toggleSoundButton.setOnMouseClicked(this::toggleSound);

    String walkSoundEffect = "src/main/resources/sounds/walking.mp3";
    Media walkMedia = new Media(new File(walkSoundEffect).toURI().toString());
    walkingMediaPlayer = new MediaPlayer(walkMedia);
    walkingMediaPlayer.setVolume(2.0);

    previousX = player.getLayoutX();
    previousY = player.getLayoutY();

    // randomly choose riddle answer
    Random random = new Random();
    int randomNumber = random.nextInt(4);
    if (randomNumber == 0) {
      riddleAnswer = "captain";
    } else if (randomNumber == 1) {
      riddleAnswer = "doctor";
    } else if (randomNumber == 2) {
      riddleAnswer = "chef";
    } else if (randomNumber == 3) {
      riddleAnswer = "engineer";
    }
    // Set all id not visible
    idCaptain.setVisible(false);
    idDoctor.setVisible(false);
    idChef.setVisible(false);
    idEngineer.setVisible(false);

    keyPressed.addListener(
        ((observableValue, aBoolean, t1) -> {
          if (!aBoolean) {
            timer.start();
          } else {
            timer.stop();
          }
        }));

    fadeTransition = new FadeTransition(Duration.seconds(1), blinkingRectangle);
    fadeTransition.setFromValue(1.0);
    fadeTransition.setToValue(0.0);
    fadeTransition.setCycleCount(FadeTransition.INDEFINITE); // Blink indefinitely
    fadeTransition.setAutoReverse(true); // Reverse the animation
    // Start the animation
    fadeTransition.play();

    btnCollect1.setVisible(false);
    btnCollect2.setVisible(false);
    btnCollect3.setVisible(false);
    btnCollect4.setVisible(false);
    btnRiddle.setVisible(false);
    blinkingRectangle.setVisible(true);
    clickLabel.setVisible(false);

    crew1Indicator.setVisible(false);
    crew2Indicator.setVisible(false);
    crew3Indicator.setVisible(false);
    crew4Indicator.setVisible(false);

    // if difficulty is selected, label is updated
    detectDifficulty();

    // when the riddle is resolved, indicators are visible
    revealIndicator();

    crewCollisionTimer.start();
  }

  // hide id and button and indicator at once
  private void hideId1() {
    idDoctor.setVisible(false);
    btnCollect1.setVisible(false);
    crew1Indicator.setVisible(true);
  }

  // hide id and button and indicator at once
  private void hideId2() {
    idCaptain.setVisible(false);
    btnCollect2.setVisible(false);
    crew2Indicator.setVisible(true);
  }

  // hide id and button and indicator at once
  private void hideId3() {
    idChef.setVisible(false);
    btnCollect3.setVisible(false);
    crew3Indicator.setVisible(true);
  }

  // hide id and button and indicator at once
  private void hideId4() {
    idEngineer.setVisible(false);
    btnCollect4.setVisible(false);
    crew4Indicator.setVisible(true);
  }

  private void showId1() {
    idDoctor.setVisible(true);
    btnCollect1.setVisible(true);
    crew1Indicator.setVisible(false);
  }

  private void showId2() {
    idCaptain.setVisible(true);
    btnCollect2.setVisible(true);
    crew2Indicator.setVisible(false);
  }

  private void showId3() {
    idChef.setVisible(true);
    btnCollect3.setVisible(true);
    crew3Indicator.setVisible(false);
  }

  private void showId4() {
    idEngineer.setVisible(true);
    btnCollect4.setVisible(true);
    crew4Indicator.setVisible(false);
  }

  // If collect button is pressed, id is corrected and state of idcollected changes
  public void onCollect1() {
    GameState.isDoctorCollected = true;
    GameState.isIdCollected = true;
    hideId1();
    crew1Indicator.setVisible(false);
    scene.requestFocus(); // Add this line
  }

  public void onCollect2() {
    GameState.isCaptainCollected = true;
    GameState.isIdCollected = true;
    hideId2();
    crew2Indicator.setVisible(false);
    scene.requestFocus(); // Add this line
  }

  public void onCollect3() {
    GameState.isChefCollected = true;
    GameState.isIdCollected = true;
    hideId3();
    crew3Indicator.setVisible(false);
    scene.requestFocus(); // Add this line
  }

  public void onCollect4() {
    GameState.isEngineerCollected = true;
    GameState.isIdCollected = true;
    hideId4();
    crew4Indicator.setVisible(false);
    scene.requestFocus(); // Add this line
  }

  @FXML
  public void onRiddle(MouseEvent evnet) throws IOException {
    if (nextToButton) {
      soundButttonClick();
      GameState.isRiddleGiven = true;
      App.setScene(AppUi.CHAT);
      enterRoom();
    }
  }

  // if the character collides the box, it will move to the map
  public void checkExit(ImageView player, Rectangle exit) {
    if (player.getBoundsInParent().intersects(exit.getBoundsInParent())) {
      exit.setOpacity(1);
      timer.stop();

      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(
          event -> {
            // Adjust the player's position to be right in front of the room
            player.setLayoutX(433);
            player.setLayoutY(468);
            GameState.isPlayerInMap = true;
            GameState.isPlayerInRoom1 = false;
            // GameState.hasHappend = false;
            App.setScene(AppUi.PLAYER);
            enterRoom();
          });
      pauseTransition.play();
    } else {
      exit.setOpacity(0.6);
    }
  }

  // Exit the loop as soon as a collision is detected
  public void checkCollision2(ImageView player, List<Rectangle> walls) {
    for (Rectangle wall : walls) {
      if (player.getBoundsInParent().intersects(wall.getBoundsInParent())) {
        player.setLayoutX(previousX); // Restore the player's previous X position
        player.setLayoutY(previousY); // Restore the player's previous Y position
      }
    }
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

  // if the character collides with the mointor, button to the riddle chat scene appears
  private void checkMonitor(ImageView player, Rectangle wall2) {
    if (player.getBoundsInParent().intersects(wall2.getBoundsInParent())) {
      blinkingRectangle.setOpacity(1);
      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(
          event -> {
            // Adjust the player's position to be right in front of the room
            blinkingRectangle.setFill(javafx.scene.paint.Color.WHITE);
            clickLabel.setVisible(true);
            nextToButton = true;
          });
      pauseTransition.play();
    } else {
      clickLabel.setVisible(false);
      blinkingRectangle.setFill(javafx.scene.paint.Color.TRANSPARENT);
      nextToButton = false;
    }
  }

  // code for player movement using wasd keys
  @FXML
  public void movementSetup() {
    scene.setOnKeyPressed(
        e -> {
          boolean wasMoving = wPressed.get() || aPressed.get() || sPressed.get() || dPressed.get();

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

          boolean isMoving = wPressed.get() || aPressed.get() || sPressed.get() || dPressed.get();

          // If we started moving and weren't before, start the sound.
          if (isMoving && !wasMoving) {
            walkingMediaPlayer.play();
          }
        });

    scene.setOnKeyReleased(
        e -> {
          boolean wasMoving = wPressed.get() || aPressed.get() || sPressed.get() || dPressed.get();

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

          boolean isMovinng = wPressed.get() || aPressed.get() || sPressed.get() || dPressed.get();

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

  // prevent the player moves out of the window
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
  private void back(ActionEvent event) {
    App.setScene(AppUi.PLAYER);
  }

  // detect change in the game state difficulty in the intro scene
  private void detectDifficulty() {
    Timer labelTimer = new Timer(true);
    labelTimer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            if (GameState.difficulty != null) {
              if (GameState.difficulty.equals("MEDIUM")) {
                Platform.runLater(() -> updateLabels());
                if (GameState.numOfHints == 0) {
                  labelTimer.cancel();
                }
              } else {
                Platform.runLater(() -> updateLabels());
                labelTimer.cancel();
              }
            }
          }
        },
        0,
        500);
  }

  // Modify your setupAlertBlinking method as follows
  private void setupAlertBlinking() {
    alert.setVisible(true); // Initially show the alert label

    // Set up the blinking animation for the alert label
    alertBlinkTimeline =
        new Timeline(
            new KeyFrame(Duration.seconds(0.5), e -> alert.setVisible(true)),
            new KeyFrame(Duration.seconds(1), e -> alert.setVisible(false)));
    alertBlinkTimeline.setCycleCount(Timeline.INDEFINITE);
    alertBlinkTimeline.play();
  }

  // Add a method to stop the alert blinking
  private void stopAlertBlinking() {
    if (alertBlinkTimeline != null) {
      alertBlinkTimeline.stop();
      alert.setVisible(false);
    }
  }

  // update labels for difficulty and hints as the game progress
  private void updateLabels() {
    difficultyLabel.setText(GameState.difficulty);
    if (GameState.difficulty == "EASY") {
      hintLabel.setText("UNLIMITED");
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

  // Detect if the character goes closer to costumes
  AnimationTimer crewCollisionTimer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          // if character near costume1 and not collected, show id card and hide indicator
          if (GameState.isRiddleResolved) {
            if (player.getBoundsInParent().intersects(crew1Collision.getBoundsInParent())) {
              isCrew1Colliding = true;
              if (!GameState.isDoctorCollected) {
                Platform.runLater(() -> showId1());
              }
            } else if (!GameState.isDoctorCollected) {
              isCrew1Colliding = false;
              Platform.runLater(() -> hideId1());
            }
            // if character near costume2 and not collected, show id and hide indicator
            if (player.getBoundsInParent().intersects(crew2Collision.getBoundsInParent())) {
              isCrew2Colliding = true;
              if (!GameState.isCaptainCollected) {
                Platform.runLater(() -> showId2());
              }
            } else if (!GameState.isCaptainCollected) {
              isCrew2Colliding = false;
              Platform.runLater(() -> hideId2());
            }
            // if character near costume3 and not collected, show id and hide indicator
            if (player.getBoundsInParent().intersects(crew3Collision.getBoundsInParent())) {
              isCrew3Colliding = true;
              if (!GameState.isChefCollected) {
                Platform.runLater(() -> showId3());
              }
            } else if (!GameState.isChefCollected) {
              isCrew3Colliding = false;
              Platform.runLater(() -> hideId3());
            }
            // if character near costume4 and not collected, show id and hide indicator
            if (player.getBoundsInParent().intersects(crew4Collision.getBoundsInParent())) {
              isCrew4Colliding = true;
              if (!GameState.isEngineerCollected) {
                Platform.runLater(() -> showId4());
              }
            } else if (!GameState.isEngineerCollected) {
              isCrew4Colliding = false;
              Platform.runLater(() -> hideId4());
            }
          }
        }
      };

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

  @FXML
  private void enterRoom() {
    String soundEffect = "src/main/resources/sounds/enterReal.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setAutoPlay(true);
  }

  // game master animation
  @FXML
  private void animateRobot() {
    TranslateTransition translate = new TranslateTransition();
    translate.setNode(gameMaster);
    translate.setDuration(Duration.millis(1000));
    translate.setCycleCount(TranslateTransition.INDEFINITE);
    translate.setByX(0);
    translate.setByY(20);
    translate.setAutoReverse(true);
    translate.play();
  }

  // Revel indicators after the player resolves the riddle
  private void revealIndicator() {
    Timer indicatorTimer = new Timer(true);
    indicatorTimer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            // if the state of irRiddleResolved changed, indicators are visible
            if (GameState.isRiddleResolved) {
              System.out.println("riddle is resolved");
              Platform.runLater(() -> showIndicators());
              indicatorTimer.cancel();
            }
          }
        },
        0,
        100);
  }

  // show all indicators at once
  private void showIndicators() {
    crew1Indicator.setVisible(true);
    crew2Indicator.setVisible(true);
    crew3Indicator.setVisible(true);
    crew4Indicator.setVisible(true);
  }
}
