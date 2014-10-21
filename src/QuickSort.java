public class QuickSort {
    // int b[] = { 49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 5, 4, 62, 99,
    // 98, 54, 56, 17, 18, 23, 34, 15, 35, 25, 53, 51 };

    // int b[] = { 49, 38, 97, 65, 13};

    int b[] = { 49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 5, 4, 62, 99,
            98, 54, 56, 17, 18, 23, 34, 15, 35, 25, 53, 51 };

    public QuickSort() {
        quick(b);
        for (int i = 0; i < b.length; i++)
            System.out.println(b[i]);
    }

    public static void main(String[] args) {
        new QuickSort();
    }

    public int getMiddle(int[] list, int low, int high) {
        int tmp = list[low]; // 数组的第一个作为中轴
        while (low < high) {
            while (low < high && list[high] >= tmp) {

                high--;
            }
            list[low] = list[high]; // 比中轴小的记录移到低端
            print(list);
            while (low < high && list[low] <= tmp) {
                low++;
            }
            list[high] = list[low]; // 比中轴大的记录移到高端

            print(list);
        }
        list[low] = tmp; // 中轴记录到尾

        print(list);

        return low; // 返回中轴的位置
    }

    public void print(int[] list) {
        String str = "";
        for (int t : list) {
            str += t + ",";
        }
        System.out.println(str);
    }

    public void _quickSort(int[] list, int low, int high) {
        if (low < high) {
            int middle = getMiddle(list, low, high); // 将list数组进行一分为二
            System.out.println("middle:" + middle);
            _quickSort(list, low, middle - 1); // 对低字表进行递归排序
            _quickSort(list, middle + 1, high); // 对高字表进行递归排序
        }
    }

    public void quick(int[] a2) {
        if (a2.length > 0) { // 查看数组是否为空
            _quickSort(a2, 0, a2.length - 1);
        }
    }
}
