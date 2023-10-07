package nz.ac.auckland.se206;

/** Represents the state of the game. */
public class GameState {

  public static boolean isPlayerInMap = true;
  public static boolean beenToMap = false;

  public static boolean isPlayerInRoom1 = false;
  public static boolean beenToRoom1 = false;

  public static boolean isPlayerInRoom2 = false;
  public static boolean beenToRoom2 = false;

  public static boolean isPlayerInRoom3 = false;
  public static boolean beenToRoom3 = false;

  public static boolean isTutorialStarted = false;

  /** Indicates whether the riddle has been resolved. */
  public static boolean foundRiddle = false;

  /** Indicates whether the riddle is given or not */
  public static boolean isRiddleGiven = false;

  public static boolean isRiddleResolved = false;
  public static boolean isIdCorrected = false;

  public static boolean isKeyFound = false;
  public static boolean foundComputer = false;
  public static boolean inComputerRoom = false;
  public static boolean isPuzzledSolved = false;
  public static boolean foundPasscode = false;

  public static boolean foundMonitor = false;
  public static boolean isPasscodeCorrect = false;
  public static boolean isIdChecked = false;

  public static boolean isGameFinished = false;

  /* password will be generated in room2 */
  public static String password = "";

  public static boolean correctPassword = false;

  /* id cards are collected in room1 */
  public static boolean isCaptainCollected = false;
  public static boolean isChefCollected = false;
  public static boolean isDoctorCollected = false;
  public static boolean isEngineerCollected = false;

  public static boolean correctId = false;

  public static int numOfHints = 5;

  public static String difficulty;
}
