import java.util.concurrent.atomic.AtomicInteger;

public class PrimeFinderAtomic implements Runnable {

	public AtomicInteger atomInt;
	
	public PrimeFinderAtomic(){
		atomInt = Main.atomCounter;
	}
	
	public void run(){
		int lower = Main.lowerBound, upper = Main.upperBound, diff = Main.difference;
		boolean[] b = Main.output;
		
		while (true){
			int next = atomInt.getAndIncrement();
			if (next <= upper) {
				b[next - lower] = isPrime(next);
			} else {
				break;
			}
		}
	}
	
	public boolean isPrime(long n){
	    if(n < 2) return false;
	    if(n == 2 || n == 3) return true;
	    if(n%2 == 0 || n%3 == 0) return false;
	    long sqrtN = (long)Math.sqrt(n)+1;
	    for(long i = 6; i <= sqrtN; i += 6) {
	        if(n%(i-1) == 0 || n%(i+1) == 0) return false;
	    }
	    return true;	
	}

}
