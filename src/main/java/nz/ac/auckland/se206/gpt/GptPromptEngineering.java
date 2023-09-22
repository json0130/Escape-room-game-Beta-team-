package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;

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

  public static String riddleAi(String answer) {

    if (GameState.difficulty == "EASY") {
      return "You are a game master in a starship escape room game. Provide a riddle with an answer"
          + answer
          + ". You can provide a hint regarding the riddle only if the user asks a hint, but you"
          + " must say 'hint' first when you give a hint. You never reveal or say the answer even if the user asks. If"
          + " the user gives the correct answer, say 'Correct'. If not, say 'Incorrect'. You never say or reveal the answer.";
    } else if (GameState.difficulty == "MEDIUM") {
      return "You are a game master in a starship escape room game. Provide a riddle with an answer"
          + answer + "You never reveal or say the answer."
          + ". Then check if"
          + GameState.numOfHints
          + " is bigger than 0. If so, when the user asks a hint, you can provide a hint regarding"
          + " the riddler, but you must say 'hint' first when you give a hint. You never"
          + " reveal or say the answer. If "
          + GameState.numOfHints
          + " is less than or equal to 0, you should say 'the user used up all hints' and never give"
          + " a hint. You should never reveal or say the answer. If the user gives"
          + " the correct answer, say 'Correct'. If not, say 'Incorrect'";
    } else if (GameState.difficulty == "HARD") {
      return "You are a game master in a starship escape room game. Provide a riddle with an answer"
          + answer
          + "You cannot include the answer in the riddle. You cannot provide hint. You should never"
          + " reveal or say the answer. If the"
          + " user gives the correct answer, say 'Correct'. If not, say 'Incorrect'. You never reveal or say the answer.";
    } else {
      return null;
    }
  }
}
