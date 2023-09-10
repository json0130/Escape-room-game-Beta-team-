package nz.ac.auckland.se206.controllers;

import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import java.net.URL;

public class PlayerController implements Initializable{
    
    private BooleanProperty wPressed = new SimpleBooleanProperty();
    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();

    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);

    private int movementVariable = 2;
    private double shapesize;

    @FXML
    private Circle player;

    @FXML
    private Rectangle shape1;

    @FXML
    private Pane scene;

    @FXML
    private Button button;

    @FXML
    void start(ActionEvent event){
        player.setLayoutX(200);
        player.setLayoutY(200);
    }

    AnimationTimer collisionTimer = new AnimationTimer(){
        @Override
        public void handle(long now){
            checkCollision(player,shape1);
        }
    };

    AnimationTimer timer = new AnimationTimer(){
        @Override
        public void handle(long now){
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button.setVisible(false);
        shapesize = player.getRadius() * 2;
        movementSetup();
        collisionTimer.start();

        keyPressed.addListener(((observableValue, aBoolean, t1) -> {
            if(!aBoolean){
                timer.start();
            } else {
                timer.stop();
            }
        }));
    }

    public void checkCollision(Circle player, Rectangle shape1){
        if(player.getBoundsInParent().intersects(shape1.getBoundsInParent())){
            button.setVisible(true);
        }
    }

    public void buttonPressed(KeyEvent event){
        // if button is pressed then it moves to the other room
        
    }

    @FXML
    public void movementSetup(){
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.W) {
                wPressed.set(true);
                player.setLayoutY(player.getLayoutY() - movementVariable);
                System.out.println(e.getCode());
            }

            if(e.getCode() == KeyCode.A) {
                aPressed.set(true);
                System.out.println(e.getCode());
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
                player.setLayoutY(player.getLayoutY() - movementVariable);
                System.out.println(e.getCode());
            }

            if(e.getCode() == KeyCode.A) {
                aPressed.set(false);
                System.out.println(e.getCode());
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
