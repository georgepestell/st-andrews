import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class fsminterpreter {

    /**
     * Each FSM is limited in it's inputs by what is given in the descriptor file
     */
    private HashSet<Character> valid_inputs;

    /**
     * Map of states for quick searching for the next state
     */
    private HashMap<Integer, State> states;

    /** Current State of operation */
    private State current_state;

    /**
     * Sets up the instance's FSM using the fsm description file given
     * 
     * @throws BadDescriptionException Thrown if the file/fsm description is invalid
     */
    public fsminterpreter(String fileName) throws BadDescriptionException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            valid_inputs = new HashSet<Character>();
            states = new HashMap<Integer, State>();

            // Loop over each line of the descriptor file and add valid_inputs and
            // transitions
            String line;
            while ((line = reader.readLine()) != null && !line.isBlank()) {
                String[] values = line.trim().split(" ");
                // Check line had 4 elements
                if (values.length != 4)
                    throw new BadDescriptionException(fileName);

                // Check input and output are valid characters
                if (values[1].trim().length() != 1 || values[2].trim().length() != 1)
                    throw new BadDescriptionException(fileName);

                // Check state_no and next_state_no are valid integers
                int state_no = Integer.valueOf(values[0]);
                int next_state_no = Integer.valueOf(values[3]);

                char input = values[1].charAt(0);
                char output = values[2].charAt(0);

                State state;

                // Get the state if it exists already, or add it if not
                if (states.containsKey(state_no)) {
                    state = states.get(state_no);
                } else {
                    state = new State(state_no);
                    states.put(state_no, state);
                }

                // Set the initial state to the first given
                if (current_state == null) {
                    current_state = state;
                }

                // Add the input to valid input list
                valid_inputs.add(input);

                // Add the transitions to the state
                state.addTransition(input, output, next_state_no);
            }

            // Check to see if the FSM is valid
            if (states.size() == 0)
                throw new BadDescriptionException(fileName);
            for (State s : states.values()) {
                for (char i : valid_inputs) {
                    if (!states.containsKey(s.getNextState(i)))
                        throw new BadDescriptionException(i);
                    s.getOutput(i);
                }
            }

        } catch (FileNotFoundException e) {
            // This exception allows it to be caught under the "Bad description" output,
            // and makes debugging easier
            throw new BadDescriptionException(fileName);
        } catch (IOException e) {
            // This is thrown when the file can't be read
            throw new BadDescriptionException(fileName);
        } catch (NumberFormatException e) {
            throw new BadDescriptionException(fileName);
        }
    }

    /**
     * Attempts to run a single transition from the given input
     */
    public void run(char input) throws BadDescriptionException, BadInputException {
        if (!valid_inputs.contains(input)) {
            throw new BadInputException(input);
        }

        // Get the transition output
        System.out.print(current_state.getOutput(input));

        // Get the transition state and set the current state to it
        int next_state_no = current_state.getNextState(input);
        if (!states.containsKey(next_state_no))
            throw new BadDescriptionException(input);

        current_state = states.get(next_state_no);

    }

    /**
     * Sets up a fsminterpreter instance from a given file and attempts to run it
     * using standard input
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java fsminterpreter <fsm-description-file>");
            return;
        }

        // Setup a fsm interpreter instance with the descriptor file

        fsminterpreter fsm;
        String fileName = args[0];
        try {
            fsm = new fsminterpreter(fileName);
        } catch (BadDescriptionException e) {
            System.out.println("Bad description");
            return;
        }

        // Run the FSM using standard input values

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                String line = scanner.next();
                for (char input : line.toCharArray()) {
                    fsm.run(input);
                }
            }
        } catch (BadDescriptionException e) {
            System.out.println("Bad description");
            return;
        } catch (BadInputException e) {
            System.out.println("Bad input");
            return;
        }
    }
}
