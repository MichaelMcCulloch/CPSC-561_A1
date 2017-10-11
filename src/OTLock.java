
public class OTLock {
	public static boolean[] flags;
	public static boolean timeOut = false;
	public static int victim = -1;

	public OTLock(int threads){
		flags = new boolean[threads];
		for (int i = 0; i < threads; i++){
			flags[i] = false;
		}
	}
	
	public boolean lock(int pid){
		
		if (timeOut) return false;
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
		//This section is Mutually Exclusive up to 'flags[pid] = false;'
		
		if (timeOut) {
			return false;
		}
		else {
			timeOut = true;
			flags[pid] = false;
			return true;
		}
		
		
		
	}
}
