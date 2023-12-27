import java.io.*;
import java.util.*;

public class IOReader {
    ArrayList<String> lines = new ArrayList<String>();

    public IOReader(String filename) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                reader.close();
                } catch (IOException e) {
                    System.out.println("Couldn't Close Reader: " + e.getMessage());
                }
            }
        }
    }

    public void printLineCount() {
        System.out.println(lines.size());
    }

    public void printWordCount() {
        int wordCount = 0;

        for (String line : lines) {
            for (String word : line.split(" ")) {
                if (!word.trim().isEmpty()) wordCount++;
            }
        }
        System.out.println(wordCount);
    }

    public void printFile() {
        for (String line : lines) {
            System.out.println(line);
        }
    }
}
