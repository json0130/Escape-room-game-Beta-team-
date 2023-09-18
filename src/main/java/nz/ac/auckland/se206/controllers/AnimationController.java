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
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;

public class AnimationController implements Initializable {

    @FXML
    private ImageView spaceship;
    @FXML
    private ImageView r1;
    @FXML
    private Button b1;
    @FXML   
    private ImageView e1;

    @FXML
    private void startAnimation() {

             // Create a timeline to continuously increase the scaling factor
             Timeline continuousScaling = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(r1.scaleXProperty(), 1.0)),
                new KeyFrame(Duration.ZERO, new KeyValue(r1.scaleYProperty(), 1.0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(r1.scaleXProperty(), 1.5)),
                new KeyFrame(Duration.seconds(3), new KeyValue(r1.scaleYProperty(), 1.5))
        );
        continuousScaling.setCycleCount(1); // Play the animation once

        // Create a translate animation for the r1
        TranslateTransition r1Translation = new TranslateTransition(Duration.seconds(3), r1);
        TranslateTransition spaceshipTranslation = new TranslateTransition(Duration.seconds(4), spaceship);

        // Set the initial position to the left corner
        r1.setTranslateX(0);
        r1.setTranslateY(-300);
        spaceship.setTranslateX(0);
        spaceship.setTranslateY(-50);

        // Set the animation properties
        r1Translation.setToX(-200); // Move to the right end of the scene
        r1Translation.setToY(100); // Move to the right end of the scene
        spaceshipTranslation.setToX(340); // Move to the right end of the scene
        spaceshipTranslation.setToY(-50); // Move to the right end of the scene
        r1Translation.setCycleCount(1); // Play the animation once
        spaceshipTranslation.setCycleCount(1); // Play the animation once
        r1Translation.setAutoReverse(false); // Don't reverse the animation
        spaceshipTranslation.setAutoReverse(false); // Don't reverse the animation

        // Start the animations
        continuousScaling.play();
        spaceshipTranslation.play();
        r1Translation.play();
        
        // Enable the button when the animation is finished
        spaceshipTranslation.setOnFinished(event -> {
            e1.setVisible(true);
            explosionAnimation(e1);
        });
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
            App.setScene(AppUi.ROOM);
        });
    
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        e1.setVisible(false);
    }
}
