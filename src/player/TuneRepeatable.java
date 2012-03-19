package player;

import java.util.List;

public class TuneRepeatable extends TuneSequence {
	
	
	public Schedulable body() {
		
		if (contents.size() == 0) {
			return null;
		} else {
			return contents.elementAt(0);
		}
		
	}
	
	public List<Schedulable> endings() {
		
		int contentLength = contents.size();
		if (contentLength <= 1) {
			return null;
		} else {
			return contents.subList(1, contentLength);
		}
		
	}
	
	public boolean hasMultipleEndings() {
		return (this.endings() != null);
	}
	
	public static TuneRepeatable fromSchedulable(Schedulable s) {
		
		TuneRepeatable out = new TuneRepeatable();
		out.add(s);
		return out;
			
	}
	
	
}
