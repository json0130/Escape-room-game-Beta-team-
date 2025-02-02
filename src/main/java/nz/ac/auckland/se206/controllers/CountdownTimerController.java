package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** The controller for the countdown timer that appears in all scenes. */
public class CountdownTimerController {

  @FXML private Label countdownLabel;
  @FXML private Timeline countdownTimeline;

  /**
   * initializes the countdown timer that appears on all scenes.
   *
   * @throws ApiProxyException if the file is not found
   */
  @FXML
  public void initialize() throws ApiProxyException {
    updateCountdownLabel();

    countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), this::updateTimer));
    countdownTimeline.setCycleCount(Timeline.INDEFINITE);
    countdownTimeline.play();

    System.out.println("countdown INITIALIZED");
  }

  private void updateCountdownLabel() {
    int minutes = App.timerSeconds / 60;
    int seconds = App.timerSeconds % 60;
    countdownLabel.setText(String.format("%02d:%02d", minutes, seconds));
    if (minutes == 0 && seconds == 0) {
      countdownTimeline.stop();
    }
  }

  private void updateTimer(ActionEvent event) {
    updateCountdownLabel();
  }
}
