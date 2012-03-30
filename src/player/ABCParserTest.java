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
		l.readLine("K:A");
		
		
		return l;
	}
	
	
	//lets start simple, testing a simplest possible parse
	@Test
	public void simpleParseTest() {

		ArrayList<ABCToken> tokenList = getBasicHeaderLexer().tokens;
		//only has three header tokens, XKT
		
		ABCEnvironment expected = new ABCEnvironment();
		
		//these are the headers deined in getBasicHeaderTokens
		expected.setHeader('X', "3");
		expected.setHeader('K', "A");
		expected.setHeader('T',"foobar");
		
		
		//DEFAULT TEMPO IS 100
		expected.setTempo(100);
		
		//with no notes, ticks per default length will be 1, but by 6 so 6
		expected.setTicksPerDefaultNote(6);
		
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
	public void oneMeasureTest() {
		Lexer l = getBasicHeaderLexer();
		//default note length is 1/8
		//default meter is 4/4
		
		l.readLine("A2 B2 A2 B2 |");
		
		for (ABCToken s : l.tokens) {
			print(s);
		}
		
		print(ABCParser.parse(l.tokens));
		
		assert false;
		
	}
	
}
