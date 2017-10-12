
public class LLLock {
	public static volatile boolean[] flags;
	public static volatile int victim = -1;
	
	public LLLock(int nThreads){
		flags = new boolean[nThreads];
	}
	public void lock(int pid){
		flags[pid] = true;
		victim = pid;
		boolean others = false;
		
		while (victim == pid && others){ //while the victim, scan the list
			for (int i = 0; i < flags.length; i++) {
				if (i != pid && flags[i]) {
					others = true;
					break;
				}
			}
		}
	}
	public void release(int pid){
		flags[pid] = false;
	}
	
}
