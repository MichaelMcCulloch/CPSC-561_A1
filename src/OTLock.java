
public class OTLock {
	private volatile boolean tooLate;
	private volatile int[] level;
	private volatile int[] victim;
	private int n;
	
	public OTLock(int nThreads){
		tooLate = false;
		n = nThreads;
		level = new int[nThreads];
		victim = new int[nThreads];
		for (int i = 0; i < nThreads; i++) {
			level[i] = 0;
			victim[i] = -1;
		}
	}
	
	public void reset(){
		tooLate = false;
		for (int i = 0; i < n; i++) {
			level[i] = 0;
			victim[i] = -1;
		}
	}
	
	public boolean lock(){
		//fastpath
		if (tooLate) return false;
		
		//compete
		int pid = (int)Thread.currentThread().getId() % n;
		for (int i = 1; i < n; i++) { //each level
			level[pid] = 1;
			victim[i] = pid;
			for (int j = 0; j < n; j++) { //each thread
				while (j != pid && level[j] >= i && victim[i] == pid){}
			}
		}
		
		//check for looser
		if (tooLate){ //looser
			level[pid] = 0;
			return false;
		} else { //winner
			tooLate = true;
			level[pid] = 0;
			return true;
		}
	}
	
	
	

}
