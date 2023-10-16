package nz.ac.auckland.se206;

import java.io.IOException;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Represents the state of the game. */
public class GameState {
  public static boolean isGameStarted = false;

  public static boolean isTutorialFinished = false;

  public static boolean isPlayerInMap = true;
  public static boolean beenToMap = false;

  public static boolean isPlayerInRoom1 = false;
  public static boolean beenToRoom1 = false;

  public static boolean isPlayerInRoom2 = false;
  public static boolean beenToRoom2 = false;

  public static boolean isPlayerInRoom3 = false;
  public static boolean beenToRoom3 = false;

  /** Game state in room1 */
  public static boolean isRiddleGiven = false;

  public static boolean isRiddleResolved = false;
  public static boolean isIdCollected = false;

  /** Game state in room2 */
  public static boolean foundComputer = false;

  public static boolean isPuzzleSolved = false;
  public static boolean foundPasscode = false;

  /* Game state in room3 */
  public static boolean foundMonitor = false;
  public static boolean isPasscodeCorrect = false;
  public static boolean isIdChecked = false;

  public static boolean isGameFinished = false;

  /* password will be generated in room2 */
  public static String password = "";

  /* id cards are collected in room1 */
  public static boolean isCaptainCollected = false;
  public static boolean isChefCollected = false;
  public static boolean isDoctorCollected = false;
  public static boolean isEngineerCollected = false;

  public static boolean correctPassword = false;

  public static int numOfHints = 5;

  public static String clickedButton = "";
  public static String clickedLevelButton = "";

  public static String difficulty;

  public static boolean isSoundEnabled = true;

  public static boolean hasHappend = false;

  public static void resetGames() throws IOException {
    isGameStarted = false;
    isPlayerInMap = true;
    beenToMap = false;

    isPlayerInRoom1 = false;
    beenToRoom1 = false;

    isPlayerInRoom2 = false;
    beenToRoom2 = false;

    isPlayerInRoom3 = false;
    beenToRoom3 = false;

    /** Game state in room1 */
    isRiddleGiven = false;
    isRiddleResolved = false;
    isIdCollected = false;

    /** Game state in room2 */
    foundComputer = false;
    isPuzzleSolved = false;
    foundPasscode = false;

    /* Game state in room3 */
    foundMonitor = false;
    isPasscodeCorrect = false;
    isIdChecked = false;

    isGameFinished = false;

    /* password will be generated in room2 */
    password = "";

    /* id cards are collected in room1 */
    isCaptainCollected = false;
    isChefCollected = false;
    isDoctorCollected = false;
    isEngineerCollected = false;

    correctPassword = false;

    numOfHints = 5;

    clickedButton = "";
    clickedLevelButton = "";

    isSoundEnabled = true;

    hasHappend = false;

    // Get a new rooms and reset everything
    App.resetRooms();
    SceneManager.getScene(AppUi.INTRO);
    App.setScene(AppUi.INTRO);
  }
}
