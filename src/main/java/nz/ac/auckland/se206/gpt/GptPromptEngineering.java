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
    return "You are the AI of an escape room. Provide a riddle with an answer "+wordToGuess+". "
    + "You should not reveal the answer.";
  }

  public static String getRiddleHint(String wordToGuess) {
    return "You provide a hint for a riddle with an undisclosed answer" + wordToGuess+ ".";
  }

  public static String getNoMoreHint() {
    return "Tell users that 5 hints are already used up.";
  }

  public static String getAnswerChecker() {
    return "If users exactly say "+ Room1Controller.riddleAnswer+ ", you should say 'Correct.' "
    + "If not, say 'Incorrect' and suggest to include 'hint' in their message for a hint.";
  }
  
  public static String riddleAi(String answer) {

    if (GameState.difficulty == "EASY") {
      return "You are a game master in a starship escape room game. Provide a riddle with an answer"
          + answer
          + ". You can provide a hint regarding the riddle only if the user asks a hint, but you"
          + " must say 'hint' first when you give a hint. You never reveal or say the answer even"
          + " if the user asks. If the user gives the correct answer, say 'Correct'. If not, say"
          + " 'Incorrect'. You never say or reveal the answer.";
    } else if (GameState.difficulty == "MEDIUM") {
      return "You are a game master in a starship escape room game. Provide a riddle with an answer"
          + answer
          + "You never reveal or say the answer."
          + ". Then check if"
          + GameState.numOfHints
          + " is bigger than 0. If so, when the user asks a hint, you can provide a hint regarding"
          + " the riddler, but you must say 'hint' first when you give a hint. You never"
          + " reveal or say the answer. If "
          + GameState.numOfHints
          + " is less than or equal to 0, you should say 'the user used up all hints' and never"
          + " give a hint. You should never reveal or say the answer. If the user gives the correct"
          + " answer, say 'Correct'. If not, say 'Incorrect'";
    } else if (GameState.difficulty == "HARD") {
      return "You are a game master in a starship escape room game. Provide a riddle with an answer"
          + answer
          + "You cannot include the answer in the riddle. You cannot provide hint. You should never"
          + " reveal or say the answer. If the user gives the correct answer, say 'Correct'. If"
          + " not, say 'Incorrect'. You never reveal or say the answer.";
    } else {
      return null;
    }
  }

  public static String greeting() {
    String hintAvailability;
    if (GameState.difficulty == "HARD") {
      hintAvailability = "However you cannot provide any direct hints to the player.";
    } else {
      hintAvailability = "You can provide hints if the player requests.";
    }

    return "Your name is 'StarshipSaviorBot' and you are the game master of Starship Escape 1. You"
        + " were designed to help crew members in the emergency situation. Now the starship"
        + " has crashed into a meteor. Your task is to communicate with the player and help"
        + " them to complete tasks and to escape within the time limit. Now the player is in"
        + " the map. There are three rooms where control room, closet room, and computer room."
        + hintAvailability
        + " Introduce yourself to the player and suggest to look around the rooms in less than 50 words.Do"
        + " not include any information that I did not tell you.";
  }

  public static String greetingRoom1() {
    return "Now the player is in the closet room where spacesuits are hanging on the wall. Players"
        + " can collect id cards which is required to escape the starship."
        + " Tell the player to move around the room and collect an id card in less than 50 words. ";
  }

  public static String greetingRoom2() {
    return "Now the player is in the computer room. Players need to get a passcode which is"
        + " required to escape the starship. Tell the player to move around the room and get"
        + " the passcode in less than 50 words.";
  }

  public static String greetingRoom3() {
    return "Now the player is in the control room. Players can escape the starship through this"
        + " room using the passcode and id card. Introduce this room to the player in less than 50 words.";
  }

  public static String easy(String message) {

    String hint = "";
    if (GameState.isPlayerInMap) {
      if (!GameState.beenToRoom1 && !GameState.beenToRoom2 && !GameState.beenToRoom3) {
        hint = "tell to visit the computer room or the closet room first";
      } else if (!GameState.beenToRoom1) {
        hint = "tell to visit the closet room.";
      } else if (!GameState.isIdCollected) {
        hint = "tell to collect id in the closet room";
      } else if (!GameState.beenToRoom2) {
        hint = "tell to visit the computer room.";
      } else if (!GameState.foundPasscode) {
        hint = "tell to find passcode in the computer room";
      } else if (!GameState.beenToRoom3) {
        hint = "tell to visit the control room";
      } else if (!GameState.isPasscodeCorrect) {
        hint = "tell to go to the computer room again to check the passcode";
      } else if (!GameState.isIdChecked) {
        hint = "tell to check the riddle answer again and collect the correct id";
      }
    } else if (GameState.isPlayerInRoom1) {
      if (!GameState.isRiddleGiven) {
        hint = "tell to interact with the monitor in the middle of the closet room";
      } else if (!GameState.isRiddleResolved) {
        hint = "tell to solve the riddle in the monitor";
      } else if (!GameState.isIdCollected) {
        hint = "tell to interact with spacesuits hanging on the wall";
      } else if (!GameState.beenToRoom2) {
        hint = "tell to go to the computer room";
      } else if (!GameState.foundPasscode) {
        hint = "tell to go to the computer room to find the passcode";
      } else if (!GameState.beenToRoom3) {
        hint = "tell to go to the control room";
      } else if (!GameState.isPasscodeCorrect) {
        hint = "tell to check the passcode again in the computer room";
      } else if (!GameState.isIdChecked) {
        hint = "tell to check the riddle answer again and collect the correct id";
      }
    } else if (GameState.isPlayerInRoom2) {
      if (!GameState.foundComputer) {
        hint = "tell to interact with the computer";
      } else if (!GameState.isPuzzleSolved) {
        hint =
            "tell to move tiles so that the middle line mathces with the word in the side monitor";
      } else if (!GameState.foundPasscode) {
        hint = "tell to check the passcode file in the computer";
      } else if (!GameState.beenToRoom1) {
        hint = "tell to go to the closet room";
      } else if (!GameState.isIdCollected) {
        hint = "tell to go to the closet room to collect id cards";
      } else if (!GameState.beenToRoom3) {
        hint = "tell to go to the control room";
      } else if (!GameState.isPasscodeCorrect) {
        hint = "tell to check the passcode again";
      } else if (!GameState.isIdChecked) {
        hint = "tell to check the riddle answer again and collect the correct id";
      }
    } else if (GameState.isPlayerInRoom3) {
      if (!GameState.beenToRoom1) {
        hint = "tell to go to the closet room";
      } else if (!GameState.beenToRoom2) {
        hint = "tell to go to the computer room";
      } else if (!GameState.foundMonitor) {
        hint = "tell to interact with the monitor";
      } else if (!GameState.isPasscodeCorrect) {
        hint = "tell to go to the computer room and check the passcode again";
      } else if (!GameState.isIdChecked) {
        hint =
            "tell to go to the closet room and check the riddle answer again to collect the correct"
                + " id";
      }
    }

    String intro =
        "The user said"
            + message
            + ".First determine if the user is asking for hint or not. This"
            + " can be related to not knowing what to do, or asking for"
            + " what to find or asking for what is the next step. The user"
            + " may ask for what to do, and that is considered as asking for"
            + " hint. If you believe "
            + " the user asking for hint, start your prompt with 'hint: '"
            + hint
            + ".If not, just naturally respond to "
            + message
            + "and do not give a hint.";

    return intro;
  }

  public static String medium(String message) {
    String intro = "";
    if (GameState.numOfHints <= 0) {
      intro = " If you believe the" + " player is asking for hint, say you cannot give more hint. ";
    } else {
      String hint = "";
      if (GameState.isPlayerInMap) {
        if (!GameState.beenToRoom1 && !GameState.beenToRoom2 && !GameState.beenToRoom3) {
          hint = "tell to visit the computer room or the closet room first";
        } else if (!GameState.beenToRoom1) {
          hint = "tell to visit the closet room.";
        } else if (!GameState.isIdCollected) {
          hint = "tell to collect id in the closet room";
        } else if (!GameState.beenToRoom2) {
          hint = "tell to visit the computer room.";
        } else if (!GameState.foundPasscode) {
          hint = "tell to find passcode in the computer room";
        } else if (!GameState.beenToRoom3) {
          hint = "tell to visit the control room";
        } else if (!GameState.isPasscodeCorrect) {
          hint = "tell to go to the computer room again to check the passcode";
        } else if (!GameState.isIdChecked) {
          hint = "tell to check the riddle answer again and collect the correct id";
        }
      } else if (GameState.isPlayerInRoom1) {
        if (!GameState.isRiddleGiven) {
          hint = "tell to interact with the monitor in the middle of the closet room";
        } else if (!GameState.isRiddleResolved) {
          hint = "tell to solve the riddle in the monitor";
        } else if (!GameState.isIdCollected) {
          hint = "tell to interact with spacesuits hanging on the wall";
        } else if (!GameState.beenToRoom2) {
          hint = "tell to go to the computer room";
        } else if (!GameState.foundPasscode) {
          hint = "tell to go to the computer room to find the passcode";
        } else if (!GameState.beenToRoom3) {
          hint = "tell to go to the control room";
        } else if (!GameState.isPasscodeCorrect) {
          hint = "tell to check the passcode again in the computer room";
        } else if (!GameState.isIdChecked) {
          hint = "tell to check the riddle answer again and collect the correct id";
        }
      } else if (GameState.isPlayerInRoom2) {
        if (!GameState.foundComputer) {
          hint = "tell to interact with the computer";
        } else if (!GameState.isPuzzleSolved) {
          hint =
              "tell to move tiles so that the middle line mathces with the word in the side"
                  + " monitor";
        } else if (!GameState.foundPasscode) {
          hint = "tell to check the passcode file in the computer";
        } else if (!GameState.beenToRoom1) {
          hint = "tell to go to the closet room";
        } else if (!GameState.isIdCollected) {
          hint = "tell to go to the closet room to collect id cards";
        } else if (!GameState.beenToRoom3) {
          hint = "tell to go to the control room";
        } else if (!GameState.isPasscodeCorrect) {
          hint = "tell to check the passcode again";
        } else if (!GameState.isIdChecked) {
          hint = "tell to check the riddle answer again and collect the correct id";
        }
      } else if (GameState.isPlayerInRoom3) {
        if (!GameState.beenToRoom1) {
          hint = "tell to go to the closet room";
        } else if (!GameState.beenToRoom2) {
          hint = "tell to go to the computer room";
        } else if (!GameState.foundMonitor) {
          hint = "tell to interact with the monitor";
        } else if (!GameState.isPasscodeCorrect) {
          hint = "tell to go to the computer room and check the passcode again";
        } else if (!GameState.isIdChecked) {
          hint =
              "tell to go to the closet room and check the riddle answer again to collect the"
                  + " correct id";
        }
      }

      intro =
        "The user said"
            + message
            + ".First determine if the user is asking for hint or not. This"
            + " can be related to not knowing what to do, or asking for"
            + " what to find or asking for what is the next step. The user"
            + " may ask for what to do, and that is considered as asking for"
            + " hint. If you believe "
            + " the user asking for hint, start your prompt with 'hint: '"
            + hint
            + ".If not, just naturally respond to "
            + message
            + "and do not give a hint.";
    }

    return intro;
  }

  public static String hard(String message) {
    return "The user said"
        + message
        + ". Naturally respond to "
        + message
        + ". If the user is asking for hint, tell you cannot provide any hints.";
  }
}
