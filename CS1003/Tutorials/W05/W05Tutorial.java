import java.util.*;

public class W05Tutorial {
	private static final int ARGUMENTS = 1;
	private static final boolean topAndTail = false;

	public static void main(String[] argv) {
		if (argv.length != ARGUMENTS) {
			System.out.printf("Must supply %s arguments\n", ARGUMENTS);
			return;
		}

		HashSet<String> bigrams = new HashSet<String>();
		for (String w : argv[0].split(" ")) {
			bigrams.addAll(getBigrams(w, topAndTail));
		}

		System.out.println(bigrams.size());
		for (String b : bigrams) {
			System.out.println("- " + b);
		}

	}

	private static HashSet<String> getBigrams(String string, Boolean topAndTail) {
		string = string.toLowerCase().replaceAll(" ", "");
		if (topAndTail) string = "$" + string + "^";

		HashSet<String> bigrams = new HashSet<String>();
		for (int i = 0; i < string.length() - 1; i++) {
			bigrams.add("" + string.charAt(i) + string.charAt(i + 1));
		}

		return bigrams;

	}
}
