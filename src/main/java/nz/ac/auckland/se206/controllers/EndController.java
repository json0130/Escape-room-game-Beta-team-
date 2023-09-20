package nz.ac.auckland.se206.controllers;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;

public class EndController implements Initializable {

    @FXML
    private Label win;
    @FXML
    private Label lose;
    @FXML private Pane scene;
    @FXML
    private Button button;
    @FXML   
    private ImageView e1;

    @FXML
    private void startAnimation(ActionEvent events) {
             // Create a timeline to continuously increase the scaling factor
             Timeline continuousScaling = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(win.scaleXProperty(), 1.0)),
                new KeyFrame(Duration.ZERO, new KeyValue(win.scaleYProperty(), 1.0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(win.scaleXProperty(), 2)),
                new KeyFrame(Duration.seconds(1), new KeyValue(win.scaleYProperty(), 2)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(win.scaleXProperty(), 1.7)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(win.scaleYProperty(), 1.7))
        );
        continuousScaling.setCycleCount(1); // Play the animation once

        // Create a translate animation for the r1
        TranslateTransition Translation = new TranslateTransition(Duration.seconds(3), win);

        // Set the animation properties
        
        Translation.setCycleCount(1); // Play the animation once
        Translation.setAutoReverse(false); // Don't reverse the animation

        // Start the animations
        continuousScaling.play();
        Translation.play();
    }

    @FXML
    private void animation(ActionEvent event){
        // Create a scale transition
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2), win);
        scaleTransition.setFromX(1.0); // Starting X scale
        scaleTransition.setFromY(1.0); // Starting Y scale
        scaleTransition.setToX(2.0);   // Ending X scale (scale up by a factor of 2)
        scaleTransition.setToY(2.0);   // Ending Y scale (scale up by a factor of 2)

        // Create a translate transition
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), win);
        translateTransition.setByX(100); // Move label 100 units to the right
        translateTransition.setByY(50);  // Move label 50 units down

        // Play both transitions in parallel
        scaleTransition.play();
        translateTransition.play();
    }

    private void explosionAnimation(ImageView e1){
        // Create a scale transition to continuously increase the scaling factor
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), e1);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(3.0); // Scale factor on X-axis
        scaleTransition.setToY(3.0); // Scale factor on Y-axis
        scaleTransition.setCycleCount(1); // Play the animation once

        // Start the scale animation
        scaleTransition.play();
        // Enable the button when the animation is finished
            scaleTransition.setOnFinished(event -> {
                App.setScene(AppUi.PLAYER);
            });
    
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // scene.sceneProperty().addListener((observable, oldScene, newScene) -> {
        //     if (newScene != null) {
        //         // Schedule the playRock method to run after the scene is shown
        //         Platform.runLater(this::startAnimation);
        //     }
        // });
    }
}