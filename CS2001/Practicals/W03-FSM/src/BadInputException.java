public class BadInputException extends Exception {
    private String fileName;
    private char input;
    private char output;
    private int next_state;

    public BadInputException(char input) {
        this.input = input;
    }

    public BadInputException(char input, char output) {
        this.input = input;
        this.output = output;
    }

    public BadInputException(char input, int next_state) {
        this.input = input;
        this.next_state = next_state;
    }

    public BadInputException(String fileName) {
    }

    @Override
    public void printStackTrace() {
        // Error either caused by invalid file/file syntax or invalid FSM design
        if (fileName == null)
            System.out.printf("input: %s, output: %s, next_state: %s\n", input, output, next_state);
        else
            System.out.printf("File %s doesn't exist or is invalid\n", fileName);

    }
}
