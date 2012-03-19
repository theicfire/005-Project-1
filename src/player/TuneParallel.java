package player;

/*
 * Extends TuneSequence only so that it appears
 * different to a visitor
 * in truth
 */

public class TuneParallel extends TuneSequence {

	@Override
	public void accept(SchedulableVisitor v) {
		v.visit(this);
	}
	
}
