package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class Room1Controller implements Initializable {

  private BooleanProperty wPressed = new SimpleBooleanProperty();
  private BooleanProperty aPressed = new SimpleBooleanProperty();
  private BooleanProperty sPressed = new SimpleBooleanProperty();
  private BooleanProperty dPressed = new SimpleBooleanProperty();

  private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);
  private int movementVariable = 5;
  private double shapesize;

  List<Rectangle> walls = new ArrayList<>();

  @FXML private ImageView player;
  @FXML private Pane scene;

  private double previousX;
  private double previousY;

  @FXML private Rectangle exit;
  @FXML private Rectangle wall1;
  @FXML private Rectangle wall2;
  @FXML private Rectangle blinkingRectangle;
  @FXML private Rectangle crew1Collision;
  @FXML private Rectangle crew2Collision;
  @FXML private Rectangle crew3Collision;
  @FXML private Rectangle crew4Collision;

  @FXML private Button btnCollect1;
  @FXML private Button btnCollect2;
  @FXML private Button btnCollect3;
  @FXML private Button btnCollect4;
  @FXML private Button btnRiddle;

  @FXML private ImageView crew1;
  @FXML private ImageView crew2;
  @FXML private ImageView crew3;
  @FXML private ImageView crew4;

  @FXML private ImageView idCaptain;
  @FXML private ImageView idChef;
  @FXML private ImageView idDoctor;
  @FXML private ImageView idEngineer;

  @FXML private ImageView crew1Indicator;
  @FXML private ImageView crew2Indicator;
  @FXML private ImageView crew3Indicator;
  @FXML private ImageView crew4Indicator;

  @FXML private Label difficultyLabel;
  @FXML private Label hintLabel;
  @FXML private Label hintLabel2;

  private FadeTransition fadeTransition;
  public static String riddleAnswer;
  public boolean isCrew1Colliding = false;
  public boolean isCrew2Colliding = false;
  public boolean isCrew3Colliding = false;
  public boolean isCrew4Colliding = false;

  AnimationTimer collisionTimer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          checkCollision2(player, walls);
          checkExit(player, exit);
          checkMonitor(player, wall2);
        }
      };

  AnimationTimer timer =
      new AnimationTimer() {
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

  public void initialize(URL url, ResourceBundle resource) {
    shapesize = player.getFitWidth();
    movementSetup();

    collisionTimer.start();
    
    walls.add(wall1);

    previousX = player.getLayoutX();
    previousY = player.getLayoutY();

    Random random = new Random();
    int randomNumber = random.nextInt(4);
    if (randomNumber == 0) {
      riddleAnswer = "captain";
    } else if (randomNumber == 1) {
      riddleAnswer = "doctor";
    } else if (randomNumber == 2 ) {
      riddleAnswer = "chef";
    } else if (randomNumber == 3) {
      riddleAnswer = "engineer";
    }
    // Set all id not visible
    idCaptain.setVisible(false);
    idDoctor.setVisible(false);
    idChef.setVisible(false);
    idEngineer.setVisible(false);

    keyPressed.addListener(
        ((observableValue, aBoolean, t1) -> {
          if (!aBoolean) {
            timer.start();
          } else {
            timer.stop();
          }
        }));

    fadeTransition = new FadeTransition(Duration.seconds(1), blinkingRectangle);
    fadeTransition.setFromValue(1.0);
    fadeTransition.setToValue(0.0);
    fadeTransition.setCycleCount(FadeTransition.INDEFINITE); // Blink indefinitely
    fadeTransition.setAutoReverse(true); // Reverse the animation
    // Start the animation
    fadeTransition.play();

    btnCollect1.setVisible(false);
    btnCollect2.setVisible(false);
    btnCollect3.setVisible(false);
    btnCollect4.setVisible(false);
    btnRiddle.setVisible(false);
    blinkingRectangle.setVisible(true);

    crew1Indicator.setVisible(false);
    crew2Indicator.setVisible(false);
    crew3Indicator.setVisible(false);
    crew4Indicator.setVisible(false);

    // if difficulty is selected, label is updated
    detectDifficulty();

    crewCollisionTimer.start();
  }

  // When crew1 is clicked and the riddle was resolved, id1 is shown only for 2 seconds
  @FXML
  public void clickCrew1(MouseEvent event) throws IOException {

    if (!GameState.isDoctorCollected && isCrew1Colliding) {
      idDoctor.setVisible(true);
    btnCollect1.setVisible(true);

    Thread hideImageThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(5000); // Sleep for 5 seconds
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              // id becomes invisible again
              Platform.runLater(() -> hideId1());
            });
    hideImageThread.start();
    }
  }

  private void hideId1() {
    idDoctor.setVisible(false);
    btnCollect1.setVisible(false);
    crew1Indicator.setVisible(false);
  }

  // When crew2 is clicked and the riddle was resolved, id2 is shown only for 2 seconds
  @FXML
  public void clickCrew2(MouseEvent event) throws IOException {
    if (!GameState.isCaptainCollected && isCrew2Colliding) {
      idCaptain.setVisible(true);
    btnCollect2.setVisible(true);

    Thread hideImageThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(5000); // Sleep for 5 seconds
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              // id becomes invisible again
              Platform.runLater(() -> hideId2());
            });
    hideImageThread.start();
    }
  }

  private void hideId2() {
    idCaptain.setVisible(false);
    btnCollect2.setVisible(false);
    crew2Indicator.setVisible(false);
  }

  // When crew3 is clicked and the riddle was resolved, id3 is shown only for 2 seconds
  @FXML
  public void clickCrew3(MouseEvent event) throws IOException {
    if(!GameState.isChefCollected && isCrew3Colliding) {
      idChef.setVisible(true);
    btnCollect3.setVisible(true);

    Thread hideImageThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(5000); // Sleep for 5 seconds
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              // id becomes invisible again
              Platform.runLater(() -> hideId3());
            });
    hideImageThread.start();
    }
  }

  private void hideId3() {
    idChef.setVisible(false);
    btnCollect3.setVisible(false);
    crew3Indicator.setVisible(false);
  }

  // When crew4 is clicked and the riddle was resolved, id4 is shown only for 2 seconds
  @FXML
  public void clickCrew4(MouseEvent event) throws IOException {
    if (!GameState.isEngineerCollected && isCrew4Colliding) {
      idEngineer.setVisible(true);
    btnCollect4.setVisible(true);

    Thread hideImageThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(5000); // Sleep for 5 seconds
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              // id becomes invisible again
              Platform.runLater(() -> hideId4());
            });
    hideImageThread.start();
    }
  }

  private void hideId4() {
    idEngineer.setVisible(false);
    btnCollect4.setVisible(false);
    crew4Indicator.setVisible(false);
  }

  // If collect button is pressed, id is corrected and state of idcollected changes
  public void onCollect1() {
    GameState.isDoctorCollected = true;
    hideId1();
  }
  public void onCollect2() {
    GameState.isCaptainCollected = true;
    hideId2();
  }
  public void onCollect3() {
    GameState.isChefCollected = true;
    hideId3();
  }
  public void onCollect4() {
    GameState.isEngineerCollected = true;
    hideId4();
  }

  @FXML
  public void onRiddle(ActionEvent evnet) throws IOException {
    App.setScene(AppUi.CHAT);
  }

  

  public void checkExit(ImageView player, Rectangle exit) {
        if (player.getBoundsInParent().intersects(exit.getBoundsInParent())) {
            exit.setOpacity(1);
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
            pauseTransition.setOnFinished(event -> {
                // Adjust the player's position to be right in front of the room
                player.setLayoutX(433);
                player.setLayoutY(475);
                App.setScene(AppUi.PLAYER);
                timer.stop();
            });
            pauseTransition.play();
        } else {
          exit.setOpacity(0.6);
        }
    }

    public void checkCollision2(ImageView player, List<Rectangle> walls){
      for(Rectangle wall : walls){
          if (player.getBoundsInParent().intersects(wall.getBoundsInParent())) {
              player.setLayoutX(previousX); // Restore the player's previous X position
              player.setLayoutY(previousY); // Restore the player's previous Y position
               // Exit the loop as soon as a collision is detected
          }
      }
  }

  private void checkMonitor(ImageView player, Rectangle wall2){
    if (player.getBoundsInParent().intersects(wall2.getBoundsInParent())) {
      blinkingRectangle.setOpacity(1);
      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
      pauseTransition.setOnFinished(event -> {
          // Adjust the player's position to be right in front of the room
          blinkingRectangle.setOpacity(0);
          btnRiddle.setVisible(true);
      });
      pauseTransition.play();
  } else {
    btnRiddle.setVisible(false);

  }
  }

   @FXML
  public void movementSetup() {
    scene.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.W) {
            wPressed.set(true);
          }

          if (e.getCode() == KeyCode.A) {
            aPressed.set(true);
          }

          if (e.getCode() == KeyCode.S) {
            sPressed.set(true);
          }

          if (e.getCode() == KeyCode.D) {
            dPressed.set(true);
          }
        });

    scene.setOnKeyReleased(
        e -> {
          if (e.getCode() == KeyCode.W) {
            wPressed.set(false);
          }

          if (e.getCode() == KeyCode.A) {
            aPressed.set(false);
          }

          if (e.getCode() == KeyCode.S) {
            sPressed.set(false);
          }

          if (e.getCode() == KeyCode.D) {
            dPressed.set(false);
          }
        });
  }

  public void squareBorder() {
    double left = 0;
    double right = scene.getWidth() - shapesize;
    double top = 0;
    double bottom = scene.getHeight() - shapesize;

    if (player.getLayoutX() < left) {
      player.setLayoutX(left);
    }

    if (player.getLayoutX() > right) {
      player.setLayoutX(right);
    }

    if (player.getLayoutY() < top) {
      player.setLayoutY(top);
    }

    if (player.getLayoutY() > bottom) {
      player.setLayoutY(bottom);
    }
  }

  @FXML
  private void back(ActionEvent event) {
    App.setScene(AppUi.PLAYER);
  }

  public void detectDifficulty() {
        Timer labelTimer = new Timer(true);
        labelTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (GameState.difficulty != null) {
                    if (GameState.difficulty == "MEDIUM") {
                        Platform.runLater(()-> updateLabels());
                        if (GameState.numOfHints == 0) {
                            labelTimer.cancel();
                        }
                    } else {
                        Platform.runLater(()-> updateLabels());
                        labelTimer.cancel();
                    }
                }
            }
        }, 0, 500); 
    }

    private void updateLabels() {
        difficultyLabel.setText(GameState.difficulty);
        if (GameState.difficulty == "EASY") {
            hintLabel.setText("UNLIMITED");
        } else if (GameState.difficulty == "MEDIUM") {
            hintLabel.setText(String.valueOf(GameState.numOfHints));
            hintLabel2.setText("HINTS");
            if (GameState.numOfHints == 1) {
                hintLabel2.setText("HINT");
            }
        } else {
            hintLabel.setText("NO");
        }
    }

    // Detect if the character goes closer to costumes
    AnimationTimer crewCollisionTimer =
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          // if character near costume1 and not collected, show indicator otherwise hide everything
          if (player.getBoundsInParent().intersects(crew1Collision.getBoundsInParent())) {
            isCrew1Colliding = true;
            if (!GameState.isDoctorCollected) {
              Platform.runLater(() -> crew1Indicator.setVisible(true));
            }
          } else {
            isCrew1Colliding = false;
            Platform.runLater(() -> hideId1());
          }
          // if character near costume2 and not collected, show indicator
          if (player.getBoundsInParent().intersects(crew2Collision.getBoundsInParent())) {
            isCrew2Colliding = true;
            if (!GameState.isCaptainCollected) {
              Platform.runLater(() -> crew2Indicator.setVisible(true));
            }
          } else {
            isCrew2Colliding = false;
            Platform.runLater(() -> hideId2());
          }
          // if character near costume3 and not collected, show indicator
          if (player.getBoundsInParent().intersects(crew3Collision.getBoundsInParent())) {
            isCrew3Colliding = true;
            if (!GameState.isChefCollected) {
              Platform.runLater(() -> crew3Indicator.setVisible(true));
            }
          } else {
            isCrew3Colliding = false;
            Platform.runLater(() -> hideId3());
          }
          // if character near costume4 and not collected, show indicator
          if (player.getBoundsInParent().intersects(crew4Collision.getBoundsInParent())) {
            isCrew4Colliding = true;
            if (!GameState.isEngineerCollected) {
              Platform.runLater(() -> crew4Indicator.setVisible(true));
            }
          } else {
            isCrew4Colliding = false;
            Platform.runLater(() -> hideId4());
          }
        }
      };
}
