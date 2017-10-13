import java.util.concurrent.atomic.AtomicInteger;

public class Main {
	
	public static int nThreads = 1;
	public static int lowerBound, upperBound, difference;
	private static int sharedCounter = 0;
	public static boolean[] output;
	public static AtomicInteger atomCounter;
	public static int[] threads = {1, 4, 8, 16, 32};
	
	static OTLock[] otLocks;
	
	private static long[][] timeInNS = new long[5][3];
	
	public static void main(String[] args) {
		int max = 125000000;//0000000;
		System.out.println("test");
		lowerBound = (int) Math.floor(max/2);
		upperBound = max;
		difference = upperBound - lowerBound;
		atomCounter = new AtomicInteger(lowerBound);
		
		otLocks = new OTLock[difference]; 
		System.out.println("Allocating an unfortunate amount of memory for one-time locks...");
		for (int i = 0; i < otLocks.length; i++) {
			otLocks[i] = new OTLock(nThreads);
		}
		System.out.println("Done!");
		
		for (int i = 0; i < threads.length; i++) {
			nThreads = threads[i];
			testPrimes3Ways(i);
		}
		
		
		printTimes();
		
		
	}
	
	public static void printTimes(){
		System.out.println("\t1\t4\t8\t16\t32");
		System.out.print("Lock:\t");
		for (int i = 0; i < threads.length; i++) {
			System.out.print(timeInNS[i][0]+"\t");
		}
		System.out.print("\nOTLock:\t");
		for (int i = 0; i < threads.length; i++) {
			System.out.print(timeInNS[i][1]+"\t");
		}
		System.out.print("\nAtomic:\t");
		for (int i = 0; i < threads.length; i++) {
			System.out.print(timeInNS[i][2]+"\t");
		}
	}
	
	
	public static void printPrimes() {
		for (int i = 0; i < output.length; i++) {
			if (output[i]){
				System.out.println(lowerBound + i);
			}
		}
	}
	
	public static void reset(){
		for (int i = 0; i < otLocks.length; i++) { // dont reallocate memory each time
			otLocks[i].reset();
		}
		atomCounter = new AtomicInteger(lowerBound);
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
	
	
	
	public static void testPrimes3Ways(int i){
		reset();
		lllock(i);
		//printPrimes();
		
		reset();
		otlock(i);
		//printPrimes();
		
		reset();
		lockFree(i);
		//printPrimes();
		
	}
	
	public static void lllock(int currThreads){
		LLLock lock = new LLLock(nThreads);
		Thread[] threads = new Thread[nThreads];
		
		//create the threads simultaneously
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new PrimeFinderLongLived(lock));
		}
		
		long start = System.nanoTime();
		//start the threads simultaneously
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
		long end = System.nanoTime();
		timeInNS[currThreads][0] = end - start;
		System.out.println("LLLock Done");
		
	}
	public static void otlock(int currThreads){
		
		Thread[] threads = new Thread[nThreads];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new PrimeFinderOneTime(otLocks));
		}
		
		long start = System.nanoTime();
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
		long end = System.nanoTime();
		timeInNS[currThreads][1] = end - start;
		
		System.out.println("OTLocks Done");

		
	}
	public static void lockFree(int currThreads){
		Thread[] threads = new Thread[nThreads];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new PrimeFinderAtomic());
		}
		
		long start = System.nanoTime();
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
		long end = System.nanoTime();
		timeInNS[currThreads][2] = end - start;
		System.out.println("LockFree Done");
	}

}
