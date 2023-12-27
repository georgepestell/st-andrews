import java.io.*;
import javax.json.*;
import javax.json.stream.*;

public class HIP2Test {
	public static void main(String[] argv) {
		JsonGeneratorFactory factory = Json.createGeneratorFactory(null);
		JsonGenerator generator;
		try {
			Writer file_out = new FileWriter(argv[1]);
			generator = factory.createGenerator(file_out);
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
			return;
		}

		try {
			// Setup input
			HIP2Reader h = new HIP2Reader(argv[0]);

			// Output
			generator.writeStartObject();
			generator.writeStartArray("stars");

			// Loop through input and output stars
			while (h.hasNextStar()) {
				HIP2Reader.HIP2Star s = h.getNextStar();

				double distance = 1000.00 / s.parallax;

				// Write star to file
				generator.writeStartObject();
				generator.write("StarID", s.StarID);
				generator.write("right-ascension", s.RA);
				generator.write("declination", s.Decl);
				generator.write("disatance", distance);
				generator.write("magnitude", s.magnitude);
				generator.writeEnd();
			}

			// close stars and json file
			generator.writeEnd();
			generator.writeEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}
		generator.close();

	}
}
