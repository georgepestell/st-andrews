import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class WordReader {
    public ArrayList<String> getWords(String filename) {
        
        ArrayList<String> words = new ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(
            new FileReader(filename))){
            
            String line;
            while ((line = reader.readLine()) != null) {
                for (String word : line.split(" ")) {
                    words.add(word);
                }
            } 

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        
        }
        
        return words;
    }

    public HashMap<String, Integer> getWordMap(ArrayList<String> words) {
        HashMap<String, Integer> wordsMap = new HashMap<String, Integer>();
        for ( String word : words ) {
            if (wordsMap.containsKey(word)) {
                wordsMap.replace(word, wordsMap.get(word)+1);
            } else { 
                wordsMap.put(word, 1);
            }
        } 
        
        return wordsMap;
    }

}
