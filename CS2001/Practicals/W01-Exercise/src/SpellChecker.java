import java.util.Arrays;

/** 
 * Builds a dictionary list from the words.txt file, which can then be used
 * to run a basic spellcheck for given words against that dictionary.
 */
public class SpellChecker implements ISpellChecker {
    /***
     * List of words to check spelling against.
     */
    private String[] dictionary;

    /*** 
     * Empty constructor which fills the dictionary attribiute.
     * @see dictionary
     */
    public SpellChecker() {
        dictionary = DictionaryLoader.getInstance().loadDictionary();
    }
    /*** 
     * Takes user input, and spellchecks the first given word against a
     * dictionary file. If incorrect, it gives the two nearest neighbours.
     * Usage: java SpellChecker words_to_check
     * @param args args[0] will be spellchecked
     * @see runChecker
     */
    public static void main(String[] args) {

        // Make sure at least one word is supplied
        if (args.length < 1) {
            System.out.println("Usage: java SpellChecker <words_to_check>");
            return;
        }

        // Create a new array with the input in lower case
        String[] checkWords = new String[1];
        checkWords[0] = args[0].toLowerCase();

        // Allows non-static functions to be ran, and runs the constructor
        // to build the dictionary.
        SpellChecker checker = new SpellChecker();

        checker.runChecker(checkWords);

    }

    /***
     * For each word supplied, it runs check() to see if it's spelt correctly,
     * and gives relevant output to the user.
     * @see check
     * @param words The array of words to check if spelt correctly
     */
    @Override
    public void runChecker(String[] words) {
        for (String word : words) {

            SpellCheckResult result = check(word);

            if (result.isCorrect()) {
                System.out.println(word + " correct");

            } else {
                String before = result.getBefore();
                String after = result.getAfter();
                System.out.print(word +  " not found - nearest neighbour(s) ");

                // Check to see if the 'and' is necessary
                // Then print non-empty neighbours
                if (before != null) {

                    if (after != null) {
                        System.out.println(before + " and " + after);
                    }
                    else {
                        System.out.println(before);
                    }
                }
                else {
                    System.out.println(after);
                }
            }
        }
    }

    /*** 
     * Searches the dictionary for a given word, and then returns a helper class
     * with the necessary info of whether it's spelt correctly, or the two nearest
     * neighbours if not spelt correctly.
     * @return SpellCheckResult Contains the result of the spell check for the word
     * @param word The word to spell check
     */
    @Override
    public SpellCheckResult check(String word) {
        // Binary search returns the positive index if found, or a negative
        // index of where it would be found
        int index = Arrays.binarySearch(dictionary, word);
        if (index > 0) {
            return new SpellCheckResult(true, null, null);
        }
        else {
            // Get the true index of where it would be
            index = -index - 1;

            // Ensures entries at the edge of the dictonary with no
            // won't encounter index errors.
            String before = null;
            String after = null;

            // If at the start of the list, there is no word before
            if (index != 0) {
                before = dictionary[index - 1];
            }

            // If at the end of the list, there is no word after
            if (index < dictionary.length) {
                after = dictionary[index];
            }

            return new SpellCheckResult(false, before, after);
        }
    }
}

