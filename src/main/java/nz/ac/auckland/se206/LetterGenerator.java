package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Generates a list of 8 random letters, 3 vowels and 5 consonants. */
public class LetterGenerator {

  /**
   * Generates a list of 8 random letters, 3 vowels and 5 consonants.
   *
   * @return a list of 8 random letters, 3 vowels and 5 consonants.
   */
  public List<String> generateCombinedList() {
    List<String> vowelsList = generateVowels();
    List<String> consonantsList = generateConsonants();

    return combineLists(consonantsList, vowelsList);
  }

  /**
   * Generates a list of 8 random letters, 3 vowels and 5 consonants.
   *
   * @return a list of 8 random letters, 3 vowels and 5 consonants.
   */
  public String letterListToString(List<String> letterList) {
    // Get a list of letters and separate them using commas into a string
    return letterList.get(0)
        + ", "
        + letterList.get(1)
        + ", "
        + letterList.get(2)
        + ", "
        + letterList.get(3)
        + ", "
        + letterList.get(4)
        + ", "
        + letterList.get(5)
        + ", "
        + letterList.get(6)
        + ", "
        + letterList.get(7);
  }

  // A method that randomly generates a number between 1 and 5, assigns a vowel to each number and
  // outputs a list of 3 randomly selected vowels.
  private List<String> generateVowels() {

    List<String> vowelsList = new ArrayList<String>();
    Random rand = new Random();
    int characterValue;
    // Until there is less than 3 vowels, randomly select letters
    while (vowelsList.size() < 3) {
      characterValue = rand.nextInt(4) + 1;
      switch (characterValue) {
          // add a if theres is no a
        case 1:
          if (vowelsList.contains("A")) {
            break;
          } else {
            vowelsList.add("A");
            break;
          }
          // add e if there is no e
        case 2:
          if (vowelsList.contains("E")) {
            break;
          }
          vowelsList.add("E");
          break;
          // add i if there is no i
        case 3:
          if (vowelsList.contains("I")) {
            break;
          }
          vowelsList.add("I");
          break;
          // add o if there is no o
        case 4:
          if (vowelsList.contains("O")) {
            break;
          }
          vowelsList.add("O");
          break;
          // add u if there is no u
        case 5:
          if (vowelsList.contains("U")) {
            break;
          }
          vowelsList.add("U");
          break;
        default:
          vowelsList.add("X");
          break;
      }
    }

    return vowelsList;
  }

  private List<String> generateConsonants() {
    Random rand = new Random();
    List<String> consonantsList = new ArrayList<String>();
    int characterValue;
    int listSize = 0;

    while (listSize < 5) {
      characterValue = rand.nextInt(24) + 66;
      if (characterValue != 69
          && characterValue != 73
          && characterValue != 79
          && characterValue != 85
          && consonantsList.contains(Character.toString((char) characterValue)) == false) {
        consonantsList.add(Character.toString((char) characterValue));
        listSize++;
      }
    }
    // System.out.print("ConsoList Size" + consonantsList.size() + " List: " + consonantsList);
    return consonantsList;
  }

  private List<String> combineLists(List<String> consonants, List<String> vowels) {
    int currentValue;
    int combinedListSize = 8;
    List<String> combinedList = new ArrayList<String>();
    List<String> combinedListRandom = new ArrayList<String>();
    combinedList.addAll(consonants);
    combinedList.addAll(vowels);
    Random rand = new Random();

    for (int i = 0; i < 8; i++) {
      currentValue = rand.nextInt(combinedListSize);
      combinedListRandom.add(combinedList.get(currentValue));
      combinedList.remove(currentValue);
      combinedListSize--;
    }
    // System.out.print("CombinedList Size" + combinedList.size() + " List: " + combinedList +
    // "\n");
    // System.out.print(
    //     "CombinedListRandom Size" + combinedListRandom.size() + " List: " + combinedListRandom);
    return combinedListRandom;
  }
}
