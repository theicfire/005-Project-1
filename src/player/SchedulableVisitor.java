package player;

/*
 * Interface for any visitors that can walk A Schedulable tree
 */

public interface SchedulableVisitor {

	public void visit(TuneSequence s);
	public void visit(TuneParallel p);
	public void visit(TuneRepeatable r);
	public void visit(Tuple t);
	public void visit(TunePrimitive p);
	
}
