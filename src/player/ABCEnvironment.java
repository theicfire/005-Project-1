package player;

import java.util.HashMap;

public class ABCEnvironment {
	
	HashMap<String,String> headers = new HashMap<String,String>();
	Schedulable ABCTree = null;
	
	int ticksPerDefaultNote;
	int tempo;
	
	public void setHeader(String k,String v) {
		headers.put(k, v);
	}
	
	public void setTempo(int n) {
		tempo = n;
	}
	
	public void setTicksPerDefaultNote(int t) {
		ticksPerDefaultNote = t;
	}
	
	public void setTreeRoot(Schedulable s) {
		ABCTree = s;
	}
	
	public Schedulable getTreeRoot() {
		return ABCTree;
	}
	

}
