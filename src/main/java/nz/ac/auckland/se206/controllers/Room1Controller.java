package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;

public class Room1Controller implements Initializable {
  @FXML private ImageView crew1;
  @FXML private ImageView crew2;
  @FXML private ImageView crew3;
  @FXML private ImageView crew4;

  @FXML private ImageView idCaptain;
  @FXML private ImageView idChef;
  @FXML private ImageView idDoctor;
  @FXML private ImageView idEngineer;

  public void initialize(URL url, ResourceBundle resource) {
    // Set all id not visible
    idCaptain.setVisible(false);
    idDoctor.setVisible(false);
    idChef.setVisible(false);
    idEngineer.setVisible(false);
  }

  // When crew1 is clicked and the riddle was resolved, id1 is shown only for 2 seconds
  @FXML
  public void clickCrew1(MouseEvent event) throws IOException {
    if (GameState.isRiddleResolved) {
      idDoctor.setVisible(true);

            Thread hideImageThread = new Thread(() -> {
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
  }

  // When crew2 is clicked and the riddle was resolved, id2 is shown only for 2 seconds
  @FXML
  public void clickCrew2(MouseEvent event) throws IOException {
    if (GameState.isRiddleResolved) {
        if (GameState.isRiddleResolved) {
      idCaptain.setVisible(true);

            Thread hideImageThread = new Thread(() -> {
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
    }
  }

  // When crew3 is clicked and the riddle was resolved, id3 is shown only for 2 seconds
  @FXML
  public void clickCrew3(MouseEvent event) throws IOException {
    if (GameState.isRiddleResolved) {
        if (GameState.isRiddleResolved) {
      idChef.setVisible(true);

            Thread hideImageThread = new Thread(() -> {
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
    }
  }

  // When crew4 is clicked and the riddle was resolved, id4 is shown only for 2 seconds
  @FXML
  public void clickCrew4(MouseEvent event) throws IOException {
    if (GameState.isRiddleResolved) {
        if (GameState.isRiddleResolved) {
      idEngineer.setVisible(true);

            Thread hideImageThread = new Thread(() -> {
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
    }
  }

  @FXML
  public void onRiddle(ActionEvent evnet) throws IOException {
    App.setRoot("chat");
  }
}
