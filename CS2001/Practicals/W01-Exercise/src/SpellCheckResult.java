/**
 * This class represents the result of checking the spelling for a particular word.
 * @author someone
 *
 */
public class SpellCheckResult {

    private final boolean correct;
    private final String before;
    private final String after;

    /**
     * This is the constructor for SpellCheckResult objects.
     *
     * @param correct
     *            a boolean indicating whether a word is correct
     * @param before
     *            the dictionary word that would come before this word
     * @param after
     *            the dictionary word that would come after this word
     */
    public SpellCheckResult(boolean correct, String before, String after) {

        this.correct = correct;
        this.before = before;
        this.after = after;
    }

    /**
     * This method returns a boolean indicating whether the word is correct.
     * 
     * @return true if the word was found in the dictionary.
     */
    public boolean isCorrect() {
        return correct;
    }

    /**
     * This method returns the word which would come before this word in the
     * dictionary.
     * 
     * @return the word in the dictionary position before the one being checked,
     *         or null if there isn't such a word
     */
    public String getBefore() {
        return before;
    }

    /**
     * This method returns the word which would come after this word in the
     * dictionary.
     * 
     * @return the word in the dictionary position after the one being checked,
     *         or null if there isn't such a word
     */
    public String getAfter() {
        return after;
    }
}
