import java.util.HashMap;

public class State {

    /** Unique State Number */
    private final int STATE_NO;

    // Transition maps for given inputs, and their corresponding outputs/next_states
    private HashMap<Character, Character> output_transitions;
    private HashMap<Character, Integer> state_transitions;

    public State(int state_no) {
        this.STATE_NO = state_no;
        this.output_transitions = new HashMap<Character, Character>();
        this.state_transitions = new HashMap<Character, Integer>();
    }

    public int getStateNumber() {
        return this.STATE_NO;
    }

    /**
     * Add a transition to another State
     */
    public void addTransition(char input, char output, int next_state) throws BadDescriptionException {
        if (output_transitions.containsKey(input) || state_transitions.containsKey(input))
            throw new BadDescriptionException(input, output, next_state);
        output_transitions.put(input, output);
        state_transitions.put(input, next_state);

    }

    /**
     * Attempt to get the state transition from a given input. Should always work
     * since we check for valid inputs in the interpreter.
     * 
     * @see fsminterpreter#run()
     * @see #getOutput()
     */
    public int getNextState(char input) throws BadDescriptionException {
        if (state_transitions.containsKey(input)) {
            return state_transitions.get(input);
        } else {
            throw new BadDescriptionException(input);
        }
    }

    /**
     * Attempt to get the output of a given transition. Should always work since we
     * check for valid inputs in the interpreter.
     * 
     * @see fsminterpreter#run()
     * @see #getNextState()
     */
    public char getOutput(char input) throws BadDescriptionException {
        if (output_transitions.containsKey(input)) {
            return output_transitions.get(input);
        } else {
            throw new BadDescriptionException(input);
        }

    }

    /**
     * Allows ArrayList.contains() to compare states solely on ther STATE_NOs which
     * should always be unique
     * 
     * @return Unique ID for the instance should be its state_no
     */
    @Override
    public int hashCode() {
        return this.STATE_NO;
    }

    /**
     * We override the equals function to allow ArrayList.contains() to search based
     * on STATE_NOs. However, we need a generic Object comparison before we overload
     * specifically for State instances.
     */
    @Override
    public boolean equals(Object obj) {
        return false;
    }

    /**
     * Allows ArrayList.contains() to compare solely on their STATE_NOs
     */
    public boolean equals(State obj) {
        return this.STATE_NO == obj.getStateNumber();
    }

}
