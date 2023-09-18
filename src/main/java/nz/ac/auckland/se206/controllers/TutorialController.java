package nz.ac.auckland.se206.controllers;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
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

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;

public class TutorialController implements Initializable {

    private BooleanProperty wPressed = new SimpleBooleanProperty();
    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();

    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);

    private int movementVariable = 5;
    private double shapesize;

    List<ImageView> rocks = new ArrayList<>();

    @FXML
    private Button button;
    @FXML
    private ImageView player;
    @FXML
    private Pane scene;
    @FXML
    private Circle c1;
    @FXML
    private Circle c2;
    @FXML
    private Circle c3;
    @FXML
    private ImageView r1;
    @FXML
    private ImageView r2;
    @FXML
    private ImageView r3;

    private double previousX;
    private double previousY;
    
    AnimationTimer collisionTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
                checkCollision(player, rocks);
                checkFinish(player, c3);
        }
    };

    AnimationTimer timer = new AnimationTimer(){
        @Override
        public void handle(long now){

            previousX = player.getLayoutX(); // Update previousX
            previousY = player.getLayoutY(); // Update previousY

            if(wPressed.get()){
                player.setLayoutY(player.getLayoutY() - movementVariable);
            }
            if(aPressed.get()){
                player.setLayoutX(player.getLayoutX() - movementVariable);
            }
            if(sPressed.get()){
                player.setLayoutY(player.getLayoutY() + movementVariable);
            }
            if(dPressed.get()){
                player.setLayoutX(player.getLayoutX() + movementVariable);
            }
            squareBorder();
        }
    };

    @FXML	
    private void play(ActionEvent event){
        setRotate(c1, true, 360, 10);
        setRotate(c2, true, 180, 18);
        setRotate(c3, true, 145, 24);
    
        // Set the movement of the images and repeat it forever
        setMovement(r1, false, 3, -900, 300,2);
        setMovement(r2, false, 3, -900, 0,4);
        setMovement(r3, false, 3, -900, -300,6);
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
        // Initialise the position of the images
        r1.setLayoutX(900);
        r1.setLayoutY(60);
        r2.setLayoutX(900);
        r2.setLayoutY(260);
        r3.setLayoutX(900);
        r3.setLayoutY(500);

        shapesize = player.getFitHeight();
        movementSetup();

        rocks.add(r1);
        rocks.add(r2);
        rocks.add(r3);

        collisionTimer.start();

        previousX = player.getLayoutX();
        previousY = player.getLayoutY();

        keyPressed.addListener(((observableValue, aBoolean, t1) -> {
            if(!aBoolean){
                timer.start();
            } else {
                timer.stop();
            }
        }));
    }

    public void checkCollision(ImageView player, List<ImageView> rocks) {
        for(ImageView rock : rocks) {
            if (player.getBoundsInParent().intersects(rock.getBoundsInParent())) {
            player.setLayoutX(previousX); // Restore the player's previous X position
            player.setLayoutY(previousY); // Restore the player's previous Y position
             // Exit the loop as soon as a collision is detected
        }
        }
    }

    public void checkFinish(ImageView player, Circle c3) {
        
        if (player.getBoundsInParent().intersects(c3.getBoundsInParent())) {
            App.setScene(AppUi.ANIMATION);
            collisionTimer.stop();
            timer.stop();
        }
    }

    @FXML
    public void movementSetup(){
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.W) {
                wPressed.set(true);
            }

            if(e.getCode() == KeyCode.A) {
                aPressed.set(true);
            }

            if(e.getCode() == KeyCode.S) {
                sPressed.set(true);
            }

            if(e.getCode() == KeyCode.D) {
                dPressed.set(true);
            }
        });

        scene.setOnKeyReleased(e -> {
            if(e.getCode() == KeyCode.W) {
                wPressed.set(false);
            }

            if(e.getCode() == KeyCode.A) {
                aPressed.set(false);
            }

            if(e.getCode() == KeyCode.S) {
                sPressed.set(false);
            }

            if(e.getCode() == KeyCode.D) {
                dPressed.set(false);
            }
        });
    }

    public void squareBorder(){
        double left = 0;
        double right = scene.getWidth() - shapesize;
        double top = 0;
        double bottom = scene.getHeight() - shapesize;

        if(player.getLayoutX() < left){
            player.setLayoutX(left);
        }

        if(player.getLayoutX() > right){
            player.setLayoutX(right);
        }

        if(player.getLayoutY() < top){
            player.setLayoutY(top);
        }

        if(player.getLayoutY() > bottom){
            player.setLayoutY(bottom);
        }
    }
}

