package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
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
import javafx.scene.input.KeyCode;
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

  public static ChatCompletionRequest chatCompletionRequest;

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

    // aiPane
    //     .visibleProperty()
    //     .addListener(
    //         new ChangeListener<Boolean>() {
    //           @Override
    //           public void changed(
    //               ObservableValue<? extends Boolean> observable,
    //               Boolean oldValue,
    //               Boolean newValue) {
    //             if (newValue) {
    //               chatTextArea.setText(App.aiWindow);
    //               chatTextArea.positionCaret(chatTextArea.getText().length());
    //               System.out.println("Pane is now visible");

    //             } else {
    //               // Pane's visibility is set to false
    //               System.out.println("Pane is now invisible");
    //             }
    //           }
    //         });

      chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(1).setTopP(1).setMaxTokens(100);
      printGreeting();
  }

  
  // /**
  //  * Appends a chat message to the chat text area.
  //  *
  //  * @param msg the chat message to append
  //  */
  // private void appendChatMessage(ChatMessage msg) {
  //   App.aiWindow = App.aiWindow.concat((msg.getRole() + ": " + msg.getContent() + "\n\n"));
  //   chatTextArea.setText(App.aiWindow);
  //   chatTextArea.positionCaret(chatTextArea.getText().length());
  // }

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
  }

  @FXML
  public void setPaneVisible() {
    if (aiPane.isVisible() == false) {
      aiPane.setVisible(true);
    }
  }

  @FXML
  private void onCloseWindowClick() {
    // chatTextArea.setText(App.aiWindow);
    // chatTextArea.clear();
    // chatTextArea.appendText("AI: How can I help?" + "\n\n");
    aiPane.setVisible(false);
  }



  // This method is to print required greeting messages when the player visits each room.
  private void printGreeting() {
    Timer greetingTimer = new Timer(true);
    greetingTimer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            // If the player visits the map for the first time, game master introduces itself
            if (GameState.isPlayerInMap && !GameState.beenToMap) {
              try {
                runGpt(new ChatMessage("user", GptPromptEngineering.greeting()));
                GameState.beenToMap = true;
              } catch (ApiProxyException e) {
                e.printStackTrace();
              }
              // If the player visits the closet room for the first time, game master introduces the
              // closet room
            } else if (GameState.isPlayerInRoom1 && !GameState.beenToRoom1) {
              try {
                runGpt(new ChatMessage("user", GptPromptEngineering.greetingRoom1()));
                GameState.beenToRoom1 = true;
              } catch (ApiProxyException e) {
                e.printStackTrace();
              }
              // If the player visits the computer room for the first time, game master introduces
              // the computer room
            } else if (GameState.isPlayerInRoom2 && !GameState.beenToRoom2) {
              try {
                runGpt(new ChatMessage("user", GptPromptEngineering.greetingRoom2()));
                GameState.beenToRoom2 = true;
              } catch (ApiProxyException e) {
                e.printStackTrace();
              }
              // If the player visits the control room for the first time, game master introduces
              // the control room
            } else if (GameState.isPlayerInRoom3 && !GameState.beenToRoom3) {
              try {
                runGpt(new ChatMessage("user", GptPromptEngineering.greetingRoom3()));
                GameState.beenToRoom3 = true;
              } catch (ApiProxyException e) {
                e.printStackTrace();
              }
              // if all room has been visited once, timer is no longer needed so cancel the timer
            } else if (GameState.beenToRoom1 && GameState.beenToRoom2 && GameState.beenToRoom3) {
              greetingTimer.cancel();
            }
          }
        },
        0,
        100);
  }
}
