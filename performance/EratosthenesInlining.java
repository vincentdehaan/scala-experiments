import java.lang.Math;

public class EratosthenesInlining {
    private static boolean[] data;
    public static void main(String[] args) {
        int MAX = 1000 * 1000 * 1000;

        long t1 = System.nanoTime();

        // This initializes all values to false.
        // We will set them to true to mark them NOT prime.
        data = new boolean[MAX + 1];
        data[0] = true;
        data[1] = true;

        int sqrt = (int) Math.sqrt((double) MAX);

        for(int i = 2; i < sqrt; i++) {
            if(isPrime(i)) {
                for(int j = i * i; j < MAX; j += i) {
                    setNotPrime(j);
                }
            }
        }

        int primeCount = 0;

        for(int i = 0; i < MAX; i++){
            if(isPrime(i)) primeCount++;
        }

        long t2 = System.nanoTime();

        System.out.println(
            "The number of primes less than " + 
            MAX + 
            " is: " + primeCount);
        
        System.out.println("Time: " + ((t2 - t1) / 1000000) + "ms");
    }

    private static boolean isPrime(int idx) {
        return !data[idx];
    }

    private static void setNotPrime(int idx) {
        data[idx] = true;
    }
}