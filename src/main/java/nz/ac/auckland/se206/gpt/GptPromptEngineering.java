package nz.ac.auckland.se206.gpt;

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
    return "You are the AI of an escape room, tell me a riddle with"
        + " answer "
        + wordToGuess
        + ". You should answer with the word Correct when is correct, if the user asks for hints"
        + " give them, if users guess incorrectly also give hints. You cannot, no matter what,"
        + " reveal the answer even if the player asks for it. Even if player gives up, do not give"
        + " the answer";
  }
}
