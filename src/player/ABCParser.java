package player;


import java.util.HashMap;
import java.util.Stack;
import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


import lexer.ABCToken;
import lexer.ABCToken.Lexeme.*;


public class ABCParser {

	
	public static ABCEnvironment parse(ArrayList<ABCToken> tokenList) {
		
		ABCParseEnvironment env =  new ABCParseEnvironment();		
		
		
		//ok. lets parse!
		for (ABCToken token : tokenList) {
			
			switch (token.lexeme) {
				
			//header handling is a bitch
			case HEADER:
				String key = token.headerKey;
				env.headers.put(key, token.headerValue);
				
				if (key.equals("C")) {
					//this header has no effect <composer>		
					
				} else if (key.equals("K")) {
					//this is the key signature header.
					env.globalKeySig = new KeySignature(token.headerValue);
					env.seenKHeader = true;
					
				} else if (key.equals("L")) {
					//this is the default duration header
					//value has the form DIGIT+/DIGIT+
					Pattern lengthPattern = Pattern.compile("(\\d+)/(\\d+)");
					Matcher match = lengthPattern.matcher(token.headerValue);
					if (!match.matches()) {
						throw new ABCParserException("Length header value invalid");
					}
					//set the defaultLengthState
					env.defaultLength = new Fraction(
										Integer.parseInt(match.group(1)),
										Integer.parseInt(match.group(2))
										);	
					
				} else if (key.equals("M")) {
					
					
				} else if (key.equals("Q")) {
					
				} else if (key.equals("T")) {
					
				} else if (key.equals("X")) {
					
				}
				/*
				C: Name of the composer.
				K: Key, which determines the key signature for the piece.
				L: Default length or duration of a note.
				M: Meter. It determines the sum of the durations of all notes within a bar.
				Q: Tempo. It represents the number of default-length notes per minute.
				T: Title of the piece.
				X:
				*/
				break;
			
			case VOICE:
				
				if (!env.inBody) {
					env.voiceSequenceMap.put(token.voiceName, new TuneSequence());
				}
				env.curSeq = env.voiceSequenceMap.get(token.voiceName);
				break;
				
			case NOTE:
				
				if (!env.inBody) {
					env.switchToBody();
				}
				
				Fraction duration = token.noteDuration;
				sound.Pitch miPi = env.barKeySig.getPitch(token.noteName, token.noteOctave);
				env.curSeq.add(TunePrimitive.Note(duration, miPi));
				
				
			case ACCIDENTAL:
			case REST:
			case STARTCHORD:
			case ENDCHORD:
			case STARTTUPLET:
			case ENDTUPLET:
			case STARTSECTION:
			case STARTBAR:
			case STARTREPEAT:
			case MULTIENDING:
			case ENDSECTION:
			case ENDBAR:
			case ENDREPEAT:
			}
			
			
			
		}
		
		
		return new ABCEnvironment();
		
	}
	
	
	private int getTicksPQ(int current,int newDom) {
		
		if (current % newDom != 0) {
			current = current*newDom;
		}
		return current;
		
	}
	
	
}
