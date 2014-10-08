import java.util.Arrays;

public class BinarySearch {
    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8};
        System.out.println(Arrays.toString(array) + " - array");

        int index = binarySearch(array, 7, 0, array.length - 1);
        System.out.println(index + " - index for value");
    }

    //	http://www.algolist.net/Algorithms/Binary_search
//	Algorithm
//	Algorithm is quite simple. It can be done either recursively or iteratively:
//	get the middle element;
//	if the middle element equals to the searched value, the algorithm stops;
//	otherwise, two cases are possible:
//	searched value is less, than the middle element. In this case, go to the step 1 for the part of the array, before middle element.
//	searched value is greater, than the middle element. In this case, go to the step 1 for the part of the array, after middle element.
    public static int binarySearch(int[] array, int value, int left, int right) {
        System.out.print("left: " + left + ", right: " + right);

        // validate invalid boundaries since it can produce an invalid middle index
        if (left > right) {
            return -1;
        }

        int middle = (left + right) / 2;
        System.out.println(", middle: " + middle);

        if (array[middle] == value) {
            return middle;
        } else if (array[middle] > value) {
            int leftArray[] = new int[middle];
            for (int i = left; i < middle; i++) {
                leftArray[i] = array[i];
            }
            System.out.println(Arrays.toString(leftArray) + " - left array");

            // search left subarray
            return binarySearch(array, value, left, middle - 1);
        } else {
            int rightArrays[] = new int[right - middle];
            int rigthArrayIndex = 0;
            for (int i = middle + 1; i <= right; i++) {
                rightArrays[rigthArrayIndex] = array[i];
                rigthArrayIndex++;
            }
            System.out.println(Arrays.toString(rightArrays) + " - right array");

            // search right subarray
            return binarySearch(array, value, middle + 1, right);
        }
    }


    /*
    这是非递归版本 适合于java和c++
* searches for a value in sorted array
*   arr is an array to search in
*   value is searched value
*   left is an index of left boundary
*   right is an index of right boundary
* returns position of searched value, if it presents in the array
* or -1, if it is absent
*/
    int binarySearch1(int arr[], int value, int left, int right) {
        while (left <= right) {
            int middle = (left + right) / 2;
            if (arr[middle] == value)
                return middle;
            else if (arr[middle] > value)
                right = middle - 1;
            else
                left = middle + 1;
        }
        return -1;
    }
}