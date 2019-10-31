public class ArrayTestJava {
    public static int[] arr;
    public static int[] arr2;
    public static int size = 10 * 1000 * 1000;

    public static void main(String[] args) {

        arr = new int[size];
        for(int i = 0; i < size; i++) {
            arr[i] = i;
        }
        arr2 = new int[size];

        long t1 = System.nanoTime();
        System.out.println(map2());
        long t2 = System.nanoTime();
        System.out.println((t2 - t1) / 1000);
    }

    public static int map2(){
        int i = 0;
        while(i < size) {
            arr2[i] = arr[i] * 2;
            i = i + 1;
        }
        return arr2[1234500];
    }
}