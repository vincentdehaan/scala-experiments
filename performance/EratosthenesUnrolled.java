import java.lang.Math;

public class EratosthenesUnrolled {
    public static void main(String[] args) {
        int MAX = 1000 * 1000 * 1000;

        long t1 = System.nanoTime();

        // This initializes all values to false.
        // We will set them to true to mark them NOT prime.
        boolean[] data = new boolean[MAX + 1];
        data[0] = true;
        data[1] = true;

        int sqrt = (int) Math.sqrt((double) MAX);

        for(int i = 2; i < sqrt; i++) {
            if(!data[i]) {
                int stop = MAX / i - i;
                int ii = i * i;
                for(int j = 0; j <= stop; j++) {
                    data[ii + i * j] = true;
                }
            }
        }

        int primeCount = 0;

        for(int i = 0; i < MAX; i++){
            if(!data[i]) primeCount++;
        }

        long t2 = System.nanoTime();

        System.out.println(
            "The number of primes less than " + 
            MAX + 
            " is: " + primeCount);
        
        System.out.println("Time: " + ((t2 - t1) / 1000000) + "ms");
    }
}