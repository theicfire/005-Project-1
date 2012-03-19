package player;


/*
 * the schedulable super class
 * defines the accept method
 * so that a SchedulableVisitor 
 * can come and visit
 */
public interface Schedulable {

	public void accept(SchedulableVisitor v);
	
}
