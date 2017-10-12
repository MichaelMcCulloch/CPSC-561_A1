
public class Main {
	
	public static int nThreads = 1;
	public static int lowerBound, upperBound, difference;
	private static int sharedCounter = 0;
	public static boolean[] output;
	
	private static long[] timeInNS = new long[3];
	
	public static void main(String[] args) {
		int max = Integer.parseInt(args[0]);
		nThreads = Integer.parseInt(args[1]);
		System.out.println("test");
		lowerBound = (int) Math.floor(max/2);
		upperBound = max;
		difference = upperBound - lowerBound;
		
		testPrimes3Ways();
		
		for (int i = 0; i < output.length; i++) {
			if (output[i]){
				System.out.println(lowerBound + i);
			}
		}
		
	}
	
	
	public static void reset(){
		sharedCounter = lowerBound;
		output = new boolean[difference+1];
	}
	
	public static int getAndIncrement(){
		if (sharedCounter <= upperBound){
			return sharedCounter++;
		} else {
			return -1; //done
		}
	}
	
	
	
	public static void testPrimes3Ways(){
		reset();
		long start = System.nanoTime();
		lllock();
		long end = System.nanoTime();
		timeInNS[0] = end - start;
		System.out.println("LLLock Done");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reset();
		start = System.nanoTime();
		otlock();
		end = System.nanoTime();
		timeInNS[1] = end - start;
		System.out.println("OTLocks Done");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//reset();
		lockFree();
		System.out.println("LockFree Done");
	}
	
	public static void lllock(){
		LLLock lock = new LLLock(nThreads);
		Thread[] threads = new Thread[nThreads];
		
		
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new PrimeFinderLongLived(lock));
		}
		
		for (int i = 0; i < threads.length; i++) {
			
			threads[i].start();
		}		
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	public static void otlock(){
		OTLock[] locks = new OTLock[difference];
		for (int i = 0; i < locks.length; i++) {
			locks[i] = new OTLock(nThreads);
		}
		
		Thread[] threads = new Thread[nThreads];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new PrimeFinderOneTime(locks));
		}
		
		
		for (int i = 0; i < threads.length; i++) {
			
			threads[i].start();
		}		
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public static void lockFree(){
	
	}

}
