
public class PrimeFinderLongLived implements Runnable {
	
	private LLLock lock;
	private int lowerBound;
	
	public PrimeFinderLongLived(LLLock lock){
		this.lock = lock;
		lowerBound = Main.lowerBound;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//get next counter, and test for primality
		while (true){
			int n = getNext();
			if (n > 0) {
				Main.output[n-lowerBound] = isPrime(n);
			} else {
				break;
			}
		}
		
	}
	
	private int getNext(){
		lock.lock();
		int i = Main.getAndIncrement();
		lock.unlock();
		return i;
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
