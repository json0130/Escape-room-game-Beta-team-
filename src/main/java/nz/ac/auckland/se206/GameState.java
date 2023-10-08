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



  public static int numOfHints = 5;

  public static String difficulty;

  public static boolean isSoundEnabled = true;
}
