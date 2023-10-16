package nz.ac.auckland.se206;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  public static int timerSeconds = 120;
  private static Scene scene;
  public static boolean tileGameComplete = false;
  public static int passcode;
  public static Boolean timerStarted = false;
  public static Timeline timerTimeline;
  public static int chosenTimer;
  public static MediaPlayer mediaPlayer;
  public static String musicType = "starting";
  public static AppUi previousRoom = AppUi.PLAYER;
  public static String aiWindow =
      "Hello! I'm EVA. Your personal AI Assistant. We're in a bit of an"
          + " emergency so if you have any questions on what you need to do, I'm here to help!"
          + " \n\n";
  public static ObservableList<ChatBubble> chatBubbleList = FXCollections.observableArrayList();
  public static VBox globalChatContainer = new VBox();
  public static String greetingInMap;
  public static String greetingInRoom1;
  public static String greetingInRoom2;
  public static String greetingInRoom3;
  public static ChatCompletionRequest chatCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(1).setTopP(1).setMaxTokens(100);
  public static Boolean hasGreeting = false;

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

  public static void resetRooms() throws IOException {
    // re initilaze all the rooms.
    try {
      Random r = new Random();
      passcode = r.nextInt((9999 - 1000) + 1) + 1000;
      GameState.password = String.valueOf(passcode);

      // String musicFile =
      //     "src/main/resources/sounds/Background-Music.mp3"; // Replace with the actual path to
      // // your audio file
      // Media media = new Media(new File(musicFile).toURI().toString());
      // mediaPlayer = new MediaPlayer(media);

      // // Set the cycle count to loop indefinitely
      // mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

      // mediaPlayer.setVolume(0.05);
      // mediaPlayer.setAutoPlay(true);

      SceneManager.addScene(AppUi.HELPERCHAT, loadFxml("helperchat"));
      SceneManager.addScene(AppUi.ROOM1, loadFxml("room1"));
      SceneManager.addScene(AppUi.CHAT, loadFxml("chat"));
      SceneManager.addScene(AppUi.PLAYER, loadFxml("player"));
      SceneManager.addScene(AppUi.END, loadFxml("end"));
      SceneManager.addScene(AppUi.WIN, loadFxml("win"));
      SceneManager.addScene(AppUi.LOSE, loadFxml("lose"));
      SceneManager.addScene(AppUi.TUTORIAL, loadFxml("tutorial"));
      SceneManager.addScene(AppUi.ANIMATION, loadFxml("animation"));
      SceneManager.addScene(AppUi.INTRO, loadFxml("start"));
      SceneManager.addScene(AppUi.TILEPUZZLE, loadFxml("tilegamedesk"));
      SceneManager.addScene(AppUi.TILEROOM, loadFxml("tilegameroom"));
      SceneManager.addScene(AppUi.ROOM3, loadFxml("room3"));
      SceneManager.addScene(AppUi.AIWINDOW, loadFxml("aiwindow"));
    } catch (IOException e) {
      e.printStackTrace();
    }
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

    String musicFile =
        "src/main/resources/sounds/Background-Music.mp3"; // Replace with the actual path to
    // your audio file
    Media media = new Media(new File(musicFile).toURI().toString());
    mediaPlayer = new MediaPlayer(media);

    // Set the cycle count to loop indefinitely
    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

    mediaPlayer.setVolume(0.05);
    mediaPlayer.setAutoPlay(true);

    SceneManager.addScene(AppUi.AIWINDOW, loadFxml("aiwindow"));
    SceneManager.addScene(AppUi.HELPERCHAT, loadFxml("helperchat"));
    SceneManager.addScene(AppUi.ROOM1, loadFxml("room1"));
    SceneManager.addScene(AppUi.CHAT, loadFxml("chat"));
    SceneManager.addScene(AppUi.PLAYER, loadFxml("player"));
    SceneManager.addScene(AppUi.END, loadFxml("end"));
    SceneManager.addScene(AppUi.WIN, loadFxml("win"));
    SceneManager.addScene(AppUi.LOSE, loadFxml("lose"));
    SceneManager.addScene(AppUi.END1, loadFxml("end1"));
    SceneManager.addScene(AppUi.TUTORIAL, loadFxml("tutorial"));
    SceneManager.addScene(AppUi.ANIMATION, loadFxml("animation"));
    SceneManager.addScene(AppUi.INTRO, loadFxml("start"));
    SceneManager.addScene(AppUi.TILEPUZZLE, loadFxml("tilegamedesk"));
    SceneManager.addScene(AppUi.TILEROOM, loadFxml("tilegameroom"));
    SceneManager.addScene(AppUi.ROOM3, loadFxml("room3"));
    scene = new Scene(SceneManager.getScene(AppUi.INTRO), 1200, 650);

    stage.setOnCloseRequest(
        event -> {
          event.consume(); // Consume the event, preventing the default close action
          handleCloseRequest(stage);
        });

    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
  }

  private void handleCloseRequest(Stage primaryStage) {
    primaryStage.close();
    Platform.exit();
    System.exit(0);
  }
}
