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

public class PlayerController implements Initializable {
  public static boolean hintContained = false;
  public static boolean answerContained = false;

  private BooleanProperty wPressed = new SimpleBooleanProperty();
  private BooleanProperty aPressed = new SimpleBooleanProperty();
  private BooleanProperty sPressed = new SimpleBooleanProperty();
  private BooleanProperty dPressed = new SimpleBooleanProperty();

  private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);
  private int movementVariable = 5;
  private double shapesize;

  private String soundEffect = "src/main/resources/sounds/door-open.mp3";
  private Media media = new Media(new File(soundEffect).toURI().toString());
  private MediaPlayer mediaPlayer = new MediaPlayer(media);

  private List<Rectangle> walls = new ArrayList<>();

  @FXML private ImageView player;
  @FXML private Rectangle room1;
  @FXML private Rectangle room2;
  @FXML private Rectangle room3;
  @FXML private Rectangle black;
  @FXML private ImageView gameMaster;
  @FXML private ImageView soundOn;
  @FXML private ImageView soundOff;

  @FXML private Label main;
  @FXML private Label computer;
  @FXML private Label closet;
  @FXML private Label control;
  @FXML private Label difficultyLabel;
  @FXML private Label hintLabel;
  @FXML private Label hintLabel2;
  @FXML private Label greeting;

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
  @FXML private Rectangle greetingBox;
  @FXML private ImageView close;

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

  @FXML private Label playerLabel;

  private double previousX;
  private double previousY;

  private boolean isGreetingShown = true;

  @FXML private Button toggleSoundButton;

  @FXML private Label countdownLabel;

  private boolean hasHappened = false;

  private Timeline alertBlinkTimeline;
  @FXML public Pane aiWindowController;

  @FXML private MediaPlayer walkingMediaPlayer;

  @FXML private MediaPlayer alertSoundPlayer;

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
          playerLabel.setVisible(false);

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

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    animateRobot();

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
    enablePlayerMovement();

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
        ((observableValue, aBoolean, t1) -> {
          if (!aBoolean) {
            timer.start();
          } else {
            timer.stop();
          }
        }));

    // if difficulty is selected, label is updated
    detectDifficulty();

    greeting.setWrapText(true);
    greeting.setText(App.greetingInMap);
  }

  @FXML
  void start(ActionEvent event) {
    player.setLayoutX(10);
    player.setLayoutY(200);
  }

  // Modify your setupAlertBlinking method as follows
  private void setupAlertBlinking() {
    alert.setVisible(true); // Initially show the alert label
    String musicFile;
    musicFile = "src/main/resources/sounds/alert.mp3";
    App.musicType = "final";
    Media media = new Media(new File(musicFile).toURI().toString());

    // Stop current playing media
    App.mediaPlayer.stop();

    // Create a new MediaPlayer specifically for the alert sound
    alertSoundPlayer = new MediaPlayer(media);

    // Check if sound is enabled before setting volume and playing.
    if (GameState.isSoundEnabled) {
      alertSoundPlayer.setVolume(0.04);
    } else {
      alertSoundPlayer.setVolume(0.0);
    }
    alertSoundPlayer.setAutoPlay(true);
    alertSoundPlayer.setCycleCount(MediaPlayer.INDEFINITE);

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
      // Stop timeline and hide label
      alertBlinkTimeline.stop();

      // Stop and clean up the Alert Sound Player as well.
      if (alertSoundPlayer != null) {
        alertSoundPlayer.stop();
        alertSoundPlayer.dispose();
      }
    }
  }

  /**
   * When the go back button is clicked, the player will go back to the intro page.
   *
   * @param event when the go back button is clicked
   * @throws IOException
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
            GameState.isPlayerInMap = false;
            GameState.isPlayerInRoom1 = true;
            // GameState.hasHappend = false;
            App.setScene(AppUi.ROOM1);
          });
      pauseTransition.play();
    } else {
      room1.setVisible(false);
    }
  }

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
            GameState.isPlayerInMap = false;
            GameState.isPlayerInRoom2 = true;
            App.setScene(AppUi.TILEROOM);
          });
      pauseTransition.play();
    } else {
      room2.setVisible(false);
    }
  }

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

            App.setScene(AppUi.ROOM3);
          });
      pauseTransition.play();
    } else {
      room3.setVisible(false);
    }
  }

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

  // code for enabling palyer to move using wasd keys
  @FXML
  public void playerMove() {
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

          // Check if we're still moving
          boolean isMovinng = wPressed.get() || aPressed.get() || sPressed.get() || dPressed.get();

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
   * When the go back button is clicked, the player will go back to the intro page.
   *
   * @param event when the go back button is clicked
   * @throws IOException
   */
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
  }

  public void detectDifficulty() {
    // detect if there is change in gamestate difficulty in the intro page using timer
    Timer labelTimer = new Timer(true);
    labelTimer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            if (GameState.difficulty != null) {
              if (GameState.difficulty == "MEDIUM") {
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

  /** When the close image is clicked, greeting disappears. */
  @FXML
  private void clickClose(MouseEvent e) {
    greeting.setVisible(false);
    greetingBox.setVisible(false);
    close.setVisible(false);
    isGreetingShown = false;
  }

  /** After the player close the greeting, the character can move. */
  private void enablePlayerMovement() {
    Timer greetingTimer = new Timer(true);
    greetingTimer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            if (!isGreetingShown) {
              playerMove();
              greetingTimer.cancel();
            }
          }
        },
        0,
        100);
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

    double volume = GameState.isSoundEnabled ? 0.05 : 0;

    if (App.mediaPlayer != null) {
      App.mediaPlayer.setVolume(volume);
    }

    if (alertSoundPlayer != null) {
      // If an Alert Sound Player exists, adjust its volume as well.
      alertSoundPlayer.setVolume(volume);
    }

    soundOn.setVisible(GameState.isSoundEnabled);
    soundOff.setVisible(!GameState.isSoundEnabled);
  }

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
}
