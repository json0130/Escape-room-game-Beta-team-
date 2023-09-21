package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class Room1Controller implements Initializable {
  @FXML private Button btnCollect1;
  @FXML private Button btnCollect2;
  @FXML private Button btnCollect3;
  @FXML private Button btnCollect4;
  

  @FXML private ImageView crew1;
  @FXML private ImageView crew2;
  @FXML private ImageView crew3;
  @FXML private ImageView crew4;

  @FXML private ImageView idCaptain;
  @FXML private ImageView idChef;
  @FXML private ImageView idDoctor;
  @FXML private ImageView idEngineer;

  @FXML private Label difficultyLabel;
  @FXML private Label hintLabel;
  @FXML private Label hintLabel2;

  public static String riddleAnswer;

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

    btnCollect1.setVisible(false);
    btnCollect2.setVisible(false);
    btnCollect3.setVisible(false);
    btnCollect4.setVisible(false);

    // if difficulty is selected, label is updated
    detectDifficulty();
  }

  // When crew1 is clicked and the riddle was resolved, id1 is shown only for 2 seconds
  @FXML
  public void clickCrew1(MouseEvent event) throws IOException {

    if (!GameState.isDoctorCollected) {
      idDoctor.setVisible(true);
    btnCollect1.setVisible(true);

    Thread hideImageThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(5000); // Sleep for 5 seconds
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              // id becomes invisible again
              Platform.runLater(() -> hideId1());
            });
    hideImageThread.start();
    }
  }

  private void hideId1() {
    idDoctor.setVisible(false);
    btnCollect1.setVisible(false);
  }

  // When crew2 is clicked and the riddle was resolved, id2 is shown only for 2 seconds
  @FXML
  public void clickCrew2(MouseEvent event) throws IOException {
    if (!GameState.isCaptainCollected) {
      idCaptain.setVisible(true);
    btnCollect2.setVisible(true);

    Thread hideImageThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(5000); // Sleep for 5 seconds
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              // id becomes invisible again
              Platform.runLater(() -> hideId2());
            });
    hideImageThread.start();
    }
  }

  private void hideId2() {
    idCaptain.setVisible(false);
    btnCollect2.setVisible(false);
  }

  // When crew3 is clicked and the riddle was resolved, id3 is shown only for 2 seconds
  @FXML
  public void clickCrew3(MouseEvent event) throws IOException {
    if(!GameState.isChefCollected) {
      idChef.setVisible(true);
    btnCollect3.setVisible(true);

    Thread hideImageThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(5000); // Sleep for 5 seconds
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              // id becomes invisible again
              Platform.runLater(() -> hideId3());
            });
    hideImageThread.start();
    }
  }

  private void hideId3() {
    idChef.setVisible(false);
    btnCollect3.setVisible(false);
  }

  // When crew4 is clicked and the riddle was resolved, id4 is shown only for 2 seconds
  @FXML
  public void clickCrew4(MouseEvent event) throws IOException {
    if (!GameState.isEngineerCollected) {
      idEngineer.setVisible(true);
    btnCollect4.setVisible(true);

    Thread hideImageThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(5000); // Sleep for 5 seconds
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              // id becomes invisible again
              Platform.runLater(() -> hideId4());
            });
    hideImageThread.start();
    }
  }

  private void hideId4() {
    idEngineer.setVisible(false);
    btnCollect4.setVisible(false);
  }

  public void onCollect1() {
    GameState.isDoctorCollected = true;
    idDoctor.setVisible(false);
    btnCollect1.setVisible(false);
  }
  public void onCollect2() {
    GameState.isCaptainCollected = true;
    idCaptain.setVisible(false);
    btnCollect2.setVisible(false);
  }
  public void onCollect3() {
    GameState.isChefCollected = true;
    idChef.setVisible(false);
    btnCollect3.setVisible(false);
  }
  public void onCollect4() {
    GameState.isEngineerCollected = true;
    idEngineer.setVisible(false);
    btnCollect4.setVisible(false);
  }

  @FXML
  public void onRiddle(ActionEvent evnet) throws IOException {
    App.setScene(AppUi.CHAT);
  }

  

  @FXML
  private void back(ActionEvent event) {
    App.setScene(AppUi.PLAYER);
  }

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
}
