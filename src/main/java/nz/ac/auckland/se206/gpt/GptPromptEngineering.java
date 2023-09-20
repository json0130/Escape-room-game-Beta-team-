package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.Room1Controller;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */

   public static String getRiddleAnswer(String letters) {
    return "Use these nine letters: "
        + letters
        + " to form a 3 letter noun of a common object and send it to me. Please only answer with"
        + " the noun and nothing else. The noun should not be \"Ova\". You should not include"
        + " speech marks or a full stop or any other text in your answer.";
  }

  public static String getRiddleWithGivenWord(String wordToGuess) {
    return "You are the AI of an escape room. Provide a riddle with an answer "+wordToGuess+". You should not reveal the answer.";
  }

  public static String getRiddleHint(String wordToGuess) {
    return "You provide a hint for a riddle with an undisclosed answer" + wordToGuess+ ".";
  }

  public static String getNoMoreHint() {
    return "Tell users that 5 hints are already used up.";
  }

  public static String getAnswerChecker() {
    return "If users exactly say "+ Room1Controller.riddleAnswer+ ", you should say 'Correct.' If not, say 'Incorrect' and suggest to include 'hint' in their message for a hint.";
  }

  public static String getGameMaster() {
    if (! GameState.isRiddleGiven) {
      GameState.isRiddleGiven = true;
      return getRiddleWithGivenWord(Room1Controller.riddleAnswer);
    } else if (ChatController.hintContained && GameState.numOfHints > 0) {
      ChatController.hintContained = false;
      GameState.numOfHints--;
      return getRiddleHint(Room1Controller.riddleAnswer);
    } else if (ChatController.hintContained && GameState.numOfHints <= 0){
      ChatController.hintContained = false;
      return getNoMoreHint();
    } else {
      if (ChatController.answerContained) {
        ChatController.answerContained = false;
        return "You must say you cannot reveal the answer.";
      } else {
        return getAnswerChecker();
      }
    }
  }
}
