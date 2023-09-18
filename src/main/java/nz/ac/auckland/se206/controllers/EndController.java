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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class EndController implements Initializable {

    @FXML private Circle spaceship1;

    @FXML
    private void startAnimation(){
            spaceship1.setVisible(true);

            // Create a timeline to continuously increase the scaling factor
            Timeline continuousScaling = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(spaceship1.scaleXProperty(), 1.0)),
                    new KeyFrame(Duration.ZERO, new KeyValue(spaceship1.scaleYProperty(), 1.0)),
                    new KeyFrame(Duration.seconds(3), new KeyValue(spaceship1.scaleXProperty(), 5.0)),
                    new KeyFrame(Duration.seconds(3), new KeyValue(spaceship1.scaleYProperty(), 5.0))
            );
            continuousScaling.setCycleCount(1); // Play the animation once
            // Create a path for the spaceship1 to follow (a curve)
            Path path = new Path();
            path.getElements().addAll(
                    new MoveTo(400, 300),          // Start at the left corner
                    new CubicCurveTo(
                            -200, -100,              // Control point 1
                            1000, -700,              // Control point 2
                            1300, -1000                // End at the right end of the scene
                    )
            );
            // Create a path transition
            PathTransition spaceship1PathTransition = new PathTransition(Duration.seconds(3), path, spaceship1);
            spaceship1PathTransition.setCycleCount(1); // Play the animation once
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}

