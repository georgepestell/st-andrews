import java.io.*;
import java.util.*;

public class IOReader {
    private HashSet<String> vowels = new HashSet<String>();
    private ArrayList<String> text = new ArrayList<String>();

    public IOReader(String filename) throws EmptyFileException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
        
            // Check if the file is empty and throw EmptyFileException
            String line = reader.readLine();
            if (line == null) throw new EmptyFileException(filename + " is an empty file");

            // Save lines to array
            do {
                text.add(line);
            }
            while ((line = reader.readLine()) != null);


        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) { 
            System.out.println("File not found: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Couldn't close reader: " + e.getMessage());
                }
            }
        }

        // Initialize Vowels Set
        vowels.add("a");
        vowels.add("e");
        vowels.add("i");
        vowels.add("o");
        vowels.add("u");

    }

    public void write(String filename) throws EmptyFileException, InvalidInputException{
        if (text.size() == 0) {
            System.out.println("No input file has been read to write");
            return;
        }

        try (PrintWriter writer = new PrintWriter(filename)) {
            for (int i=0; i<text.size(); i++) {
                text.set(i, removeVowels(text.get(i)));
                writer.println(text.get(i));
            }
            printCharCount();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    private boolean isVowel(String character) throws InvalidInputException {
        // Check if String is not a single character
        if (character.length() > 1) {
            System.out.println("String is not a character");
            throw new InvalidInputException("Input character is not a character");
        }

        // Check if String is a vowel
        if (vowels.contains(character)) return true;
        else return false;
    }

    private String removeVowels(String s) throws InvalidInputException {
        String out = "";
        for (String c : s.split("")) {
            if (!isVowel(c.toLowerCase())) {
                out += c;
            }
        }
        return out;
    }

    private void printCharCount() {
        int charCount = 0;

        for (String line : text) {
            if (!line.isEmpty())
                charCount += line.length();
        }
        charCount += text.size();

        System.out.println(charCount);
    }

}
