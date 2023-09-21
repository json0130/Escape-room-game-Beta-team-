package nz.ac.auckland.se206.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class ExitController implements Initializable {
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

  @FXML private TextArea screen;
  @FXML private Rectangle idScanner;
  @FXML private Rectangle ids;
  @FXML private Rectangle light;
  @FXML private Rectangle monitor;
  @FXML private Label idLabel;


  private String password = "";

  private double mouseAnchorX;
  private double mouseAnchorY;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

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
      background.setVisible(false);

      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
            pauseTransition.setOnFinished(event -> {
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

                idScanner.setVisible(true);
                light.setVisible(true);
                idLabel.setVisible(true);
            });
            pauseTransition.play();
      
      // one.setDisable(true);
      // two.setDisable(true);
      // three.setDisable(true);
      // four.setDisable(true);
      // five.setDisable(true);
      // six.setDisable(true);
      // seven.setDisable(true);
      // eight.setDisable(true);
      // nine.setDisable(true);
      // zero.setDisable(true);
      // enter.setDisable(true);
      // reset.setDisable(true);
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
        idCaptain.setLayoutX(272);
        idCaptain.setLayoutY(118);
        idCaptain.setVisible(true);
      }
      if (GameState.isChefCollected) {
        idChef.setLayoutX(272);
        idChef.setLayoutY(210);
        idChef.setVisible(true);
      }
      if (GameState.isDoctorCollected) {
        idDoctor.setLayoutX(272);
        idDoctor.setLayoutY(303);
        idDoctor.setVisible(true);
      }
      if (GameState.isEngineerCollected) {
        idEngineer.setLayoutX(272);
        idEngineer.setLayoutY(392);
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
  public void makeDraggable(Node node) {

    node.setOnMousePressed(
        mouseEvent -> {
          mouseAnchorX = mouseEvent.getX();
          mouseAnchorY = mouseEvent.getY();
        });

    node.setOnMouseDragged(
        mouseEvent -> {
          node.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
          node.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
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
              } else {
                light.setFill(Color.RED);
              }
            }
          }
        }
      };


    public void detectDifficulty() {
        Timer labelTimer = new Timer(true);
        labelTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (GameState.difficulty != null) {
                    if (GameState.difficulty == "MEDIUM") {
                        Platform.runLater(()-> updateLabels());
                        if (GameState.numOfHints == 0) {
                            labelTimer.cancel();
                        }
                    } else {
                        Platform.runLater(()-> updateLabels());
                        labelTimer.cancel();
                    }
                }
            }
        }, 0, 500); 
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
  @FXML
  private void back(ActionEvent event) {
    App.setScene(AppUi.PLAYER);
  }
}
