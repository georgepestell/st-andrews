import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;

public class SortedMetricsTests {
    @Test
    public void testLSSOnSortedList() {
        List<Integer> sequence = List.of(1, 2, 3, 4, 5, 6, 7);

        int sortedness = SortednessMetrics.longestIncreasingSubsequence(sequence);

        assertEquals(sequence.size(), sortedness);
    }

    @Test
    public void testLSSOnRevSortedList() {
        List<Integer> sequence = List.of(7, 6, 5, 4, 3, 2, 1);

        int sortedness = SortednessMetrics.longestIncreasingSubsequence(sequence);

        assertEquals(1, sortedness);
    }

    @Test
    public void testLSSOnSortedListWithDuplicates() {
        List<Integer> sequence = List.of(1, 2, 3, 3, 3, 6, 7);

        int sortedness = SortednessMetrics.longestIncreasingSubsequence(sequence);

        assertEquals(sequence.size(), sortedness);
    }

    @Test
    public void testLSSOnListWithLastElementMovedToTheFront() {
        List<Integer> sequence = List.of(7, 1, 2, 3, 3, 3, 6);

        int sortedness = SortednessMetrics.longestIncreasingSubsequence(sequence);

        assertEquals(6, sortedness);
    }

    @Test
    public void testEDOnSortedList() {
        List<Integer> sequence = List.of(1, 2, 3, 4, 6, 8, 11);

        int sortedness = SortednessMetrics.editDistance(sequence);

        assertEquals(0, sortedness);
    }

    @Test
    public void testEDOnReverseSortedList() {
        List<Integer> sequence = List.of(11, 8, 6, 4, 3, 2, 1);

        int sortedness = SortednessMetrics.editDistance(sequence);

        assertEquals(6, sortedness);
    }

    @Test
    public void testEDOnReverseWithLastAndFirstElementsSwapped() {
        List<Integer> sequence = List.of(11, 2, 3, 4, 6, 8, 1);

        int sortedness = SortednessMetrics.editDistance(sequence);

        assertEquals(2, sortedness);
    }
}
