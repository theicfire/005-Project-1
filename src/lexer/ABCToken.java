package lexer;

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
	
	public String noteName;
	public int noteOctave;
	public int noteDuration;
	
	public String accNote;
	public String accModifier;
	
	public int restDuration;
	
	public int startTupletNoteCount;
	
	public int multiEndingNumber;
	public ABCToken() {
		// builder takes care of everything
	}
	
	public String toString() {
		// more needed...
		return "ABCToken: " + lexeme + " headerKey " + headerKey + " headerValue " + headerValue;
	}
}
