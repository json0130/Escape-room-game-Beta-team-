package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class ChatController {
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;
  @FXML private ImageView robotBase;
  @FXML private ImageView robotReply;
  @FXML private ImageView robotThink;
  @FXML private Label hintLabel;
  @FXML private Label hintLabel2;
  @FXML private ImageView glitch;
  @FXML private ImageView riddleHint;
  @FXML private ImageView riddleCorrect;
  @FXML private ImageView riddleWrong;
  @FXML private ImageView riddleGreeting;

  private ChatCompletionRequest chatCompletionRequest;
  public static boolean isRiddleGiven = false;

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

              e.printStackTrace();
            }
          }
        });
    chatTextArea.setEditable(false);
    chatCompletionRequest =  new ChatCompletionRequest().setN(1).setTemperature(1).setTopP(1).setMaxTokens(100);
    runGpt(new ChatMessage("user", GptPromptEngineering.riddleAi(Room1Controller.riddleAnswer)));
    System.out.println(Room1Controller.riddleAnswer);
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
    glitch.setVisible(true);
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
                      riddleCorrect();
                    } else if (result.getChatMessage().getRole().equals("assistant")
                        && result.getChatMessage().getContent().startsWith("Hint")) {
                      GameState.numOfHints--;
                      riddleHint();
                    } else if (result.getChatMessage().getRole().equals("assistant")
                        && result.getChatMessage().getContent().startsWith("Incorrect")) {
                          riddleWrong();
                    } else {
                      riddleGreeting();
                    }
                    glitch.setVisible(false);
                  });
              return result.getChatMessage();
            } catch (ApiProxyException e) {

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
    soundButttonClick();
    String message = inputText.getText();
    if (message.trim().isEmpty()) {
      return;
    }

    inputText.clear();
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    msg = new ChatMessage("user", GptPromptEngineering.checkRiddleAnswer(message));

    runGpt(msg);
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
    App.setScene(AppUi.ROOM1);
  }

  @FXML
  private void soundButttonClick() {
    String soundEffect = "src/main/resources/sounds/button-click.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setAutoPlay(true);
  }

  private void riddleCorrect() {
    riddleHint.setVisible(false);
    riddleWrong.setVisible(false);
    riddleCorrect.setVisible(true);
    riddleGreeting.setVisible(false);
  }

  private void riddleHint() {
    riddleHint.setVisible(true);
    riddleWrong.setVisible(false);
    riddleCorrect.setVisible(false);
    riddleGreeting.setVisible(false);
  }

  private void riddleWrong() {
    riddleHint.setVisible(false);
    riddleWrong.setVisible(true);
    riddleCorrect.setVisible(false);
    riddleGreeting.setVisible(false);
  }

  private void riddleGreeting() {
    riddleHint.setVisible(false);
    riddleWrong.setVisible(false);
    riddleCorrect.setVisible(false);
    riddleGreeting.setVisible(true);
  }
}
