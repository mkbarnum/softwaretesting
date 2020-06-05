package edu.byu.cs329;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
  Set<String> allWords = new HashSet<String>();
  List<Character> usedWords = new ArrayList<Character>();
  int lengthOfWord;
  String pattern = "";


  EvilHangmanGame() {
  }


  /**
   * Starts a new game of evil hangman using words from <code>dictionary</code>
   * with length <code>wordLength</code>.
   *	<p>
   *	This method should set up everything required to play the game,
   *	but should not actually play the game. (ie. There should not be
   *	a loop to prompt for input from the user.)
   *
   * @param dictionary Dictionary of words to use for the game
   * @param wordLength Number of characters in the word to guess
   */
  public void startGame(File dictionary, int wordLength){

    try {
      lengthOfWord = wordLength;
      Scanner words = new Scanner(dictionary);
      while (words.hasNext()) {
        String line = words.nextLine();
        if (line.length() == lengthOfWord) {
          allWords.add(line);
        }
      }
      pattern = "";
      for (int i = 0; i < lengthOfWord; i++) {
        pattern = (pattern + "-");
      }
    }
    catch(IOException io){
      System.out.println("Error reading file");
    }

  }


  /**
   * Make a guess in the current game.
   *
   * @param guess The character being guessed
   *
   * @return The set of strings that satisfy all the guesses made so far
   * in the game, including the guess made in this call. The game could claim
   * that any of these words had been the secret word for the whole game.
   *
   * @throws IEvilHangmanGame.GuessAlreadyMadeException If the character <code>guess</code>
   * has already been guessed in this game.
   */
  public Set<String> makeGuess(char guess) throws IEvilHangmanGame.GuessAlreadyMadeException{ // NEEDS TO THROW EXCEPTION!!!
    Set<String> newWords = new HashSet<String>();
    HashMap<String,TreeSet<String>> wordGroups = new HashMap<String,TreeSet<String>>();

    if(usedWords.contains(guess)){
      throw new IEvilHangmanGame.GuessAlreadyMadeException();
    }

    usedWords.add(guess);

    for(String word : allWords) {
      List<Integer> patternLocations = new ArrayList<Integer>();
      for(int i = 0; i < word.length(); i++){
        if(word.charAt(i) == guess) {
          patternLocations.add(i);
        }
      }
      StringBuilder newPattern = new StringBuilder(pattern);
      for(int j = 0; j < patternLocations.size(); j++){
        newPattern.setCharAt(patternLocations.get(j),guess);
      }
      String nPattern = newPattern.toString();
      boolean inMap = false;
      for (Map.Entry<String, TreeSet<String>> entry : wordGroups.entrySet()) {
        String key = entry.getKey();
        TreeSet<String> value = entry.getValue();
        if(nPattern.equals(key)){
          value.add(word);
          wordGroups.put(key,value);
          inMap = true;
        }
      }
      if(!inMap){
        TreeSet<String> newSet = new TreeSet<String>();
        newSet.add(word);
        wordGroups.put(nPattern,newSet);
      }
    }

    String bestPattern = chooseGroup(wordGroups, guess);

    for (Map.Entry<String, TreeSet<String>> entry : wordGroups.entrySet()) {
      String key = entry.getKey();
      TreeSet<String> value = entry.getValue();
      if (key == bestPattern){
        newWords.addAll(value);
      }
    }

    pattern = bestPattern;
    allWords = newWords;
    return allWords;
  }

  public void printGroups(HashMap<String,TreeSet<String>> wordGroups){
    for (Map.Entry<String, TreeSet<String>> entry : wordGroups.entrySet()) {
      String key = entry.getKey();
      TreeSet<String> value = entry.getValue();
      System.out.print(key + ":");
      for(String word : value){
        System.out.print(" " + word);
      }
      System.out.println();
    }
  }

  public String chooseGroup(HashMap<String, TreeSet<String>> wordGroups, char guess){
    int maxGroup = 0;
    String maxPattern = "";
    Set<String> longGroups = new HashSet<String>();
    for (Map.Entry<String, TreeSet<String>> entry : wordGroups.entrySet()) {
      String key = entry.getKey();
      TreeSet<String> value = entry.getValue();
      if(value.size() > maxGroup){
        maxGroup = value.size();
        maxPattern = key;
      }
    }

    for (Map.Entry<String, TreeSet<String>> entry : wordGroups.entrySet()) {
      String key = entry.getKey();
      TreeSet<String> value = entry.getValue();
      if(value.size() == maxGroup){
        longGroups.add(key);
      }
    }

    if(longGroups.size() == 1){
      return maxPattern;
    }
    else {

      // TESTING TO FIND GROUP WITHOUT LETTER IN IT

      for (String testPattern : longGroups){
        if(testPattern.indexOf(guess) == (-1)){
          return testPattern;
        }
      }

      // TESTING TO FIND GROUP WITH FEWEST LETTERS

      int maxCount = 0;
      String leastLetters = "";
      List<String> leastLets = new ArrayList<String>();
      for (String testPattern : longGroups){
        int count = testPattern.length() - testPattern.replaceAll("-","").length();
        if(count > maxCount){
          maxCount =  count;
          leastLetters = testPattern;
        }
      }

      for (String testPattern : longGroups){
        int count = testPattern.length() - testPattern.replaceAll("-","").length();
        if(count == maxCount){
          leastLets.add(testPattern);
        }
      }

      if(leastLets.size() == 1){
        return leastLetters;
      }

      // TESTING TO FIND GROUP WITH THE RIGHTMOST GUESSED LETTER

      else{
        String rightMostPattern = "";
        Set<String> mostRights = new HashSet<String>();
        boolean found = false;
        List<String> safety = new ArrayList<String>();
        safety.addAll(leastLets);

        while(!found){
          int mostRightIndex = 0;
          for(int i = 0; i < leastLets.size(); i++){
            for(int j = leastLets.get(i).length()-1; j > 0; j--){
              if(leastLets.get(i).charAt(j) == guess && (j > mostRightIndex)){
                mostRightIndex = j;
                rightMostPattern = leastLets.get(i);
              }
            }
          }

          List<String> newPatterns = new ArrayList<String>();
          for(int i = 0; i < leastLets.size(); i++){
            for(int j = leastLets.get(i).length()-1; j > 0; j--){
              if(leastLets.get(i).charAt(j) == guess && (j == mostRightIndex)){
                newPatterns.add(leastLets.get(i));
              }
            }
          }

          List<String> temp = new ArrayList<String>();
          temp.addAll(newPatterns);
          leastLets = newPatterns;

          if(leastLets.size() == 1) {
            found = true;
            String safe = "";
            int safeNum = leastLets.get(0).length();
            for(int k = 0; k < safety.size(); k++){
              if (safety.get(k).substring(0, safeNum).equals(leastLets.get(0))){
                safe = safety.get(k);
              }
            }
            return safe;
          }
          else{
            leastLets.clear();
            for(String word : temp){
              word = word.substring(0,mostRightIndex);
              leastLets.add(word);
            }
          }
        }


      }




    }

    return maxPattern;
  }


}
