package player;

import sound.Pitch;

public class KeySignature {
	
	
	public Pitch getPitch(String note, int octave) {
		
		return new Pitch('C');
		
	}
	public KeySignature fromAccidental(String name, String modifier) {
		return new KeySignature("d");
	}
	
	public KeySignature(String v) {
		
	}

}
