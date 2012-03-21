package player;

import java.util.HashMap;

import sound.Pitch;

public class KeySignature {
	private HashMap<Character, Pitch> keyPitches;
	private HashMap<String, Pitch> modNotes;
	public final boolean modified;
	
	
	public Pitch getPitch(char note, int octave) {
		if (!modified)
			return keyPitches.get(note).octaveTranspose(octave);
		else {
			if (modNotes.containsKey(Character.toString(note) + octave)) {
				return modNotes.get(Character.toString(note) + octave);
			}
			return keyPitches.get(note).octaveTranspose(octave);
		}
		
	}
	// Change modifier to int
	public KeySignature fromAccidental(char name, String modifier, int octave) {
		int pitchChange = 0;
		if (modifier.equals("#"))
			pitchChange = 1;
		else if (modifier.equals("##"))
			pitchChange = 2;
		else if (modifier.equals("b"))
			pitchChange = -1;
		else if (modifier.equals("bb"))
			pitchChange = -2;
		else if (modifier.equals("="))
			pitchChange = 0;
			
		HashMap<Character, Pitch> keySig = (HashMap<Character, Pitch>) keyPitches.clone();
		HashMap<String, Pitch> mod = (HashMap<String, Pitch>) modNotes.clone();
		mod.put(Character.toString(name) + octave, new Pitch(name).accidentalTranspose(pitchChange).octaveTranspose(octave));
		
		return new KeySignature(keySig, mod);
		
	}
	
	public KeySignature(String key) {
		HashMap<String, Integer> noteToNum = new HashMap<String, Integer>();
		noteToNum.put("C", 0);
		noteToNum.put("Am", 0);
		
		noteToNum.put("F", 1);
		noteToNum.put("Dm", 1);
		
		noteToNum.put("Bb", 2);
		noteToNum.put("Gm", 2);
		
		noteToNum.put("Eb", 3);
		noteToNum.put("Cm", 3);
		
		noteToNum.put("Ab", 4);
		noteToNum.put("Fm", 4);
		
		noteToNum.put("Db", 5);
		noteToNum.put("Bbm", 5);
		
		noteToNum.put("Gb", 6);
		noteToNum.put("Ebm", 6);
		
		noteToNum.put("Cb", 7);
		noteToNum.put("Abm", 7);
		
		noteToNum.put("Fb", 8);
		noteToNum.put("Dbm", 8);
		
		noteToNum.put("G", 9);
		noteToNum.put("Em", 9);
		
		noteToNum.put("D", 10);
		noteToNum.put("Bm", 10);
		
		noteToNum.put("A", 11);
		noteToNum.put("F#m", 11);
		
		noteToNum.put("E", 12);
		noteToNum.put("C#m", 12);
		
		noteToNum.put("B", 13);
		noteToNum.put("G#m", 13);
		
		noteToNum.put("F#", 14);
		noteToNum.put("D#m", 14);
		
		noteToNum.put("C#", 15);
		noteToNum.put("A#m", 15);
		
		noteToNum.put("G#", 16);
		noteToNum.put("E#", 16);
		
		noteToNum.put("D#", 17);
		noteToNum.put("B#m", 17);
		
		keyPitches = new HashMap<Character, Pitch>();
		modNotes = new HashMap<String, Pitch>();
		
		switch (noteToNum.get(key)) {
		case 0 : 
			keyPitches.put('C', new Pitch('C'));
			keyPitches.put('D', new Pitch('D'));
			keyPitches.put('E', new Pitch('E'));
			keyPitches.put('F', new Pitch('F'));
			keyPitches.put('G', new Pitch('G'));
			keyPitches.put('A', new Pitch('A'));
			keyPitches.put('B', new Pitch('B'));
			break;
		case 1 : 
			keyPitches.put('C', new Pitch('C'));
			keyPitches.put('D', new Pitch('D'));
			keyPitches.put('E', new Pitch('E'));
			keyPitches.put('F', new Pitch('F'));
			keyPitches.put('G', new Pitch('G'));
			keyPitches.put('A', new Pitch('A'));
			keyPitches.put('B', new Pitch('B').accidentalTranspose(-1));
			break;
		case 2 :
			keyPitches.put('C', new Pitch('C'));
			keyPitches.put('D', new Pitch('D'));
			keyPitches.put('E', new Pitch('E').accidentalTranspose(-1));
			keyPitches.put('F', new Pitch('F'));
			keyPitches.put('G', new Pitch('G'));
			keyPitches.put('A', new Pitch('A'));
			keyPitches.put('B', new Pitch('B').accidentalTranspose(-1));

			break;
		case 3 : 
			keyPitches.put('C', new Pitch('C'));
			keyPitches.put('D', new Pitch('D'));
			keyPitches.put('E', new Pitch('E').accidentalTranspose(-1));
			keyPitches.put('F', new Pitch('F'));
			keyPitches.put('G', new Pitch('G'));
			keyPitches.put('A', new Pitch('A').accidentalTranspose(-1));
			keyPitches.put('B', new Pitch('B').accidentalTranspose(-1));
			
			break;
		case 4 : 
			keyPitches.put('C', new Pitch('C'));
			keyPitches.put('D', new Pitch('D').accidentalTranspose(-1));
			keyPitches.put('E', new Pitch('E').accidentalTranspose(-1));
			keyPitches.put('F', new Pitch('F'));
			keyPitches.put('G', new Pitch('G'));
			keyPitches.put('A', new Pitch('A').accidentalTranspose(-1));
			keyPitches.put('B', new Pitch('B').accidentalTranspose(-1));
			break;
		case 5 : 
			keyPitches.put('C', new Pitch('C'));
			keyPitches.put('D', new Pitch('D').accidentalTranspose(-1));
			keyPitches.put('E', new Pitch('E').accidentalTranspose(-1));
			keyPitches.put('F', new Pitch('F'));
			keyPitches.put('G', new Pitch('G').accidentalTranspose(-1));
			keyPitches.put('A', new Pitch('A').accidentalTranspose(-1));
			keyPitches.put('B', new Pitch('B').accidentalTranspose(-1));
			break;
		case 6 : 
			keyPitches.put('C', new Pitch('C').accidentalTranspose(-1));
			keyPitches.put('D', new Pitch('D').accidentalTranspose(-1));
			keyPitches.put('E', new Pitch('E').accidentalTranspose(-1));
			keyPitches.put('F', new Pitch('F'));
			keyPitches.put('G', new Pitch('G').accidentalTranspose(-1));
			keyPitches.put('A', new Pitch('A').accidentalTranspose(-1));
			keyPitches.put('B', new Pitch('B').accidentalTranspose(-1));
			break;
		case 7 : 
			keyPitches.put('C', new Pitch('C').accidentalTranspose(-1));
			keyPitches.put('D', new Pitch('D').accidentalTranspose(-1));
			keyPitches.put('E', new Pitch('E').accidentalTranspose(-1));
			keyPitches.put('F', new Pitch('F').accidentalTranspose(-1));
			keyPitches.put('G', new Pitch('G').accidentalTranspose(-1));
			keyPitches.put('A', new Pitch('A').accidentalTranspose(-1));
			keyPitches.put('B', new Pitch('B').accidentalTranspose(-1));
			break;
		case 8 : 
			keyPitches.put('C', new Pitch('C').accidentalTranspose(-1));
			keyPitches.put('D', new Pitch('D').accidentalTranspose(-1));
			keyPitches.put('E', new Pitch('E').accidentalTranspose(-1));
			keyPitches.put('F', new Pitch('F').accidentalTranspose(-1));
			keyPitches.put('G', new Pitch('G').accidentalTranspose(-1));
			keyPitches.put('A', new Pitch('A').accidentalTranspose(-1));
			keyPitches.put('B', new Pitch('B').accidentalTranspose(-2));
			break;
		case 9 : 
			keyPitches.put('C', new Pitch('C'));
			keyPitches.put('D', new Pitch('D'));
			keyPitches.put('E', new Pitch('E'));
			keyPitches.put('F', new Pitch('F').accidentalTranspose(1));
			keyPitches.put('G', new Pitch('G'));
			keyPitches.put('A', new Pitch('A'));
			keyPitches.put('B', new Pitch('B'));
			break;
		case 10 : 
			keyPitches.put('C', new Pitch('C').accidentalTranspose(1));
			keyPitches.put('D', new Pitch('D'));
			keyPitches.put('E', new Pitch('E'));
			keyPitches.put('F', new Pitch('F').accidentalTranspose(1));
			keyPitches.put('G', new Pitch('G'));
			keyPitches.put('A', new Pitch('A'));
			keyPitches.put('B', new Pitch('B'));
			break;
		case 11 : 
			keyPitches.put('C', new Pitch('C').accidentalTranspose(1));
			keyPitches.put('D', new Pitch('D'));
			keyPitches.put('E', new Pitch('E'));
			keyPitches.put('F', new Pitch('F').accidentalTranspose(1));
			keyPitches.put('G', new Pitch('G').accidentalTranspose(1));
			keyPitches.put('A', new Pitch('A'));
			keyPitches.put('B', new Pitch('B'));
			break;
		case 12 : 
			keyPitches.put('C', new Pitch('C').accidentalTranspose(1));
			keyPitches.put('D', new Pitch('D').accidentalTranspose(1));
			keyPitches.put('E', new Pitch('E'));
			keyPitches.put('F', new Pitch('F').accidentalTranspose(1));
			keyPitches.put('G', new Pitch('G').accidentalTranspose(1));
			keyPitches.put('A', new Pitch('A'));
			keyPitches.put('B', new Pitch('B'));
			break;
		case 13 : 
			keyPitches.put('C', new Pitch('C').accidentalTranspose(1));
			keyPitches.put('D', new Pitch('D').accidentalTranspose(1));
			keyPitches.put('E', new Pitch('E'));
			keyPitches.put('F', new Pitch('F').accidentalTranspose(1));
			keyPitches.put('G', new Pitch('G').accidentalTranspose(1));
			keyPitches.put('A', new Pitch('A').accidentalTranspose(1));
			keyPitches.put('B', new Pitch('B'));
			break;
		case 14 : 
			keyPitches.put('C', new Pitch('C').accidentalTranspose(1));
			keyPitches.put('D', new Pitch('D').accidentalTranspose(1));
			keyPitches.put('E', new Pitch('E').accidentalTranspose(1));
			keyPitches.put('F', new Pitch('F').accidentalTranspose(1));
			keyPitches.put('G', new Pitch('G').accidentalTranspose(1));
			keyPitches.put('A', new Pitch('A').accidentalTranspose(1));
			keyPitches.put('B', new Pitch('B'));
			break;
		case 15 : 
			keyPitches.put('C', new Pitch('C').accidentalTranspose(1));
			keyPitches.put('D', new Pitch('D').accidentalTranspose(1));
			keyPitches.put('E', new Pitch('E').accidentalTranspose(1));
			keyPitches.put('F', new Pitch('F').accidentalTranspose(1));
			keyPitches.put('G', new Pitch('G').accidentalTranspose(1));
			keyPitches.put('A', new Pitch('A').accidentalTranspose(1));
			keyPitches.put('B', new Pitch('B').accidentalTranspose(1));
			break;
		case 16 : 
			keyPitches.put('C', new Pitch('C').accidentalTranspose(1));
			keyPitches.put('D', new Pitch('D').accidentalTranspose(1));
			keyPitches.put('E', new Pitch('E').accidentalTranspose(1));
			keyPitches.put('F', new Pitch('F').accidentalTranspose(2));
			keyPitches.put('G', new Pitch('G').accidentalTranspose(1));
			keyPitches.put('A', new Pitch('A').accidentalTranspose(1));
			keyPitches.put('B', new Pitch('B').accidentalTranspose(1));
			break;
		case 17 : 
			keyPitches.put('C', new Pitch('C').accidentalTranspose(2));
			keyPitches.put('D', new Pitch('D').accidentalTranspose(1));
			keyPitches.put('E', new Pitch('E').accidentalTranspose(1));
			keyPitches.put('F', new Pitch('F').accidentalTranspose(2));
			keyPitches.put('G', new Pitch('G').accidentalTranspose(1));
			keyPitches.put('A', new Pitch('A').accidentalTranspose(1));
			keyPitches.put('B', new Pitch('B').accidentalTranspose(1));
			break;
		}
		
		modified = false;
	
	}
	
	private KeySignature(HashMap<Character, Pitch> keyMap, HashMap<String, Pitch> modMap) {
		keyPitches = keyMap;
		modified = true;
		modNotes = modMap;
	}

}
