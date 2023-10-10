package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;
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

  public static String riddleAi(String answer) {

    return "You are the game maser in a starship escape room game. Provide a riddle"
        + " which the answer is "
        + answer
        + " in 30 words. Do not include the answer in your prompt.";
  }

  public static String checkRiddleAnswer(String message) {
    String user = "The user said " + message;
    String answer = "";

    if ((GameState.difficulty == "MEDIUM" && GameState.numOfHints <= 0)
        || GameState.difficulty == "HARD") {
          answer =
          "Do not give a hint. Do not include the answer in your prompt. If ths user is asking for"
              + " hints, say you cannot give hints. If the user asks for the answer, say you cannot"
              + " reveal the answer. If the answer is correct, start your prompt with 'Correct'. If"
              + " the answer is wrong, say incorrect.";
    } else {
      answer =
          "If the user is asking for hints, start your prompt with 'Hint' and give a hint."
              + " Otherwise, do not give a hint. If the user asks for the answer, say you cannot"
              + " reveal the answer. If the answer is correct, start your prompt with 'Correct'. If"
              + " the answer is wrong, say incorrect. Do not include the answer in your prompt.";
    }

    return user + answer;
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
        + " the map. There are three rooms where the player can visit."
        + hintAvailability
        + " Very briefly introduce yourself to the player and suggest to look around the rooms.Do"
        + " not include any information that I did not tell you.";
  }

  public static String greetingRoom1() {
    return "Now the player is in the closet room where spacesuits are hanging on the wall. Players"
        + " can collect id cards which is required to escape the starship."
        + " Tell the player to move around the room and collect an id card. ";
  }

  public static String greetingRoom2() {
    return "Now the player is in the computer room. Players need to get a passcode which is"
        + " required to escape the starship. Tell the player to move around the room and get"
        + " the passcode.";
  }

  public static String greetingRoom3() {
    return "Now the player is in the control room. Players can escape the starship through this"
        + " room using the passcode and id card. Introduce this room to the player. ";
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
