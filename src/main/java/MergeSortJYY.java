package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jyy on 2014/11/10.
 */
public class MergeSortJYY {
    // Use a fixed seed to always get the same random values back
    private static final Random random = new Random();


    /**
     * Generates an array of {@code elements} random elements
     * @param elements the number of elements requested in the array
     * @return an array of {@code elements} random elements
     */
    private static int[] generateArray(int elements) {
        int[] array = new int[elements];
        for (int i = 0; i < elements; ++i) {
            array[i] = random.nextInt(100);
        }
        return array;
    }

    private static int[] mergeSort(int[] array) {
        return merge(array,0,array.length-1);
    }

    private static int[] merge(int[] array, int start, int end) {
        if ( start >= end ) {
            return array;
        }

        int middle =  (end+start)/2;
        merge(array,start,middle);
        merge(array,middle+1,end);//注意这两个middle+1的地方

        innerMerge(array,start,middle,end);
        return array;
    }

    private static void innerMerge(int[] array, int start, int middle, int end) {
        List<Integer> list = new ArrayList<Integer>();//存储临时数组

        int i = start;
        int j = middle+1 ;//注意这两个middle+1的地方

        while ( i<=middle && j<=end ) {
            if (array[i] < array[j]) {
                list.add(array[i++]);
            } else {
                list.add(array[j++]);
            }
        }

        while(i<=middle) {
            list.add(array[i++]);
        }

        while(j<=end) {
            list.add(array[j++]);
        }

        //System.out.println(list);

        //复制回原数组
        i=0;
        for(int k=start;k<=end;k++) {
            array[k] = list.get(i++);
        }

    }

    public static void main(String[] args) {
        int[] array = generateArray(4);
        for ( int i : array ) {
            System.out.print(i+",");
        }

        mergeSort(array);

        System.out.println();
        for ( int i : array ) {
            System.out.print(i+",");
        }

    }
}
