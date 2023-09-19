package nz.ac.auckland.se206.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameState;

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
  @FXML private ImageView door;
  @FXML private ImageView lever;
  @FXML private TextArea screen;
  private Boolean isDoorShown = false;
  private double startX;
  private double startY;
  private double angle;
  private String password = "";

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // TODO Auto-generated method stub
    screen.setEditable(false);
    one.setDisable(true);
    two.setDisable(true);
    three.setDisable(true);
    four.setDisable(true);
    five.setDisable(true);
    six.setDisable(true);
    seven.setDisable(true);
    eight.setDisable(true);
    nine.setDisable(true);
    zero.setDisable(true);
    enter.setDisable(true);
    reset.setDisable(true);
  }

  // Lever is draggable while the exit is not shown
  @FXML
  public void pressLever(MouseEvent event) {
    if (!isDoorShown) {
      startX = event.getSceneX();
      startY = event.getSceneY();
    }
  }

  @FXML
  public void dragLever(MouseEvent event) {
    if (!isDoorShown) {
      double deltaX = event.getSceneX() - startX;
      double deltaY = event.getSceneY() - startY;
      angle = Math.toDegrees(Math.atan2(deltaY, deltaX));

      if (angle < 0) {
        angle = 0;
      } else if (angle > 180) {
        angle = 180;
      }
      lever.setRotate(angle);
      startX = event.getSceneX();
      startY = event.getSceneY();
      // check if the angle is 180
      checkRotation(angle);
    }
  }

  // if the angle is 180, exit is shown
  private void checkRotation(double angle) {
    if (angle == 180) {
      TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(3), door);
      translateTransition.setByY(-650);
      translateTransition.play();
      isDoorShown = true;
    }
  }

  // if the door is not shown, enlarge lever on hover
  @FXML
  private void enterLever(MouseEvent event) {
    if (!isDoorShown) {
      ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), lever);
      scaleTransition.setToX(1.1);
      scaleTransition.setToY(1.1);
      scaleTransition.play();
    }
  }

  @FXML
  private void exitLever(MouseEvent event) {
    if (!isDoorShown) {
      ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), lever);
      scaleTransition.setToX(1.0);
      scaleTransition.setToY(1.0);
      scaleTransition.play();
      one.setDisable(false);
      two.setDisable(false);
      three.setDisable(false);
      four.setDisable(false);
      five.setDisable(false);
      six.setDisable(false);
      seven.setDisable(false);
      eight.setDisable(false);
      nine.setDisable(false);
      zero.setDisable(false);
      enter.setDisable(false);
      reset.setDisable(false);
    }
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
  private void onEnter(ActionEvent event) {
    if (password == "") {
      return;
    }
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
      password = null;
    } else {
      screen.setText("CORRECT");
      GameState.correctPassword = true;
      one.setDisable(true);
      two.setDisable(true);
      three.setDisable(true);
      four.setDisable(true);
      five.setDisable(true);
      six.setDisable(true);
      seven.setDisable(true);
      eight.setDisable(true);
      nine.setDisable(true);
      zero.setDisable(true);
      enter.setDisable(true);
      reset.setDisable(true);
    }
  }

  @FXML
  private void onReset() {
    password = "";
    screen.setText("");
  }
}
