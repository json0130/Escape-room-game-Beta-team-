package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatBubble;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller class for the room view. */
public class TileGameRoomController extends RoomController {
  public static ObservableList<ChatBubble> chatBubbleListTileRoom =
      FXCollections.observableArrayList();

  private BooleanProperty up = new SimpleBooleanProperty();
  private BooleanProperty left = new SimpleBooleanProperty();
  private BooleanProperty down = new SimpleBooleanProperty();
  private BooleanProperty right = new SimpleBooleanProperty();

  private BooleanBinding keyPressed = up.or(left).or(down).or(right);
  private int movementVariable = 5;

  private List<Rectangle> walls = new ArrayList<>();

  @FXML private ImageView player;
  @FXML private Pane scene;
  @FXML private Pane alert;

  private double previousX;
  private double previousY;
  @FXML private Label difficultyLabel;
  @FXML private Label hintLabel;
  @FXML private Label hintLabel2;

  @FXML private Rectangle door;
  @FXML private Rectangle window;
  @FXML private Rectangle vase;
  @FXML private Rectangle startTileGame;
  @FXML private Label clickButton;

  @FXML private Rectangle exit;
  @FXML private Rectangle wall;
  @FXML private Rectangle wall2;
  @FXML private Rectangle wall3;
  @FXML private Rectangle wall4;
  @FXML private Rectangle wall5;
  @FXML private Rectangle wall6;
  @FXML private Rectangle wall7;
  @FXML private Rectangle wall8;
  @FXML private Rectangle wall9;
  @FXML private Rectangle wall10;
  @FXML private Rectangle wall11;
  @FXML private Rectangle wall12;
  @FXML private Rectangle wall13;
  @FXML private Rectangle wall14;
  @FXML private Rectangle wall15;
  @FXML private Rectangle wall16;
  @FXML private Rectangle wall17;
  @FXML private Rectangle wall18;
  @FXML private Rectangle wall19;
  @FXML private Rectangle wall20;
  @FXML private Rectangle blinkingRectangle;
  @FXML private FadeTransition fadeTransition;
  @FXML private ImageView soundOn;
  @FXML private ImageView soundOff;
  @FXML private ScrollPane chatPaneOne;
  @FXML private VBox chatContainerOne;

  @FXML private Rectangle black2;
  @FXML private Rectangle resetBox;
  @FXML private Label resetLabel;
  @FXML private Button resetYes;
  @FXML private Button resetCancel;

  private boolean hasHappend = false;

  @FXML private Button toggleSoundButton;
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

  private Boolean walkAnimationPlaying = false;
  private boolean nextToButton = false;
  @FXML private Button btnRoom1;
  @FXML private Button button;
  @FXML private ImageView exclamationMark;
  @FXML private ImageView gameMaster;

  @FXML private Pane aiWindowController;

  private TranslateTransition translate = new TranslateTransition();

  private boolean isGreetingShown = true;

  private AnimationTimer collisionTimer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          checkCollision2(player, walls);
          checkExit(player, exit);
          checkMonitor(player, blinkingRectangle);
        }
      };

  private AnimationTimer timer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {

          previousX = player.getLayoutX(); // Update previousX
          previousY = player.getLayoutY(); // Update previousY

          if (up.get()) {
            player.setLayoutY(player.getLayoutY() - movementVariable);
          }
          if (left.get()) {
            player.setLayoutX(player.getLayoutX() - movementVariable);
          }
          if (down.get()) {
            player.setLayoutY(player.getLayoutY() + movementVariable);
          }
          if (right.get()) {
            player.setLayoutX(player.getLayoutX() + movementVariable);
          }
          squareBorder();
        }
      };

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    animateExclamationMark();

    // if difficulty is selected, label is updated
    detectDifficulty();

    walls.add(wall);
    walls.add(wall2);
    walls.add(wall3);
    walls.add(wall4);
    walls.add(wall5);
    walls.add(wall6);
    walls.add(wall7);
    walls.add(wall8);
    walls.add(wall9);
    walls.add(wall10);
    walls.add(wall11);
    walls.add(wall12);
    walls.add(wall13);
    walls.add(wall14);
    walls.add(wall15);
    walls.add(wall16);
    walls.add(wall17);
    walls.add(wall18);
    walls.add(wall19);
    walls.add(wall20);

    black2.setVisible(false);
    resetBox.setVisible(false);
    resetLabel.setVisible(false);
    resetYes.setVisible(false);
    resetCancel.setVisible(false);

    // Add an event handler to the Toggle Sound button
    toggleSoundButton.setOnMouseClicked(this::toggleSound);

    String walkSoundEffect = "src/main/resources/sounds/walking.mp3";
    Media walkMedia = new Media(new File(walkSoundEffect).toURI().toString());
    walkingMediaPlayer = new MediaPlayer(walkMedia);
    walkingMediaPlayer.setVolume(2.0);

    alert.setVisible(false); // Initially hide the alert label
    aiWindowController.setVisible(true);

    player.getFitWidth();
    movingSetup();

    collisionTimer.start();

    previousX = player.getLayoutX();
    previousY = player.getLayoutY();

    keyPressed.addListener(
        ((observableValue, isKeyPressed, time) -> {
          if (!isKeyPressed) {
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

    ListChangeListener<ChatBubble> listener3 =
        change -> {
          Platform.runLater(
              () -> {
                chatContainerOne
                    .getChildren()
                    .addAll(
                        chatBubbleListTileRoom
                            .get(chatBubbleListTileRoom.size() - 1)
                            .getBubbleBox());
                chatContainerOne.setAlignment(Pos.TOP_RIGHT);
                chatPaneOne.vvalueProperty().bind(chatContainerOne.heightProperty());
                System.out.println(
                    "Added: "
                        + chatBubbleListTileRoom
                            .get(chatBubbleListTileRoom.size() - 1)
                            .getBubbleText()
                            .getText()
                        + " "
                        + this.getClass().getSimpleName());
              });
        };
    chatBubbleListTileRoom.addListener(listener3);
  }

  private void checkExit(ImageView player, Rectangle exit) {
    if (player.getBoundsInParent().intersects(exit.getBoundsInParent())) {
      exit.setOpacity(1);
      timer.stop();

      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(
          event -> {
            // Adjust the player's position to be right in front of the room
            player.setLayoutX(404);
            player.setLayoutY(410);
            GameState.isPlayerInMap = true;
            GameState.isPlayerInRoom2 = false;
            GameState.hasHappend = false;
            App.setScene(AppUi.PLAYER);
            simulateKeyPressAfterDelay();
            // if (aPressed.get() == true && sPressed.get() == true) {}
            enterRoom();
          });
      pauseTransition.play();
    } else {
      exit.setOpacity(0.6);
    }
  }

  /**
   * Check if the player is colliding with any of the walls.
   *
   * @param player the player
   * @param walls A list of the walls
   */
  @FXML
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

  /**
   * Check if the player is next to the monitor.
   *
   * @param player the player
   * @param wa the monitor
   * @return true if the player is next to the monitor, false otherwise
   */
  @FXML
  private void checkMonitor(ImageView player, Rectangle wa) {
    // if player is next to the monitor, show the button
    if (player.getBoundsInParent().intersects(wa.getBoundsInParent())) {
      blinkingRectangle.setOpacity(1);
      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(
          event -> {
            // Adjust the player's position to be right in front of the room
            blinkingRectangle.setFill(javafx.scene.paint.Color.WHITE);
            clickButton.setVisible(true);
            nextToButton = true;
          });
      pauseTransition.play();
    } else {
      clickButton.setVisible(false);
      blinkingRectangle.setFill(javafx.scene.paint.Color.TRANSPARENT);
      nextToButton = false;
    }
  }

  /**
   * code for player movement using wasd keys. This method also plays the animations which are gifs
   * that switch in and out depending on what keys the player has pressed.
   */
  @FXML
  public void movingSetup() {
    scene.setOnKeyPressed(
        e -> {
          boolean wasMoving = up.get() || left.get() || down.get() || right.get();
          // when w key is pressed, player moves up
          if (e.getCode() == KeyCode.W) {
            if (walkAnimationPlaying == false) {
              player.setImage(lastPlayedWalk);
              walkAnimationPlaying = true;
            }
            up.set(true);
          }
          // when a key is pressed, player moves left
          if (e.getCode() == KeyCode.A) {
            if (player.getImage() != leftCharacterAnimation) {
              player.setImage(leftCharacterAnimation);
              walkAnimationPlaying = true;
              lastPlayedWalk = player.getImage();
            }
            left.set(true);
          }
          // when s key is pressed, player moves down
          if (e.getCode() == KeyCode.S) {
            if (walkAnimationPlaying == false) {
              player.setImage(lastPlayedWalk);
              walkAnimationPlaying = true;
            }
            down.set(true);
          }
          // when d key is pressed, player moves right
          if (e.getCode() == KeyCode.D) {
            if (player.getImage() != rightCharacterAnimation) {
              player.setImage(rightCharacterAnimation);
              walkAnimationPlaying = true;
              lastPlayedWalk = player.getImage();
            }
            right.set(true);
          }

          boolean isMoving = up.get() || left.get() || down.get() || right.get();

          // If we started moving and weren't before, start the sound.
          if (isMoving && !wasMoving) {
            walkingMediaPlayer.play();
          }
        });

    scene.setOnKeyReleased(
        e -> {
          boolean wasMoving = up.get() || left.get() || down.get() || right.get();
          // when w key is released, the player stops at its current position
          if (e.getCode() == KeyCode.W) {
            if (player.getImage() == leftCharacterAnimation
                && down.get() == false
                && left.get() == false) {
              player.setImage(leftCharacterIdle);
              walkAnimationPlaying = false;
            } else if (down.get() == true) {
              player.setImage(lastPlayedWalk);
            } else if (left.get() == false && right.get() == false && down.get() == false) {
              player.setImage(rightCharacterIdle);
              walkAnimationPlaying = false;
            }
            up.set(false);
          }
          // when a key is released, the player stops at its current position
          if (e.getCode() == KeyCode.A) {
            if (right.get() == false && up.get() == false && down.get() == false) {
              player.setImage(leftCharacterIdle);
              walkAnimationPlaying = false;
            } else if (right.get() == true) {
              player.setImage(rightCharacterAnimation);
            }

            left.set(false);
          }
          // when s key is released, the player stops at its current position
          if (e.getCode() == KeyCode.S) {
            if (player.getImage() == leftCharacterAnimation
                && up.get() == false
                && left.get() == false) {
              player.setImage(leftCharacterIdle);
              walkAnimationPlaying = false;
            } else if (up.get() == true) {
              player.setImage(lastPlayedWalk);
            } else if (left.get() == false && right.get() == false && up.get() == false) {
              player.setImage(rightCharacterIdle);
              walkAnimationPlaying = false;
            }
            down.set(false);
          }
          // when d key is released, the player stops at its current position
          if (e.getCode() == KeyCode.D) {
            if (left.get() == false && up.get() == false && down.get() == false) {
              player.setImage(rightCharacterIdle);
              walkAnimationPlaying = false;
            } else if (left.get() == true) {
              player.setImage(leftCharacterAnimation);
            }

            right.set(false);
          }

          boolean isMovinng = up.get() || left.get() || down.get() || right.get();

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

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }

  /**
   * Handles the click event on the window.
   *
   * @param event the mouse event
   * @throws IOException if the game cannot be reset
   */
  @FXML
  public void clickWindow(MouseEvent event) {
    System.out.println("window clicked");
  }

  /**
   * Method to allow player to enter the tile game scene.
   *
   * @throws IOException if the game cannot be reset
   */
  @FXML
  public void onTileGameButtonClick() throws IOException {
    translate.stop();
    // Change the scene to the desk scene and change the background music
    if (nextToButton) {
      GameState.foundComputer = true;
      exclamationMark.setVisible(false);
      App.setScene(AppUi.TILEPUZZLE);
      enterRoom();
    }
  }

  /** detect change in game state difficulty which is selected in the intro scene. */
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

  // exclamation mark for monitor animation
  @FXML
  private void animateExclamationMark() {
    translate.setNode(exclamationMark);
    translate.setDuration(Duration.millis(1000)); // exclamation mark moves every 1 second
    translate.setCycleCount(TranslateTransition.INDEFINITE); // the animaion continuously goes
    translate.setByX(0);
    translate.setByY(20);
    translate.setAutoReverse(true); // the exclamation mark returns to its original position
    translate.play();
  }

  /** After the player close the greeting, the character can move. */
  @FXML
  private void enablePlayerMovement() {
    Timer greetingTimer = new Timer(true);
    // frequently check if the greeting is shown or not
    greetingTimer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            // if greeting is shown, enable player movement
            if (!isGreetingShown) {
              movingSetup();
              greetingTimer.cancel();
            }
          }
        },
        0,
        100);
  }

  /**
   * Releases W A S D keys using the keyReleased KeyEvent. This method is triggered when player is
   * leaving a room that isnt the main room.
   */
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
  private void onRestartLabelClick(ActionEvent event) throws IOException {
    black2.setVisible(true);
    resetBox.setVisible(true);
    resetLabel.setVisible(true);
    resetYes.setVisible(true);
    resetCancel.setVisible(true);
  }

  @FXML
  private void onCancelRestartClick(ActionEvent event) throws IOException {
    black2.setVisible(false);
    resetBox.setVisible(false);
    resetLabel.setVisible(false);
    resetYes.setVisible(false);
    resetCancel.setVisible(false);
  }

  @FXML
  private void onRestartButtonClick(ActionEvent event) throws IOException {
    try {
      GameState.resetGames();
    } catch (Exception e) {
      // TODO: handle exception
    }
  }
}
