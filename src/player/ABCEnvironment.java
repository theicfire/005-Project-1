package player;

import java.util.HashMap;

public class ABCEnvironment {
	
	HashMap<String,String> headers = new HashMap<String,String>();
	Schedulable ABCTree = null;
	
	int ticksPerDefaultNote;
	int tempo;
	
	public void setHeader(char c,String v) {
		
		String k = "none";
		switch (c) {
		case 'X':
			k = "Track Number";
			break;
		case 'T':
			k = "Title";
			break;
		case 'C':
			k = "Composer";
			break;
		case 'M':
			k = "Meter";
			break;
		case 'L':
			k = "Default note length";
			break;
		case 'K':
			k = "Key Signature";
			break;
		case 'Q':
			k = "Tempo";
			break;
		}
		
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
