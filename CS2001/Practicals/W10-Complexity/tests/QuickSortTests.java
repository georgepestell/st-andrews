import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;

public class QuickSortTests {

    @Test
    public void testSortEvenSizedSequence() {
        List<Integer> unsortedSequence = List.of(5, 3, 1, 3, 5, 2, 8, 10);

        List<Integer> sortedSequence = QuickSort.sort(unsortedSequence);

        assertEquals(sortedSequence.size(), SortednessMetrics.longestIncreasingSubsequence(sortedSequence));

    }

    @Test
    public void testSortOddSizedSequence() {
        List<Integer> unsortedSequence = List.of(5, 3, 1, 3, 5, 2, 8, 10, 2);

        List<Integer> sortedSequence = QuickSort.sort(unsortedSequence);

        assertEquals(sortedSequence.size(), SortednessMetrics.longestIncreasingSubsequence(sortedSequence));

    }

    @Test
    public void testSortOnSortedSequence() {
        List<Integer> unsortedSequence = List.of(1, 2, 2, 3, 3, 5, 5, 8, 10);

        List<Integer> sortedSequence = QuickSort.sort(unsortedSequence);

        assertEquals(sortedSequence.size(), SortednessMetrics.longestIncreasingSubsequence(sortedSequence));

    }

    @Test
    public void testSortEmptySequences() {
        List<Integer> unsortedSequence = List.of();

        List<Integer> sortedSequence = QuickSort.sort(unsortedSequence);

        assertEquals(sortedSequence.size(), SortednessMetrics.longestIncreasingSubsequence(sortedSequence));

    }
}
