package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
public class AIWindowController {
  @FXML public TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;
  @FXML private ImageView robotBase;
  @FXML private ImageView robotReply;
  @FXML private ImageView robotThink;

  @FXML private Pane aiPane;
  @FXML private ImageView closeWindow;

  private nz.ac.auckland.se206.chatHistory chatHistory;
  private String currentRoomName;

  public static ChatCompletionRequest chatCompletionRequest;

  private AnimationTimer timer;

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {

    chatTextArea.setEditable(false);
    aiPane
        .visibleProperty()
        .addListener(
            new ChangeListener<Boolean>() {
              @Override
              public void changed(
                  ObservableValue<? extends Boolean> observable,
                  Boolean oldValue,
                  Boolean newValue) {
                if (newValue) {
                  chatTextArea.setText(App.aiWindow);
                  chatTextArea.positionCaret(chatTextArea.getText().length());
                  System.out.println("Pane is now visible");

                } else {
                  // Pane's visibility is set to false
                  System.out.println("Pane is now invisible");
                }
              }
            });

    timer =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            chatTextArea.setText(App.aiWindow);
            chatTextArea.setScrollTop(Double.MAX_VALUE);
          }
        };

    timer.start();

    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(1).setTopP(1).setMaxTokens(100);
    App.greetingInMap =
        runGptWithoutPrinting(new ChatMessage("user", GptPromptEngineering.greeting()));
    App.aiWindow = App.aiWindow.concat("EVA: " + App.greetingInMap + "\n\n");
    // App.greetingInRoom1 =
    //     runGptWithoutPrinting(new ChatMessage("user", GptPromptEngineering.greetingRoom1()));
    // App.greetingInRoom2 =
    //     runGptWithoutPrinting(new ChatMessage("user", GptPromptEngineering.greetingRoom2()));
    // App.greetingInRoom3 =
    //     runGptWithoutPrinting(new ChatMessage("user", GptPromptEngineering.greetingRoom3()));
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  // private void appendChatMessage(ChatMessage msg) {
  //   App.aiWindow = App.aiWindow.concat((msg.getRole() + ": " + msg.getContent() + "\n\n"));
  //   chatTextArea.setText(App.aiWindow);
  //   Platform.runLater(() -> {
  //     //chatTextArea.positionCaret(chatTextArea.getText().length());
  //     chatTextArea.setScrollTop(Double.MAX_VALUE); // this will scroll to the bottom
  //     System.out.println(App.aiWindow);
  //     GameState.hasHappend = false;
  //   });
  // }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    App.aiWindow = App.aiWindow.concat(msg.getRole() + ": " + msg.getContent() + "\n\n");
    Platform.runLater(
        () -> {
          chatTextArea.setText(App.aiWindow);
          chatTextArea.setScrollTop(Double.MAX_VALUE); // this will scroll to the bottom
        });
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
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
              Platform.runLater(
                  () -> {
                    if (result.getChatMessage().getRole().equals("assistant")
                        && result.getChatMessage().getContent().startsWith("Hint")) {
                      GameState.numOfHints--;
                    }
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
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private String runGptWithoutPrinting(ChatMessage msg) {
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      return result.getChatMessage().getContent();
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

    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);

    if (GameState.difficulty == "EASY") {
      robotThink();
      // Handle Easy difficulty
      runGpt(new ChatMessage("user", GptPromptEngineering.easy(message)));
    } else if (GameState.difficulty == "MEDIUM") {
      robotThink();
      // Handle Medium difficulty
      runGpt(new ChatMessage("user", GptPromptEngineering.medium(message)));
    } else if (GameState.difficulty == "HARD") {
      robotThink();
      runGpt(new ChatMessage("user", GptPromptEngineering.hard(message)));
    }
    // if (lastMsg.getRole().equals("assistant") && lastMsg.getContent().startsWith("Correct")) {
    //   GameState.isRiddleResolved = true;
    // }
    // if (lastMsg.getRole().equals("assistant") && lastMsg.getContent().startsWith("hint")) {
    //   GameState.numOfHints--;
    // }
    aiPane.requestFocus(); // Add this line
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
    chatTextArea.setText(App.aiWindow);
  }

  @FXML
  public void setPaneVisible() {
    if (aiPane.isVisible() == false) {
      aiPane.setVisible(true);
    }
  }

  // private ChatCompletionRequest getChatCompletionRequest() {
  //   return this.chatCompletionRequest;
  // }

  // private void setChatCompletionRequest(ChatCompletionRequest incoming) {
  //   this.chatCompletionRequest = incoming;
  // }
}
