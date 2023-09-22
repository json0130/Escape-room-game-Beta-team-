package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class PlayerController implements Initializable {

  private BooleanProperty wPressed = new SimpleBooleanProperty();
  private BooleanProperty aPressed = new SimpleBooleanProperty();
  private BooleanProperty sPressed = new SimpleBooleanProperty();
  private BooleanProperty dPressed = new SimpleBooleanProperty();

  private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);
  private int movementVariable = 5;
  private double shapesize;
  private double progressSize = 4.0;
  private Boolean soundPlaying = false;

  String soundEffect = "src/main/resources/sounds/door-open.mp3";
  Media media = new Media(new File(soundEffect).toURI().toString());
  MediaPlayer mediaPlayer = new MediaPlayer(media);

  List<Rectangle> walls = new ArrayList<>();

  @FXML private ImageView player;
  @FXML private Rectangle room1;
  @FXML private Rectangle room2;
  @FXML private Rectangle room3;
  @FXML private Rectangle black;
  @FXML private ImageView gameMasterBox;

  @FXML private Label playerLabel;
  @FXML private Label main;
  @FXML private Label computer;
  @FXML private Label closet;
  @FXML private Label control;
  @FXML private Label difficultyLabel;
  @FXML private Label hintLabel;
  @FXML private Label hintLabel2;

  @FXML private Rectangle wall;
  @FXML private Rectangle wall1;
  @FXML private Rectangle wall2;
  @FXML private Rectangle wall3;
  @FXML private Rectangle wall4;
  @FXML private Rectangle wall5;
  @FXML private Rectangle wall6;
  @FXML private Rectangle wall7;
  @FXML private Rectangle wall8;
  @FXML private Rectangle wall9;
  @FXML private Rectangle wall10;
  @FXML private Rectangle wall11;
  @FXML private Rectangle wall12;
  @FXML private Rectangle wall13;
  @FXML private Rectangle wall14;
  @FXML private Rectangle wall15;
  @FXML private Rectangle wall16;
  @FXML private Rectangle wall17;
  @FXML private Rectangle wall18;
  @FXML private Rectangle wall19;
  @FXML private Rectangle wall20;
  @FXML private Rectangle wall21;

  @FXML private Pane scene;

  @FXML private Button reset;
  @FXML private Button btnSend;
  @FXML private Button btnClose;
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  private double previousX;
  private double previousY;

  @FXML private Label countdownLabel;

  private ChatCompletionRequest chatCompletionRequest;
  public static boolean hintContained = false;
  public static boolean answerContained = false;
  private String lastUserMessage = ""; // Track the last user message for GPT response

  @FXML
  void start(ActionEvent event) {
    player.setLayoutX(10);
    player.setLayoutY(200);
  }

  AnimationTimer collisionTimer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          checkCollision2(player, walls);
          checkRoom1(player, room1);
          checkRoom2(player, room2);
          checkRoom3(player, room3);
        }
      };

  AnimationTimer timer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          playerLabel.setVisible(false);
          black.setVisible(false);

          previousX = player.getLayoutX(); // Update previousX
          previousY = player.getLayoutY(); // Update previousY

          previousX = player.getLayoutX(); // Update previousX
          previousY = player.getLayoutY(); // Update previousY

          if (wPressed.get()) {
            player.setLayoutY(player.getLayoutY() - movementVariable);
          }
          if (aPressed.get()) {
            player.setLayoutX(player.getLayoutX() - movementVariable);
          }
          if (sPressed.get()) {
            player.setLayoutY(player.getLayoutY() + movementVariable);
          }
          if (dPressed.get()) {
            player.setLayoutX(player.getLayoutX() + movementVariable);
          }
          squareBorder();
        }
      };

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    introTextToSpeech();
    playerLabel.setVisible(true);
    black.setVisible(true);

    room1.setVisible(false);
    room2.setVisible(false);
    room3.setVisible(false);
    // Set labels to have white text and black stroke for the text
    main.setStyle("-fx-text-fill: white; -fx-stroke: black; -fx-stroke-width: 1px;");
    computer.setStyle("-fx-text-fill: white; -fx-stroke: black; -fx-stroke-width: 1px;");
    closet.setStyle("-fx-text-fill: white; -fx-stroke: black; -fx-stroke-width: 1px;");
    control.setStyle("-fx-text-fill: white; -fx-stroke: black; -fx-stroke-width: 1px;");

    shapesize = player.getFitWidth();
    movementSetup();

    walls.add(wall);
    walls.add(wall1);
    walls.add(wall2);
    walls.add(wall3);
    walls.add(wall4);
    walls.add(wall5);
    walls.add(wall6);
    walls.add(wall7);
    walls.add(wall8);
    walls.add(wall9);
    walls.add(wall10);
    walls.add(wall11);
    walls.add(wall12);
    walls.add(wall13);
    walls.add(wall14);
    walls.add(wall15);
    walls.add(wall16);
    walls.add(wall17);
    walls.add(wall18);
    walls.add(wall19);
    walls.add(wall20);
    walls.add(wall21);

    //     // when the enter key is pressed, message is sent
    // inputText.setOnKeyPressed(
    //     event -> {
    //       if (event.getCode() == KeyCode.ENTER) {
    //         try {
    //           onSendMessage(new ActionEvent());
    //         } catch (ApiProxyException | IOException e) {
    //           e.printStackTrace();
    //         }
    //       }
    //     });
    // chatTextArea.setEditable(false);

    // chatCompletionRequest =
    //     new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    //   try {
    //     runGpt(new ChatMessage("user", GptPromptEngineering.getGameMaster()));
    //   } catch (ApiProxyException e) {
    //     e.printStackTrace();
    //   }

    collisionTimer.start();

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
    // if difficulty is selected, label is updated
    detectDifficulty();

    Platform.runLater(
        () -> {
          Stage stage = (Stage) scene.getScene().getWindow();

          stage.setOnCloseRequest(
              event -> {
                Platform.exit();
                System.exit(0);
              });
        });
  }

  private void introTextToSpeech() {
    Task<Void> introTask =
        new Task<>() {

          @Override
          protected Void call() throws Exception {
            TextToSpeech textToSpeech = new TextToSpeech();
            textToSpeech.speak("Welcome to STARSHIP ESCAPE 1!");
            return null;
          }
        };
    Thread introThread = new Thread(introTask);
    introThread.start();

    gameMasterBox.setVisible(false);
    inputText.setVisible(false);
    chatTextArea.setVisible(false);
    btnClose.setVisible(false);
    btnSend.setVisible(false);
  }

  public void checkRoom1(ImageView player, Rectangle room1) {
    if (player.getBoundsInParent().intersects(room1.getBoundsInParent())) {
      room1.setVisible(true);

      // System.out.println(mediaPlayer.getMedia());
      // if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
      //   // soundDoorOpen();
      // }

      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(
          event -> {
            // Adjust the player's position to be right in front of the room
            player.setLayoutX(272);
            player.setLayoutY(336);
            App.setScene(AppUi.ROOM1);
            timer.stop();
          });
      pauseTransition.play();
    } else {
      room1.setVisible(false);
    }
  }

  public void checkRoom2(ImageView player, Rectangle room2) {
    if (player.getBoundsInParent().intersects(room2.getBoundsInParent())) {
      room2.setVisible(true);

      // if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
      //   soundDoorOpen();
      // }
      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(
          event -> {
            // Adjust the player's position to be right in front of the room
            player.setLayoutX(500);
            player.setLayoutY(284);

            App.setScene(AppUi.TILEROOM);
            timer.stop();
          });
      pauseTransition.play();
    } else {
      room2.setVisible(false);
    }
  }

  public void checkRoom3(ImageView player, Rectangle room3) {
    if (player.getBoundsInParent().intersects(room3.getBoundsInParent())) {
      room3.setVisible(true);
      String musicFile;
      System.out.println("ENTERED ROOM3");
      if (App.timerSeconds < 60 && App.musicType.equals("starting")) {
        App.musicType = "final";
        musicFile = "srcsrc/main/resources/sounds/final-BG-MUSIC.mp3";
        Media media = new Media(new File(musicFile).toURI().toString());

        App.mediaPlayer.stop();
        App.mediaPlayer = new MediaPlayer(media);
        App.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        App.mediaPlayer.setVolume(0.1);
        App.mediaPlayer.setAutoPlay(true);
      }

      // if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
      //   soundDoorOpen();
      // }
      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(
          event -> {
            // Adjust the player's position to be right in front of the room
            player.setLayoutX(674);
            player.setLayoutY(292);

            App.setScene(AppUi.ROOM3);
            timer.stop();
          });
      pauseTransition.play();
    } else {
      room3.setVisible(false);
    }
  }

  public void checkCollision2(ImageView player, List<Rectangle> walls) {
    for (Rectangle wall : walls) {
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

  @FXML
  public void onRoom3(ActionEvent event) {

    App.setScene(AppUi.ROOM3);
  }

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

  @FXML
  public void clickGameMaster(MouseEvent event) {
    App.setScene(AppUi.HELPERCHAT);
    // gameMasterBox.setVisible(true);
    // inputText.setVisible(true);
    // chatTextArea.setVisible(true);
    // btnClose.setVisible(true);
    // btnSend.setVisible(true);
  }

  @FXML
  public void onClose(ActionEvent event) {
    gameMasterBox.setVisible(false);
    inputText.setVisible(false);
    chatTextArea.setVisible(false);
    btnClose.setVisible(false);
    btnSend.setVisible(false);
  }

  @FXML
  public void onSend(ActionEvent event) {}
}
