package nz.ac.auckland.se206.controllers;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class ExitController {
  @FXML private ImageView door;
  @FXML private ImageView lever;
  private Boolean isDoorShown = false;
  private double startX;
  private double startY;
  private double angle;

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
    }
  }

}
