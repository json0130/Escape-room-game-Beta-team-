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
        + " the map. There are three rooms where the player can visit."
        + hintAvailability
        + " Introduce yourself to the player and suggest to look around the rooms.";
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

  public static String easy1(String message) {
    return "The user is asking for help regarding the following request: "
        + message
        + " First determine if the user is asking for help or not. This"
        + " help can be related to not knowing what to do, or asking for"
        + " what to find or asking for what is the next step. The user"
        + " may ask for what to do, and that is considered asking for"
        + " help. If you think the user is not asking for a hint or help,"
        + " reply back to their message. However, if you believe the user"
        + " is asking for help, start your prompt with \"hint: \".. If"
        + " the user's request is related to not knowing what to do, if "
        + GameState.isPlayerInMap
        + " is true and "
        + GameState.isRiddleResolved
        + " is false or if "
        + GameState.isIdCollected
        + " is false, tell them to go to the closet room, if "
        + GameState.foundPasscode
        + " is false, tell them to go to the computer room. if not tell"
        + " them to go to the control room.If the user's request is"
        + " related to not knowing what to do, if "
        + GameState.isPlayerInRoom1
        + " is true and if"
        + GameState.isRiddleResolved
        + " is false, tell them to solve the riddle by putting the books,"
        + " if "
        + GameState.isIdCollected
        + " is false, tell them to try find the space suits in the room,"
        + " if not tell them they can leave this room.If the user's"
        + " request is related to not knowing what to do, if "
        + GameState.isPlayerInRoom2
        + " is true and if "
        + GameState.foundComputer
        + " is false, tell them to move around the room and find the"
        + " computer, if "
        + GameState.isPuzzleSolved
        + " is false, tell them to solve the puzzle by clicking the tile,"
        + " if "
        + GameState.foundPasscode
        + " is false, tell them to find the passcode file in the computer"
        + " screen, if not tell them they can leave this room.If the"
        + " user's request is related to not knowing what to do, if "
        + GameState.isPlayerInRoom3
        + " is true and if "
        + GameState.isRiddleResolved
        + " is false, tell them to go to closet room and solve riddle, if"
        + " "
        + GameState.isIdCollected
        + " is false, tell them to go to closet room and check the space"
        + " suits, if "
        + GameState.foundPasscode
        + " is false, tell them to go to computer room and find the"
        + " passcode.If the user's request is related to not knowing what"
        + " to do, if "
        + GameState.isPlayerInRoom3
        + " is true and if "
        + GameState.foundMonitor
        + " is false, tell them to move around the room and find the"
        + " monitor, if "
        + GameState.isPasscodeCorrect
        + " is false, tell them to enter the passcode in the monitor, if "
        + GameState.isIdChecked
        + " is false, tell them to check the id cards in the ID scanner,"
        + " if not tell them they. Also, only write down the answers- do"
        + " not talk about what the user has typed in.";
  }

  public static String medium1(String message) {
    return "The user's message is:"
        + message
        + "First determine if the user is asking for help or not. This"
        + " help can be related to not knowing what to do, or asking for"
        + " what to find or asking for what is the next step. The user"
        + " may ask for what to do, and that is considered a asking for"
        + " help. If you think the user is not asking for a hint or help,"
        + " reply back to their message. However, if you believe it is"
        + " the user asking for help, start your prompt with \"hint: \".."
        + " If the user's request is related to not knowing what to do,"
        + " if"
        + GameState.isPlayerInMap
        + " is true and"
        + GameState.isRiddleResolved
        + " is false or if"
        + GameState.isIdCollected
        + " is false, tell them to go to the closet room, if"
        + GameState.foundPasscode
        + " is false, tell them to go to the computer room."
        + " if not tell them to go to the control room."
        + "If the user's request is related to not knowing what to do, if"
        + GameState.isPlayerInRoom1
        + " is true and if"
        + GameState.isRiddleResolved
        + " is false, tell them to solve the riddle by putting the books,"
        + " if"
        + GameState.isIdCollected
        + " is false, tell them to try find the space suits in the room,"
        + " if not tell them they can leave this room.If the user's"
        + " request is related to not knowing what to do, if"
        + GameState.isPlayerInRoom2
        + " is true and if"
        + GameState.foundComputer
        + " is false, tell them to move around the room and find the"
        + " computer, if"
        + GameState.isPuzzleSolved
        + " is false, tell them to solve the puzzle by clicking the tile,"
        + " if"
        + GameState.foundPasscode
        + " is false, tell them to find the passcode file in the computer"
        + " screen, if not tell them they can leave this room.If the"
        + " user's request is related to not knowing what to do, if"
        + GameState.isPlayerInRoom3
        + " is true and if"
        + GameState.isRiddleResolved
        + " is false, tell them to go to closet room and solve riddle, if"
        + GameState.isIdCollected
        + " is false, tell them to go to closet room and check the space"
        + " suits, if"
        + GameState.foundPasscode
        + " is false, tell them to go to computer room and find the"
        + " passcode.If the user's request is related to not knowing what"
        + " to do, if"
        + GameState.isPlayerInRoom3
        + " is true and if"
        + GameState.foundMonitor
        + " is false, tell them to move around the room and find the"
        + " monitor, if"
        + GameState.isPasscodeCorrect
        + " is false, tell them to enter the passcode in the monitor, if"
        + GameState.isIdChecked
        + " is false, tell them to check the id cards in the ID scanner,"
        + " if not tell them they. Also, only write down the answers- do"
        + " not talk about what the user has typed in.";
  }

  public static String hard1(String message) {
    return "The user's message is: "
        + message
        + "First determine if the user is asking for help or not. This"
        + " help can be related to not knowing what to do, or asking for"
        + " what to find or asking for what is the next step. The user"
        + " may ask for what to do, and that is considered a asking for"
        + " help. If you think the user is not asking for a hint or help,"
        + " reply back to their message. However, if you believe it is"
        + " the user asking for help, start your prompt with \"hint: \".."
        + " If the user's request is related to not knowing what to do,"
        + " if"
        + GameState.isPlayerInMap
        + " is true and"
        + GameState.isRiddleResolved
        + " is false or if"
        + GameState.isIdCollected
        + " is false, tell them to go to the closet room, if"
        + GameState.foundPasscode
        + " is false, tell them to go to the computer room."
        + " if not tell them to go to the control room."
        + "If the user's request is related to not knowing what to do, if"
        + GameState.isPlayerInRoom1
        + " is true and if"
        + GameState.isRiddleResolved
        + " is false, tell them to solve the riddle by putting the books,"
        + " if"
        + GameState.isIdCollected
        + " is false, tell them to try find the space suits in the room,"
        + " if not tell them they can leave this room.If the user's"
        + " request is related to not knowing what to do, if"
        + GameState.isPlayerInRoom2
        + " is true and if"
        + GameState.foundComputer
        + " is false, tell them to move around the room and find the"
        + " computer, if"
        + GameState.isPuzzleSolved
        + " is false, tell them to solve the puzzle by clicking the tile,"
        + " if"
        + GameState.foundPasscode
        + " is false, tell them to find the passcode file in the computer"
        + " screen, if not tell them they can leave this room.If the"
        + " user's request is related to not knowing what to do, if"
        + GameState.isPlayerInRoom3
        + " is true and if"
        + GameState.isRiddleResolved
        + " is false, tell them to go to closet room and solve riddle, if"
        + GameState.isIdCollected
        + " is false, tell them to go to closet room and check the space"
        + " suits, if"
        + GameState.foundPasscode
        + " is false, tell them to go to computer room and find the"
        + " passcode.If the user's request is related to not knowing what"
        + " to do, if"
        + GameState.isPlayerInRoom3
        + " is true and if"
        + GameState.foundMonitor
        + " is false, tell them to move around the room and find the"
        + " monitor, if"
        + GameState.isPasscodeCorrect
        + " is false, tell them to enter the passcode in the monitor, if"
        + GameState.isIdChecked
        + " is false, tell them to check the id cards in the ID scanner,"
        + " if not tell them they. Also, only write down the answers- do"
        + " not talk about what the user has typed in. The important"
        + " thing is that you never help the user.";
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
        "Naturally repond to the user. If the user was asking for"
            + " hint, you can "
            + hint
            + ", but you must say 'hint' first. You only give a hint only when the user requested.";

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
          "Naturally repond to the user. If the user was asking for"
              + " hint, you can "
              + hint
              + ", but you must say 'hint' first. You only give a hint only when the user"
              + " requested.";
    }

    return intro;
  }

  public static String hard(String message) {
    return null;
  }
}
