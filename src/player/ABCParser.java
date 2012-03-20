package player;


import java.util.HashMap;
import java.util.Stack;
import java.util.ArrayList;
import lexer.ABCToken;
import lexer.ABCToken.Lexeme.*;

public class ABCParser {

	
	public static ABCEnvironment parse(ArrayList<ABCToken> tokenList) {
		
		ABCEnvironment outenv =  new ABCEnvironment();		
		
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
		
		HashMap<String,TuneSequence> voiceSequenceMap = new HashMap<String,TuneSequence>();
		TuneParallel rootParallel = new TuneParallel();
		
		int ticksPerDefaultNote;
		
		//defaults meter: 4/4, tempo: 100DefaultNotesPerMinute, default note length: 1/8
		Fraction meter = new Fraction(4,4);
		Fraction defaultLength = new Fraction(1,8);
		
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
		
		//ok. lets parse!
		for (ABCToken token : tokenList) {
			
			switch (token.lexeme) {
				
			//header handling is a bitch
			case HEADER:
				String key = token.headerKey;
				
				if (key.equals("C")) {
					
					//this header has no effect <composer>
					outenv.setHeader(key, token.headerValue);
					
				} else if (key.equals("K")) {
					
					//this is the key signature header.
					globalKeySig = KigSignature(token.headerValue);
					seenKHeader = true;
					
				} else if (key.equals("L")) {
					
					
				} else if (key.equals("M")) {
					
				} else if (key.equals("Q")) {
					
				} else if (key.equals("T")) {
					
				} else if (key.equals("X")) {
					
				}
				
				C: Name of the composer.
				K: Key, which determines the key signature for the piece.
				L: Default length or duration of a note.
				M: Meter. It determines the sum of the durations of all notes within a bar.
				Q: Tempo. It represents the number of default-length notes per minute.
				T: Title of the piece.
				X:
				
			}
			
			
			
		}
		
		
		return outenv;
		
	}
	
	
	private int getTicksPQ(int current,int newDom) {
		
		if (current % newDom != 0) {
			current = current*newDom;
		}
		return current;
		
	}
	
	
}
