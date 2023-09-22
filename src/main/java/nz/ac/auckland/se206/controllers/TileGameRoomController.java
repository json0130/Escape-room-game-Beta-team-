package nz.ac.auckland.se206.controllers;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller class for the room view. */
public class TileGameRoomController implements javafx.fxml.Initializable {

  private BooleanProperty wPressed = new SimpleBooleanProperty();
  private BooleanProperty aPressed = new SimpleBooleanProperty();
  private BooleanProperty sPressed = new SimpleBooleanProperty();
  private BooleanProperty dPressed = new SimpleBooleanProperty();

  private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);
  private int movementVariable = 5;
  private double shapesize;

  List<Rectangle> walls = new ArrayList<>();

  @FXML private ImageView player;
  @FXML private Pane scene;

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
  private FadeTransition fadeTransition;

  private boolean nextToButton = false;
  @FXML private Button btnRoom1;
  @FXML private Button button;
  @FXML ImageView eMark;
  @FXML private ImageView gameMaster;
  TranslateTransition translate = new TranslateTransition();

  AnimationTimer collisionTimer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          checkCollision2(player, walls);
          checkExit(player, exit);
          checkMonitor(player, blinkingRectangle);
        }
      };

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

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    animateExclamationMark();
    animateRobot();

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

    // if difficulty is selected, label is updated
    detectDifficulty();

    shapesize = player.getFitWidth();
    movementSetup();

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

    fadeTransition = new FadeTransition(Duration.seconds(1), blinkingRectangle);
    fadeTransition.setFromValue(1.0);
    fadeTransition.setToValue(0.0);
    fadeTransition.setCycleCount(FadeTransition.INDEFINITE); // Blink indefinitely
    fadeTransition.setAutoReverse(true); // Reverse the animation
    // Start the animation
    fadeTransition.play();
  }

  public void checkExit(ImageView player, Rectangle exit) {
    if (player.getBoundsInParent().intersects(exit.getBoundsInParent())) {
      exit.setOpacity(1);
      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(
          event -> {
            // Adjust the player's position to be right in front of the room
            player.setLayoutX(436);
            player.setLayoutY(488);
            App.setScene(AppUi.PLAYER);
            timer.stop();
          });
      pauseTransition.play();
    } else {
      exit.setOpacity(0.6);
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
  }

  private void checkMonitor(ImageView player, Rectangle wa) {
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

  // code for player movement using wasd keys
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
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickDoor(MouseEvent event) throws IOException {
    System.out.println("door clicked");

    if (!GameState.isRiddleResolved) {
      showDialog("Info", "Riddle", "You need to resolve the riddle!");
      App.setRoot("chat");
      return;
    }

    if (!GameState.isKeyFound) {
      showDialog(
          "Info", "Find the key!", "You resolved the riddle, now you know where the key is.");
    } else {
      showDialog("Info", "You Won!", "Good Job!");
    }
  }

  /**
   * Handles the click event on the vase.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickVase(MouseEvent event) {
    System.out.println("vase clicked");
    if (GameState.isRiddleResolved && !GameState.isKeyFound) {
      showDialog("Info", "Key Found", "You found a key under the vase!");
      GameState.isKeyFound = true;
    }
  }

  /**
   * Handles the click event on the window.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickWindow(MouseEvent event) {
    System.out.println("window clicked");
  }

  @FXML
  public void onTileGameButtonClick() throws IOException {
    translate.stop();
    eMark.setVisible(false);
    if (nextToButton) {
      App.setScene(AppUi.TILEPUZZLE);
      System.out.println("button clicked");
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
                labelTimer.cancel();
              }
            }
          }
        },
        0,
        500);
  }

  // labels for hint and difficulty updates as the game progress
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

  @FXML
  private void back(ActionEvent event) throws IOException {
    App.setScene(AppUi.PLAYER);
  }

  // exclamation mark for monitor animation
  @FXML
  private void animateExclamationMark() {
    translate.setNode(eMark);
    translate.setDuration(Duration.millis(1000));
    translate.setCycleCount(TranslateTransition.INDEFINITE);
    translate.setByX(0);
    translate.setByY(20);
    translate.setAutoReverse(true);

    translate.play();
  }

  @FXML
  private void onGameMasterClick() {
    App.previousRoom = AppUi.TILEROOM;
    App.setScene(AppUi.HELPERCHAT);
  }

  // game master robot animation
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
}
