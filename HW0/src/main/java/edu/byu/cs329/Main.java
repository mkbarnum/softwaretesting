package edu.byu.cs329;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;

public class Main {
  public static void main(String args[]) throws FileNotFoundException{

    File dictionary = new File(args[0]);
    int wordLength = Integer.parseInt(args[1]);
    int numGuesses = Integer.parseInt(args[2]);

    if(wordLength < 2){
      System.out.println("Word length must be at least two");
      return;
    }

    if(numGuesses < 1) {
      System.out.println("Number of guesses must be at least 1");
      return;
    }

    EvilHangmanGame game = new EvilHangmanGame();
    game.startGame(dictionary,wordLength);
    Scanner scan = new Scanner(System.in);
    for(int i = 0; i < numGuesses; i++){

      System.out.println("You have " + (numGuesses-i) + " guesses left");
      System.out.print("Used letters: ");
      if(game.usedWords.size() == 0) {
        System.out.println();
      }
      Collections.sort(game.usedWords);
      for(int j = 0; j < game.usedWords.size(); j++) {
        if (j == game.usedWords.size() - 1) {
          System.out.println(game.usedWords.get(j));
        } else {
          System.out.print(game.usedWords.get(j) + " ");
        }
      }
      System.out.println("Word: " + game.pattern);

      boolean goodInput = false;
      String input = "";
      while(!goodInput) {
        System.out.print("Enter guess: ");
        input = scan.nextLine();
        if(input.length() > 1 ){
          System.out.println("INVALID INPUT: Please guess a letter" + "\n");
        }
        else if (!isAlpha(input)){
          System.out.println("INVALID INPUT: Please guess a letter" + "\n");
        }
        else if(game.usedWords.contains(input.charAt(0))){
          System.out.println("You already used that letter" + "\n");
        }
        else {
          goodInput = true;
        }
      }
      input = input.toLowerCase();
      char guess = input.charAt(0);

      try {
        game.makeGuess(guess);
      }
      catch(IEvilHangmanGame.GuessAlreadyMadeException ex){
        System.out.println("You already used that letter");
      }
      if(game.pattern.indexOf(guess) == (-1)){
        if ( i == numGuesses-1){
          System.out.println("You lose!");
          String randomWord = "";
          int size = game.allWords.size();
          int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
          int test = 0;
          for(String word : game.allWords)
          {
            if (test == item)
              randomWord = word;
            test++;
          }
          System.out.println("The word was: " + randomWord);
        }
        else {
          System.out.println("Sorry, there are no " + guess + "'s");
        }
      }
      else{
        if(game.pattern.indexOf("-")==(-1)){
          String winningWord = "";
          for(String word : game.allWords){
            winningWord = word;
          }
          System.out.println("The word was: " + winningWord);
          return;
        }
        else {
          int count = game.pattern.length() - game.pattern.replaceAll(input, "").length();
          System.out.println("Yes, there is " + count + " " + guess);
          i--;
        }

      }
      System.out.print("\n");
    }


  }

  public static boolean isAlpha(String name) {
    return name.matches("[a-zA-Z]+");
  }
}
