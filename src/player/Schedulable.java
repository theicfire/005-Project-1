package player;

public class Schedulable {

	public void accept(SchedulableVisitor v) {
		v.visit(this);
	}
	
}
