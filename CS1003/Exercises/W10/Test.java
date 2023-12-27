import java.util.*;
import java.awt.*;

public class Test {
	public static void main(String args[]) throws Exception {
		// Load the CSV file into a Loader object
		Loader L = new Loader("postcodes.csv");

		// Get the min and max latitudes
		float minLat = L.latitudes[0];
		float maxLat = L.latitudes[0];
		for (Float l : L.latitudes) {
			if (l > maxLat)
				maxLat = l;
			else if (l < minLat)
				minLat = l;
		}

		// Get the min and max longitudes
		float minLong = L.longitudes[0];
		float maxLong = L.longitudes[0];
		for (Float l : L.longitudes) {
			if (l > maxLong)
				maxLong = l;
			else if (l < minLong)
				minLong = l;
		}

		System.out.printf("Max: %s, %s. Min: %s, %s\n", maxLat, maxLong, minLat, minLong);

		// Now plot three points to show how to use the SimpleCanvas
		// These will be very small, but they will be more visible when we start plotting many
		// points
		ISimpleCanvas C = new SimpleCanvas();

		for (int i = 0; i < L.postcodes.length; i++) {
			int x = Math.round(999 * ((L.latitudes[i] - minLat) / (maxLat - minLat)));
			int y = Math.round(999 * ((L.longitudes[i] - minLong) / (maxLong - minLong)));
			C.setPointBlack(x, y);
		}

		int K = Integer.valueOf(args[0]);
		HashMap<Integer, ArrayList<Integer>> clusters = new HashMap<Integer, ArrayList<Integer>>();
		for (int k = 0; k < K; k++) {
			int p_index;
			do {
				p_index = Math.round((float) Math.random() * (float) L.postcodes.length);
			} while (clusters.keySet().contains(p_index));

			clusters.put(p_index, new ArrayList<Integer>());

		}

		for (int i = 0; i < L.postcodes.length; i++) {
			int closest = 0;
			double dist_sq = 1000000;

			for (Integer k : clusters.keySet()) {
				double dist = Math.pow(L.latitudes[i] - L.latitudes[k], 2)
						+ Math.pow(L.longitudes[i] - L.longitudes[k], 2);
				if (dist < dist_sq) {
					dist_sq = dist;
					closest = k;
				}
			}

			clusters.get(closest).add(i);
		}

		for (int k : clusters.keySet()) {
			float total_lat = 0;
			float total_long = 0;
			for (int i : clusters.get(k)) {
				total_lat += L.latitudes[i];
				total_long += L.longitudes[i];
			}
			float avg_lat = total_lat / clusters.get(k).size();
			float avg_long = total_long / clusters.get(k).size();

			int x = Math.round(999 * ((avg_lat - minLat) / (maxLat - minLat)));
			int y = Math.round(999 * ((avg_long - minLong) / (maxLong - minLong)));


			System.out.printf("x: %s, y: %s\n", x, y);
			C.setPointColour(x, y, Color.RED);

		}

	}
}

