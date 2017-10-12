
public class PrimeFinderOneTime implements Runnable {
	
	private OTLock[] locks;
	private int lowerBound;
	
	public PrimeFinderOneTime(OTLock[] locks) {
		this.locks = locks;
		lowerBound = Main.lowerBound;
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		//get next counter, and test for primality
		for (int i = 0; i < locks.length; i++) { //try the lock for each, if pass, great, if not skip it.
			if (locks[i].lock()) {
				Main.output[i] = isPrime(i+lowerBound);
			} else{
				continue;
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
