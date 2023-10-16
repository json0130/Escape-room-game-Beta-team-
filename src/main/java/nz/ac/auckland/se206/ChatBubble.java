package nz.ac.auckland.se206;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.gpt.ChatMessage;

public class ChatBubble {

  private Label bubbleText;
  private HBox bubbleBox;
  private Boolean isGreeting;

  public ChatBubble(ChatMessage msg, Boolean isGreeting) {

    bubbleText = new Label(msg.getContent());
    bubbleText.setWrapText(true);
    bubbleText.setFont(Font.font("Arial", 15));
    bubbleBox = new HBox();
    if (msg.getRole().equals("user")) {
      bubbleBox.setAlignment(Pos.CENTER_RIGHT);
      bubbleBox.setPadding(new Insets(3, 4, 3, 4));
      bubbleText.setStyle(
          "-fx-background-color: lightblue; -fx-background-radius: 10;-fx-padding: 10,20,20,10;");
    } else {
      if (isGreeting) {
        bubbleBox.setAlignment(Pos.CENTER_LEFT);
        bubbleBox.setPadding(new Insets(3, 4, 3, 4));
        bubbleText.setStyle(
            "-fx-background-color: lightgreen; -fx-background-radius: 10;-fx-padding:"
                + " 10,20,20,10;");
      } else {
        bubbleBox.setAlignment(Pos.CENTER_LEFT);
        bubbleBox.setPadding(new Insets(3, 4, 3, 4));
        bubbleText.setStyle(
            "-fx-background-color: lightyellow; -fx-background-radius: 10;-fx-padding:"
                + " 10,20,20,10;");
      }
    }

    bubbleBox.getChildren().addAll(bubbleText);
  }

  public Label getBubbleText() {
    return this.bubbleText;
  }

  public HBox getBubbleBox() {
    return this.bubbleBox;
  }
}
