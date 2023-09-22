package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the chat view. */
public class HelperChatController {
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;
  @FXML private ImageView robotBase;
  @FXML private ImageView robotReply;
  @FXML private ImageView robotThink;

  private ChatCompletionRequest chatCompletionRequest;
  public static boolean hintContained = false;
  public static boolean answerContained = false;

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    // when the enter key is pressed, message is sent
    inputText.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            try {
              onSendMessage(new ActionEvent());
            } catch (ApiProxyException | IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
        });
    chatTextArea.setEditable(false);

    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    runGpt(new ChatMessage("user", GptPromptEngineering.getGameMaster()));
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
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.5).setTopP(0.2).setMaxTokens(100);
    Task<ChatMessage> runningGptTask =
        new Task<ChatMessage>() {
          @Override
          protected ChatMessage call() {
            chatCompletionRequest.addMessage(msg);
            try {
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequest.addMessage(result.getChatMessage());
              appendChatMessage(result.getChatMessage());
              // Check the correctness of player's answer for the riddle
              Platform.runLater(
                  () -> {
                    if (result.getChatMessage().getRole().equals("assistant")
                        && result.getChatMessage().getContent().startsWith("Correct")) {
                      GameState.isRiddleResolved = true;
                    }
                  });
              return result.getChatMessage();
            } catch (ApiProxyException e) {
              // TODO handle exception appropriately
              e.printStackTrace();
              return null;
            }
          }
        };
    Thread gptThread = new Thread(runningGptTask);
    gptThread.setDaemon(true);
    gptThread.start();
    return msg;
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
    // lastUserMessage = message;

    if ((GameState.difficulty == "MEDIUM") && (GameState.numOfHints <= 0)) {
      // Inform the user that they have reached the hint limit
      Platform.runLater(
          () -> {
            robotReply();
            chatTextArea.setText(
                "You've used all of hints for Medium difficulty."
                    + "You can still ask for help, but you will not get any hints.");

            PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(2));
            delay.setOnFinished(event1 -> {});

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
                  robotThink();
                  // Handle Easy difficulty
                  lastMsg =
                      runGpt(
                          new ChatMessage(
                              "user",
                              "The user is asking for help regarding the following request: "
                                  + message
                                  + "First determine if the user is asking for help or not. This"
                                  + " help can be related to not knowing what to do, or asking for"
                                  + " what to find or asking for what is the next step. The user"
                                  + " may ask for what to do, and that is considered a asking for"
                                  + " help. If you think the user is not asking for a hint or help,"
                                  + " reply back to their message. However, if you believe it is"
                                  + " the user asking for help, start your prompt with \"hint: \".."
                                  + " If the user's request is related to not knowing what to do,"
                                  + " if"
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
                                  + " is false, tell them to solve the riddle by putting the books,"
                                  + " if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to try find the space suits in the room,"
                                  + " if not tell them they can leave this room.If the user's"
                                  + " request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom2
                                  + " is true and if"
                                  + GameState.foundComputer
                                  + " is false, tell them to move around the room and find the"
                                  + " computer, if"
                                  + GameState.isPuzzledSolved
                                  + " is false, tell them to solve the puzzle by clicking the tile,"
                                  + " if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to find the passcode file in the computer"
                                  + " screen, if not tell them they can leave this room.If the"
                                  + " user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom3
                                  + " is true and if"
                                  + GameState.isRiddleResolved
                                  + " is false, tell them to go to closet room and solve riddle, if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to go to closet room and check the space"
                                  + " suits, if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to go to computer room and find the"
                                  + " passcode.If the user's request is related to not knowing what"
                                  + " to do, if"
                                  + GameState.isPlayerInRoom3
                                  + " is true and if"
                                  + GameState.foundMonitor
                                  + " is false, tell them to move around the room and find the"
                                  + " monitor, if"
                                  + GameState.isPasscodeCorrect
                                  + " is false, tell them to enter the passcode in the monitor, if"
                                  + GameState.isIdChecked
                                  + " is false, tell them to check the id cards in the ID scanner,"
                                  + " if not tell them they. Also, only write down the answers- do"
                                  + " not talk about what the user has typed in."));
                } else if (GameState.difficulty == "MEDIUM") {
                  robotThink();
                  // Handle Medium difficulty

                  lastMsg =
                      runGpt(
                          new ChatMessage(
                              "user",
                              "The user's message is:"
                                  + message
                                  + "First determine if the user is asking for help or not. This"
                                  + " help can be related to not knowing what to do, or asking for"
                                  + " what to find or asking for what is the next step. The user"
                                  + " may ask for what to do, and that is considered a asking for"
                                  + " help. If you think the user is not asking for a hint or help,"
                                  + " reply back to their message. However, if you believe it is"
                                  + " the user asking for help, start your prompt with \"hint: \".."
                                  + " If the user's request is related to not knowing what to do,"
                                  + " if"
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
                                  + " is false, tell them to solve the riddle by putting the books,"
                                  + " if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to try find the space suits in the room,"
                                  + " if not tell them they can leave this room.If the user's"
                                  + " request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom2
                                  + " is true and if"
                                  + GameState.foundComputer
                                  + " is false, tell them to move around the room and find the"
                                  + " computer, if"
                                  + GameState.isPuzzledSolved
                                  + " is false, tell them to solve the puzzle by clicking the tile,"
                                  + " if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to find the passcode file in the computer"
                                  + " screen, if not tell them they can leave this room.If the"
                                  + " user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom3
                                  + " is true and if"
                                  + GameState.isRiddleResolved
                                  + " is false, tell them to go to closet room and solve riddle, if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to go to closet room and check the space"
                                  + " suits, if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to go to computer room and find the"
                                  + " passcode.If the user's request is related to not knowing what"
                                  + " to do, if"
                                  + GameState.isPlayerInRoom3
                                  + " is true and if"
                                  + GameState.foundMonitor
                                  + " is false, tell them to move around the room and find the"
                                  + " monitor, if"
                                  + GameState.isPasscodeCorrect
                                  + " is false, tell them to enter the passcode in the monitor, if"
                                  + GameState.isIdChecked
                                  + " is false, tell them to check the id cards in the ID scanner,"
                                  + " if not tell them they. Also, only write down the answers- do"
                                  + " not talk about what the user has typed in."));
                  if (isHintRequest(lastMsg)) {
                    // Check hint limit for Medium difficulty
                    GameState.numOfHints--; // Decrease the hint count
                    // Provide hints or guidance for Medium difficulty
                    // Example: runGpt(new ChatMessage("user", "Can you give me a hint?"));
                    System.out.println(isHintRequest(lastMsg));
                    System.out.println(GameState.numOfHints);
                  }
                } else if (GameState.difficulty == "HARD") {
                  robotThink();
                  lastMsg =
                      runGpt(
                          new ChatMessage(
                              "user",
                              "The user's message is: "
                                  + message
                                  + "First determine if the user is asking for help or not. This"
                                  + " help can be related to not knowing what to do, or asking for"
                                  + " what to find or asking for what is the next step. The user"
                                  + " may ask for what to do, and that is considered a asking for"
                                  + " help. If you think the user is not asking for a hint or help,"
                                  + " reply back to their message. However, if you believe it is"
                                  + " the user asking for help, start your prompt with \"hint: \".."
                                  + " If the user's request is related to not knowing what to do,"
                                  + " if"
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
                                  + " is false, tell them to solve the riddle by putting the books,"
                                  + " if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to try find the space suits in the room,"
                                  + " if not tell them they can leave this room.If the user's"
                                  + " request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom2
                                  + " is true and if"
                                  + GameState.foundComputer
                                  + " is false, tell them to move around the room and find the"
                                  + " computer, if"
                                  + GameState.isPuzzledSolved
                                  + " is false, tell them to solve the puzzle by clicking the tile,"
                                  + " if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to find the passcode file in the computer"
                                  + " screen, if not tell them they can leave this room.If the"
                                  + " user's request is related to not knowing what to do, if"
                                  + GameState.isPlayerInRoom3
                                  + " is true and if"
                                  + GameState.isRiddleResolved
                                  + " is false, tell them to go to closet room and solve riddle, if"
                                  + GameState.isIdCorrected
                                  + " is false, tell them to go to closet room and check the space"
                                  + " suits, if"
                                  + GameState.foundPasscode
                                  + " is false, tell them to go to computer room and find the"
                                  + " passcode.If the user's request is related to not knowing what"
                                  + " to do, if"
                                  + GameState.isPlayerInRoom3
                                  + " is true and if"
                                  + GameState.foundMonitor
                                  + " is false, tell them to move around the room and find the"
                                  + " monitor, if"
                                  + GameState.isPasscodeCorrect
                                  + " is false, tell them to enter the passcode in the monitor, if"
                                  + GameState.isIdChecked
                                  + " is false, tell them to check the id cards in the ID scanner,"
                                  + " if not tell them they. Also, only write down the answers- do"
                                  + " not talk about what the user has typed in. The important"
                                  + " thing is that you never help the user."));
                }

              } catch (ApiProxyException e) {
                e.printStackTrace();
              }

              final ChatMessage finalLastMsg = lastMsg;
              Platform.runLater(
                  () -> {
                    // Update the UI with the response
                    if (finalLastMsg != null) {

                      chatTextArea.setText(message);
                    }

                    PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(2));
                    delay.setOnFinished(
                        event1 -> {
                          robotThink();
                        });

                    delay.play();
                  });
            });

    // Start the background thread
    backgroundThread.start();
  }

  private Boolean isHintRequest(ChatMessage lastMsg) {
    if (lastMsg.getContent().trim().isEmpty()) {
      return false;
    }
    if (lastMsg.getContent().contains("hint") || lastMsg.getContent().contains("Hint")) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    soundButttonClick();
    App.setScene(AppUi.PLAYER);
  }

  @FXML
  private void soundButttonClick() {
    String soundEffect = "src/main/resources/sounds/button-click.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setAutoPlay(true);
  }

  @FXML
  private void robotThink() {
    robotBase.setVisible(false);
    robotReply.setVisible(false);
    robotThink.setVisible(true);

    Platform.runLater(
        () -> {
          PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(2));
          delay.setOnFinished(
              event1 -> {
                robotReply();
              });
          delay.play();
        });
  }

  @FXML
  private void robotReply() {
    robotBase.setVisible(false);
    robotReply.setVisible(true);
    robotThink.setVisible(false);

    Platform.runLater(
        () -> {
          PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(2));
          delay.setOnFinished(
              event1 -> {
                robotIdle();
              });
          delay.play();
        });
  }

  @FXML
  private void robotIdle() {
    robotBase.setVisible(true);
    robotReply.setVisible(false);
    robotThink.setVisible(false);
  }
}
