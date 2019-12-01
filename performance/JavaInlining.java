public class JavaInlining {
    public static void main(String[] args) {
        int MAX = 1000 * 1000 * 10;

        long res = 0;
        long t1 = System.nanoTime();
        for(int i = 0; i < MAX; i++) {
            res = add(i, res);
        }
        long t2 = System.nanoTime();

        System.err.println(res + "\t" + ((t2 - t1) / 1000));
    }

    public static long add(long a, long b) {
        return a + b;
    }
}