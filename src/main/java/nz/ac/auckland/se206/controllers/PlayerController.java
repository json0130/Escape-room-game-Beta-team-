package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Action;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;
import javafx.scene.layout.Pane;
import java.net.URL;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class PlayerController implements Initializable {

  private BooleanProperty wPressed = new SimpleBooleanProperty();
  private BooleanProperty aPressed = new SimpleBooleanProperty();
  private BooleanProperty sPressed = new SimpleBooleanProperty();
  private BooleanProperty dPressed = new SimpleBooleanProperty();

  private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);
    private int movementVariable = 5;
    private double shapesize;
    private double progressSize = 4.0;

  List<Rectangle> walls = new ArrayList<>();

    @FXML private ImageView player;
    @FXML private Rectangle room1;
    @FXML private Rectangle room2;
    @FXML private Rectangle room3;

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
  private double previousX;
  private double previousY;

  @FXML private Label countdownLabel;

  @FXML private Rectangle gpt;
  @FXML private Rectangle gptBackground;
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        gptBackground.setVisible(false);
        chatTextArea.setVisible(false);
        inputText.setVisible(false);
        sendButton.setVisible(false);


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
    }
    public void checkRoom1(ImageView player, Rectangle room1) {
        if (player.getBoundsInParent().intersects(room1.getBoundsInParent())) {
            room1.setVisible(true);
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
            pauseTransition.setOnFinished(event -> {
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
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
            pauseTransition.setOnFinished(event -> {
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
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
            pauseTransition.setOnFinished(event -> {
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

    public void checkCollision2(ImageView player, List<Rectangle> walls){
        for(Rectangle wall : walls){
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
    private void callGPT(MouseEvent event) {
        // if the player clicked the gpt rectangle, the gpt scene is loaded
      chatTextArea.setVisible(true);
      inputText.setVisible(true);
      sendButton.setVisible(true);
      gptBackground.setVisible(true);

    }

      /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    chatTextArea.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
        ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
        Choice result = chatCompletionResult.getChoices().iterator().next();
        chatCompletionRequest.addMessage(result.getChatMessage());
        return result.getChatMessage();
    } catch (ApiProxyException e) {
        e.printStackTrace();
        return null;
    }
}

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    String message = inputText.getText();
    if (message.trim().isEmpty()) {
      return;
    }
    inputText.clear();
    
    // Store the user message for GPT response
    lastUserMessage = message;

    if ((GameState.difficulty == "MEDIUM") && (GameState.numOfHints <= 0)) {
      // Inform the user that they have reached the hint limit
      Platform.runLater(
          () -> {
            chatTextArea.setText("You've used all of hints for Medium difficulty." +
            "You can still ask for help, but you will not get any hints.");
            
            PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(2));
            delay.setOnFinished(
                event1 -> {
                  
                });
            delay.play();
          });
      return; // Exit the method without sending a message to GPT
    }

    // Create a background thread for running GPT
    Thread backgroundThread =
        new Thread(
            () -> {
              ChatMessage lastMsg = null;
              try {

                if (GameState.difficulty == "EASY") {
                  // Handle Easy difficulty
                  lastMsg =
                      runGpt(
                          new ChatMessage(
                              "user",
                              "The user is asking for help regarding the following request: "
                                  + message
                                   + "First determine if the user is asking for help or not. This"
                                  + " help can be related to not knowing what to do, or asking for"
                                  + " what to find or asking for what is the next step."
                                  + " The user may ask for what to do,"
                                  + " and that is considered a asking for help. If you think the"
                                  + " user is not asking for a hint or help, reply back to their"
                                  + " message. However, if you believe it is the user asking for"
                                  + " help, start your prompt with \"hint: \"."
                                  + ". If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInMap
                                  + " is true and"
                                  + GameState.isRiddleResolved
                                  + " is false or if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to go to the closet room, if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to go to the computer room."
                                  + " if not tell them to go to the control room."
                                  + "If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom1
                                  + " is true and if"
                                  + GameState.isRiddleResolved
                                  + " is false, tell them to solve the riddle by putting the books, if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to try find the space suits in the room, if not"
                                  + " tell them they can leave this room."
                                  + "If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom2
                                  + " is true and if"
                                  + GameState.foundComputer
                                  + " is false, tell them to move around the room and find the computer, if"
                                  + GameState.isPuzzledSolved
                                  + " is false, tell them to solve the puzzle by clicking the tile, if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to find the passcode file in the computer screen, if not"
                                  + " tell them they can leave this room."
                                  + "If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom3
                                  + " is true and if"
                                  + GameState.isRiddleResolved
                                  + " is false, tell them to go to closet room and solve riddle, if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to go to closet room and check the space suits, if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to go to computer room and find the passcode."
                                  + "If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom3
                                  + " is true and if"
                                  + GameState.foundMonitor
                                  + " is false, tell them to move around the room and find the monitor, if"
                                  + GameState.isPasscodeCorrect
                                  + " is false, tell them to enter the passcode in the monitor, if"
                                  + GameState.isIdChecked
                                  + " is false, tell them to check the id cards in the ID scanner, if not"
                                  + " tell them they."
                                  + " Also, only write down the answers- do not talk"
                                  + " about what the user has typed in."));
                } else if (GameState.difficulty == "MEDIUM") {
                  // Handle Medium difficulty

                  lastMsg =
                      runGpt(
                          new ChatMessage(
                              "user",
                              "The user's message is:"
                                  + message
                                  + "First determine if the user is asking for help or not. This"
                                  + " help can be related to not knowing what to do, or asking for"
                                  + " what to find or asking for what is the next step."
                                  + " The user may ask for what to do,"
                                  + " and that is considered a asking for help. If you think the"
                                  + " user is not asking for a hint or help, reply back to their"
                                  + " message. However, if you believe it is the user asking for"
                                  + " help, start your prompt with \"hint: \"."
                                  + ". If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInMap
                                  + " is true and"
                                  + GameState.isRiddleResolved
                                  + " is false or if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to go to the closet room, if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to go to the computer room."
                                  + " if not tell them to go to the control room."
                                  + "If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom1
                                  + " is true and if"
                                  + GameState.isRiddleResolved
                                  + " is false, tell them to solve the riddle by putting the books, if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to try find the space suits in the room, if not"
                                  + " tell them they can leave this room."
                                  + "If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom2
                                  + " is true and if"
                                  + GameState.foundComputer
                                  + " is false, tell them to move around the room and find the computer, if"
                                  + GameState.isPuzzledSolved
                                  + " is false, tell them to solve the puzzle by clicking the tile, if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to find the passcode file in the computer screen, if not"
                                  + " tell them they can leave this room."
                                  + "If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom3
                                  + " is true and if"
                                  + GameState.isRiddleResolved
                                  + " is false, tell them to go to closet room and solve riddle, if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to go to closet room and check the space suits, if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to go to computer room and find the passcode."
                                  + "If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom3
                                  + " is true and if"
                                  + GameState.foundMonitor
                                  + " is false, tell them to move around the room and find the monitor, if"
                                  + GameState.isPasscodeCorrect
                                  + " is false, tell them to enter the passcode in the monitor, if"
                                  + GameState.isIdChecked
                                  + " is false, tell them to check the id cards in the ID scanner, if not"
                                  + " tell them they."
                                  + " Also, only write down the answers- do not talk"
                                  + " about what the user has typed in."));
                  if (isHintRequest(lastMsg)) {
                    // Check hint limit for Medium difficulty
                    GameState.numOfHints--; // Decrease the hint count
                    // Provide hints or guidance for Medium difficulty
                    // Example: runGpt(new ChatMessage("user", "Can you give me a hint?"));
                    System.out.println(isHintRequest(lastMsg));
                    System.out.println(GameState.numOfHints);
                  }
                } else if (GameState.difficulty == "HARD") {
                  lastMsg =
                      runGpt(
                          new ChatMessage(
                              "user",
                              "The user's message is: "
                                  + message
                                  + "First determine if the user is asking for help or not. This"
                                  + " help can be related to not knowing what to do, or asking for"
                                  + " what to find or asking for what is the next step."
                                  + " The user may ask for what to do,"
                                  + " and that is considered a asking for help. If you think the"
                                  + " user is not asking for a hint or help, reply back to their"
                                  + " message. However, if you believe it is the user asking for"
                                  + " help, start your prompt with \"hint: \"."
                                  + ". If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInMap
                                  + " is true and"
                                  + GameState.isRiddleResolved
                                  + " is false or if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to go to the closet room, if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to go to the computer room."
                                  + " if not tell them to go to the control room."
                                  + "If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom1
                                  + " is true and if"
                                  + GameState.isRiddleResolved
                                  + " is false, tell them to solve the riddle by putting the books, if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to try find the space suits in the room, if not"
                                  + " tell them they can leave this room."
                                  + "If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom2
                                  + " is true and if"
                                  + GameState.foundComputer
                                  + " is false, tell them to move around the room and find the computer, if"
                                  + GameState.isPuzzledSolved
                                  + " is false, tell them to solve the puzzle by clicking the tile, if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to find the passcode file in the computer screen, if not"
                                  + " tell them they can leave this room."
                                  + "If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom3
                                  + " is true and if"
                                  + GameState.isRiddleResolved
                                  + " is false, tell them to go to closet room and solve riddle, if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to go to closet room and check the space suits, if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to go to computer room and find the passcode."
                                  + "If the user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom3
                                  + " is true and if"
                                  + GameState.foundMonitor
                                  + " is false, tell them to move around the room and find the monitor, if"
                                  + GameState.isPasscodeCorrect
                                  + " is false, tell them to enter the passcode in the monitor, if"
                                  + GameState.isIdChecked
                                  + " is false, tell them to check the id cards in the ID scanner, if not"
                                  + " tell them they."
                                  + " Also, only write down the answers- do not talk"
                                  + " about what the user has typed in. The"
                                  + " important thing is that you never help the user."));
                }

              } catch (ApiProxyException e) {
                e.printStackTrace();
              }

              final ChatMessage finalLastMsg = lastMsg;
              Platform.runLater(
                  () -> {
                    // Update the UI with the response
                    if (finalLastMsg != null) {
                      chatTextArea.setText(finalLastMsg.getContent());
                    }
                    
                    PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(2));
                    delay.setOnFinished(
                        event1 -> {
                          
                        });
                    delay.play();
                  });
            });

    // Start the background thread
    backgroundThread.start();
  }

  private boolean isHintRequest(ChatMessage messageFromGpt) {
    // Extract the content of the message from GPT's response
    String gptResponse = messageFromGpt.getContent().toLowerCase();

    // Check if the GPT response starts with "hint: "
    if (gptResponse.startsWith("hint: ")) {
      return true;
    }
    // If the GPT response does not start with "hint: ", it's not a hint request
    return false;
  }

}
