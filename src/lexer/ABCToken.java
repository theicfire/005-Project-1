package lexer;

import player.Fraction;

public class ABCToken {
	public static enum Lexeme {
		HEADER,
		VOICE,
		NOTE,
		ACCIDENTAL,
		REST,
		STARTCHORD,
		ENDCHORD,
		STARTTUPLET,
		ENDTUPLET,
		STARTSECTION,
		STARTBAR,
		STARTREPEAT,
		MULTIENDING,
		ENDSECTION,
		ENDBAR,
		ENDREPEAT
	}
	public Lexeme lexeme;
	public String headerKey;
	public String headerValue;
	
	public String voiceName;
	
	// [A-G]
	public char noteName;
	
	/*noteOctave is a number that tells how many octaves from middle C we are
	 * C --> 0
	 * c --> 1
	 * c' --> 2
	 * C, --> -1
	 */
	public int noteOctave;
	
	public Fraction noteDuration;
	
	public char accNote;
	
	// =, ^, _
	public String accModifier;
	
	public Fraction restDuration;
	
	public int startTupletNoteCount;
	
	public int multiEndingNumber;
	public ABCToken() {
		// builder takes care of everything
	}
	
	@Override
	public String toString() {
		return "ABCToken [lexeme=" + lexeme + ", headerKey=" + headerKey
				+ ", headerValue=" + headerValue + ", voiceName=" + voiceName
				+ ", noteName=" + noteName + ", noteOctave=" + noteOctave
				+ ", noteDuration=" + noteDuration + ", accNote=" + accNote
				+ ", accModifier=" + accModifier + ", restDuration="
				+ restDuration + ", startTupletNoteCount="
				+ startTupletNoteCount + ", multiEndingNumber="
				+ multiEndingNumber + "]\n";
	}
}
