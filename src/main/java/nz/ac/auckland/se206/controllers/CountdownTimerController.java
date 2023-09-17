package nz.ac.auckland.se206.controllers;

import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class CountdownTimerController {

    @FXML private Label countdownLabel;
    @FXML private Timeline countdownTimeline;
    private int remainingSeconds = 120;

    @FXML
    public void initialize() throws ApiProxyException {
         // Initialize the countdown timer in the initialize method
        updateCountdownLabel();

        countdownTimeline = new Timeline(
            new KeyFrame(Duration.seconds(1), this::updateTimer)
        );
        countdownTimeline.setCycleCount(remainingSeconds);

        // Start the countdown immediately when the scene is loaded
        countdownTimeline.play();
    }

    private void updateCountdownLabel() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        countdownLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateTimer(ActionEvent event) {
        remainingSeconds--;
        updateCountdownLabel();

        if (remainingSeconds <= 0) {
            // Countdown finished, you can handle this event here
            countdownTimeline.stop();
        }
    }
}
