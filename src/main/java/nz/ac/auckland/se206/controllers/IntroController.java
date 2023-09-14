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
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;

public class IntroController implements Initializable {

    @FXML
    private Button startButton;
    @FXML
    private ImageView spaceship;
    @FXML
    private Circle c1;
    @FXML
    private Circle c2;
    @FXML
    private Circle c3;
    @FXML
    private ImageView background;
    @FXML
    private ImageView r2;
    @FXML
    private ImageView r3;

    private boolean animationStarted = false;

    @FXML
    private void startAnimation() { 
        // if (!animationStarted) {
        //     // Disable the button to prevent multiple clicks during animation
        //     startButton.setDisable(true);
        //     background.setVisible(false);

        //     // Create a timeline to continuously increase the scaling factor
        //     Timeline continuousScaling = new Timeline(
        //             new KeyFrame(Duration.ZERO, new KeyValue(spaceship.scaleXProperty(), 1.0)),
        //             new KeyFrame(Duration.ZERO, new KeyValue(spaceship.scaleYProperty(), 1.0)),
        //             new KeyFrame(Duration.seconds(2), new KeyValue(spaceship.scaleXProperty(), 7.0)),
        //             new KeyFrame(Duration.seconds(2), new KeyValue(spaceship.scaleYProperty(), 7.0))
        //     );
        //     continuousScaling.setCycleCount(1); // Play the animation once

        //     // Create a translate animation for the spaceship
        //     TranslateTransition spaceshipTranslation = new TranslateTransition(Duration.seconds(2), spaceship);

        //     // Set the initial position to the left corner
        //     spaceship.setTranslateX(-200);
        //     spaceship.setTranslateY(300);

        //     // Set the animation properties
        //     spaceshipTranslation.setToX(1000); // Move to the right end of the scene
        //     spaceshipTranslation.setToY(-1000); // Move to the top of the scene
        //     spaceshipTranslation.setCycleCount(1); // Play the animation once
        //     spaceshipTranslation.setAutoReverse(false); // Don't reverse the animation

        //     // Start the animations
        //     continuousScaling.play();
        //     spaceshipTranslation.play();

        //     // Enable the button when the animation is finished
        //     spaceshipTranslation.setOnFinished(event -> {
        //         startButton.setDisable(false);
        //         animationStarted = false;
        //     });

        //     animationStarted = true;
        // }

        if (!animationStarted) {
            // Disable the button to prevent multiple clicks during animation
            //startButton.setDisable(true);

            // Create a timeline to continuously increase the scaling factor
            Timeline continuousScaling = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(spaceship.scaleXProperty(), 1.0)),
                    new KeyFrame(Duration.ZERO, new KeyValue(spaceship.scaleYProperty(), 1.0)),
                    new KeyFrame(Duration.seconds(3), new KeyValue(spaceship.scaleXProperty(), 5.0)),
                    new KeyFrame(Duration.seconds(3), new KeyValue(spaceship.scaleYProperty(), 5.0))
            );
            continuousScaling.setCycleCount(1); // Play the animation once

            // Create a path for the spaceship to follow (a curve)
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
            PathTransition spaceshipPathTransition = new PathTransition(Duration.seconds(3), path, spaceship);
            spaceshipPathTransition.setCycleCount(1); // Play the animation once

            // Start the animations
            continuousScaling.play();
            spaceshipPathTransition.play();

            // Enable the button when the animation is finished
            spaceshipPathTransition.setOnFinished(event -> {
                startButton.setDisable(false);
                animationStarted = false;
            });

            animationStarted = true;
        }
    }




    private void setRotate(Circle c, boolean reverse, int angle, int duration){
        RotateTransition rt = new RotateTransition(Duration.seconds(duration), c);
        rt.setAutoReverse(reverse);
        rt.setByAngle(angle);
        rt.setDelay(Duration.seconds(0));
        rt.setRate(3);
        rt.setCycleCount(18);
        rt.play();
    }

    private void setMovement(ImageView r, boolean reverse, int duration, double endX, double endY, int delaySeconds) {
    double startX = r.getTranslateX();
    double startY = r.getTranslateY();

    Path path = new Path();
    path.getElements().add(new MoveTo(startX, startY));
    path.getElements().add(new LineTo(endX, endY));

    PathTransition pathTransition = new PathTransition();
    pathTransition.setNode(r);
    pathTransition.setPath(path);
    pathTransition.setDuration(Duration.seconds(duration));
    pathTransition.setCycleCount(reverse ? Animation.INDEFINITE : 1);
    pathTransition.setAutoReverse(reverse);

    SequentialTransition sequentialTransition = new SequentialTransition();
    sequentialTransition.getChildren().addAll(
        new PauseTransition(Duration.seconds(delaySeconds)),
        pathTransition
    );

    sequentialTransition.setOnFinished(event -> {
        // Reset the position of the ImageView to its original location
        r.setTranslateX(startX);
        r.setTranslateY(startY);

        // Start the animation again
        sequentialTransition.playFromStart();
    });

    sequentialTransition.play();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}

