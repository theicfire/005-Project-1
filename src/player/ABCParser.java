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
				char key = token.headerKey;
				env.headers.put(key, token.headerValue);
				
				if (key == 'C') {
					//this header has no effect <composer>		
					
				} else if (key == 'K') {
					//this is the key signature header.
					env.globalKeySig = new KeySignature(token.headerValue);
					env.seenKHeader = true;
					
				} else if (key == 'L') {
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
					
				} else if (key == 'M') {
				} else if (key == 'Q') {
				} else if (key == 'T') {
				} else if (key == 'X') {
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
					//the new stack for this voice
					Stack<TuneSequence> newStack = new Stack<TuneSequence>();
					//linking the voice to it
					env.voiceStackMap.put(token.voiceName, newStack);
					
					//the base sequence for this voice
					TuneSequence baseSequence = new TuneSequence();
					//the first sequence within this voice
					TuneSequence firstSequence = new TuneSequence();
					baseSequence.add(firstSequence);
					newStack.push(baseSequence);
					newStack.push(firstSequence);
					
				}
				env.curStack = env.voiceStackMap.get(token.voiceName);
				break;
				
			case NOTE:
				env.checkBody();
				
				//adds the note to the top of the current stack
				Fraction duration = token.noteDuration;
				
				env.barDuration = env.barDuration.add(duration);
				
				sound.Pitch miPi = env.barKeySig.getPitch(token.noteName, token.noteOctave);
				env.curStack.peek().add(TunePrimitive.Note(duration, miPi));
				break;				
				
			case ACCIDENTAL:
				env.checkBody();
				
				//replaces the barKeySig with the new one
				env.barKeySig = env.barKeySig.fromAccidental(token.noteName,token.accModifier,0);
				break;
				
			case REST:
				env.checkBody();
				
				if (!env.inBody) {
					env.switchToBody();
				}
				
				env.barDuration = env.barDuration.add(token.restDuration);
				
				env.curStack.peek().add(TunePrimitive.Rest(token.restDuration));
				break;				
				
			case STARTCHORD:
				env.checkBody();
				
				Chord newChord = new Chord();
				env.curStack.peek().add(newChord);
				env.curStack.push(newChord);
				env.inChord = true;
				break;
	
			case ENDCHORD:
				if (!env.inChord) {
					throw new ABCParserException("End chord cannot come before new Chord!");
				}
				
				env.curStack.pop();
				env.inChord = false;
				break;
				
			case STARTTUPLET:
				env.checkBody();
				
				Tuple newTuplet = new Tuple(token.startTupletNoteCount);
				env.curStack.peek().add(newTuplet);
				env.curStack.push(newTuplet);
				env.inTuplet = true;
				break;
				
			case ENDTUPLET:
				if (!env.inTuplet) {
					throw new ABCParserException("End tuplet cannot come before new tuplet!");
				}
				
				env.curStack.pop();
				env.inTuplet = false;
				break;
				
			case STARTSECTION:
				env.checkBody();
				
				//close the old section, open the new section
				env.newSection();
				break;
				
			case STARTBAR:
				env.checkBody();
				
				env.barDuration = new Fraction(0,1);
				env.barKeySig = env.globalKeySig;
				break;
				
			case STARTREPEAT:
				env.checkBody();
				
				if (env.inRepeat) {
					throw new ABCParserException("Cannot have STARTREPEAT inside a repeat");
				}
				
				env.repeatizeTop();
				env.inRepeat = true;
				break;
				
			case MULTIENDING:
				if (!env.inRepeat) {
					env.repeatizeTop();
					env.inRepeat = true;
				}
				
				//now close and open a section
				env.newSection();
				break;
				
			case ENDSECTION:
				//do nothing for now... we cannot have an end section without a 
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
