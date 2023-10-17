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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatBubble;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the chat view. */
public class WindowController {
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;
  @FXML private ImageView robotBase;
  @FXML private ImageView robotReply;
  @FXML private ImageView robotThink;

  @FXML private Pane aiPane;
  @FXML private ImageView closeWindow;

  private AnimationTimer timer;

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
    System.out.println(Room1Controller.riddleAnswer);
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

    timer.start();

    if (App.hasGreeting == false) {
      App.hasGreeting = true;
      runGpt(new ChatMessage("user", GptPromptEngineering.greeting()), true);
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg, Boolean isGreeting) {
    ChatBubble newMessage1 = new ChatBubble(msg, isGreeting);
    ChatBubble newMessage2 = new ChatBubble(msg, isGreeting);
    ChatBubble newMessage3 = new ChatBubble(msg, isGreeting);
    ChatBubble newMessage4 = new ChatBubble(msg, isGreeting);
    ChatBubble newMessage5 = new ChatBubble(msg, isGreeting);
    ChatBubble newMessage6 = new ChatBubble(msg, isGreeting);

    App.chatBubbleList.add(newMessage1);
    PlayerController.chatBubbleListPlayer.add(newMessage2);
    Room1Controller.chatBubbleListRoom1.add(newMessage3);
    TileGameRoomController.chatBubbleListTileRoom.add(newMessage4);
    TileGameDeskController.chatBubbleListTileDesk.add(newMessage5);
    ExitController.chatBubbleListExit.add(newMessage6);
    System.out.println(
        "Appended: "
            + App.chatBubbleList
                .get(PlayerController.chatBubbleListPlayer.size() - 1)
                .getBubbleText()
                .getText());
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg, Boolean isGreeting) throws ApiProxyException {
    Task<ChatMessage> runningGptTask =
        new Task<ChatMessage>() {
          @Override
          protected ChatMessage call() {
            App.chatCompletionRequest.addMessage(msg);
            try {
              ChatCompletionResult chatCompletionResult = App.chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              App.chatCompletionRequest.addMessage(result.getChatMessage());
              Platform.runLater(
                  () -> {
                    appendChatMessage(result.getChatMessage(), isGreeting);
                  });
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
    appendChatMessage(msg, false);

    if (GameState.difficulty == "EASY") {
      robotThink();
      // Handle Easy difficulty
      runGpt(new ChatMessage("user", GptPromptEngineering.settingEasy(message)), false);
    } else if (GameState.difficulty == "MEDIUM") {
      robotThink();
      // Handle Medium difficulty
      runGpt(new ChatMessage("user", GptPromptEngineering.settingMedium(message)), false);
    } else if (GameState.difficulty == "HARD") {
      robotThink();
      runGpt(new ChatMessage("user", GptPromptEngineering.settingHard(message)), false);
    }
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
    // This method is used to change the robot image to thinking
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
    // This method is used to change the robot image to replying
    robotBase.setVisible(false);
    robotReply.setVisible(true);
    robotThink.setVisible(false);

    Platform.runLater(
        () -> {
          PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(2));
          delay.setOnFinished(
              event1 -> {
                robotBase.setVisible(true);
                robotReply.setVisible(false);
                robotThink.setVisible(false);
              });
          delay.play();
        });
  }
}
