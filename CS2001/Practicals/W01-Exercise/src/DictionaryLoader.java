import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * This singleton class provides the functionality to load in a dictionary at
 * the path specified by DICTIONARY_PATH and return the dictionary as an array
 * of words in alphabetical order.
 *
 * @author jonl
 *
 */
public final class DictionaryLoader {

    private static final String DICTIONARY_PATH = "words.txt";
    private static DictionaryLoader loaderInstance = null;

    private DictionaryLoader() {
    }

    /**
     * Static method which returns the singleton instance of the
     * DictionaryLoader class.
     * 
     * @return the DictionaryLoader instance
     */
    public static DictionaryLoader getInstance() {
        if (loaderInstance == null) {
            loaderInstance = new DictionaryLoader();
        }
        return loaderInstance;
    }

    /**
     * Loads the dictionary from a local file.
     * 
     * @return an array of words in alphabetical order
     */
    public String[] loadDictionary() {

        try {
            Scanner myScanner = new Scanner(new BufferedReader(new FileReader(DICTIONARY_PATH)));
            List<String> unsortedListOfWords = new ArrayList<String>();

            while (myScanner.hasNext()) {
                unsortedListOfWords.add(myScanner.next().toLowerCase(Locale.ENGLISH));
            }

            String[] arrayOfWords = unsortedListOfWords.toArray(new String[0]);
            Arrays.sort(arrayOfWords);
            myScanner.close();
            return arrayOfWords;

        } catch (FileNotFoundException fne) {
            System.err.println("Standard dictionary file " + DICTIONARY_PATH + " missing");
            return new String[0];
        }
    }
}
