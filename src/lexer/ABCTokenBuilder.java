package lexer;

import player.Fraction;

public class ABCTokenBuilder {
	private final ABCToken obj = new ABCToken();
	private boolean done = false;
	
	public static ABCTokenBuilder createBuilder() {
		return new ABCTokenBuilder();
	}
	
	public ABCToken build() {
		done = true;
		return obj;
	}
	
	public ABCTokenBuilder setHeaderKey(String k) {
		check();
		obj.headerKey = k;
		return this;
	}
	
	public ABCTokenBuilder setHeaderValue(String v) {
		check();
		obj.headerValue = v;
		return this;
	}
	
	public ABCTokenBuilder setLexeme(ABCToken.Lexeme l) {
		check();
		obj.lexeme = l;
		return this;
	}
	
	public ABCTokenBuilder setNoteName(char c) {
		check();
		obj.noteName = c;
		return this;
	}
	
	public ABCTokenBuilder setNoteOctave(int o) {
		check();
		obj.noteOctave = o;
		return this;
	}
	
	public ABCTokenBuilder increaseOctave() {
		check();
		obj.noteOctave += 1;
		return this;
	}
	
	public ABCTokenBuilder decreaseOctave() {
		check();
		obj.noteOctave -= 1;
		return this;
	}
	
	public ABCTokenBuilder setNoteDuration(Fraction fraction) {
		check();
		obj.noteDuration = fraction;
		return this;
	}
	
	public ABCTokenBuilder setAccModifier(int num) {
		check();
		obj.accModifier = num;
		return this;
	}
	
	public ABCTokenBuilder setVoiceName(String v) {
		check();
		obj.voiceName = v;
		return this;
	}
	
	private void check() {
        if (done) {
        	throw new IllegalArgumentException("Do use other builder to create new instance");
        }
	}

	
}
