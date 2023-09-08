package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;

public class Room1Controller implements Initializable {
  @FXML private ImageView crew1;
  @FXML private ImageView crew2;
  @FXML private ImageView crew3;
  @FXML private ImageView crew4;

  private Stage idStage; // New stage for id cards

  public void initialize(URL url, ResourceBundle resource) {
    idStage = new Stage();
    idStage.setResizable(false);
    idStage.setAlwaysOnTop(true);
  }

  // When crew1 is clicked, id1 is shown
  @FXML
  public void clickCrew1(MouseEvent event) throws IOException {
    if (!idStage.isShowing()) {
      System.out.println("crew 1 clicked");
      Parent root = new FXMLLoader(App.class.getResource("/fxml/id1.fxml")).load();
      Scene scene = new Scene(root, 300, 200);
      idStage.setScene(scene);
      idStage.show();
    }
  }

  // When crew2 is clicked, id2 is shown
  @FXML
  public void clickCrew2(MouseEvent event) throws IOException {
    if (!idStage.isShowing()) {
      System.out.println("crew 2 clicked");
      Parent root = new FXMLLoader(App.class.getResource("/fxml/id2.fxml")).load();
      Scene scene = new Scene(root, 300, 200);
      idStage.setScene(scene);
      idStage.show();
    }
  }

  // When crew3 is clicked, id3 is shown
  @FXML
  public void clickCrew3(MouseEvent event) throws IOException {
    if (!idStage.isShowing()) {
      System.out.println("crew 3 clicked");
      Parent root = new FXMLLoader(App.class.getResource("/fxml/id3.fxml")).load();
      Scene scene = new Scene(root, 300, 200);
      idStage.setScene(scene);
      idStage.show();
    }
  }

  // When crew4 is clicked, id4 is shown
  @FXML
  public void clickCrew4(MouseEvent event) throws IOException {
    if (!idStage.isShowing()) {
      System.out.println("crew 4 clicked");
      Parent root = new FXMLLoader(App.class.getResource("/fxml/id4.fxml")).load();
      Scene scene = new Scene(root, 300, 200);
      idStage.setScene(scene);
      idStage.show();
    }
  }
}
