import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

import turingmachine.TuringMachine;
import turingmachine.TuringMachineParser;

public class Main {
    public static void main(String[] args) {
        TuringMachine tm;
        try {
            if (args.length > 1) {
                tm = TuringMachineParser.parseTuringMachine(args[0], args[1]);
            } else {
                tm = TuringMachineParser.parseTuringMachine(args[0]);
            }

            for (String arg : args) {
                if (arg.equals("-d")) {
                    tm.setDebug();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(3);
            return;
        
        } catch (InvalidParameterException | IOException | NoSuchElementException e) {
            e.printStackTrace();
            System.out.println("input error");
            System.exit(2);
            return;
        }

        // tm.printStatus();
        tm.run();
    }
}