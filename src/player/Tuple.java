package player;

public class Tuple extends TuneSequence {

	Fraction multiplier = null;
	
	public Tuple(int noteCount) {
		switch (noteCount) {
			case 2:
				multiplier = new Fraction(3,2);
				break;
			case 3:
				multiplier = new Fraction(2,3);
				break;
			case 4:
				multiplier = new Fraction(3,4);
				break;
			default:
				throw new RuntimeException(String.format("Note count to Tuple must be either 2,3, or 4, not %d",noteCount));
		}
	}
	
	@Override
	public void accept(SchedulableVisitor v) {
		v.visit(this);
	}
	
	
}
