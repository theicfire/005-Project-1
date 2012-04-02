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
	
	//for tests
	public boolean equals(ABCEnvironment other) {
		//root must be equal
		//ticks per must be equal
		//tempo must be equal
		//headers must be equal
		boolean ticksOK = (ticksPerDefaultNote == other.ticksPerDefaultNote);
		boolean tempoOK = (tempo == other.tempo);
		boolean headersOK = headers.equals(other.headers);
		
		SchedulePrinter myRootPrinter = new SchedulePrinter();
		SchedulePrinter otherRootPrinter = new SchedulePrinter();
		
		ABCTree.accept(myRootPrinter);
		other.ABCTree.accept(otherRootPrinter);
		
		boolean treeOK = myRootPrinter.outString.toString().equals(otherRootPrinter.outString.toString());
		
		return ticksOK && tempoOK && headersOK && treeOK;
		
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder("ABCEnvironment:\n");
		b.append("Headers:\n");
		for (String s : headers.keySet()) {
			b.append(String.format("\t%s: %s\n" , s,headers.get(s)));
		}
		b.append(String.format("Tempo: %d\n", tempo));
		b.append(String.format("Ticks per default: %d\n", ticksPerDefaultNote));
		b.append("Tree:\n");
		
		SchedulePrinter p = new SchedulePrinter();
		p.depth++;
		ABCTree.accept(p);
		b.append(p.outString);
		
		return b.toString();
	}
	

}
