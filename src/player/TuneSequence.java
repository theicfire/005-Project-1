package player;

import java.util.Stack;
import java.util.Iterator;

public class TuneSequence implements Schedulable,Iterable<Schedulable> {

	
	Stack<Schedulable> contents = new Stack<Schedulable>();
	
	public void add(Schedulable s) {
		contents.push(s);
	}
	
	public Iterator<Schedulable> iterator() {
		return contents.iterator();
	}
	
	public void accept(SchedulableVisitor v) {
		v.visit(this);
	}
	
}
