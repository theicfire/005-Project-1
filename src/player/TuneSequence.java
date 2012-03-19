package player;

import java.util.Stack;
import java.util.Iterator;

public class TuneSequence extends Schedulable implements Iterable<Schedulable> {

	
	Stack<Schedulable> contents = new Stack<Schedulable>();
	
	public void add(Schedulable s) {
		contents.push(s);
	}
	
	public Iterator<Schedulable> iterator() {
		return contents.iterator();
	}
	
}
