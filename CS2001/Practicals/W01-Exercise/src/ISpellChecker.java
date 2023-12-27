/**
 * Interface for a Spelling Checker.
 * @author someone
 *
 */
public interface ISpellChecker {

    /**
     * Runs the checker for the given array of words to check.
     * 
     * @param words the array of words to check
     */
    void runChecker(String[] words);


    /**
     * Checks whether a word is correctly spelled.
     * 
     * @param word the word to be checked
     * @return an object representing the outcome of the check
     */
    SpellCheckResult check(String word);

}
