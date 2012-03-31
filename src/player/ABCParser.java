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
					//this is the meter header
					//value has the form C | C\| | DIGIT+/DIGIT+
					Fraction meter = null;
					if (token.headerValue.equals("C")) {
						meter = new Fraction(4,4);
					} else if (token.headerValue.equals("C|")) {
						meter = new Fraction(2,2);
					} else {
						//the regex match
						Pattern meterPattern = Pattern.compile("(\\d+)/(\\d+)");
						Matcher match = meterPattern.matcher(token.headerValue);
						if (!match.matches()) {
							throw new ABCParserException("Meter header value invalid");
						}
						
						env.meter = meter;
						
					}
					
				} else if (key == 'Q') {
					//the tempo header
					int tempoValue;
					try {
						tempoValue = Integer.parseInt(token.headerValue);
					} catch (NumberFormatException e) {
						throw new ABCParserException("Tempo value is invalid");
					}
					
					if (tempoValue <= 0) {
						throw new ABCParserException("Tempo cannot be less than or equal to 0");
					}
					env.tempo = tempoValue;					
					
				} else if (key == 'T') {
					env.seenTHeader = true;
				} else if (key == 'X') {
					env.seenXHeader = true;
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
					env.createVoice(token.voiceName);

					
				}
				env.curStack = env.voiceStackMap.get(token.voiceName);
				break;
				
			case NOTE:
				env.checkBody();
				
				//adds the note to the top of the current stack
				Fraction duration = token.noteDuration;
				
				env.updateDuration(duration);				
				env.handleNewDenominator(duration.dom);
				
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
				
				env.updateDuration(token.noteDuration);
				env.handleNewDenominator(token.noteDuration.dom);
				
				env.curStack.peek().add(TunePrimitive.Rest(token.noteDuration));
				break;				
				
			case STARTCHORD:
				env.checkBody();
				
				Chord newChord = new Chord();
				env.curStack.peek().add(newChord);
				env.curStack.push(newChord);
				
				env.chordLength = new Fraction(0,1);
				env.inChord = true;
				break;
	
			case ENDCHORD:
				if (!env.inChord) {
					throw new ABCParserException("End chord cannot come before new Chord!");
				}
				
				env.curStack.pop();

				//handle adding the chord duration
				env.barDuration = env.barDuration.add(env.chordLength.times(env.defaultLength));
				
				env.inChord = false;
				break;
				
			case STARTTUPLET:
				env.checkBody();
				
				Tuple newTuplet = new Tuple(token.startTupletNoteCount);
				
				env.tupleMultiplier = newTuplet.multiplier;
				
				env.curStack.peek().add(newTuplet);
				env.curStack.push(newTuplet);
				env.inTuplet = true;
				break;
				
			case ENDTUPLET:
				if (!env.inTuplet) {
					throw new ABCParserException("End tuplet cannot come before new tuplet!");
				}
				
				env.tupleMultiplier = null;
				
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
				
				env.resetBar();
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
					env.newSection();
				} else {
					//i dont have to do anything... the preceeding endrepeat has to take care of it
				}
				
				//now close and open a section
				break;
				
			case ENDSECTION:
				//do nothing for now... we cannot have an end section without a start section..
				//yuck
				break;
			
			case ENDBAR:
				//check to make sure that the duration matches OK
				env.checkBarDuration();
				
				break;
				
			case ENDREPEAT:
				if (!env.inRepeat) {
					//then no multi endings!
					env.repeatizeTop();
					//pops the first element of the new repeat off of the stack
					env.curStack.pop();
					
					//closes the repeatable, starts a new section
					env.newSection();
				} else {
					//then there have been multiendings...
					env.newSection();
				}
				
				
				break;
				
				
			}

			
			
			
		}
		
		//one last measure test, only if duration != 0
		if (!env.barDuration.equals(0)) {
			env.checkBarDuration();
		}
		
		TuneParallel rootParallel = new TuneParallel();
		for (TuneSequence s : env.baseSequences) {
			rootParallel.add(s);
		}
		
		
		ABCEnvironment outenv = new ABCEnvironment();
		
		//passing the headers over
		for (char c : env.headers.keySet()) {
			outenv.setHeader(c, env.headers.get(c));
		}
		
		outenv.setTempo(env.tempo);
		
		//multiply by 6 in case of tuples
		outenv.setTicksPerDefaultNote(env.ticksPerDefaultNote*6);
		
		outenv.setTreeRoot(rootParallel);
		
		return outenv;
		
	}
	
	
	
}
