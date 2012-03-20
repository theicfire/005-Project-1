package player;

import java.util.HashMap;

public class ABCParseEnvironment {
	

	
	/*
	 * Instantiation stuff
	 *  - hash map pointing voices to sequence
	 *  - tune parallel with all those sequences
	 *  - current smallest denominator
	 *  - meter (sum of durations)
	 *  - tempo (quarter notes per minute)
	 *  - inBody
	 *  - inRepeat
	 *  - bodySum
	 *  - globalKeySig
	 *  - seenXHeader
	 *  - seenTHeader
	 *  - seenKHeader
	 */
	
	HashMap<String,String> headers = new HashMap<String,String>();
	
	HashMap<String,TuneSequence> voiceSequenceMap = new HashMap<String,TuneSequence>();
	TuneParallel rootParallel = new TuneParallel();
	TuneSequence curSeq = null;
	
	int ticksPerDefaultNote;
	
	//defaults meter: 4/4, tempo: 100DefaultNotesPerMinute, default note length: 1/8
	Fraction meter = new Fraction(4,4);
	Fraction defaultLength = new Fraction(1,8);
	int tempo;
	
	//statestuff
	boolean inBody = false;
	boolean inRepeat = false;
	
	//keeping track of duration within bar
	Fraction barDuration = new Fraction(0,1);
	
	KeySignature globalKeySig;
	KeySignature barKeySig;
	
	//mandatory header booleans
	boolean seenXHeader = false;
	boolean seenTHeader = false;
	boolean seenKHeader = false;
	
	//ticksPerDefaultNote * tempo = defaultNotes per minute
	
	
	public void switchToBody() {
		inBody = true;
		
		//check that we have the necessary headers
		if (! (seenXHeader && seenTHeader && seenKHeader)) {
			throw new ABCParserException("Missing a mandatory header");
		}
		
		//calculate the tempo;
		
		//instantiate the voices that we have
		//I DONT HAVE TO DO THAT! (already done. when they are created)
		
		//mimic a new bar for the key signature scope
		barKeySig = globalKeySig;
		
	}
	

}

