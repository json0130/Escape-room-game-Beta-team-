package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.LetterGenerator;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Tile;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * Controller class for the room view. This class controls everything from dialogue, generating AI
 * responses, setting up the tiles, the movement of the tiles and the win condition of the game as
 * well as animations for different props within the scene.
 */
public class TileGameDeskController {

  public static String riddleAnswer;

  @FXML private AnchorPane root;
  @FXML private Text dialogueText;
  @FXML private Button nextDialogue;
  @FXML private Rectangle pharaoh;
  @FXML private ImageView dialogueBox;
  @FXML private ImageView openDoor;
  @FXML private ImageView closedDoor;
  @FXML private ImageView indicatorTriangle;
  @FXML private ImageView dialogueCloseButton;
  @FXML private Label timerLabel;

  private int dialogueState = 0;
  private List<String> dialogueList = new ArrayList<String>();
  private TranslateTransition translate = new TranslateTransition();
  private TextToSpeech textToSpeech = new TextToSpeech();
  private int tileClickCounter = 0;

  private Duration duration = Duration.seconds(1);
  private KeyFrame keyFrame;
  private Timeline timeline;
  private int timerSeconds = 120;

  private ChatCompletionRequest chatCompletionRequest;

  @FXML private Rectangle topLeftBox;
  @FXML private Rectangle topMiddleBox;
  @FXML private Rectangle topRightBox;
  @FXML private Rectangle middleLeftBox;
  @FXML private Rectangle middleMiddleBox;
  @FXML private Rectangle middleRightBox;
  @FXML private Rectangle bottomLeftBox;
  @FXML private Rectangle bottomMiddleBox;
  @FXML private Rectangle bottomRightBox;

  @FXML private ImageView imageOne;
  @FXML private ImageView imageTwo;
  @FXML private ImageView imageThree;
  @FXML private ImageView imageFour;
  @FXML private ImageView imageFive;
  @FXML private ImageView imageSix;
  @FXML private ImageView imageSeven;
  @FXML private ImageView imageEight;

  private Tile tileOne = new Tile();
  private Tile tileTwo = new Tile();
  private Tile tileThree = new Tile();
  private Tile tileFour = new Tile();
  private Tile tileFive = new Tile();
  private Tile tileSix = new Tile();
  private Tile tileSeven = new Tile();
  private Tile tileEight = new Tile();
  private Tile tileNine = new Tile();

  @FXML private ImageView welcomeScreen;
  @FXML private ImageView bigScreenOff;
  @FXML private ImageView smallScreenOff;
  // @FXML private Rectangle powerButton;
  @FXML private Text wordText;
  @FXML private Button releaseAirLockButton;
  @FXML private Label crewMemberName;
  @FXML private ImageView loadingGif;

  @FXML private ImageView experimentFileIcon;
  @FXML private ImageView scheduleFileIcon;
  @FXML private ImageView passcodeFileIcon;
  @FXML private Pane computerHomeScreen;
  @FXML private Pane schedulePane;
  @FXML private Pane passcodePane;
  @FXML private Pane experimentPane;

  @FXML private Rectangle passcodeFileCloseButton;
  @FXML private Rectangle scheduleFileCloseButton;
  @FXML private Rectangle experimentFileCloseButton;
  @FXML private Label computerPasscodeLabel;
  @FXML private Button loadCaptchaButton;
  @FXML private Label puzzleTutorial;
  @FXML private Pane tutorialScreen;
  @FXML private Button puzzleInfoButton;
  @FXML private Button leaveComputerButton;
  @FXML private ImageView powerButton;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws ApiProxyException
   */
  public void initialize() throws ApiProxyException {
    timerSeconds = 120;

    // dialogueList.add("WHO DARES DISTURB MY SLUMBER!?!");
    dialogueList.add("Ahhh... Another treasure hunter who wishes to steal from me!");
    dialogueList.add("The only way you can ever leave this tomb is by answering my riddle:");

    LetterGenerator letterGenerator = new LetterGenerator();
    List<String> letterList = letterGenerator.generateCombinedList();
    String lettersForInput = letterGenerator.letterListToString(letterList);
    System.out.println(lettersForInput);

    setImage(letterList);
    animateIndicator(false);

    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.4).setMaxTokens(100);

    riddleAnswer =
        runGpt(new ChatMessage("user", GptPromptEngineering.getRiddleAnswer(lettersForInput)))
            .getContent();
    System.out.println(riddleAnswer);

    wordText.setText(riddleAnswer);
  }

  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);

    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      return result.getChatMessage();
    } catch (ApiProxyException e) {

      e.printStackTrace();
      return null;
    }
  }

  @FXML
  private void setImage(List<String> letterList) {
    // Based on the list of letters generated, this method sets up the tile grid with 8 unique
    // images. Each image corresponds to a letter.
    InputStream imagePath;
    String currentLetter;

    for (int i = 0; i < letterList.size(); i++) {
      currentLetter = letterList.get(i);

      imagePath = getClass().getResourceAsStream("/images/" + currentLetter + ".png");

      switch (i) {
        case (0):
          refactorImage(imageOne, 0, 0, currentLetter, imagePath);
          tileOne.tileInitialise(imageOne, false, currentLetter, 0, 0);
          tileOne.setTilePos(null, tileTwo, tileFour, null);
          break;
        case (1):
          // ImageTwo = new ImageView(aPath);
          refactorImage(imageTwo, 121, 0, currentLetter, imagePath);
          tileTwo.tileInitialise(imageTwo, false, currentLetter, 121, 0);
          tileTwo.setTilePos(tileOne, tileThree, tileFive, null);
          break;
        case (2):
          // ImageThree = new ImageView(aPath);
          refactorImage(imageThree, 246, 0, currentLetter, imagePath);
          tileThree.tileInitialise(imageThree, false, currentLetter, 246, 0);
          tileThree.setTilePos(tileTwo, null, tileSix, null);
          break;
        case (3):
          refactorImage(imageFour, 0, 120, currentLetter, imagePath);
          tileFour.tileInitialise(imageFour, false, currentLetter, 0, 120);
          tileFour.setTilePos(null, tileFive, tileSeven, tileOne);
          break;
        case (4):
          refactorImage(imageFive, 120, 120, currentLetter, imagePath);
          tileFive.tileInitialise(imageFive, false, currentLetter, 120, 120);
          tileFive.setTilePos(tileFour, tileSix, tileEight, tileTwo);
          break;
        case (5):
          refactorImage(imageSix, 245, 120, currentLetter, imagePath);
          tileSix.tileInitialise(imageSix, false, currentLetter, 245, 120);
          tileSix.setTilePos(tileFive, null, tileNine, tileThree);
          break;
        case (6):
          refactorImage(imageSeven, 0, 245, currentLetter, imagePath);
          tileSeven.tileInitialise(imageSeven, false, currentLetter, 0, 245);
          tileSeven.setTilePos(null, tileEight, null, tileFour);
          break;
        case (7):
          refactorImage(imageEight, 120, 245, currentLetter, imagePath);
          tileEight.tileInitialise(imageEight, false, currentLetter, 120, 245);
          tileEight.setTilePos(tileSeven, tileNine, null, tileFive);

          tileNine.tileInitialise(null, true, null, 240, 240);
          tileNine.setTilePos(tileEight, null, null, tileSix);
          break;
        default:
          break;
      }
      try {
        imagePath.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  @FXML
  private List<ImageView> imagesListCreator() {
    List<ImageView> imagesList = new ArrayList<ImageView>();
    imagesList.add(imageOne);
    imagesList.add(imageTwo);
    imagesList.add(imageThree);
    imagesList.add(imageFour);
    imagesList.add(imageFive);
    imagesList.add(imageSix);
    imagesList.add(imageSeven);
    imagesList.add(imageEight);

    return imagesList;
  }

  @FXML
  private void refactorImage(
      ImageView imageView,
      double xCoordinates,
      double yCoordinates,
      String letter,
      InputStream path) {

    Image imageFile = new Image(path);
    imageView.setImage(imageFile);

    imageView.setFitWidth(130);
    imageView.setFitHeight(130);
    imageView.setId(letter);

    imageView.setLayoutY(yCoordinates);
    imageView.setLayoutX(xCoordinates);
  }

  @FXML
  private void onTileHitboxPress(Tile currentTile) {
    // Checks if the left, right, top and bottom tiles are empty. If it is empty it will swap all
    // values with the empty tile.

    if (dialogueState == 3) {
      tileClickCounter++;
      System.out.println("tile clicks: " + tileClickCounter);
    }
    // If the tile on the left is empty, swap them.
    if (currentTile.getLeftTile() != null && currentTile.getLeftTile().isFree() == true) {
      currentTile.getLeftTile().setCurrentImage(currentTile.getImage());
      currentTile.getLeftTile().setLetter(currentTile.getLetter());
      currentTile.getLeftTile().setFreeSlot(false);
      currentTile.getLeftTile().getImage().setLayoutX(currentTile.getLeftTile().getXCoordinates());
      currentTile.getLeftTile().getImage().setLayoutY(currentTile.getLeftTile().getYCoordinates());

      clearTile(currentTile);
      System.out.println("Moved Left");
      soundTileClick();
      // If the tile on the Right is empty, swap them.
    } else if (currentTile.getRightTile() != null && currentTile.getRightTile().isFree() == true) {
      currentTile.getRightTile().setCurrentImage(currentTile.getImage());
      currentTile.getRightTile().setLetter(currentTile.getLetter());
      currentTile.getRightTile().setFreeSlot(false);
      currentTile
          .getRightTile()
          .getImage()
          .setLayoutX(currentTile.getRightTile().getXCoordinates());
      currentTile
          .getRightTile()
          .getImage()
          .setLayoutY(currentTile.getRightTile().getYCoordinates());

      clearTile(currentTile);
      System.out.println("Moved Right");
      soundTileClick();
      // If the tile on the bottom is empty, swap them.
    } else if (currentTile.getBottomTile() != null
        && currentTile.getBottomTile().isFree() == true) {
      currentTile.getBottomTile().setCurrentImage(currentTile.getImage());
      currentTile.getBottomTile().setLetter(currentTile.getLetter());
      currentTile.getBottomTile().setFreeSlot(false);
      currentTile
          .getBottomTile()
          .getImage()
          .setLayoutX(currentTile.getBottomTile().getXCoordinates());
      currentTile
          .getBottomTile()
          .getImage()
          .setLayoutY(currentTile.getBottomTile().getYCoordinates());

      clearTile(currentTile);
      System.out.println("Moved Bottom");
      soundTileClick();
      // If the tile on the Top is empty, swap them.
    } else if (currentTile.getTopTile() != null && currentTile.getTopTile().isFree() == true) {
      currentTile.getTopTile().setCurrentImage(currentTile.getImage());
      currentTile.getTopTile().setLetter(currentTile.getLetter());
      currentTile.getTopTile().setFreeSlot(false);
      currentTile.getTopTile().getImage().setLayoutX(currentTile.getTopTile().getXCoordinates());
      currentTile.getTopTile().getImage().setLayoutY(currentTile.getTopTile().getYCoordinates());

      clearTile(currentTile);
      soundTileClick();

    } else {
      return;
    }
    System.out.println("Moved Top");

    // Checks if swapping the tiles causes the game to be over and the player to have won.
    checkIfWon();
  }

  private void clearTile(Tile tile) {
    tile.setCurrentImage(null);
    tile.setLetter(null);
    tile.setFreeSlot(true);
  }

  @FXML
  private void topLeftHitboxPress() {
    System.out.println("click");
    onTileHitboxPress(tileOne);
  }

  @FXML
  private void topMiddleHitboxPress() {
    System.out.println("click");
    onTileHitboxPress(tileTwo);
  }

  @FXML
  private void topRightHitboxPress() {
    System.out.println("click");
    onTileHitboxPress(tileThree);
  }

  @FXML
  private void middleLeftHitboxPress() {
    System.out.println("click");
    onTileHitboxPress(tileFour);
  }

  @FXML
  private void middleMiddleHitboxPress() {
    System.out.println("click");
    onTileHitboxPress(tileFive);
  }

  @FXML
  private void middleRightHitboxPress() {
    System.out.println("click");
    onTileHitboxPress(tileSix);
  }

  @FXML
  private void bottomLeftHitboxPress() {
    System.out.println("click");
    onTileHitboxPress(tileSeven);
  }

  @FXML
  private void bottomMiddleHitboxPress() {
    System.out.println("click");
    onTileHitboxPress(tileEight);
  }

  @FXML
  private void bottomRightHitboxPress() {
    System.out.println("click");
    onTileHitboxPress(tileNine);
  }

  private void checkIfWon() {
    if (riddleAnswer != null) {
      String firstChar = String.valueOf(riddleAnswer.charAt(0));
      String secondChar = String.valueOf(riddleAnswer.charAt(1));
      String thirdChar = String.valueOf(riddleAnswer.charAt(2));
      System.out.println(tileFour.getLetter() + tileFive.getLetter() + tileSix.getLetter());
      System.out.println(
          firstChar.toUpperCase() + secondChar.toUpperCase() + thirdChar.toUpperCase());
      if (tileFour.getLetter() != null
          && tileFive.getLetter() != null
          && tileSix.getLetter() != null
          && tileFour.getLetter().equalsIgnoreCase(firstChar)
          && tileFive.getLetter().equalsIgnoreCase(secondChar)
          && tileSix.getLetter().equalsIgnoreCase(thirdChar)) {
        System.out.println("done");

        disableImages();

        // try {
        //   Thread.sleep(1000);
        // } catch (InterruptedException e) {
        //   // TODO Auto-generated catch block
        //   e.printStackTrace();
        // }

        showHomeScreen();
      }
    }
  }

  @FXML
  private void onOpenDoorClick() throws IOException {
    if (timeline != null) {
      timeline.stop();
    }
    App.setRoot("winnerscreen");
  }

  @FXML
  private void animateIndicator(boolean rotated) {
    translate.setNode(indicatorTriangle);
    translate.setDuration(Duration.millis(1000));
    translate.setCycleCount(TranslateTransition.INDEFINITE);

    if (rotated) {
      translate.setByY(0);
      translate.setByX(20);
    } else {
      translate.setByX(0);
      translate.setByY(20);
    }
    translate.setAutoReverse(true);
    translate.play();
  }

  private String timerFormat(int seconds) {
    int min = seconds / 60;
    int remainingSecs = seconds % 60;
    return String.format("%02   d:%02d", min, remainingSecs);
  }

  private void sayLine() {
    Task<Void> sayLine =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            textToSpeech.setInterupt();
            textToSpeech.speak(dialogueText.getText());
            // textToSpeech.terminate();
            return null;
          }
        };
    Thread sayLineThread = new Thread(sayLine, "say third Line");
    sayLineThread.start();
  }

  public String getRiddleAnswer() {
    return riddleAnswer;
  }

  @FXML
  private void onPowerButtonPressed() {

    bigScreenOff.setVisible(false);
    loadCaptchaButton.setVisible(true);
    System.out.println("hi");
    smallScreenOff.setVisible(false);
  }

  @FXML
  private void onReleaseAirLockButtonClick() {
    App.tileGameComplete = true;
    System.out.println("Tile Game Complete");
  }

  @FXML
  private void disableImages() {
    imageOne.setVisible(false);
    imageTwo.setVisible(false);
    imageThree.setVisible(false);
    imageFour.setVisible(false);
    imageFive.setVisible(false);
    imageSix.setVisible(false);
    imageSeven.setVisible(false);
    imageEight.setVisible(false);
  }

  // @FXML
  // private void showWelcomeScreen() {
  //   welcomeScreen.setVisible(true);
  //   crewMemberName.setVisible(true);
  //   loadingGif.setVisible(true);
  // }

  @FXML
  private void showHomeScreen() {
    computerHomeScreen.setVisible(true);
  }

  @FXML
  private void onScheduleFileClick() {
    schedulePane.setVisible(true);
    System.out.println("DONE3");
  }

  @FXML
  private void onPasscodeFileClick() {
    int currentPasscode = App.passcode;
    String stringPasscode = Integer.toString(currentPasscode);
    computerPasscodeLabel.setText(stringPasscode);
    passcodePane.setVisible(true);

    System.out.println("DONE1");
  }

  @FXML
  private void onExperimentFileClick() {
    experimentPane.setVisible(true);
    System.out.println("DONE2");
  }

  @FXML
  private void onPasscodeFileCloseButtonClick() {
    passcodePane.setVisible(false);
  }

  @FXML
  private void onscheduleFileCloseButtonClick() {
    schedulePane.setVisible(false);
  }

  @FXML
  private void onexperimentFileCloseButtonClick() {
    experimentPane.setVisible(false);
  }

  @FXML
  private void onCaptchaButtonClick() {
    // welcomeScreen.setVisible(false);
    // puzzleTutorial.setVisible(false);
    // loadCaptchaButton.setVisible(false);
    tutorialScreen.setVisible(false);
    loadCaptchaButton.setVisible(false);
  }

  @FXML
  private void onpuzzleInfoButtonClick() {
    tutorialScreen.setVisible(true);
    loadCaptchaButton.setVisible(true);
  }

  @FXML
  private void onPuzzleGoBackClick() {
    String musicFile;
    App.setScene(AppUi.TILEROOM);

    if (App.timerSeconds < 60) {
      musicFile = "src/main/resources/sounds/final-BG-MUSIC.mp3";
      App.musicType = "final";
    } else {
      musicFile = "src/main/resources/sounds/Background-Music.mp3";
    }
    Media media = new Media(new File(musicFile).toURI().toString());

    App.mediaPlayer.stop();
    App.mediaPlayer = new MediaPlayer(media);
    App.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    App.mediaPlayer.setVolume(0.1);
    App.mediaPlayer.setAutoPlay(true);
    System.out.println("click");
  }

  @FXML
  private void soundTileClick() {
    String soundEffect = "src/main/resources/sounds/tile-move.mp3";
    Media media = new Media(new File(soundEffect).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setAutoPlay(true);
  }
}
