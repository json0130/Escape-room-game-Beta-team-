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
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
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
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

public class PlayerController implements Initializable {
  public static boolean hintContained = false;
  public static boolean answerContained = false;
  public static ObservableList<ChatBubble> chatBubbleListPlayer =
      FXCollections.observableArrayList();

  private BooleanProperty isWKeyPressed = new SimpleBooleanProperty();
  private BooleanProperty isAKeyPressed = new SimpleBooleanProperty();
  private BooleanProperty isSKeyPressed = new SimpleBooleanProperty();
  private BooleanProperty isDKeyPressed = new SimpleBooleanProperty();

  private BooleanBinding keyPressed =
      isWKeyPressed.or(isAKeyPressed).or(isSKeyPressed).or(isDKeyPressed);
  private int movementVariable = 5;
  private double shapesize;

  private List<Rectangle> walls = new ArrayList<>();

  @FXML private ImageView player;
  @FXML private Rectangle room1;
  @FXML private Rectangle room2;
  @FXML private Rectangle room3;
  @FXML private Rectangle black;
  @FXML private ImageView gameMaster;
  @FXML private ImageView soundOn;
  @FXML private ImageView soundOff;
  @FXML private Label playerLabel;
  @FXML private Label greeting;

  @FXML private Label main;
  @FXML private Label computer;
  @FXML private Label closet;
  @FXML private Label control;
  @FXML private Label difficultyLabel;
  @FXML private Label hintLabel;
  @FXML private Label hintLabel2;

  @FXML private Rectangle wall;
  @FXML private Rectangle wall1;
  @FXML private Rectangle wall2;
  @FXML private Rectangle wall4;
  @FXML private Rectangle wall5;
  @FXML private Rectangle wall6;
  @FXML private Rectangle wall7;
  @FXML private Rectangle wall8;
  @FXML private Rectangle wall9;
  @FXML private Rectangle wall10;
  @FXML private Rectangle wall12;
  @FXML private Rectangle wall13;
  @FXML private Rectangle wall14;
  @FXML private Rectangle wall15;
  @FXML private Rectangle wall16;
  @FXML private Rectangle wall17;
  @FXML private Rectangle wall18;
  @FXML private Rectangle wall19;
  @FXML private Rectangle wall20;
  @FXML private Rectangle wall21;

  @FXML private Pane scene;
  @FXML private Pane alert;

  @FXML private Button reset;
  @FXML private Button btnSend;
  @FXML private Button btnClose;
  @FXML private Button resetButton;

  @FXML private Rectangle black2;
  @FXML private Rectangle resetBox;
  @FXML private Label resetLabel;
  @FXML private Button resetYes;
  @FXML private Button resetCancel;

  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private ScrollPane chatPane;
  @FXML private VBox chatContainer;

  private double previousX;
  private double previousY;

  private boolean isGreetingShown = true;

  @FXML private Button toggleSoundButton;

  @FXML private Label countdownLabel;

  @FXML private boolean hasHappened = false;

  @FXML private Timeline alertBlinkTimeline;
  @FXML public Pane aiWindowController;

  @FXML private MediaPlayer walkingMediaPlayer;

  @FXML
  Image rightCharacterAnimation =
      new Image(
          new File("src/main/resources/images/walkingRight.gif").toURI().toString(),
          171,
          177,
          false,
          false);

  @FXML
  Image leftCharacterAnimation =
      new Image(
          new File("src/main/resources/images/walkingLeft.gif").toURI().toString(),
          171,
          177,
          false,
          false);

  @FXML
  Image leftCharacterIdle =
      new Image(
          new File("src/main/resources/images/gameCharacterArtLeft.png").toURI().toString(),
          171,
          177,
          false,
          false);

  @FXML
  Image rightCharacterIdle =
      new Image(
          new File("src/main/resources/images/gameCharacterArtRight.png").toURI().toString(),
          171,
          177,
          false,
          false);

  @FXML
  Image lastPlayedWalk =
      new Image(
          new File("src/main/resources/images/walkingLeft.gif").toURI().toString(),
          171,
          177,
          false,
          false);

  Boolean walkAnimationPlaying = false;

  private AnimationTimer collisionTimer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          checkCollision2(player, walls);
          checkRoom1(player, room1);
          checkRoom2(player, room2);
          checkRoom3(player, room3);
        }
      };

  private AnimationTimer timer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          black.setVisible(false);

          previousX = player.getLayoutX(); // Update previousX
          previousY = player.getLayoutY(); // Update previousY

          if (isWKeyPressed.get()) {
            player.setLayoutY(player.getLayoutY() - movementVariable);
          }
          if (isAKeyPressed.get()) {
            player.setLayoutX(player.getLayoutX() - movementVariable);
          }
          if (isSKeyPressed.get()) {
            player.setLayoutY(player.getLayoutY() + movementVariable);
          }
          if (isDKeyPressed.get()) {
            player.setLayoutX(player.getLayoutX() + movementVariable);
          }
          squareBorder();
        }
      };

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    playerLabel.setVisible(true);
    black.setVisible(true);

    black2.setVisible(false);
    resetBox.setVisible(false);
    resetLabel.setVisible(false);
    resetYes.setVisible(false);
    resetCancel.setVisible(false);

    // Add an event handler to the Toggle Sound button
    toggleSoundButton.setOnMouseClicked(this::toggleSound);
    aiWindowController.setVisible(true);

    room1.setVisible(false);
    room2.setVisible(false);
    room3.setVisible(false);
    // Set labels to have white text and black stroke for the text
    // main.setStyle("-fx-text-fill: white; -fx-stroke: black; -fx-stroke-width: 1px;");
    computer.setStyle("-fx-text-fill: white; -fx-stroke: black; -fx-stroke-width: 1px;");
    closet.setStyle("-fx-text-fill: white; -fx-stroke: black; -fx-stroke-width: 1px;");
    control.setStyle("-fx-text-fill: white; -fx-stroke: black; -fx-stroke-width: 1px;");

    alert.setVisible(false);

    shapesize = player.getFitWidth();

    walls.add(wall);
    walls.add(wall1);
    walls.add(wall2);
    walls.add(wall4);
    walls.add(wall5);
    walls.add(wall6);
    walls.add(wall7);
    walls.add(wall8);
    walls.add(wall9);
    walls.add(wall10);
    walls.add(wall12);
    walls.add(wall13);
    walls.add(wall14);
    walls.add(wall15);
    walls.add(wall16);
    walls.add(wall17);
    walls.add(wall18);
    walls.add(wall19);
    walls.add(wall20);
    walls.add(wall21);

    String walkSoundEffect = "src/main/resources/sounds/walking.mp3";
    Media walkMedia = new Media(new File(walkSoundEffect).toURI().toString());
    walkingMediaPlayer = new MediaPlayer(walkMedia);
    walkingMediaPlayer.setVolume(4.0);

    collisionTimer.start();

    previousX = player.getLayoutX();
    previousY = player.getLayoutY();

    keyPressed.addListener(
        ((observable, wasKeyPressed, isKeyPressed) -> {
          if (!wasKeyPressed) {
            timer.start();
          } else {
            timer.stop();
          }
        }));

    // if difficulty is selected, label is updated
    detectDifficulty();
    movementSetup();

    ListChangeListener<ChatBubble> listener2 =
        change -> {
          Platform.runLater(
              () -> {
                chatContainer
                    .getChildren()
                    .addAll(
                        chatBubbleListPlayer.get(chatBubbleListPlayer.size() - 1).getBubbleBox());
                chatContainer.setAlignment(Pos.TOP_CENTER);
                chatPane.vvalueProperty().bind(chatContainer.heightProperty());
                System.out.println(
                    "Added: "
                        + chatBubbleListPlayer
                            .get(chatBubbleListPlayer.size() - 1)
                            .getBubbleText()
                            .getText()
                        + " "
                        + this.getClass().getSimpleName());
              });
        };
    chatBubbleListPlayer.addListener(listener2);
  }

  @FXML
  void start(ActionEvent event) {
    player.setLayoutX(10);
    player.setLayoutY(200);
  }

  // Modify your setupAlertBlinking method as follows
  private void setupAlertBlinking() {
    alert.setVisible(true); // Initially show the alert label
    // Stop current playing media
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

  @FXML
  private void stopAlertBlinking() {
    // Add a method to stop the alert blinking
    if (alertBlinkTimeline != null) {
      // Stop timeline and hide label
      alertBlinkTimeline.stop();
      App.alertSoundPlayer.stop();
    }
  }

  /**
   * When the player is having collisions with room1 then it will go to the room1.
   *
   * @param event when the player is having collisions with room1
   * @throws IOException if an input or output exception occurred
   */
  @FXML
  public void checkRoom1(ImageView player, Rectangle room1) {
    if (player.getBoundsInParent().intersects(room1.getBoundsInParent())) {
      room1.setVisible(true);
      timer.stop();
      enterRoom();

      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(
          event -> {
            // Adjust the player's position to be right in front of the room
            player.setLayoutX(272);
            player.setLayoutY(336);
            GameState.isPlayerInRoom1 = true;
            GameState.isPlayerInMap = false;
            GameState.beenToRoom1 = true;
            App.setScene(AppUi.ROOM1);
            simulateKeyPressAfterDelay();
          });
      pauseTransition.play();
    } else {
      room1.setVisible(false);
    }
  }

  /**
   * When player is having collisions with room2 then it will go to the room2.
   *
   * @param event when the player is having collisions with room2
   * @throws IOException
   */
  public void checkRoom2(ImageView player, Rectangle room2) {
    if (player.getBoundsInParent().intersects(room2.getBoundsInParent())) {
      room2.setVisible(true);
      timer.stop();
      enterRoom();

      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(
          event -> {
            // Adjust the player's position to be right in front of the room
            player.setLayoutX(500);
            player.setLayoutY(284);

            GameState.isPlayerInRoom2 = true;
            GameState.isPlayerInMap = false;
            GameState.beenToRoom2 = true;
            App.setScene(AppUi.TILEROOM);
            simulateKeyPressAfterDelay();
          });
      pauseTransition.play();
    } else {
      room2.setVisible(false);
    }
  }

  /**
   * When the go back button is clicked, the player will go back to the intro page.
   *
   * @param event when the go back button is clicked
   * @throws IOException
   */
  @FXML
  public void checkRoom3(ImageView player, Rectangle room3) {
    // If the player intersects with the room, go to the room.
    if (player.getBoundsInParent().intersects(room3.getBoundsInParent())) {
      room3.setVisible(true);
      timer.stop();
      enterRoom();

      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(
          event -> {
            // Adjust the player's position to be right in front of the room
            player.setLayoutX(674);
            player.setLayoutY(292);

            GameState.isPlayerInRoom3 = true;
            GameState.isPlayerInMap = false;
            GameState.beenToRoom3 = true;
            App.setScene(AppUi.ROOM3);
            simulateKeyPressAfterDelay();
          });
      pauseTransition.play();
    } else {
      room3.setVisible(false);
    }
  }

  /**
   * When the go back button is clicked, the player will go back to the intro page.
   *
   * @param event when the go back button is clicked
   * @throws IOException
   */
  public void checkCollision2(ImageView player, List<Rectangle> walls) {
    for (Rectangle wall : walls) {
      if (player.getBoundsInParent().intersects(wall.getBoundsInParent())) {
        player.setLayoutX(previousX); // Restore the player's previous X position
        player.setLayoutY(previousY); // Restore the player's previous Y position
        // Exit the loop as soon as a collision is detected
      }
    }
    if (App.timerSeconds == 30) {
      if (!hasHappened) {
        System.out.println("30 seconds left");
        hasHappened = true;
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
   * When the go back button is clicked, the player will go back to the intro page.
   *
   * @param event when the go back button is clicked
   * @throws IOException
   */
  @FXML
  public void playerMove() {
    // code for enabling palyer to move using wasd keys
    scene.setOnKeyPressed(
        e -> {
          boolean wasMoving =
              isWKeyPressed.get()
                  || isAKeyPressed.get()
                  || isSKeyPressed.get()
                  || isDKeyPressed.get();

          if (e.getCode() == KeyCode.W) {
            isWKeyPressed.set(true);
          }

          if (e.getCode() == KeyCode.A) {
            isAKeyPressed.set(true);
          }

          if (e.getCode() == KeyCode.S) {
            isSKeyPressed.set(true);
          }

          if (e.getCode() == KeyCode.D) {
            isDKeyPressed.set(true);
          }

          boolean isMoving =
              isWKeyPressed.get()
                  || isAKeyPressed.get()
                  || isSKeyPressed.get()
                  || isDKeyPressed.get();

          // If we started moving and weren't before, start the sound.
          if (isMoving && !wasMoving) {
            walkingMediaPlayer.play();
          }
        });

    scene.setOnKeyReleased(
        e -> {
          boolean wasMoving =
              isWKeyPressed.get()
                  || isAKeyPressed.get()
                  || isSKeyPressed.get()
                  || isDKeyPressed.get();

          if (e.getCode() == KeyCode.W) {
            isWKeyPressed.set(false);
          }

          if (e.getCode() == KeyCode.A) {
            isAKeyPressed.set(false);
          }

          if (e.getCode() == KeyCode.S) {
            isSKeyPressed.set(false);
          }

          if (e.getCode() == KeyCode.D) {
            isDKeyPressed.set(false);
          }

          // Check if we're still moving
          boolean isMovinng =
              isWKeyPressed.get()
                  || isAKeyPressed.get()
                  || isSKeyPressed.get()
                  || isDKeyPressed.get();

          // If we stopped moving and were before, stop the sound.
          if (!isMovinng && wasMoving) {
            PauseTransition pause = new PauseTransition(Duration.seconds(0.3));
            pause.setOnFinished(
                event -> {
                  walkingMediaPlayer.stop();
                  try {
                    // This line will reset audio clip from start when stopped
                    walkingMediaPlayer.seek(Duration.ZERO);
                  } catch (Exception ex) {
                    System.out.println("Error resetting audio: " + ex.getMessage());
                  }
                });
            pause.play();
          }
        });
  }

  /**
   * When the player is touching the border then it will keep the player to be in the position.
   *
   * @param event when the player is touching the border
   */
  @FXML
  public void squareBorder() {
    // Border that the player cannot move outof the window.
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

  /**
   * When the go back button is clicked, the player will go back to the intro page.
   *
   * @param event when the go back button is clicked
   * @throws IOException
   */
  @FXML
  public void onRoom3(ActionEvent event) {
    App.setScene(AppUi.ROOM3);
    simulateKeyPressAfterDelay();
  }

  /**
   * When the go back button is clicked, the player will go back to the intro page.
   *
   * @param event when the go back button is clicked
   * @throws IOException
   */
  @FXML
  public void detectDifficulty() {
    // detect if there is change in gamestate difficulty in the intro page using timer
    Timer labelTimer = new Timer(true);
    labelTimer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            // if difficulty is selected, label is updated
            if (GameState.difficulty != null) {
              if (GameState.difficulty == "MEDIUM") {
                Platform.runLater(() -> updateLabels());
                if (GameState.numOfHints == 0) {
                  labelTimer.cancel();
                }
              } else {
                Platform.runLater(() -> updateLabels());
              }
            }
          }
        },
        0,
        500);
  }

  private void updateLabels() {
    // update the header labels as the hint decreases
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

  @FXML
  private void animateRobot() {
    // game master robot moves up and down
    TranslateTransition translate = new TranslateTransition();
    translate.setNode(gameMaster);
    translate.setDuration(Duration.millis(1000));
    translate.setCycleCount(TranslateTransition.INDEFINITE);
    translate.setByX(0);
    translate.setByY(20);
    translate.setAutoReverse(true);
    translate.play();
  }

  // code for player movement using wasd keys
  @FXML
  public void movementSetup() {

    scene.setOnKeyPressed(
        e -> {
          boolean wasMoving =
              isWKeyPressed.get()
                  || isAKeyPressed.get()
                  || isSKeyPressed.get()
                  || isDKeyPressed.get();

          if (e.getCode() == KeyCode.W) {
            if (walkAnimationPlaying == false) {
              player.setImage(lastPlayedWalk);
              walkAnimationPlaying = true;
            }
            isWKeyPressed.set(true);
          }

          if (e.getCode() == KeyCode.A) {
            if (player.getImage() != leftCharacterAnimation) {
              player.setImage(leftCharacterAnimation);
              walkAnimationPlaying = true;
              lastPlayedWalk = player.getImage();
            }
            isAKeyPressed.set(true);
          }

          if (e.getCode() == KeyCode.S) {
            if (walkAnimationPlaying == false) {
              player.setImage(lastPlayedWalk);
              walkAnimationPlaying = true;
            }
            isSKeyPressed.set(true);
          }

          if (e.getCode() == KeyCode.D) {
            if (player.getImage() != rightCharacterAnimation) {
              player.setImage(rightCharacterAnimation);
              walkAnimationPlaying = true;
              lastPlayedWalk = player.getImage();
            }
            isDKeyPressed.set(true);
          }

          boolean isMoving =
              isWKeyPressed.get()
                  || isAKeyPressed.get()
                  || isSKeyPressed.get()
                  || isDKeyPressed.get();

          // If we started moving and weren't before, start the sound.
          if (isMoving && !wasMoving) {
            walkingMediaPlayer.play();
          }
        });

    scene.setOnKeyReleased(
        e -> {
          boolean wasMoving =
              isWKeyPressed.get()
                  || isAKeyPressed.get()
                  || isSKeyPressed.get()
                  || isDKeyPressed.get();

          if (e.getCode() == KeyCode.W) {
            if (player.getImage() == leftCharacterAnimation
                && isSKeyPressed.get() == false
                && isAKeyPressed.get() == false) {
              player.setImage(leftCharacterIdle);
              walkAnimationPlaying = false;
            } else if (isSKeyPressed.get() == true) {
              player.setImage(lastPlayedWalk);
            } else if (isAKeyPressed.get() == false
                && isDKeyPressed.get() == false
                && isSKeyPressed.get() == false) {
              player.setImage(rightCharacterIdle);
              walkAnimationPlaying = false;
            }
            isWKeyPressed.set(false);
          }

          if (e.getCode() == KeyCode.A) {
            if (isDKeyPressed.get() == false
                && isWKeyPressed.get() == false
                && isSKeyPressed.get() == false) {
              player.setImage(leftCharacterIdle);
              walkAnimationPlaying = false;
            } else if (isDKeyPressed.get() == true) {
              player.setImage(rightCharacterAnimation);
            }

            isAKeyPressed.set(false);
          }

          if (e.getCode() == KeyCode.S) {
            if (player.getImage() == leftCharacterAnimation
                && isWKeyPressed.get() == false
                && isAKeyPressed.get() == false) {
              player.setImage(leftCharacterIdle);
              walkAnimationPlaying = false;
            } else if (isWKeyPressed.get() == true) {
              player.setImage(lastPlayedWalk);
            } else if (isAKeyPressed.get() == false
                && isDKeyPressed.get() == false
                && isWKeyPressed.get() == false) {
              player.setImage(rightCharacterIdle);
              walkAnimationPlaying = false;
            }
            isSKeyPressed.set(false);
          }

          if (e.getCode() == KeyCode.D) {
            if (isAKeyPressed.get() == false
                && isWKeyPressed.get() == false
                && isSKeyPressed.get() == false) {
              player.setImage(rightCharacterIdle);
              walkAnimationPlaying = false;
            } else if (isAKeyPressed.get() == true) {
              player.setImage(leftCharacterAnimation);
            }

            isDKeyPressed.set(false);
          }

          boolean isMovinng =
              isWKeyPressed.get()
                  || isAKeyPressed.get()
                  || isSKeyPressed.get()
                  || isDKeyPressed.get();

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

  @FXML
  private void enterRoom() {
    String soundEffect = "src/main/resources/sounds/enterReal.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setAutoPlay(true);
  }

  @FXML
  private void toggleSound(MouseEvent event) {
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
  private void handleRestartButtonClick(ActionEvent event) throws IOException {
    black2.setVisible(true);
    resetBox.setVisible(true);
    resetLabel.setVisible(true);
    resetYes.setVisible(true);
    resetCancel.setVisible(true);
  }

  /**
   * Show buttons to restart the game or cancel.
   *
   * @param event mouse is clicked
   * @throws IOException if the objects don't exist
   */
  @FXML
  private void restartClicked(ActionEvent event) throws IOException {
    black2.setVisible(true);
    resetBox.setVisible(true);
    resetLabel.setVisible(true);
    resetYes.setVisible(true);
    resetCancel.setVisible(true);
  }

  @FXML
  private void restartCanceled(ActionEvent event) throws IOException {
    black2.setVisible(false);
    resetBox.setVisible(false);
    resetLabel.setVisible(false);
    resetYes.setVisible(false);
    resetCancel.setVisible(false);
  }

  @FXML
  private void reset(ActionEvent event) throws IOException {
    try {
      GameState.resetGames();
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  private void simulateKeyPressAfterDelay() {
    Thread thread =
        new Thread(
            () -> {
              try {
                Thread.sleep(50); // Delay of 0.1 seconds
                KeyEvent keyReleaseEventS =
                    new KeyEvent(
                        KeyEvent.KEY_RELEASED, "S", "S", KeyCode.S, false, false, false, false);

                KeyEvent keyReleaseEventA =
                    new KeyEvent(
                        KeyEvent.KEY_RELEASED, "A", "A", KeyCode.A, false, false, false, false);

                KeyEvent keyReleaseEventW =
                    new KeyEvent(
                        KeyEvent.KEY_RELEASED, "W", "W", KeyCode.W, false, false, false, false);

                KeyEvent keyReleaseEventD =
                    new KeyEvent(
                        KeyEvent.KEY_RELEASED, "D", "D", KeyCode.D, false, false, false, false);

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
}
