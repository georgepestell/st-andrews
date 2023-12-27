public class W02_2_Exceptions {

	public static void main(String[] args) throws EmptyFileException, InvalidInputException {

		if (args.length < 2 ) {
			System.out.println("Must supply input file and output file names");
		} else {
			IOReader io = new IOReader(args[0]);
			io.write(args[1]);
		}

	}
}
