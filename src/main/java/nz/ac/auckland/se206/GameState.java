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

  public static boolean isRiddleGiven = false;

  public static boolean isRiddleResolved = false;
  public static boolean isIdCollected = false;

  public static boolean foundComputer = false;

  public static boolean isPuzzleSolved = false;
  public static boolean foundPasscode = false;

  public static boolean foundMonitor = false;
  public static boolean isPasscodeCorrect = false;
  public static boolean isIdChecked = false;

  public static boolean isGameFinished = false;

  public static String password = "";

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

  /**
   * Reset the game state to the initial state.
   *
   * @throws IOException if the game cannot be reset
   */
  public static void resetGames() throws IOException {
    // Player is initially in the map
    isGameStarted = false;
    isPlayerInMap = true;
    beenToMap = false;
    // Player never been to room1 and not in the room1
    isPlayerInRoom1 = false;
    beenToRoom1 = false;
    // Player never been to room2 and not in the room2
    isPlayerInRoom2 = false;
    beenToRoom2 = false;
    // Player never beent room3 and not in the room3
    isPlayerInRoom3 = false;
    beenToRoom3 = false;

    // All the games states are false initially
    isRiddleGiven = false;
    isRiddleResolved = false;
    isIdCollected = false;

    foundComputer = false;
    isPuzzleSolved = false;
    foundPasscode = false;

    foundMonitor = false;
    isPasscodeCorrect = false;
    isIdChecked = false;

    isGameFinished = false;
    // Reset the password
    password = "";

    isCaptainCollected = false;
    isChefCollected = false;
    isDoctorCollected = false;
    isEngineerCollected = false;

    correctPassword = false;
    // number of hint available is 5 intially
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
