import java.util.*;
public class W02_1_IO {

	public static void main(String[] args) {

		if (args.length < 1 ) {
			System.out.println("Must supply filename to read");
		} else {
			IOReader io = new IOReader(args[0]);
			io.printFile();
			io.printLineCount();
			io.printWordCount();
		}

	}
}
