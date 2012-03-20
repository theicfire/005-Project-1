package player;

import sound.Pitch;

public class KeySignature {
	
	
	public Pitch getPitch(char note, int octave) {
		
		return new Pitch('C');
		
	}
	public KeySignature fromAccidental(char name, String modifier) {
		return new KeySignature("d");
	}
	
	public KeySignature(String v) {
		
	}

}
