public class W04Exercise1 {

	public static void main(String[] argv) throws EmptyFileException, InvalidInputException {

		if (argv.length < 2) {
			System.out.println("Incorrect No. Arguments");
			System.out.println("Usage: java W04Exercise1 <input-file> <search-term>");
		} else {
			IOReader io = new IOReader(argv[0]);

			io.printWordCount();
			for (int i = 1; i < argv.length; i++) {
				io.printWordCount(argv[i]);
			}
		}

	}
}
