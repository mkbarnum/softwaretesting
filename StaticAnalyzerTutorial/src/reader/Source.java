package reader;

import java.util.Locale;

/**
 * This program has the same functionality of last week's program.
 * The only changes are in comments, the three @Override functions,
 * a few extra getters, and the use of a String instead of a List
 * for our sentence we create.
 */
/*  default */ class Source {
  /**
   * This program will take four arguments.
   *     Program arguments should be arg[0] readMe1.txt, arg[1] readMe2.txt,
   *     arg[2] readMe3.txt, arg[3] how many letters you want in your sentence
   * @param args listed above
   */
  public static void main(final String[] args) {
    final CustomFileReader reader1 = new CustomFileReader(args[0]);
    final CustomFileReader reader2 = new CustomFileReader(args[1]);
    final CustomFileReader reader3 = new CustomFileReader(args[2]);
    final int numberOfWords = Integer.parseInt(args[3]);

    for (int i = 0; i < numberOfWords; i++) {
      int howManyWords;
      howManyWords = reader1.howManyWordsInFile();
      final String wordFromIndex = reader2.returnThatWord(howManyWords);
      final CharSequence firstLetter = wordFromIndex.substring(0, 1).toLowerCase(Locale.US);
      reader3.findNewWord(firstLetter);
    }

    final String newSentence = reader3.getNewSentence();
    reader1.setNewSentence(newSentence);

    System.out.println(reader1.getNewSentence());
  }
}