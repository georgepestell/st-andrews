

import java.util.ArrayList;
import java.util.List;

public class QuickSort {
    public static List<Integer> sort(List<Integer> sequence) {
        // If the array has zero or one elements, it is already sorted
        if (sequence.size() <= 1) {
            return sequence;
        }

        // Set the pivot to the last element
        int pivot = sequence.get(sequence.size() - 1);

        // Creater into equal-to, less-than and greater-than the pivot lists
        ArrayList<Integer> equalToPivot = new ArrayList<Integer>();
        ArrayList<Integer> lessThanPivot = new ArrayList<Integer>();
        ArrayList<Integer> greaterThanPivot = new ArrayList<Integer>();

        // Add each element to one of the three lists
        for (int element : sequence) {

            if (element == pivot) {
                equalToPivot.add(element);

            } else if (element < pivot) {
                lessThanPivot.add(element);

            } else {
                greaterThanPivot.add(element);
            }
        }

        // Recursively sort the greater-than and less-than lists
        lessThanPivot = (ArrayList<Integer>) QuickSort.sort(lessThanPivot);
        greaterThanPivot = (ArrayList<Integer>) QuickSort.sort(greaterThanPivot);

        // Create a sorted list from the three parts

        List<Integer> sortedSequence = new ArrayList<Integer>();

        sortedSequence.addAll(lessThanPivot);
        sortedSequence.addAll(equalToPivot);
        sortedSequence.addAll(greaterThanPivot);

        // Return the sorted sequence
        return sortedSequence;
    }



}
