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

    return "Your name is 'WALL-E' and you are the game maser in a starship escape room game."
        + " Introduce yourself to the player and provide a riddle which the answer is ."
        + answer
        + " in 50 words. Do not include the answer in your prompt.";
  }

  public static String checkRiddleAnswer(String message) {
    // Inform gpt about what the user just said
    String user = "The user said " + message;
    // Depending on the difficulty the player chose, hint availability differes
    String answer = "";

    if (message.toLowerCase().contains(Room1Controller.riddleAnswer)) {
      return message
          + "If the user is guessing the answer and you think it's the valid answer, start your"
          + " prompt with 'Correct'.";
    }
    if ((GameState.difficulty == "MEDIUM" && GameState.numOfHints <= 0)
        || GameState.difficulty == "HARD") {
      answer =
          "Determine if the user is asking for help or asking for answer or quessing the answer or"
              + " just want to talk to you. Guessing includes sending a single answer. If ths user"
              + " is asking for help whether directly or indirectly, say you cannot give hints and"
              + " do not give a hint and do not inclue the answer in your prompt. If the user asks"
              + " for the answer, say you cannot reveal the answer and do not give a hint and do"
              + " not include the answer in your prompt. If the user is guessing the answer, you"
              + " must start you prompt with 'Incorrect' whatever the guess is. Do not include the"
              + " answer in your prompt and do not give a hint when the user is guessing the"
              + " answer. Otherwise, naturally respond to "
              + message
              + ", but do not give a hint.";
    } else {
      answer =
          "Determine if the user is asking for help or asking for answer or quessing the answer or"
              + " just want to talk to you. Guessing includes sending a single answer. If the user"
              + " is asking for help whether directly or indirectly, you must start your prompt"
              + " with 'Hint' and give a hint, but do not include the answer in your prompt. If the"
              + " user asks for the answer, say you cannot reveal the answer, but do not give a"
              + " hint and do not include the answer in your prompt. If the user is guessing the"
              + " answer, you must start you prompt with 'Incorrect' whatever the guess is. Do not"
              + " include the answer in your prompt and do not give a hint when the user is"
              + " guessing the answer. Otherwise, naturally respond to "
              + message
              + ", but do not give a hint.";
    }
    // Send the final message to the gpt
    return user + answer;
  }

  public static String greeting() {
    // Prompt gpt to introduce itself and the rooms where player needs to visit to escape
    return "Your name is 'EVA' and you are the game master of Starship Escape 1. You were designed"
        + " to help crew members which is the user in the emergency situation. Now the"
        + " starship has crashed into a meteor. Your task is to communicate with the player"
        + " and help them to complete tasks and to escape within the time limit. Now the"
        + " player is in the map. There are three rooms that are closet room, computer room,"
        + " and control room, where the player can visit."
        + " Introduce yourself to the player and suggest to look around the rooms in less than 50"
        + " words.";
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
        + " room using the passcode and id card. Introduce this room to the player in less"
        + " than 50 words.";
  }

  public static String settingEasy(String message) {
    // check the player's progress and prepare proper hint
    String hint = "";
    // hint when the player is in the map
    if (GameState.isPlayerInMap) {
      if (!GameState.beenToRoom1 && !GameState.beenToRoom2 && !GameState.beenToRoom3) {
        hint = "tell the user to visit the computer room or the closet room to complete tasks";
      } else if (!GameState.beenToRoom1) {
        hint = "tell the user to visit the closet room.";
      } else if (!GameState.isIdCollected) {
        hint = "tell the user to collect id in the closet room";
      } else if (!GameState.beenToRoom2) {
        hint = "tell the user to visit the computer room.";
      } else if (!GameState.foundPasscode) {
        hint = "tell the user to find passcode in the desktop in the computer room";
      } else if (!GameState.beenToRoom3) {
        hint = "tell the user to visit the control room";
      } else if (!GameState.isPasscodeCorrect) {
        hint = "tell the user to go to the computer room again to check the passcode";
      } else if (!GameState.isIdChecked) {
        hint = "tell the user to check the riddle answer again and collect the correct id";
      }
      // hint when the player is in room1
    } else if (GameState.isPlayerInRoom1) {
      if (!GameState.isRiddleGiven) {
        hint = "tell the user to interact with the monitor in the middle of the closet room";
      } else if (!GameState.isRiddleResolved) {
        hint = "tell the user to solve the riddle in the monitor";
      } else if (!GameState.isIdCollected) {
        hint = "tell the user to interact with spacesuits hanging on the wall";
      } else if (!GameState.beenToRoom2) {
        hint = "tell the user to go to the computer room";
      } else if (!GameState.foundPasscode) {
        hint = "tell the user to go to the computer room to find the passcode";
      } else if (!GameState.beenToRoom3) {
        hint = "tell the user to go to the control room";
      } else if (!GameState.isPasscodeCorrect) {
        hint = "tell the user to check the passcode again in the computer room";
      } else if (!GameState.isIdChecked) {
        hint = "tell the user to check the riddle answer again and collect the correct id";
      }
      // hint when the player is in room2
    } else if (GameState.isPlayerInRoom2) {
      if (!GameState.foundComputer) {
        hint = "tell the user to interact with the computer";
      } else if (!GameState.isPuzzleSolved) {
        hint =
            "tell the user to move tiles so that the middle line mathces with the word in the side"
                + " monitor";
      } else if (!GameState.foundPasscode) {
        hint = "tell the user to check the passcode file in the computer";
      } else if (!GameState.beenToRoom1) {
        hint = "tell the user to go to the closet room";
      } else if (!GameState.isIdCollected) {
        hint = "tell the user to go to the closet room to collect id cards";
      } else if (!GameState.beenToRoom3) {
        hint = "tell the user to go to the control room";
      } else if (!GameState.isPasscodeCorrect) {
        hint = "tell the user to check the passcode again";
      } else if (!GameState.isIdChecked) {
        hint = "tell the user to check the riddle answer again and collect the correct id";
      }
      // hint when the player is in room3
    } else if (GameState.isPlayerInRoom3) {
      if (!GameState.beenToRoom1) {
        hint = "tell the user to go to the closet room";
      } else if (!GameState.beenToRoom2) {
        hint = "tell the user to go to the computer room";
      } else if (!GameState.foundMonitor) {
        hint = "tell the user to interact with the monitor";
      } else if (!GameState.isPasscodeCorrect) {
        hint = "tell the user to go to the computer room and check the passcode again";
      } else if (!GameState.isIdChecked) {
        hint =
            "tell the user to go to the closet room and check the riddle answer again to collect"
                + " the correct id";
      }
    }

    // Read player's input and let gpt to prepare proper respond to that
    String intro =
        "The user said"
            + message
            + ". First determine if the user is asking for hint or not. This"
            + " can be related to not knowing what to do, or asking for"
            + " what to find or asking for what is the next step. The user"
            + " may ask for what to do, and that is considered as asking for"
            + " hint. If you believe the user asking for hint, then start your prompt with 'Hint: '"
            + "and based on this"
            + hint
            + " give a single hint to user and do not give the same hint everytime"
            + ".If not, just naturally respond to "
            + message
            + "and do not give a hint.Most importantly make your response simple and consise like 1"
            + " or 2 sentences. Do not include any extra";

    return intro;
  }

  public static String settingMedium(String message) {
    String intro = "";
    // If the player used up all hints, no hints are available
    if (GameState.numOfHints <= 0) {
      intro =
          " Since player used all of the hint, so if you believe the"
              + " player is asking for hint, say you cannot give more hint. ";
    } else {
      // Check the player's progress and prepare proper hint
      String hint = "";
      // hint when the player is in map
      if (GameState.isPlayerInMap) {
        if (!GameState.beenToRoom1 && !GameState.beenToRoom2 && !GameState.beenToRoom3) {
          hint = "tell the user to visit the computer room or the closet room first";
        } else if (!GameState.beenToRoom1) {
          hint = "tell the user to visit the closet room.";
        } else if (!GameState.isIdCollected) {
          hint = "tell the user to collect id in the closet room";
        } else if (!GameState.beenToRoom2) {
          hint = "tell the user to visit the computer room.";
        } else if (!GameState.foundPasscode) {
          hint = "tell the user to find passcode in the computer room";
        } else if (!GameState.beenToRoom3) {
          hint = "tell the user to visit the control room";
        } else if (!GameState.isPasscodeCorrect) {
          hint = "tell the user to go to the computer room again to check the passcode";
        } else if (!GameState.isIdChecked) {
          hint = "tell the user to check the riddle answer again and collect the correct id";
        }
        // hint when the player is in room
      } else if (GameState.isPlayerInRoom1) {
        if (!GameState.isRiddleGiven) {
          hint = "tell the user to interact with the monitor in the middle of the closet room";
        } else if (!GameState.isRiddleResolved) {
          hint = "tell the user to solve the riddle in the monitor";
        } else if (!GameState.isIdCollected) {
          hint = "tell the user to interact with spacesuits hanging on the wall";
        } else if (!GameState.beenToRoom2) {
          hint = "tell the user to go to the computer room";
        } else if (!GameState.foundPasscode) {
          hint = "tell the user to go to the computer room to find the passcode";
        } else if (!GameState.beenToRoom3) {
          hint = "tell the user to go to the control room";
        } else if (!GameState.isPasscodeCorrect) {
          hint = "tell the user to check the passcode again in the computer room";
        } else if (!GameState.isIdChecked) {
          hint = "tell the user to check the riddle answer again and collect the correct id";
        }
        // hint when the player is in room2
      } else if (GameState.isPlayerInRoom2) {
        if (!GameState.foundComputer) {
          hint = "tell the user to interact with the computer";
        } else if (!GameState.isPuzzleSolved) {
          hint =
              "tell the user to move tiles so that the middle line mathces with the word in the"
                  + " side monitor";
        } else if (!GameState.foundPasscode) {
          hint = "tell the user to check the passcode file in the computer";
        } else if (!GameState.beenToRoom1) {
          hint = "tell the user to go to the closet room";
        } else if (!GameState.isIdCollected) {
          hint = "tell the user to go to the closet room to collect id cards";
        } else if (!GameState.beenToRoom3) {
          hint = "tell the user to go to the control room";
        } else if (!GameState.isPasscodeCorrect) {
          hint = "tell the user to check the passcode again";
        } else if (!GameState.isIdChecked) {
          hint = "tell the user to check the riddle answer again and collect the correct id";
        }
        // hint when the player is in room3
      } else if (GameState.isPlayerInRoom3) {
        if (!GameState.beenToRoom1) {
          hint = "tell the user to go to the closet room";
        } else if (!GameState.beenToRoom2) {
          hint = "tell the user to go to the computer room";
        } else if (!GameState.foundMonitor) {
          hint = "tell the user to interact with the monitor";
        } else if (!GameState.isPasscodeCorrect) {
          hint = "tell the user to go to the computer room and check the passcode again";
        } else if (!GameState.isIdChecked) {
          hint =
              "tell the user to go to the closet room and check the riddle answer again to collect"
                  + " the correct id";
        }
      }
      // Read player's input and let gpt to prepare proper respond to that
      intro =
          "The user said"
              + message
              + ". First determine if the user is asking for hint or not. This can be related to"
              + " not knowing what to do, or asking for what to find or asking for what is the next"
              + " step. The user may ask for what to do, and that is considered as asking for hint."
              + " If you believe the user asking for hint, then start your prompt with 'Hint: 'and"
              + " based on this"
              + hint
              + " provide a single hint to user and do not give the same hint everytime and do not"
              + " give multiple hints at once If users ask for multiple hints at once then say"
              + " sorry and only give one hint at a time.If not, just naturally respond to "
              + message
              + "and do not give a hint. Most importantly make your response simple and consise"
              + " like 1 or 2 sentences. Do not include any extra";
    }
    System.out.println("print1");
    return intro;
  }

  public static String settingHard(String message) {
    // Return the message explains the current situation but cannot provide hint to the user
    return "The user said"
        + message
        + ". you should naturally respond to user and do not give any hint. If the user is asking"
        + " for hint, tell you cannot provide any hints. If the user is keep asking for hint, still"
        + " you cannot provide any hint and tell you cannot provide any hints. When you answer to"
        + " the question make sure your response simple and consise like 1 or 2 sentences. Do not"
        + " include any extra";
  }
}
