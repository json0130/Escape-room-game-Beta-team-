package nz.ac.auckland.se206;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  public static boolean isTutorialStarted = false;
  /** Indicates whether the riddle is given or not */
  public static boolean isRiddleGiven = false;

  public static int numOfHints = 5;
}
