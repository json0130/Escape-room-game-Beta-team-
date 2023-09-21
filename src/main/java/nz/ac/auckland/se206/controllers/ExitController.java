package nz.ac.auckland.se206.controllers;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class ExitController implements Initializable {

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

  @FXML private Rectangle exit1;
  @FXML private Rectangle wall;

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

  @FXML private ImageView idCaptain;
  @FXML private ImageView idChef;
  @FXML private ImageView idDoctor;
  @FXML private ImageView idEngineer;
  @FXML private Label difficultyLabel;
  @FXML private Label hintLabel;
  @FXML private Label hintLabel2;
  @FXML private ImageView pad;
  @FXML private ImageView background;
  @FXML private ImageView background2;
  @FXML private ImageView background3;

  @FXML private TextArea screen;
  @FXML private Rectangle idScanner;
  @FXML private Rectangle ids;
  @FXML private Rectangle light;
  @FXML private Rectangle monitor;
  @FXML private Label idLabel;

  private String password = "";

  private double mouseAnchorX;
  private double mouseAnchorY;

  AnimationTimer collisionTimers =
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          checkCollision2(player, walls);
          checkExit(player, exit1);
        }
      };

  AnimationTimer timer =
      new AnimationTimer() {
        @Override
        public void handle(long now){
            
                previousX = player.getLayoutX(); // Update previousX
                previousY = player.getLayoutY(); // Update previousY

                if(wPressed.get()){
                    player.setLayoutY(player.getLayoutY() - movementVariable);
                }
                if(aPressed.get()){
                    player.setLayoutX(player.getLayoutX() - movementVariable);
                }
                if(sPressed.get()){
                    player.setLayoutY(player.getLayoutY() + movementVariable);
                }
                if(dPressed.get()){
                    player.setLayoutX(player.getLayoutX() + movementVariable);
                }
                squareBorder();
        }
      };

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    background.setOpacity(1);
    walls.add(wall);

    shapesize = player.getFitWidth();
    movementSetup();

    collisionTimers.start();
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

    screen.setEditable(false);
    makeInvisible();
    makeDraggable(idCaptain);
    makeDraggable(idChef);
    makeDraggable(idDoctor);
    makeDraggable(idEngineer);
    collisionTimer.start();
    // if difficulty is selected, label is updated
    detectDifficulty();
  }

  public void checkExit(ImageView player, Rectangle exit1) {
    if (player.getBoundsInParent().intersects(exit1.getBoundsInParent())) {
        exit1.setOpacity(1);
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
        pauseTransition.setOnFinished(event -> {
            // Adjust the player's position to be right in front of the room
            player.setLayoutX(68);
            player.setLayoutY(508);
            App.setScene(AppUi.PLAYER);
            timer.stop();
        });
        pauseTransition.play();
    } else {
        exit1.setOpacity(0.6);
    }
}

public void checkCollision2(ImageView player, List<Rectangle> walls){
  for(Rectangle wall : walls){
      if (player.getBoundsInParent().intersects(wall.getBoundsInParent())) {
          player.setLayoutX(previousX); // Restore the player's previous X position
          player.setLayoutY(previousY); // Restore the player's previous Y position
           // Exit the loop as soon as a collision is detected
      }
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

  private void makeInvisible(){
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
  }

  // when the rectangle is clicked, the keypad is shown
  @FXML
  private void monitorClicked(MouseEvent event) {
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
  }

  @FXML
  private void clickExit(ActionEvent event) {
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
  }

  @FXML
  private void onOne(ActionEvent event) {
    password += "1";
    screen.setText(password);
  }

  @FXML
  private void onTwo(ActionEvent event) {

    password += "2";

    screen.setText(password);
  }

  @FXML
  private void onThree(ActionEvent event) {

    password += "3";

    screen.setText(password);
  }

  @FXML
  private void onFour(ActionEvent event) {

    password += "4";

    screen.setText(password);
  }

  @FXML
  private void onFive(ActionEvent event) {

    password += "5";

    screen.setText(password);
  }

  @FXML
  private void onSix(ActionEvent event) {

    password += "6";

    screen.setText(password);
  }

  @FXML
  private void onSeven(ActionEvent event) {
    password += "7";
    screen.setText(password);
  }

  @FXML
  private void onEight(ActionEvent event) {
    password += "8";
    screen.setText(password);
  }

  @FXML
  private void onNine(ActionEvent event) {
    password += "9";
    screen.setText(password);
  }

  @FXML
  private void onZero(ActionEvent event) {
    password += "0";
    screen.setText(password);
  }

  @FXML
  private void onEnter(ActionEvent events) {
    if (password == "") {
      return;
    }
    // incorrect password, show incorrect and reset the password
    if (!password.equals(GameState.password)) {
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
      screen.setText("CORRECT");
      GameState.correctPassword = true;

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

            changeOpacity();
          });
      pauseTransition.play();
    }
  }

  // reset the typed password
  @FXML
  private void onReset() {
    password = "";
    screen.setText("");
  }

  // when the scanner is clicked, ids are shown depending on its state
  @FXML
  private void clickIdScanner(MouseEvent event) {
    // if the ids are not shown and the correct id was not tagged yet
    if (ids.isVisible() == false && !GameState.correctId) {
      ids.setVisible(true);
      idCaptain.setVisible(true);
      idChef.setVisible(true);
      idDoctor.setVisible(true);
      idEngineer.setVisible(true);

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
    } else {
      ids.setVisible(false);
      idCaptain.setVisible(false);
      idChef.setVisible(false);
      idDoctor.setVisible(false);
      idEngineer.setVisible(false);
    }
  }

  // idcards can be dragged
  public void makeDraggable(ImageView imageView) {
    double originalX = imageView.getLayoutX();
    double originalY = imageView.getLayoutY();
  

    imageView.setOnMousePressed(
        mouseEvent -> {
          mouseAnchorX = mouseEvent.getX();
          mouseAnchorY = mouseEvent.getY();
        });

    imageView.setOnMouseDragged(
        mouseEvent -> {
          // Calculate the new position based on the mouse movement
          double newX = mouseEvent.getSceneX() - mouseAnchorX;
          double newY = mouseEvent.getSceneY() - mouseAnchorY;

          // Update the layout of the image
          imageView.setLayoutX(newX);
          imageView.setLayoutY(newY);
        });

    imageView.setOnMouseReleased(
        mouseEvent -> {
          // Return the image to its original position
          imageView.setLayoutX(originalX);
          imageView.setLayoutY(originalY);
        });

  }

  AnimationTimer collisionTimer =
      new AnimationTimer() {
        @Override
        public void handle(long timeStamp) {
          checkCollision(idCaptain, idScanner);
          checkCollision(idChef, idScanner);
          checkCollision(idDoctor, idScanner);
          checkCollision(idEngineer, idScanner);
        }

        private void checkCollision(Node node1, Node node2) {
          if (node1.getBoundsInParent().intersects(node2.getBoundsInParent())) {
            if (Room1Controller.riddleAnswer == "captain") {
              if (node1 == idCaptain) {
                light.setFill(Color.GREEN);
                GameState.correctId = true;
                ids.setVisible(false);
                idCaptain.setVisible(false);
                idChef.setVisible(false);
                idDoctor.setVisible(false);
                idEngineer.setVisible(false);
                changeOpacity2();
              } else {
                light.setFill(Color.RED);
              }
            } else if (Room1Controller.riddleAnswer == "chef") {
              if (node1 == idChef) {
                light.setFill(Color.GREEN);
                GameState.correctId = true;
                ids.setVisible(false);
                idCaptain.setVisible(false);
                idChef.setVisible(false);
                idDoctor.setVisible(false);
                idEngineer.setVisible(false);
                changeOpacity2();
              } else {
                light.setFill(Color.RED);
              }
            } else if (Room1Controller.riddleAnswer == "doctor") {
              if (node1 == idDoctor) {
                light.setFill(Color.GREEN);
                GameState.correctId = true;
                ids.setVisible(false);
                idCaptain.setVisible(false);
                idChef.setVisible(false);
                idDoctor.setVisible(false);
                idEngineer.setVisible(false);
                changeOpacity2();
              } else {
                light.setFill(Color.RED);
              }
            } else if (Room1Controller.riddleAnswer == "engineer") {
              if (node1 == idEngineer) {
                light.setFill(Color.GREEN);
                GameState.correctId = true;
                ids.setVisible(false);
                idCaptain.setVisible(false);
                idChef.setVisible(false);
                idDoctor.setVisible(false);
                idEngineer.setVisible(false);
                changeOpacity2();
              } else {
                light.setFill(Color.RED);
              }
            }
          }
        }
      };

  public void detectDifficulty() {
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

  private void changeOpacity2() {
    if (GameState.correctId) {
        // Create a FadeTransition for both background images
        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(2), background2);
        fadeTransition1.setToValue(0.0); // Set the target opacity value (1.0 for fully opaque)
        fadeTransition1.play(); // Start the animation for background1
        idScanner.setVisible(false);
        light.setVisible(false);
        idLabel.setVisible(false);

        FadeTransition fadeTransition2 = new FadeTransition(Duration.seconds(2), background3);
        fadeTransition2.setToValue(1.0); // Set the target opacity value (1.0 for fully opaque)
        fadeTransition2.play(); // Start the animation for background2

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2.0));
            pauseTransition.setOnFinished(event -> {
                // Adjust the player's position to be right in front of the room
                endingAnimation();
            });
            pauseTransition.play();
    }
  }

  private void endingAnimation(){
        // Create a timeline to continuously increase the scaling factor
             Timeline continuousScaling = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(background3.scaleXProperty(), 1.0)),
                new KeyFrame(Duration.ZERO, new KeyValue(background3.scaleYProperty(), 1.0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(background3.scaleXProperty(), 2)),
                new KeyFrame(Duration.seconds(3), new KeyValue(background3.scaleYProperty(), 2))
        );
        continuousScaling.setCycleCount(1); // Play the animation once

        // Create a translate animation for the r1
        TranslateTransition Translation = new TranslateTransition(Duration.seconds(2.0), background3);

        // Set the animation properties
        
        Translation.setCycleCount(1); // Play the animation once
        Translation.setAutoReverse(false); // Don't reverse the animation

        // Start the animations
        continuousScaling.play();
        Translation.play();
        // Enable the button when the animation is finished
            Translation.setOnFinished(event -> {
                App.setScene(AppUi.END);
            });
    }



  @FXML
  private void back(ActionEvent event) {
    App.setScene(AppUi.PLAYER);
  }
}
