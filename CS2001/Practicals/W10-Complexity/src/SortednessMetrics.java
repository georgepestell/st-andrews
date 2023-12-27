
import java.util.List;
import java.util.stream.Collectors;

public class SortednessMetrics {
    /**
     * Calculates the longest increasing sequence sortedness metric for a given
     * sequence
     */
    public static int longestIncreasingSubsequence(List<Integer> sequence) {
        // Create an array which will hold the maxSizes of the sortedSequences from that
        // index
        int[] subsequences = new int[sequence.size()];

        // Get the maximum sorted sequence size from each starting index
        for (int topElem = 0; topElem < sequence.size(); topElem++) {

            // We know at least one element is in this sub-sequence
            subsequences[topElem] = 1;

            // Loop over the sub-sequences before this top element
            for (int prevSubSequence = 0; prevSubSequence < topElem; prevSubSequence++) {

                // If the previous sub-sequence is smaller than this element, than grow that
                // subsequence
                if (sequence.get(topElem) >= sequence.get(prevSubSequence)) {
                    // If this previous sub-sequence is greater to the ones checked, then this max
                    // sorted sequence size is one greater than the previous sub-sequence.
                    subsequences[topElem] = Math.max(subsequences[topElem], subsequences[prevSubSequence] + 1);
                }
            }
        }

        // Check to see which starting index derives the greatest subsequence length
        int maxSortedness = 0;
        for (int s : subsequences) {
            maxSortedness = Math.max(maxSortedness, s);
        }

        // Return the greatest subsequence length
        return maxSortedness;

    }

    public static int getEdits(List<Integer> sequence, List<Integer> sortedSequence, int seqPos, int sortedSeqPos) {

        // If we reach the start of the sequence, then we must add all of the remaining
        // sorted sequence
        if (seqPos <= 0) {
            return sortedSeqPos;
        }

        // If we reach the start of the sorted sequence, then we must remove all of the
        // remaining sorted sequence
        if (sortedSeqPos <= 0) {
            return seqPos;
        }

        // Skip over matching elements
        if (sequence.get(seqPos - 1) == sortedSequence.get(sortedSeqPos - 1)) {
            return getEdits(sequence, sortedSequence, seqPos - 1, sortedSeqPos - 1);
        }

        // Get the number of edits needed if we removed or added the sorted elements,
        // and if we swapped the sorted value for the unsorted sequence value
        int addEdits = getEdits(sequence, sortedSequence, seqPos, sortedSeqPos - 1);
        int removeEdits = getEdits(sequence, sortedSequence, seqPos - 1, sortedSeqPos);
        int swapEdits = getEdits(sequence, sortedSequence, seqPos - 1, sortedSeqPos - 1);

        // Determine which operation requires minimum edits
        int minEdits = Math.min(addEdits, removeEdits);
        minEdits = Math.min(minEdits, swapEdits);

        return 1 + minEdits;
    }

    public static int editDistance(List<Integer> sequence) {

        // Sort the sequence
        List<Integer> sortedSequence = List.copyOf(sequence).stream().sorted().collect(Collectors.toList());

        return SortednessMetrics.getEdits(sequence, sortedSequence, sortedSequence.size(), sortedSequence.size());

    }
}
