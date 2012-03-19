package player;



public class TunePrimitive implements Schedulable {

	Fraction duration = null;
	sound.Pitch midiPitch = null;
	int midiNote = 0;
	boolean isNote = false;
	
	public static TunePrimitive Note(Fraction d,sound.Pitch miPi) {
		TunePrimitive out = new TunePrimitive(d);
		out.midiPitch = miPi;
		out.midiNote = miPi.toMidiNote();
		out.isNote = true;
		return out;
	}
	
	public static TunePrimitive Rest(Fraction d) {
		return new TunePrimitive(d);
	}
	
	public TunePrimitive(Fraction d) {
		duration = d;
	}
	
	public int getNote() {
		return midiNote;
	}
	
	public Fraction getDuration() {
		return duration;
	}
	
	@Override
	public void accept(SchedulableVisitor v) {
		v.visit(this);
	}
	
}
