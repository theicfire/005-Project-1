package player;

public class Note extends TunePrimitive {

	
	sound.Pitch midiPitch = null;
	int midiNote;
	
	public Note(Fraction d,sound.Pitch miPi) {
		duration = d;
		midiPitch = miPi;
		midiNote = miPi.toMidiNote();
	}
	
	public int getNote() {
		return midiNote;
	}
	
	public Fraction getDuration() {
		return duration;
	}
	
	
}
