package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.Random;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  public static int timerSeconds = 120;
  private Label countdownLabel;
  private static Scene scene;
  private StackPane mainLayout;
  public static boolean tileGameComplete = false;
  public static int passcode;
  public static Boolean timerStarted = false;
  public static Timeline timerTimeline;
  public static int chosenTimer;

  public static void main(final String[] args) {

    launch();
  }

  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  public static void setScene(AppUi fxml) {
    scene.setRoot(SceneManager.getScene(fxml));
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  public static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {

    Random r = new Random();
    passcode = r.nextInt((9999 - 1000) + 1) + 1000;
    GameState.password = String.valueOf(passcode);

    SceneManager.addScene(AppUi.CHAT, loadFxml("chat"));
    SceneManager.addScene(AppUi.TUTORIAL, loadFxml("tutorial"));
    SceneManager.addScene(AppUi.ANIMATION, loadFxml("animation"));
    SceneManager.addScene(AppUi.INTRO, loadFxml("start"));

    scene = new Scene(SceneManager.getScene(AppUi.INTRO), 1000, 650);

    stage.setScene(scene);
    stage.show();
  }
}
