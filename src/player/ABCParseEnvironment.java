package player;

import java.util.HashMap;

import java.util.Stack;

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
	
	HashMap<Character,String> headers = new HashMap<Character,String>();
	
	HashMap<String,Stack<TuneSequence>> voiceStackMap = new HashMap<String,Stack<TuneSequence>>();
	String curVoice = null;
	
	Stack<TuneSequence> baseSequences = new Stack<TuneSequence>();
	Stack<TuneSequence> curStack = null;
	
	int ticksPerDefaultNote = 1;
	
	//defaults meter: 4/4, tempo: 100DefaultNotesPerMinute, default note length: 1/8
	Fraction meter = new Fraction(4,4);
	Fraction defaultLength = new Fraction(1,8);
	int tempo = 100;


	
	//statestuff
	boolean inBody = false;
	boolean firstBar = true;
	
	boolean inRepeat = false;
	boolean multiEndings = false;
	
	boolean inChord = false;
	boolean inTuplet = false;
	
	//voice specific state stuff
	HashMap<String,Boolean> voiceInRepeatMap = new HashMap<String,Boolean>();
	HashMap<String,Boolean> voiceInMultiendingMap = new HashMap<String,Boolean>();

	int tokenCount = 0;
	int numTokens;
	
	Fraction tupleMultiplier = null;
	int tupletCount;
	Fraction chordLength = new Fraction(0,1);
	
	//keeping track of duration within bar
	Fraction barDuration = new Fraction(0,1);
	
	KeySignature globalKeySig;
	KeySignature barKeySig;
	
	//mandatory header booleans
	boolean seenXHeader = false;
	boolean seenTHeader = false;
	boolean seenKHeader = false;
	
	//ticksPerDefaultNote * tempo = defaultNotes per minute
	
	public void incrTokenCount() {
		tokenCount++;
	}
	
	public void switchToBody() {
		inBody = true;
		
		//check that we have the necessary headers
		if (! (seenXHeader && seenTHeader && seenKHeader)) {
			throw new ABCParserException("Missing a mandatory header");
		}
		
		//calculate the tempo;
		
		//instantiate the voices that we have
		//I DONT HAVE TO DO THAT! (already done. when they are created)
		//but i do have to check to make sure that i have at least one voice.
		//and if i dont, i have to pretend
		if (baseSequences.size()==0) {
			curStack = createVoice("default");
		}
		
		//mimic a new bar for the key signature scope
		resetBar();
		
	}
	
	public void checkBody() {
		if (!inBody) {
			switchToBody();
		}
	}
	
	public Stack<TuneSequence> createVoice(String voiceName) {
		//the new stack for this voice
		Stack<TuneSequence> newStack = new Stack<TuneSequence>();
		//linking the voice to it
		voiceStackMap.put(voiceName, newStack);
		voiceInRepeatMap.put(voiceName,false);
		voiceInMultiendingMap.put(voiceName,false);
		
		//the base sequence for this voice
		TuneSequence baseSequence = new TuneSequence();
		baseSequences.push(baseSequence);
		//the first sequence within this voice
		TuneSequence firstSequence = new TuneSequence();
		baseSequence.add(firstSequence);
		newStack.push(baseSequence);
		newStack.push(firstSequence);
		
		return newStack;
	}
	
	public void switchVoice(String voice) {
		curStack = voiceStackMap.get(voice);
		
		voiceInRepeatMap.put(curVoice, inRepeat);
		voiceInMultiendingMap.put(curVoice, multiEndings);
		
		inRepeat = voiceInRepeatMap.get(voice);
		multiEndings = voiceInRepeatMap.get(voice);
		
		curVoice = voice;
		
	}
	
	public void repeatizeTop() {
		//replaces the top elemented of the current stack
		//with a reapeat that has the prior top element as its first element
		TuneSequence oldTop = curStack.pop();
		TuneRepeatable newRepeat = TuneRepeatable.fromSchedulable(oldTop);
		//remove oldTop from the end of the top
		curStack.peek().contents.pop();
		curStack.peek().add(newRepeat);
		curStack.push(newRepeat);
		curStack.push(oldTop);
	}
	
	public void newSection() {
		TuneSequence newSection = new TuneSequence();
		curStack.pop(); //close the old one
		curStack.peek().add(newSection);
		curStack.push(newSection);
	}
	
	public void checkTuplet() {
		if (inTuplet) {
			//then the top o' the stack is a tuple
			//if im not in a chord, and i've filled up the tuplet, pop it away
			if (!inChord && tupletCount == curStack.peek().contents.size()) {
				curStack.pop();
				inTuplet = false;
			}
		}
	}
	
	public void resetBar() {
		barDuration = new Fraction(0,1);
		barKeySig = globalKeySig;
	}
	
	public void updateDuration(Fraction d) {
		
		if (inChord) {
			if (d.greaterThan(chordLength)) {
				chordLength = d;
			}
		} else if (inTuplet) {
			barDuration = barDuration.add(d.times(tupleMultiplier.times(defaultLength)));
		} else {
			barDuration = barDuration.add(d.times(defaultLength));
		}
		
	}
	
	public void checkBarDuration() {
		
		if (firstBar) {
			firstBar = false;
		} else {
			//ugly.
			//if not
			//	barDuration is OK
			//	or barDuration is 0
			//	or we are are the last token
			if (! (barDuration.equals(meter) || barDuration.equals(0) || tokenCount == numTokens)) {
				throw new ABCParserException(
						String.format("Bar duration (%s) does not match meter (%s)",barDuration.toShortString(),meter.toShortString()));
			}
		}
	}
	
	public void handleNewDenominator(int d) {
		//adjusts the ticksPerDefaultNote
		if (! (ticksPerDefaultNote % d == 0) ) {
			ticksPerDefaultNote *= d;
		}
	}

}

