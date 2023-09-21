package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class Room1Controller implements Initializable {

  private BooleanProperty wPressed = new SimpleBooleanProperty();
  private BooleanProperty aPressed = new SimpleBooleanProperty();
  private BooleanProperty sPressed = new SimpleBooleanProperty();
  private BooleanProperty dPressed = new SimpleBooleanProperty();

  private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);
  private int movementVariable = 5;
  private double shapesize;

  @FXML private ImageView player;
  @FXML private Pane scene;

  private double previousX;
  private double previousY;

  @FXML private ImageView crew1;
  @FXML private ImageView crew2;
  @FXML private ImageView crew3;
  @FXML private ImageView crew4;

  @FXML private ImageView idCaptain;
  @FXML private ImageView idChef;
  @FXML private ImageView idDoctor;
  @FXML private ImageView idEngineer;

  public static String riddleAnswer;

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

  public void initialize(URL url, ResourceBundle resource) {
    Random random = new Random();
    int randomNumber = random.nextInt(4);
    if (randomNumber == 0) {
      riddleAnswer = "captain";
    } else if (randomNumber == 1) {
      riddleAnswer = "doctor";
    } else if (randomNumber == 2 ) {
      riddleAnswer = "chef";
    } else if (randomNumber == 3) {
      riddleAnswer = "engineer";
    }
    // Set all id not visible
    idCaptain.setVisible(false);
    idDoctor.setVisible(false);
    idChef.setVisible(false);
    idEngineer.setVisible(false);

    shapesize = player.getFitWidth();
    movementSetup();

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
  }

  // When crew1 is clicked and the riddle was resolved, id1 is shown only for 2 seconds
  @FXML
  public void clickCrew1(MouseEvent event) throws IOException {

    idDoctor.setVisible(true);

    Thread hideImageThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(2000); // Sleep for 2 seconds
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              // id becomes invisible again
              Platform.runLater(() -> idDoctor.setVisible(false));
            });
    hideImageThread.start();
  }

  // When crew2 is clicked and the riddle was resolved, id2 is shown only for 2 seconds
  @FXML
  public void clickCrew2(MouseEvent event) throws IOException {
    idCaptain.setVisible(true);

    Thread hideImageThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(2000); // Sleep for 2 seconds
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              // id becomes invisible again
              Platform.runLater(() -> idCaptain.setVisible(false));
            });
    hideImageThread.start();
  }

  // When crew3 is clicked and the riddle was resolved, id3 is shown only for 2 seconds
  @FXML
  public void clickCrew3(MouseEvent event) throws IOException {

    idChef.setVisible(true);

    Thread hideImageThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(2000); // Sleep for 2 seconds
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              // id becomes invisible again
              Platform.runLater(() -> idChef.setVisible(false));
            });
    hideImageThread.start();
  }

  // When crew4 is clicked and the riddle was resolved, id4 is shown only for 2 seconds
  @FXML
  public void clickCrew4(MouseEvent event) throws IOException {

    idEngineer.setVisible(true);

    Thread hideImageThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(2000); // Sleep for 2 seconds
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              // id becomes invisible again
              Platform.runLater(() -> idEngineer.setVisible(false));
            });
    hideImageThread.start();
  }

  @FXML
  public void onRiddle(ActionEvent evnet) throws IOException {
    App.setScene(AppUi.CHAT);
  }

  @FXML
  public void enterCrew1(MouseEvent event) {

    // Enlarge image on hober by 10%
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), crew1);
    scaleTransition.setToX(1.1);
    scaleTransition.setToY(1.1);
    scaleTransition.play();
  }

  @FXML
  public void exitCrew1(MouseEvent event) {

    // Set to normal
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), crew1);
    scaleTransition.setToX(1.0);
    scaleTransition.setToY(1.0);
    scaleTransition.play();
  }

  @FXML
  public void enterCrew2(MouseEvent event) {

    // Enlarge image on hober by 10%
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), crew2);
    scaleTransition.setToX(1.1);
    scaleTransition.setToY(1.1);
    scaleTransition.play();
  }

  @FXML
  public void exitCrew2(MouseEvent event) {

    // Set to normal
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), crew2);
    scaleTransition.setToX(1.0);
    scaleTransition.setToY(1.0);
    scaleTransition.play();
  }

  @FXML
  public void enterCrew3(MouseEvent event) {

    // Enlarge image on hober by 10%
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), crew3);
    scaleTransition.setToX(1.1);
    scaleTransition.setToY(1.1);
    scaleTransition.play();
  }

  @FXML
  public void exitCrew3(MouseEvent event) {

    // Set to normal
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), crew3);
    scaleTransition.setToX(1.0);
    scaleTransition.setToY(1.0);
    scaleTransition.play();
  }

  @FXML
  public void enterCrew4(MouseEvent event) {

    // Enlarge image on hober by 10%
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), crew4);
    scaleTransition.setToX(1.1);
    scaleTransition.setToY(1.1);
    scaleTransition.play();
  }

  @FXML
  public void exitCrew4(MouseEvent event) {

    // Set to normal
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), crew4);
    scaleTransition.setToX(1.0);
    scaleTransition.setToY(1.0);
    scaleTransition.play();
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

  @FXML
  private void back(ActionEvent event) {
    App.setScene(AppUi.PLAYER);
  }
}
