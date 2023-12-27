import java.util.ArrayList;

/**
 * Tests the time complexity of a QuickSort which partitions using the last
 * element. It creates a dataset with a range of levels of sortedness and the
 * time taken for the sort to run.
 */
public class DataPrinter {
    private static final int LSS_SEQUENCE_LENGTH = 100;
    private static final int ED_SEQUENCE_LENGTH = 14;
    private static final int LSS_TEST_REPEATS = 5;
    private static final int ED_TEST_REPEATS = 20;

    private static long getTimeToSort(ArrayList<Integer> sequence) {

        // Get the current system time before the sort in milliseconds
        long before = System.nanoTime();

        QuickSort.sort(sequence);

        // Get the current system time after the sort in milliseconds
        long after = System.nanoTime();

        // Calculate and return the time spent sorting the sequence in milliseconds
        return after - before;
    }

    /**
     * Run tests for various sorted metrics and writes the information in a given
     * data folder
     */
    public static void main(String[] args) {

        // Run tests for a range of sorted values using the longest subsequence metric.

        // [ 1, 2, 3, 4 ] - 4
        // [ 4, 1, 2, 3 ] - 3
        // [ 4, 3, 1, 2 ] - 2
        // [ 4, 3, 2, 1 ] - 1

        // Create a csv data file for this metric

        // Create a sorted list of the default size
        ArrayList<Integer> sequence = new ArrayList<Integer>();
        for (int val = 0; val < LSS_SEQUENCE_LENGTH; val++) {

            // Add incrememnting values to the list
            sequence.add(val);

        }

        // For each offset, move the last element to the offset position to decrease LSS
        // sortedness
        for (int offset = 0; offset < LSS_SEQUENCE_LENGTH; offset++) {

            // Check the time taken to sort the list
            // Repeat tests to create range bars.
            int sortedness = SortednessMetrics.longestIncreasingSubsequence(sequence);
            for (int repeat = 0; repeat < LSS_TEST_REPEATS; repeat++) {

                long timeTaken = getTimeToSort(sequence);

                System.out.printf("LSS, %s, %s\n", sortedness, timeTaken);

            }

            // Decrease the LSS by shifting the last element to the offset position giving a
            // semi-reverse sort

            int tmp = sequence.get(sequence.size() - 1);
            sequence.remove(sequence.size() - 1);

            sequence.add(offset, tmp);

        }

        // Run tests for a range of sorted values using the edit distance metric.

        // [ 1, 2, 3, 4 ] = 0
        // [ 4, 2, 3, 1 ] = 2
        // [ 3, 4, 2, 1 ] = 4
        // [ 4, 3, 2, 1 ] = 4

        // Create a sorted list of the default size
        sequence = new ArrayList<Integer>();
        for (int val = 0; val < ED_SEQUENCE_LENGTH; val++) {

            // Add incrememnting values to the list
            sequence.add(val);

        }

        // For each offset, move the last element to the front of the list
        for (int offset = 0; offset < Math.floorDiv(ED_SEQUENCE_LENGTH, 2) + 1; offset++) {

            // Check the time taken to sort the list
            // Repeat tests to create range bars.
            int sortedness = SortednessMetrics.editDistance(sequence);
            for (int repeat = 0; repeat < ED_TEST_REPEATS; repeat++) {

                long timeTaken = getTimeToSort(sequence);

                System.out.printf("ED, %s, %s\n", sortedness, timeTaken);

            }

            // Decrease the ED by shifting the last element to the front of the list
            int tmp = sequence.get(sequence.size() - 1);

            sequence.remove(sequence.size() - 1);

            sequence.add(0, tmp);

        }
    }
}
