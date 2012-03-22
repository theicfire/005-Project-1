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
	public char headerKey = '$';
	public String headerValue;
	
	public String voiceName;
	
	// [A-G]
	public char noteName = 'Q'; // some wierd default value
	
	/*noteOctave is a number that tells how many octaves from middle C we are
	 * C --> 0
	 * c --> 1
	 * c' --> 2
	 * C, --> -1
	 */
	public int noteOctave;
	
	public Fraction noteDuration;
	
	// in the range of -2 to 2 
	// [__->-2....^^->2
	public int accModifier; 
	
	public int startTupletNoteCount;
	
	public int multiEndingNumber;
	public ABCToken() {
		// builder takes care of everything
	}
	
	public void checkRep() {
		assert true : "Lexeme is null";
	}
	
	@Override
	public String toString() {
		return "ABCToken [lexeme=" + lexeme + ", headerKey=" + headerKey
				+ ", headerValue=" + headerValue + ", voiceName=" + voiceName
				+ ", noteName=" + noteName + ", noteOctave=" + noteOctave
				+ ", noteDuration=" + noteDuration
				+ ", accModifier=" + accModifier + ", startTupletNoteCount="
				+ startTupletNoteCount + ", multiEndingNumber="
				+ multiEndingNumber + "]\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accModifier;
		result = prime * result + headerKey;
		result = prime * result
				+ ((headerValue == null) ? 0 : headerValue.hashCode());
		result = prime * result + ((lexeme == null) ? 0 : lexeme.hashCode());
		result = prime * result + multiEndingNumber;
		result = prime * result
				+ ((noteDuration == null) ? 0 : noteDuration.hashCode());
		result = prime * result + noteName;
		result = prime * result + noteOctave;
		result = prime * result + startTupletNoteCount;
		result = prime * result
				+ ((voiceName == null) ? 0 : voiceName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass()) {
			return false;
		}
		ABCToken other = (ABCToken) obj;
		if (accModifier != other.accModifier) {
			System.out.println("something");
			return false;
		}
		if (headerKey != other.headerKey) {
			System.out.println("somethingsfdsf");
			return false;
		}
		if (headerValue == null) {
			if (other.headerValue != null) {
				System.out.println("someasdfthing");
				return false;
			}
		} else if (!headerValue.equals(other.headerValue)) {
			System.out.println("sometasaahing");
			return false;
		}
		if (lexeme != other.lexeme) {
			System.out.println("somasdething");
			return false;
		}
		if (multiEndingNumber != other.multiEndingNumber) {
			System.out.println("somafdfething");
			return false;
		}
		if (noteDuration == null) {
			if (other.noteDuration != null) {
				System.out.println("somesdaathing");
				return false;
			}
		} else if (!noteDuration.equals(other.noteDuration)) {
			System.out.println("sosaasdmething");
			return false;
		}
		if (noteName != other.noteName) {
			System.out.println("soffdmething");
			return false;
		}
		if (noteOctave != other.noteOctave) {
			System.out.println("saafsomething");
			return false;
		}
		if (startTupletNoteCount != other.startTupletNoteCount) {
			System.out.println("someqqqqwething");
			return false;
		}
		if (voiceName == null) {
			if (other.voiceName != null) {
				System.out.println("somffdething");
				return false;
			}
		} else if (!voiceName.equals(other.voiceName)) {
			System.out.println("sometqqsdhing");
			return false;
		}
		return true;
	}
	
	
}
