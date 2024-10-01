import java.util.*;

class QuickSelectDeterministic {

    // Using Median of Medians algorithm
    static int findKthSmallest(int[] arr, int left, int right, int k) {
        int size = right - left + 1;

        if (k > 0 && k <= size) {

            int medianCount = (size + 4) / 5;
            int[] medians = new int[medianCount];

            // Calculate the medians array
            for (int i = 0; i < size / 5; i++) {
                medians[i] = getMedian(arr, left + i * 5, left + i * 5 + 5);
            }
            if (size % 5 != 0) {
                medians[medianCount - 1] = getMedian(arr, left + (size / 5) * 5, right + 1);
            }

            // Find the median of medians
            int medianOfMedians = (medianCount == 1) ? medians[0] : findKthSmallest(medians, 0, medianCount - 1, medianCount / 2);

            // Partition along medianOfMedians
            int partitionIndex = partitionArray(arr, left, right, medianOfMedians);

            // Handle 3 cases (equal,left,right)

            if (partitionIndex - left == k - 1)
                return arr[partitionIndex];


            if (partitionIndex - left > k - 1)
                return findKthSmallest(arr, left, partitionIndex - 1, k);

            return findKthSmallest(arr, partitionIndex + 1, right, k - partitionIndex + left - 1);
        }

        return Integer.MAX_VALUE; // Return MAX_VALUE if k is out of bounds
    }

    // Helper method to find the median of a subarray
    static int getMedian(int[] arr, int start, int end) {
        Arrays.sort(arr, start, end); // Sort the subarray
        return arr[start + (end - start) / 2]; // Return the median
    }

    // Swap helper
    static void swapElements(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Smart partition
    static int partitionArray(int[] arr, int left, int right, int pivot) {
        int i = left;
        for (int j = left; j < right; j++) {
            if (arr[j] == pivot) {
                swapElements(arr, j, right);
                break;
            }
        }

        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                swapElements(arr, i, j);
                i++;
            }
        }
        swapElements(arr, i, right);
        return i;
    }

    // Random array generator
    public static int[] createRandomArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(100);
        }
        return arr;
    }

    // OG Median Finder
    public static int findActualMedian(int[] arr) {
        Arrays.sort(arr);
        int n = arr.length;
        return arr[(n+1)/2];
    }

    public static void main(String[] args) {
        int n = 100;
        int[] arr = createRandomArray(n);

//        System.out.println(n + "Array size\t"  + " Median (Algo)\t"  + " Median (Actual)\t" + "Time (ns)" );

        // Run for 100 iterations
        for (int test = 1; test <= 100; test++) {
            int k = (n + 1) / 2; // Median is the middle element in the sorted order

            // Calculate Execution time
            long start = System.nanoTime();
            int algoMedian = findKthSmallest(arr, 0, n - 1, k);
            long duration = System.nanoTime() - start;

            // Find the median by OG Way
            int[] sortedArray = Arrays.copyOf(arr, arr.length); // Copy the original array to sort it
            int actualMedian = findActualMedian(sortedArray);

            // Results
            System.out.println(n + " \t" + duration);


            // Uncomment this and header line above if you wanna check algo is correct and comment above line for better clarity
//            System.out.println(n + " \t" + algoMedian  + " \t" + actualMedian  + " \t" + duration );



            n += 100; // Increase the size for the next test
            arr = createRandomArray(n);
        }
    }
}
