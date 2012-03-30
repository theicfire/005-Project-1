package lexer;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import player.Fraction;

public class LexerTest {
	@Test
	public void testLots() {
		Lexer l = new Lexer("X:1\n" + 
			"T:Trad.\n" +
			"K:D\n" +
			"c''|:(3A,,Bf:|[1z1/4[2_a'2[c,,,b]1/3", false);
		Lexer ans = new Lexer();
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.HEADER)
				.setHeaderKey('X')
				.setHeaderValue("1")
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.HEADER)
				.setHeaderKey('T')
				.setHeaderValue("Trad.")
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.HEADER)
				.setHeaderKey('K')
				.setHeaderValue("D")
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.STARTSECTION)
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.NOTE)
				.setNoteName('C')
				.setNoteOctave(3)
				.setNoteDuration(new Fraction(1, 1))
				.build());
		
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.STARTREPEAT)
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.STARTTUPLET)
				.setStartTupletNoteCount(3)
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.NOTE)
				.setNoteName('A')
				.setNoteOctave(-2)
				.setNoteDuration(new Fraction(1, 1))
				.build());
		
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.NOTE)
				.setNoteName('B')
				.setNoteOctave(0)
				.setNoteDuration(new Fraction(1, 1))
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.NOTE)
				.setNoteName('F')
				.setNoteOctave(1)
				.setNoteDuration(new Fraction(1, 1))
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.ENDTUPLET)
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.ENDREPEAT)
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.MULTIENDING)
				.setMultiEndingNumber(1)
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.REST)
				.setNoteDuration(new Fraction(1, 4))
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.MULTIENDING)
				.setMultiEndingNumber(2)
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.ACCIDENTAL)
				.setNoteName('A')
				.setNoteOctave(1)
				.setAccModifier(-1)
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.NOTE)
				.setNoteName('A')
				.setNoteOctave(1)
				.setNoteDuration(new Fraction(2, 2))
				.build());
		
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.STARTCHORD)
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.NOTE)
				.setNoteName('C')
				.setNoteOctave(-2)
				.setNoteDuration(new Fraction(1, 3))
				.build());
		
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.NOTE)
				.setNoteName('B')
				.setNoteOctave(1)
				.setNoteDuration(new Fraction(1, 3))
				.build());
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.ENDCHORD)
				.build());
		
		
		ans.tokens.add(ABCTokenBuilder.createBuilder()
				.setLexeme(ABCToken.Lexeme.ENDSECTION)
				.build());
		System.out.println(l.tokens);
		assertEquals(l.tokens, ans.tokens);
	}
}
