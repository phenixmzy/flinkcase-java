package com.myplan.suanfa.paixu;

public class QSort {
    public static void main(String[] args) {
        int[] dataArr = new int[] {3,10,2,15,7,90,23,0,1};
        printSort(dataArr);
        qsort(dataArr,0, dataArr.length-1);
        printSort(dataArr);
    }

    public static int[] qsort(int[] arr, int low, int high) {
        int p = arr[low];
        int l = low;
        int h = high;
        while (l < h) {
            while ((l<h) && (arr[h]>p))
                h--;

            while ((l<h) && (arr[l] < p))
                l++;

            if ((l<h) && (arr[l] == arr[h])) {
                l++;
            } else {
                int temp = arr[l];
                arr[l] = arr[h];
                arr[h] = temp;
            }
        }
        if (l - 1 > low) arr = qsort(arr, low, l-1);
        if (h + 1 < high) arr = qsort(arr, h+1, high);
        return arr;
    }

    public static void printSort(int[] arr) {
        System.out.println();
        for (int item : arr) {
            System.out.print(item + ",");
        }
    }
}
