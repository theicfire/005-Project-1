package player;



import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;

import lexer.ABCToken;
import lexer.ABCToken.Lexeme;
import lexer.ABCTokenBuilder;
import lexer.Lexer;



public class ABCParserTest {

	
	/*
	 * the parser gets a list of tokens as input
	 * it returns a ABCEnvironment
	 * so ABCEnviroments must match
	 */
	
	//some utility shit
	
	
	Fraction eighth = new Fraction(1,8);
	Fraction quarter = new Fraction(1,4);
	Fraction half = new Fraction(1,2);
	
	Fraction one = new Fraction(1,1);
	Fraction two = new Fraction(2,1);
	Fraction three = new Fraction(3,1);
	Fraction four = new Fraction(4,1);
	
	boolean playAll = true;
	
	
	//so i can hear things!
	private void play(ABCEnvironment e) {
		ABCPlayer.playEnvironment(e);
	}
	
	private void print(Object o) {
		
		if (o instanceof Schedulable) {
			SchedulePrinter p = new SchedulePrinter();
			((Schedulable) o).accept(p);
			print(p.outString);
		} else {
			System.out.print(o);
		}
	}
	
	private Lexer getBasicHeaderLexer() {
		
		Lexer l = new Lexer();
		
		l.readLine("X:3");
		l.readLine("T:foobar");
		l.readLine("K:C");
		
		
		return l;
	}
	
	private ABCEnvironment getDefaultEnv() {
		ABCEnvironment expected = new ABCEnvironment();
		
		//these are the headers deined in getBasicHeaderTokens
		expected.setHeader('X', "3");
		expected.setHeader('K', "C");
		expected.setHeader('T',"foobar");
		
		
		//DEFAULT TEMPO IS 100
		expected.setTempo(100);
		
		//with no notes, ticks per default length will be 1, but by 6 so 6
		expected.setTicksPerDefaultNote(6);
		return expected;
	}
	
	
	//lets start simple, testing a simplest possible parse
	@Test
	public void simpleParseTest() {

		ArrayList<ABCToken> tokenList = getBasicHeaderLexer().tokens;
		//only has three header tokens, XKT
		
		ABCEnvironment expected = getDefaultEnv();
		
		//with no notes, we wil parse an empty TuneParallel
		expected.setTreeRoot(new TuneParallel());
		
		ABCEnvironment e = ABCParser.parse(tokenList);
		//print(e);
		//print(expected);
		
		assert e.equals(expected);
		assert true;
	}
	
	//we will need to stress test a lot of the header edge case stuff, but that can be done later
	//TODO: stress test the headers
	
	
	//ok, lets add a measure!
	@Test
	public void simpleScaleTest() {
		Lexer l = getBasicHeaderLexer();
		//default note length is 1/8
		//default meter is 4/4
		
		l.readLine("C D E F G A B c");
		
		
		ABCEnvironment e = ABCParser.parse(l.tokens);
		
		if (playAll) play(e);
		
		ABCEnvironment expected = getDefaultEnv();
		
		//lets build this fucking expected tree!
		
		//the default key sig
		KeySignature keySig = new KeySignature("C");
		
		TuneParallel baseParallel = new TuneParallel();
		TuneSequence baseSequence = new TuneSequence();
		TuneSequence subSequence = new TuneSequence();
		
		
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('C', 0)));
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('D', 0)));
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('E', 0)));
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('F', 0)));
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('G', 0)));
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('A', 0)));
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('B', 0)));
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('C', 1)));
		
		baseSequence.add(subSequence);
		baseParallel.add(baseSequence);
		
		expected.setTreeRoot(baseParallel);
				
		assert expected.equals(e);
		
	}
	
	//lets test the rests
	@Test
	public void testRests() {
		Lexer l = getBasicHeaderLexer();
		l.readLine("A B z z C z F z");
		ABCEnvironment e = ABCParser.parse(l.tokens);
		if (playAll) play(e);
		
		ABCEnvironment expected = getDefaultEnv();
		KeySignature keySig = new KeySignature("C");
		
		TuneParallel baseParallel = new TuneParallel();
		TuneSequence baseSequence = new TuneSequence();
		TuneSequence subSequence = new TuneSequence();

		
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('A', 0)));
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('B', 0)));
		subSequence.add(TunePrimitive.Rest(one));
		subSequence.add(TunePrimitive.Rest(one));
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('C', 0)));
		subSequence.add(TunePrimitive.Rest(one));
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('F', 0)));
		subSequence.add(TunePrimitive.Rest(one));
		
		baseSequence.add(subSequence);
		baseParallel.add(baseSequence);
		
		expected.setTreeRoot(baseParallel);
		
				
		assert expected.equals(e);
	}
	
	//Ok. lets test playing with the primitives...
	//this is also going to test simple bars
	//yippee
	@Test
	public void primitiveTest() {
		Lexer l = getBasicHeaderLexer();
		l.readLine("z2 a A A, z c/4 d/4 e/4 f/4 G | G z/ f/2 c'1/2 b'/ a'/4 g/4 f/4 e/4 e2 E2 | C4 G,2 F2 | C,,8");
		ABCEnvironment e = ABCParser.parse(l.tokens);
		if (playAll) play(e);
		
		ABCEnvironment expected = getDefaultEnv();
		KeySignature keySig = new KeySignature("C");
		TuneParallel baseParallel = new TuneParallel();
		TuneSequence baseSequence = new TuneSequence();
		TuneSequence subSequence = new TuneSequence();
		
		subSequence
			.add(TunePrimitive.Rest(two))
			.add(TunePrimitive.Note(one, keySig.getPitch('A', 1)))
			.add(TunePrimitive.Note(one, keySig.getPitch('A', 0)))
			.add(TunePrimitive.Note(one, keySig.getPitch('A', -1)))
			.add(TunePrimitive.Rest(one))
			.add(TunePrimitive.Note(quarter, keySig.getPitch('C', 1)))
			.add(TunePrimitive.Note(quarter, keySig.getPitch('D', 1)))
			.add(TunePrimitive.Note(quarter, keySig.getPitch('E', 1)))
			.add(TunePrimitive.Note(quarter, keySig.getPitch('F', 1)))
			.add(TunePrimitive.Note(one, keySig.getPitch('G', 0)))
			.add(TunePrimitive.Note(one, keySig.getPitch('G', 0)))
			.add(TunePrimitive.Rest(half))
			.add(TunePrimitive.Note(half, keySig.getPitch('F', 1)))
			.add(TunePrimitive.Note(half, keySig.getPitch('C', 2)))
			.add(TunePrimitive.Note(half, keySig.getPitch('B', 2)))
			.add(TunePrimitive.Note(quarter, keySig.getPitch('A', 2)))
			.add(TunePrimitive.Note(quarter, keySig.getPitch('G', 1)))
			.add(TunePrimitive.Note(quarter, keySig.getPitch('F', 1)))
			.add(TunePrimitive.Note(quarter, keySig.getPitch('E', 1)))
			.add(TunePrimitive.Note(two, keySig.getPitch('E', 1)))
			.add(TunePrimitive.Note(two, keySig.getPitch('E', 0)))
			.add(TunePrimitive.Note(four, keySig.getPitch('C', 0)))
			.add(TunePrimitive.Note(two, keySig.getPitch('G', -1)))
			.add(TunePrimitive.Note(two, keySig.getPitch('F', 0)))
			.add(TunePrimitive.Note(four.times(two), keySig.getPitch('C',-2)));
		
		
		baseSequence.add(subSequence);
		baseParallel.add(baseSequence);
		
		expected.setTreeRoot(baseParallel);
		expected.setTicksPerDefaultNote(24);
		//print(expected);
		//play(expected);
		//play(e);
		
		assert expected.equals(e);
		
		
	}
	
	//chord tests
	@Test
	public void simpleChordTest() {
		Lexer l = getBasicHeaderLexer();
		l.readLine("[bdf] a4 [c3e3g3]");
		ABCEnvironment e = ABCParser.parse(l.tokens);
		if (playAll) play(e);
		
		
		ABCEnvironment expected = getDefaultEnv();
		KeySignature keySig = new KeySignature("C");
		TuneParallel baseParallel = new TuneParallel();
		TuneSequence baseSequence = new TuneSequence();
		TuneSequence subSequence = new TuneSequence();
		
		subSequence.add(new Chord()
					.add(TunePrimitive.Note(one, keySig.getPitch('B', 1)))
					.add(TunePrimitive.Note(one, keySig.getPitch('D', 1)))
					.add(TunePrimitive.Note(one, keySig.getPitch('F', 1)))
				)
				.add(TunePrimitive.Note(four, keySig.getPitch('A', 1)))
				.add(new Chord()
					.add(TunePrimitive.Note(three, keySig.getPitch('C', 1)))
					.add(TunePrimitive.Note(three, keySig.getPitch('E', 1)))
					.add(TunePrimitive.Note(three, keySig.getPitch('G', 1)))
				);
		
		baseSequence.add(subSequence);
		baseParallel.add(baseSequence);
		
		expected.setTreeRoot(baseParallel);
		
		assert expected.equals(e);
	}
	
	//tuple test
	@Test
	public void simpleTupleTest() {
		Lexer l  = getBasicHeaderLexer();
		l.readLine("(3CzC (2E/E/ B5/2 A2");
		ABCEnvironment e = ABCParser.parse(l.tokens);
		if (playAll) play(e);
		
		ABCEnvironment expected = getDefaultEnv();
		KeySignature keySig = new KeySignature("C");
		TuneParallel baseParallel = new TuneParallel();
		TuneSequence baseSequence = new TuneSequence();
		TuneSequence subSequence = new TuneSequence();
		
		subSequence.add(new Tuple(3)
				.add(TunePrimitive.Note(one, keySig.getPitch('C', 0)))
				.add(TunePrimitive.Rest(one))
				.add(TunePrimitive.Note(one, keySig.getPitch('C', 0)))
			)
			.add(new Tuple(2)
				.add(TunePrimitive.Note(half, keySig.getPitch('E',0)))
				.add(TunePrimitive.Note(half, keySig.getPitch('E',0)))
			)
			.add(TunePrimitive.Note((four.add(one)).times(half), keySig.getPitch('B', 0)))
			.add(TunePrimitive.Note(two, keySig.getPitch('A', 0)));
		
		baseSequence.add(subSequence);
		baseParallel.add(baseSequence);
		expected.setTicksPerDefaultNote(12);
		expected.setTreeRoot(baseParallel);
		
		assert expected.equals(e);
	}
	
	//tuple test with chords... crash and burn
	@Test
	public void chordTupleTest() {
		Lexer l = getBasicHeaderLexer();
		l.readLine("(3 [C2 E2 G2] [E2 G2 B2] [E2 G2 c2] c/A/F/A/ [c2 A2 F2] |");

		ABCEnvironment e = ABCParser.parse(l.tokens);
		
		if (playAll) play(e);
		
		ABCEnvironment expected = getDefaultEnv();
		KeySignature keySig = new KeySignature("C");
		TuneParallel baseParallel = new TuneParallel();
		TuneSequence baseSequence = new TuneSequence();
		TuneSequence subSequence = new TuneSequence();
		
		subSequence.add(new Tuple(3)
				.add(new Chord()
					.add(TunePrimitive.Note(two, keySig.getPitch('C', 0)))
					.add(TunePrimitive.Note(two, keySig.getPitch('E', 0)))
					.add(TunePrimitive.Note(two, keySig.getPitch('G', 0)))
				)
				.add(new Chord()
					.add(TunePrimitive.Note(two, keySig.getPitch('E', 0)))
					.add(TunePrimitive.Note(two, keySig.getPitch('G', 0)))
					.add(TunePrimitive.Note(two, keySig.getPitch('B', 0)))
				)
				.add(new Chord()
					.add(TunePrimitive.Note(two, keySig.getPitch('E', 0)))
					.add(TunePrimitive.Note(two, keySig.getPitch('G', 0)))
					.add(TunePrimitive.Note(two, keySig.getPitch('C', 1)))
				)
			)
			.add(TunePrimitive.Note(half, keySig.getPitch('C', 1)))
			.add(TunePrimitive.Note(half, keySig.getPitch('A', 0)))
			.add(TunePrimitive.Note(half, keySig.getPitch('F', 0)))
			.add(TunePrimitive.Note(half, keySig.getPitch('A', 0)))
			.add(new Chord()
				.add(TunePrimitive.Note(two, keySig.getPitch('C', 1)))
				.add(TunePrimitive.Note(two, keySig.getPitch('A', 0)))
				.add(TunePrimitive.Note(two, keySig.getPitch('F', 0)))
			);
		
		
		baseSequence.add(subSequence);
		baseParallel.add(baseSequence);
		expected.setTicksPerDefaultNote(12);
		expected.setTreeRoot(baseParallel);
		
		
		assert expected.equals(e);
		
	}
	
	//some accidental tests!
	@Test
	public void basicAccidentalTest() {
		
		Lexer l = getBasicHeaderLexer();
		//basic headers put us in C
		l.readLine("F F ^F F _F F =F F");
		
		for (ABCToken t : l.tokens) {
			print(t);
		}
		
		ABCEnvironment e = ABCParser.parse(l.tokens);

		if (playAll) play(e);
		
		ABCEnvironment expected = getDefaultEnv();
		KeySignature keySig = new KeySignature("C");
		TuneParallel baseParallel = new TuneParallel();
		TuneSequence baseSequence = new TuneSequence();
		TuneSequence subSequence = new TuneSequence();
		
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('F', 0)))
			.add(TunePrimitive.Note(one, keySig.getPitch('F', 0)));
		
		keySig = keySig.fromAccidental('F', 1, 0);
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('F', 0)))
			.add(TunePrimitive.Note(one, keySig.getPitch('F', 0)));
		
		keySig = keySig.fromAccidental('F', -1, 0);
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('F', 0)))
			.add(TunePrimitive.Note(one, keySig.getPitch('F', 0)));
		
		keySig = keySig.fromAccidental('F', 0, 0);
		subSequence.add(TunePrimitive.Note(one, keySig.getPitch('F', 0)))
			.add(TunePrimitive.Note(one, keySig.getPitch('F', 0)));
		
		baseSequence.add(subSequence);
		baseParallel.add(baseSequence);
		expected.setTreeRoot(baseParallel);
		assert expected.equals(e);
		
	}
		
	//now we need to test accidental functionality across octaves
	@Test
	public void multiOctaveAccidentalTest() {
		
		assert false;
		
	}
	
	//accidentals across bars
	@Test
	public void multiBarAccidentalTest() {
		
		assert false;
	}
	
	//accidentals across sections
	@Test
	public void multiSectionAccidentalTest() {
		
		assert false;
	}
	
	//tests still to write:
	//repeat, single, left and right
	//repeat, single, start of piece, right
	//repeat, single, section, right
	//repeat, multi, left
	//repeat, multi, beginning of peice
	//repeat, multi, section
	
	//nested single repeat
	
	//mutli ending eith a repeat in the second ending (the rest of the piece durr...)
	
	//accidentals and repeat boundary (only
	//	simple single
	//	simple multi)
	
	//VOICES
	//actually not that hard. just have some voices thrown in, simple, complex
	//probably around 4 tests though.
	
	//and then all the header edge case tests
	//like tempo!
	//TEMPO is tricky
	
	//and i guess all the error cases too!!!! FUCK
	//gah. thats a lot of work.
	
}
