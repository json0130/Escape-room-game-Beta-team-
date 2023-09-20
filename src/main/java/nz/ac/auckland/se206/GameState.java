package nz.ac.auckland.se206;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  /* password will be generated in room2 */
  public static String password = "1234";

  public static boolean correctPassword = false;

  /* id cards are collected in room1 */
  public static boolean isCaptainCollected = false;
  public static boolean isChefCollected = false;
  public static boolean isDoctorCollected = false;
  public static boolean isEngineerCollected = false;

  public static boolean correctId = false;

  public static String answerOfRiddle = "";
  public static boolean isTutorialStarted = false;
  /** Indicates whether the riddle is given or not */
  public static boolean isRiddleGiven = false;

  public static int numOfHints = 5;
}
