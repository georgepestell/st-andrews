import java.util.*;

public class W02_T01 {
    public static void main(String[] args) {
        // Check for filename argument
        if (args.length < 1)
            System.out.println("Must supply filename argument");

        WordReader reader = new WordReader();
        ArrayList<String> words = reader.getWords(args[0]);
        Map<String, Integer> wordMap = reader.getWordMap(words);
        wordMap.forEach((String word, Integer count) -> {
            System.out.println(word + "\t:" + count);
        });
    }

}
