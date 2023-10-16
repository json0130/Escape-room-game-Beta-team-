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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatBubble;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class ExitController implements Initializable {
  public static ObservableList<ChatBubble> chatBubbleListExit = FXCollections.observableArrayList();

  private BooleanProperty isWPressed = new SimpleBooleanProperty();
  private BooleanProperty isAPressed = new SimpleBooleanProperty();
  private BooleanProperty isSPressed = new SimpleBooleanProperty();
  private BooleanProperty isDPressed = new SimpleBooleanProperty();

  private BooleanBinding keyPressed = isWPressed.or(isAPressed).or(isSPressed).or(isDPressed);
  private int movementVariable = 5;
  private double shapesize;

  private List<Rectangle> walls = new ArrayList<>();

  @FXML private ImageView player;
  @FXML private Pane scene;
  @FXML private Pane alert;

  private double previousX;
  private double previousY;

  @FXML private Rectangle exit1;
  @FXML private Rectangle wall;
  @FXML private Rectangle wall1;
  @FXML private Rectangle id;

  @FXML private Button one;
  @FXML private Button two;
  @FXML private Button three;
  @FXML private Button four;
  @FXML private Button five;
  @FXML private Button six;
  @FXML private Button seven;
  @FXML private Button eight;
  @FXML private Button nine;
  @FXML private Button zero;
  @FXML private Button enter;
  @FXML private Button reset;
  @FXML private Button button;
  @FXML private Button exit;
  @FXML private Button exit2;

  @FXML private ImageView idCaptain;
  @FXML private ImageView idChef;
  @FXML private ImageView idDoctor;
  @FXML private ImageView idEngineer;
  @FXML private Label difficultyLabel;
  @FXML private Label hintLabel;
  @FXML private Label hintLabel2;
  @FXML private Label idCardList;
  @FXML private ImageView pad;
  @FXML private ImageView background;
  @FXML private ImageView background2;
  @FXML private ImageView background3;
  @FXML private ImageView soundOn;
  @FXML private ImageView soundOff;

  @FXML private TextArea screen;
  @FXML private Rectangle idScanner;
  @FXML private Rectangle ids;
  @FXML private Rectangle light;
  @FXML private Rectangle monitor;
  @FXML private Rectangle clickMonitor;
  @FXML private Label idLabel;
  @FXML private Label clickButton;
  @FXML private Label click;
  @FXML private ImageView gameMaster;
  @FXML private ImageView close;

  @FXML private Rectangle black2;
  @FXML private Rectangle resetBox;
  @FXML private Label resetLabel;
  @FXML private Button resetYes;
  @FXML private Button resetCancel;

  @FXML private Pane aiWindowController;
  @FXML private ScrollPane chatPaneOne;
  @FXML private VBox chatContainerOne;

  private boolean nextToButton = false;
  private FadeTransition fadeTransition;

  @FXML private Button toggleSoundButton;
  private MediaPlayer walkingMediaPlayer;

  private boolean hasHappend = false;
  private boolean keyboardControlEnabled = true;
  private boolean idTouching = false;

  // Add this variable to your class
  private Timeline alertBlinkTimeline;

  private String password = "";

  private double mouseAnchorX;
  private double mouseAnchorY;

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

  private AnimationTimer collisionTimers =
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          checkCollision2(player, walls);
          checkExit(player, exit1);
          checkComputer(player, clickMonitor);
          checkId(player, id);
        }
      };

  private AnimationTimer timer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {

          previousX = player.getLayoutX(); // Update previousX
          previousY = player.getLayoutY(); // Update previousY

          if (isWPressed.get()) {
            player.setLayoutY(player.getLayoutY() - movementVariable);
          }
          if (isAPressed.get()) {
            player.setLayoutX(player.getLayoutX() - movementVariable);
          }
          if (isSPressed.get()) {
            player.setLayoutY(player.getLayoutY() + movementVariable);
          }
          if (isDPressed.get()) {
            player.setLayoutX(player.getLayoutX() + movementVariable);
          }
          squareBorder();
        }
      };

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    background.setOpacity(1);
    clickButton.setVisible(false);
    alert.setVisible(false); // Initially hide the alert label
    aiWindowController.setVisible(true);

    // if difficulty is selected, label is updated
    detectDifficulty();

    walls.add(wall);
    walls.add(wall1);

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

    shapesize = player.getFitWidth();
    setUpMovement();

    collisionTimers.start();
    previousX = player.getLayoutX();
    previousY = player.getLayoutY();

    keyPressed.addListener(
        ((observableValue, aBoolean, time) -> {
          if (!aBoolean) {
            timer.start();
          } else {
            timer.stop();
          }
        }));

    fadeTransition = new FadeTransition(Duration.seconds(1), monitor);
    fadeTransition.setFromValue(1.0);
    fadeTransition.setToValue(0.0);
    fadeTransition.setCycleCount(FadeTransition.INDEFINITE); // Blink indefinitely
    fadeTransition.setAutoReverse(true); // Reverse the animation
    // Start the animation
    fadeTransition.play();

    screen.setEditable(false);
    makeInvisible();
    makeDraggable(idCaptain);
    makeDraggable(idChef);
    makeDraggable(idDoctor);
    makeDraggable(idEngineer);
    collisionTimer.start();
    // if difficulty is selected, label is updated
    detectDifficulty();

    ListChangeListener<ChatBubble> listener3 =
        change -> {
          Platform.runLater(
              () -> {
                chatContainerOne
                    .getChildren()
                    .addAll(chatBubbleListExit.get(chatBubbleListExit.size() - 1).getBubbleBox());
                chatContainerOne.setAlignment(Pos.TOP_RIGHT);
                chatPaneOne.vvalueProperty().bind(chatContainerOne.heightProperty());
                System.out.println(
                    "Added: "
                        + chatBubbleListExit
                            .get(chatBubbleListExit.size() - 1)
                            .getBubbleText()
                            .getText()
                        + " "
                        + this.getClass().getSimpleName());
              });
        };
    chatBubbleListExit.addListener(listener3);
  }

  /**
   * If the charcter collides rectangle for exit, scene changes back to map.
   *
   * @param player the character image
   * @param exit1 the exit which is connected to the map
   */
  public void checkExit(ImageView player, Rectangle exit1) {
    if (player.getBoundsInParent().intersects(exit1.getBoundsInParent())) {
      exit1.setOpacity(1);
      timer.stop();

      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(
          event -> {
            // Adjust the player's position to be right in front of the room
            player.setLayoutX(68);
            player.setLayoutY(508);
            GameState.isPlayerInMap = true;
            GameState.isPlayerInRoom3 = false;
            // GameState.hasHappend = false;
            App.setScene(AppUi.PLAYER);
            enterRoom();
            simulateKeyPressAfterDelay();
          });
      pauseTransition.play();
    } else {
      exit1.setOpacity(0.6);
    }
  }

  /**
   * Prevent the player move across the walls.
   *
   * @param player the character
   * @param walls the border that the player cannot move axross
   */
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

  /** Enable the player can move using wasd keys */
  @FXML
  public void setUpMovement() {

    // while the keys are pressed, the player moves
    scene.setOnKeyPressed(
        e -> {
          boolean wasMoving =
              isWPressed.get() || isAPressed.get() || isSPressed.get() || isDPressed.get();

          if (keyboardControlEnabled) {
            // player moves up
            if (e.getCode() == KeyCode.W) {
              if (walkAnimationPlaying == false) {
                player.setImage(lastPlayedWalk);
                walkAnimationPlaying = true;
              }
              isWPressed.set(true);
            }

            // player moves left
            if (e.getCode() == KeyCode.A) {
              if (player.getImage() != leftCharacterAnimation) {
                player.setImage(leftCharacterAnimation);
                walkAnimationPlaying = true;
                lastPlayedWalk = player.getImage();
              }
              isAPressed.set(true);
            }

            // player moves down
            if (e.getCode() == KeyCode.S) {
              if (walkAnimationPlaying == false) {
                player.setImage(lastPlayedWalk);
                walkAnimationPlaying = true;
              }
              isSPressed.set(true);
            }

            // player moves right
            if (e.getCode() == KeyCode.D) {
              if (player.getImage() != rightCharacterAnimation) {
                player.setImage(rightCharacterAnimation);
                walkAnimationPlaying = true;
                lastPlayedWalk = player.getImage();
              }
              isDPressed.set(true);
            }
            boolean isMoving =
                isWPressed.get() || isAPressed.get() || isSPressed.get() || isDPressed.get();

            // If we started moving and weren't before, start the sound.
            if (isMoving && !wasMoving) {
              walkingMediaPlayer.play();
            }
          }
        });

    // if the keys are released, players stay in the last position
    scene.setOnKeyReleased(
        e -> {
          boolean wasMoving =
              isWPressed.get() || isAPressed.get() || isSPressed.get() || isDPressed.get();

          if (keyboardControlEnabled) {
            // If the player was moving up, image changes
            if (e.getCode() == KeyCode.W) {
              if (player.getImage() == leftCharacterAnimation
                  && isSPressed.get() == false
                  && isAPressed.get() == false) {
                player.setImage(leftCharacterIdle);
                walkAnimationPlaying = false;
              } else if (isSPressed.get() == true) {
                player.setImage(lastPlayedWalk);
              } else if (isAPressed.get() == false
                  && isDPressed.get() == false
                  && isSPressed.get() == false) {
                player.setImage(rightCharacterIdle);
                walkAnimationPlaying = false;
              }
              isWPressed.set(false);
            }

            // If the player was moving left, image changes
            if (e.getCode() == KeyCode.A) {
              if (isDPressed.get() == false
                  && isWPressed.get() == false
                  && isSPressed.get() == false) {
                player.setImage(leftCharacterIdle);
                walkAnimationPlaying = false;
              } else if (isDPressed.get() == true) {
                player.setImage(rightCharacterAnimation);
              }

              isAPressed.set(false);
            }

            // If the player was moving down, image changes
            if (e.getCode() == KeyCode.S) {
              if (player.getImage() == leftCharacterAnimation
                  && isWPressed.get() == false
                  && isAPressed.get() == false) {
                player.setImage(leftCharacterIdle);
                walkAnimationPlaying = false;
              } else if (isWPressed.get() == true) {
                player.setImage(lastPlayedWalk);
              } else if (isAPressed.get() == false
                  && isDPressed.get() == false
                  && isWPressed.get() == false) {
                player.setImage(rightCharacterIdle);
                walkAnimationPlaying = false;
              }
              isSPressed.set(false);
            }

            // If the player was moving right, image changes
            if (e.getCode() == KeyCode.D) {
              if (isAPressed.get() == false
                  && isWPressed.get() == false
                  && isSPressed.get() == false) {
                player.setImage(rightCharacterIdle);
                walkAnimationPlaying = false;
              } else if (isAPressed.get() == true) {
                player.setImage(leftCharacterAnimation);
              }

              isDPressed.set(false);
            }

            boolean isMovinng =
                isWPressed.get() || isAPressed.get() || isSPressed.get() || isDPressed.get();

            // If we stopped moving and were before, stop the sound.
            if (!isMovinng && wasMoving) {
              PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
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
          }
        });
  }

  /** Prevent the player moves out of the window. */
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

  /** Modify your setupAlertBlinking method as follows. */
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

  /** Add a method to stop the alert blinking. */
  private void stopAlertBlinking() {
    if (alertBlinkTimeline != null) {
      alertBlinkTimeline.stop();
      alert.setVisible(false);
    }
  }

  /** Check if the id card is touching the id pad or not. */
  private void checkId(ImageView player, Rectangle id) {
    if (player.getBoundsInParent().intersects(id.getBoundsInParent())) {
      idTouching = true;
    } else {
      idTouching = false;
    }
  }

  /** Idcards can be dragged. */
  private void makeDraggable(ImageView imageView) {
    double originalX = imageView.getLayoutX();
    double originalY = imageView.getLayoutY();

    // When the mouse is pressed, the position of id card is stored.
    imageView.setOnMousePressed(
        mouseEvent -> {
          mouseAnchorX = mouseEvent.getX();
          mouseAnchorY = mouseEvent.getY();
        });

    // While the mouse is dragged, the position of id card keep changing
    imageView.setOnMouseDragged(
        mouseEvent -> {
          // Calculate the new position based on the mouse movement
          double newX = mouseEvent.getSceneX() - mouseAnchorX;
          double newY = mouseEvent.getSceneY() - mouseAnchorY;

          // Update the layout of the image
          imageView.setLayoutX(newX);
          imageView.setLayoutY(newY);
        });

    // When the mouse is released, the final position of id card is stored.
    imageView.setOnMouseReleased(
        mouseEvent -> {
          // Return the image to its original position
          imageView.setLayoutX(originalX);
          imageView.setLayoutY(originalY);
        });
  }

  /** Detect change in gamestate difficulty which is seleced in intro scene. */
  private void detectDifficulty() {
    Timer labelTimer = new Timer(true);
    labelTimer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            if (GameState.difficulty != null) {
              // if the difficulty is medium, keep detecting the number of hints until it reaches to
              // 0.
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

  /** If the player moves closer to the computer, button for keypad appears. */
  private void checkComputer(ImageView player, Rectangle wall2) {
    if (player.getBoundsInParent().intersects(wall2.getBoundsInParent())) {
      monitor.setOpacity(1);
      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(
          event -> {
            // Adjust the player's position to be right in front of the room)
            if (!GameState.correctPassword) {
              nextToButton = true;
              monitor.setFill(javafx.scene.paint.Color.WHITE);
              clickButton.setVisible(true);
            } else {
              nextToButton = false;
              clickButton.setVisible(false);
              monitor.setFill(javafx.scene.paint.Color.TRANSPARENT);
            }
          });
      pauseTransition.play();
    } else {
      nextToButton = false;
      clickButton.setVisible(false);
      monitor.setFill(javafx.scene.paint.Color.TRANSPARENT);
    }
  }

  /** Make all the nodes invisible at once. */
  private void makeInvisible() {
    idScanner.setVisible(false);
    light.setVisible(false);
    idLabel.setVisible(false);
    ids.setVisible(false);
    idCaptain.setVisible(false);
    idChef.setVisible(false);
    idDoctor.setVisible(false);
    idEngineer.setVisible(false);
    screen.setVisible(false);
    one.setVisible(false);
    two.setVisible(false);
    three.setVisible(false);
    four.setVisible(false);
    five.setVisible(false);
    six.setVisible(false);
    seven.setVisible(false);
    eight.setVisible(false);
    nine.setVisible(false);
    zero.setVisible(false);
    enter.setVisible(false);
    reset.setVisible(false);
    pad.setVisible(false);
    exit.setVisible(false);
    clickButton.setVisible(false);
    exit2.setVisible(false);
    idCardList.setVisible(false);
    id.setVisible(false);
    click.setVisible(false);
  }

  /**
   * When the rectangle is clicked, the keypad is shown.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void monitorClicked(MouseEvent event) {
    if (nextToButton) {
      keyboardControlEnabled = false; // Disable keyboard control
      screen.setVisible(true);
      one.setVisible(true);
      two.setVisible(true);
      three.setVisible(true);
      four.setVisible(true);
      five.setVisible(true);
      six.setVisible(true);
      seven.setVisible(true);
      eight.setVisible(true);
      nine.setVisible(true);
      zero.setVisible(true);
      enter.setVisible(true);
      reset.setVisible(true);
      pad.setVisible(true);
      monitor.setVisible(false);
      exit.setVisible(true);
      player.setVisible(false);
      clickMonitor.setVisible(false);
      scene.requestFocus(); // Add this line to request focus on the scene
      GameState.foundMonitor = true;
    }
  }

  /**
   * If exit is clicked, unrelated node becomes invisible.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void onClickExit(ActionEvent event) {
    keyboardControlEnabled = true; // Enable keyboard control
    screen.setVisible(false);
    one.setVisible(false);
    two.setVisible(false);
    three.setVisible(false);
    four.setVisible(false);
    five.setVisible(false);
    six.setVisible(false);
    seven.setVisible(false);
    eight.setVisible(false);
    nine.setVisible(false);
    zero.setVisible(false);
    enter.setVisible(false);
    reset.setVisible(false);
    pad.setVisible(false);
    exit.setVisible(false);
    monitor.setVisible(true);
    player.setVisible(true);
    clickMonitor.setVisible(true);
    scene.requestFocus(); // Add this line
  }

  /**
   * If the buton is clicked, everything related to the id pad is invisible.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void onClickExit2(ActionEvent event) {
    keyboardControlEnabled = true; // Enable keyboard control
    player.setVisible(true);
    exit2.setVisible(false);
    idCaptain.setVisible(false);
    idChef.setVisible(false);
    idDoctor.setVisible(false);
    idEngineer.setVisible(false);
    ids.setVisible(false);
    idCardList.setVisible(false);
    scene.requestFocus(); // Add this line
  }

  /**
   * When the button one is clicked, password is shown on the screen.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void onClickOne(ActionEvent event) {
    clickSoundButtton();
    password += "1";
    screen.setText(password);
  }

  /**
   * When the button two is clicked, password is shown on the screen.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void onClickTwo(ActionEvent event) {
    clickSoundButtton();
    password += "2";
    screen.setText(password);
  }

  /**
   * When the button three is clicked, password is shown on the screen.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void onClickThree(ActionEvent event) {
    clickSoundButtton();
    password += "3";
    screen.setText(password);
  }

  /**
   * When the button four is clicked, password is shown on the screen.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void onClickFour(ActionEvent event) {
    clickSoundButtton();
    password += "4";
    screen.setText(password);
  }

  /**
   * When the button five is clicked, password is shown on the screen.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void onClickFive(ActionEvent event) {
    clickSoundButtton();
    password += "5";
    screen.setText(password);
  }

  /**
   * When the button six is clicked, password is shown on the screen.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void onClickSix(ActionEvent event) {
    clickSoundButtton();
    password += "6";
    screen.setText(password);
  }

  /**
   * When the button seven is clicked, password is shown on the screen.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void onClickSeven(ActionEvent event) {
    clickSoundButtton();
    password += "7";
    screen.setText(password);
  }

  /**
   * When the button eight is clicked, password is shown on the screen.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void onClickEight(ActionEvent event) {
    clickSoundButtton();
    password += "8";
    screen.setText(password);
  }

  /**
   * When the button nine is clicked, password is shown on the screen.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void onClickNine(ActionEvent event) {
    clickSoundButtton();
    password += "9";
    screen.setText(password);
  }

  /**
   * When the button zero is clicked, password is shown on the screen.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void onClickZero(ActionEvent event) {
    clickSoundButtton();
    password += "0";
    screen.setText(password);
  }

  /**
   * If the enter button is pressed, check the passcode is correct or not.
   *
   * @param events mouse is clicked
   */
  @FXML
  private void onClickEnter(ActionEvent events) {
    if (password == "") {
      return;
    }
    // incorrect password, show incorrect and reset the password
    if (!password.equals(GameState.password)) {
      soundIncorrect();
      screen.setText("INCORRECT");
      Thread clearThread =
          new Thread(
              () -> {
                try {
                  Thread.sleep(1000); // Sleep for 1 second
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                Platform.runLater(() -> screen.clear());
              });
      clearThread.start();
      password = "";
      // correct password, buttons are disabled to prevent further change in correctPassword state
    } else {
      soundCorrect();
      screen.setText("CORRECT");
      keyboardControlEnabled = true; // Enable keyboard control
      GameState.isPasscodeCorrect = true;
      GameState.correctPassword = true;
      scene.requestFocus(); // Add this line

      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
      pauseTransition.setOnFinished(
          event -> {
            screen.setVisible(false);
            one.setVisible(false);
            two.setVisible(false);
            three.setVisible(false);
            four.setVisible(false);
            five.setVisible(false);
            six.setVisible(false);
            seven.setVisible(false);
            eight.setVisible(false);
            nine.setVisible(false);
            zero.setVisible(false);
            enter.setVisible(false);
            reset.setVisible(false);
            pad.setVisible(false);
            exit.setVisible(false);
            clickButton.setVisible(false);
            monitor.setVisible(false);
            clickMonitor.setVisible(false);
            player.setVisible(true);
            id.setVisible(true);
            changeOpacity();
          });
      pauseTransition.play();
    }
  }

  /** Reset the passcode on the screen. */
  @FXML
  private void onClickReset() {
    password = "";
    screen.setText("");
  }

  /**
   * When the scanner is clicked, ids are shown depending on its state.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void clickIdScanner(MouseEvent event) {
    if (idTouching) {
      clickSoundButtton();
      System.out.println("clicked");
      keyboardControlEnabled = false; // Disable keyboard control
      player.setVisible(false);

      // if the ids are not shown and the correct id was not tagged yet
      if (ids.isVisible() == false && !GameState.isIdChecked) {
        ids.setVisible(true);
        exit2.setVisible(true);
        idCardList.setVisible(true);
        click.setVisible(true);

        if (GameState.isCaptainCollected) {
          idCaptain.setVisible(true);
        }
        if (GameState.isChefCollected) {
          idChef.setVisible(true);
        }
        if (GameState.isDoctorCollected) {
          idDoctor.setVisible(true);
        }
        if (GameState.isEngineerCollected) {
          idEngineer.setVisible(true);
        }
      }
    }
  }

  /** Check if correct id cards are tagged on the pad. */
  private AnimationTimer collisionTimer =
      new AnimationTimer() {
        @Override
        public void handle(long timeStamp) {
          checkCollision(idCaptain, idScanner);
          checkCollision(idChef, idScanner);
          checkCollision(idDoctor, idScanner);
          checkCollision(idEngineer, idScanner);
        }

        /**
         * Detect collision between two objects.
         *
         * @param node1 id cards
         * @param node2 id scanner
         */
        private void checkCollision(Node node1, Node node2) {
          if (node1.getBoundsInParent().intersects(node2.getBoundsInParent())) {
            // if the anwer is captain, idcaptain give green light, others give red
            if (Room1Controller.riddleAnswer == "captain") {
              if (node1 == idCaptain) {
                light.setFill(Color.GREEN);
                GameState.isGameFinished = true;
                soundCorrectCard();
                GameState.isIdChecked = true;
                ids.setVisible(false);
                idCaptain.setVisible(false);
                idChef.setVisible(false);
                idDoctor.setVisible(false);
                idEngineer.setVisible(false);
                exit2.setVisible(false);
                idCardList.setVisible(false);
                endingMediaChange();
                changeOpacity2();
              } else {
                // The answer is wrong, so give red light
                soundIncorrect();
                light.setFill(Color.RED);
              }
              // only chef id gives green light, others give red
            } else if (Room1Controller.riddleAnswer == "chef") {
              if (node1 == idChef) {
                light.setFill(Color.GREEN);
                GameState.isGameFinished = true;
                soundCorrectCard();
                GameState.isIdChecked = true;
                ids.setVisible(false);
                idCaptain.setVisible(false);
                idChef.setVisible(false);
                idDoctor.setVisible(false);
                idEngineer.setVisible(false);
                exit2.setVisible(false);
                idCardList.setVisible(false);
                endingMediaChange();
                changeOpacity2();
              } else {
                // The answer is wrong, so give red light
                soundIncorrect();
                light.setFill(Color.RED);
              }
              // only doctor gives green light, others give red
            } else if (Room1Controller.riddleAnswer == "doctor") {
              if (node1 == idDoctor) {
                light.setFill(Color.GREEN);
                GameState.isGameFinished = true;
                soundCorrectCard();
                GameState.isIdChecked = true;
                ids.setVisible(false);
                idCaptain.setVisible(false);
                idChef.setVisible(false);
                idDoctor.setVisible(false);
                idEngineer.setVisible(false);
                exit2.setVisible(false);
                idCardList.setVisible(false);
                endingMediaChange();
                changeOpacity2();
              } else {
                // The answer is wrong, so give red light
                soundIncorrect();
                light.setFill(Color.RED);
              }
              // only engineer gives green light, others give red
            } else if (Room1Controller.riddleAnswer == "engineer") {
              if (node1 == idEngineer) {
                light.setFill(Color.GREEN);
                GameState.isGameFinished = true;
                soundCorrectCard();
                GameState.isIdChecked = true;
                ids.setVisible(false);
                idCaptain.setVisible(false);
                idChef.setVisible(false);
                idDoctor.setVisible(false);
                idEngineer.setVisible(false);
                exit2.setVisible(false);
                idCardList.setVisible(false);
                endingMediaChange();
                changeOpacity2();
              } else {
                // The answer is wrong, so give red light
                soundIncorrect();
                light.setFill(Color.RED);
              }
            }
          }
        }
      };

  /** Change the background music when 3 seconds are left. */
  private void endingMediaChange() {
    // Wait for 2 second and change the media
    App.alertSoundPlayer.stop();
  }

  /** Update the labels of hint and difficulty as the game progresses. */
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

  /** Background image is changed after the correct passcode is typed. */
  private void changeOpacity() {
    // Create a FadeTransition for background
    FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(2), background);
    fadeTransition1.setFromValue(1.0); // Start with fully opaque
    fadeTransition1.setToValue(0.0); // Fade to fully transparent
    fadeTransition1.play(); // Start the animation for background

    // Create a FadeTransition for background2
    FadeTransition fadeTransition2 = new FadeTransition(Duration.seconds(2), background2);
    fadeTransition2.setFromValue(0.0); // Start with fully transparent
    fadeTransition2.setToValue(1.0); // Fade to fully opaque
    fadeTransition2.play(); // Start the animation for background2
    idScanner.setVisible(true);
    light.setVisible(true);
    idLabel.setVisible(true);
  }

  /** Background image is changed after the correct id is tagged. */
  private void changeOpacity2() {
    if (GameState.isIdChecked) {
      player.setVisible(false);
      // Create a FadeTransition for both background images
      FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(2), background2);
      fadeTransition1.setToValue(0.0); // Set the target opacity value (1.0 for fully opaque)
      fadeTransition1.play(); // Start the animation for background1

      FadeTransition fadeTransition2 = new FadeTransition(Duration.seconds(2), background3);
      fadeTransition2.setToValue(1.0); // Set the target opacity value (1.0 for fully opaque)
      fadeTransition2.play(); // Start the animation for background2

      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2.0));
      pauseTransition.setOnFinished(
          event -> {
            idScanner.setVisible(false);
            light.setVisible(false);
            idLabel.setVisible(false);
            // Adjust the player's position to be right in front of the room
            endingAnimation();
          });
      pauseTransition.play();
    }
  }

  /** Background image is changed for the ending is shown. */
  private void endingAnimation() {
    // Create a FadeTransition for both background images
    Timeline continuousScaling =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(background3.scaleXProperty(), 1.0)),
            new KeyFrame(Duration.ZERO, new KeyValue(background3.scaleYProperty(), 1.0)),
            new KeyFrame(Duration.seconds(3), new KeyValue(background3.scaleXProperty(), 2)),
            new KeyFrame(Duration.seconds(3), new KeyValue(background3.scaleYProperty(), 2)));
    continuousScaling.setCycleCount(1); // Play the animation once

    // Create a translate animation for the r1
    TranslateTransition translation = new TranslateTransition(Duration.seconds(2.0), background3);

    // Set the animation properties
    translation.setCycleCount(1); // Play the animation once
    translation.setAutoReverse(false); // Don't reverse the animation

    // Start the animations
    continuousScaling.play();
    translation.play();
    // Enable the button when the animation is finished
    translation.setOnFinished(
        event -> {
          App.setScene(AppUi.END);
        });
  }

  /**
   * Move the player to the map.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void onBack(ActionEvent event) {
    App.setScene(AppUi.PLAYER);
  }

  /** Sound effect for button clicking is played. */
  @FXML
  private void clickSoundButtton() {
    String soundEffect = "src/main/resources/sounds/button-click.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setAutoPlay(true);
  }

  /** Sound effect for the correct passcode is played. */
  @FXML
  private void soundCorrect() {
    String soundEffect = "src/main/resources/sounds/correct.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setAutoPlay(true);
  }

  /** Sound effect for the incorrect passcode is played. */
  @FXML
  private void soundIncorrect() {
    String soundEffect = "src/main/resources/sounds/incorrect.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setAutoPlay(true);
  }

  /** Sound effect for the correct id card is played. */
  @FXML
  private void soundCorrectCard() {
    String soundEffect = "src/main/resources/sounds/correct-card.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setAutoPlay(true);
  }

  /** Sound effect is played when the player enters the room. */
  @FXML
  private void enterRoom() {
    String soundEffect = "src/main/resources/sounds/enterReal.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setAutoPlay(true);
  }

  /** Game master becomes visible. */
  @FXML
  private void onGameMasterClick() {

    aiWindowController.setVisible(true);
    System.out.print("HI");
  }

  /**
   * Turn on and off the background music.
   *
   * @param event mouse is clicked.
   */
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

  /** Move game master robot up and down. */
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

  /**
   * Game master becomes visible.
   *
   * @param event mouse is clicked
   */
  @FXML
  private void clickGameMaster(MouseEvent event) {
    aiWindowController.setVisible(true);
    System.out.print("HI");
  }

  @FXML
  private void restartClicked(ActionEvent event) throws IOException {
    black2.setVisible(true);
    resetBox.setVisible(true);
    resetLabel.setVisible(true);
    resetYes.setVisible(true);
    resetCancel.setVisible(true);
  }

  /**
   * The player can keep playing the game.
   *
   * @param event mouse is clicked
   * @throws IOException if the objects don't exist
   */
  @FXML
  private void restartCanceled(ActionEvent event) throws IOException {
    black2.setVisible(false);
    resetBox.setVisible(false);
    resetLabel.setVisible(false);
    resetYes.setVisible(false);
    resetCancel.setVisible(false);
  }

  /**
   * Reload the whole game again.
   *
   * @param event mouse is clicked
   * @throws IOException if the game state does not exit
   */
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
                // scene.fireEvent(keyPressEvent);
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
