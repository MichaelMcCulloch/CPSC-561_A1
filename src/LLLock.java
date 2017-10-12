
public class LLLock {
	private volatile int[] level;
	private volatile int[] victim;
	private int n;
	
	public LLLock(int nThreads){
		n = nThreads;
		level = new int[nThreads];
		victim = new int[nThreads];
		for (int i = 0; i < nThreads; i++) {
			level[i] = 0;
			victim[i] = -1;
		}
	}
	
	public void lock(){
		int pid = (int)Thread.currentThread().getId() % n;
		for (int i = 1; i < n; i++) { //each level
			level[pid] = 1;
			victim[i] = pid;
			for (int j = 0; j < n; j++) { //each thread
				while (j != pid && level[j] >= i && victim[i] == pid){}
			}
		}
	}
	
	public void unlock() {
        int pid = (int)Thread.currentThread().getId() % n;
        level[pid] = 0;
    }
	
	
}
