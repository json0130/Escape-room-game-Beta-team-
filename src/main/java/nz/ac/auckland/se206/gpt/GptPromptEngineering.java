package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {
    return "You are the AI of an escape room. Please provide a riddle with an answer "+wordToGuess+". You can offer"
               + " hints and assistance but should not reveal the answer. When the user provides"
               + " the correct answer "+wordToGuess+", respond with 'Correct.'";
  }
}
