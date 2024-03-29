package player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

	public ArrayList<ABCToken> tokens = new ArrayList<ABCToken>();
	public boolean isHeader = true;
	
	public ABCTokenBuilder curbuilder = null;
	public int tupletCount = -1;
	public int fieldCount = 0;
	public int lastKey = 'Q';
	
	Fraction chordFraction = null;

	public Lexer() {
		tokens = new ArrayList<ABCToken>();
	}
	public Lexer(String fileName) {
		this(fileName, true);
	}
	
	public Lexer(String inp, boolean isFileName) {
		BufferedReader input;
		if (isFileName) {
			// read file line by line
			try {
				input = new BufferedReader(new FileReader(inp));
				try {
					String line = null; // not declared within while loop
					while ((line = input.readLine()) != null) {
						this.readLine(line);
					}
				} finally {
					input.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			input = new BufferedReader(new StringReader(inp));
			String line = null;
			try {
				while ((line = input.readLine()) != null) {
					this.readLine(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			ABCToken token = ABCTokenBuilder.createBuilder()
					.setLexeme(ABCToken.Lexeme.ENDSECTION)
					.build();
			tokens.add(token);
		}

	}

	// given a line from the file, parse it
	public void readLine(String line) {
		if (line.indexOf('%') != -1) { // take out comments
			line = line.substring(0, line.indexOf('%'));
		}
		if (line.isEmpty()) {
			return;
		}
		if (line.charAt(0) == 'V') {
			// voice header
			try {
				String voiceName = line.substring(2).trim();
				ABCToken token = ABCTokenBuilder.createBuilder()
						.setLexeme(ABCToken.Lexeme.VOICE)
						.setVoiceName(voiceName)
						.build();
				tokens.add(token);
				fieldCount += 1;
				if (fieldCount == 1 || fieldCount == 2) {
					throw new RuntimeException("X or T are in the wrong place");
				}
				return;
			} catch (Exception e) {
				throw new RuntimeException("Badly formatted voice");
			}
		}
		if (isHeader) {
			
			// use a regex to read the key, value seperated by the :
			String pattern = "^([^|]):(.*)";

			// Create a Pattern object
			Pattern r = Pattern.compile(pattern);

			// Now create matcher object.
			Matcher m = r.matcher(line);
			if (m.find()) {
				char key = m.group(1).charAt(0);
				fieldCount += 1;
				lastKey = key;
				if ((key == 'X' && fieldCount != 1) || (key != 'X' && fieldCount == 1)) {
					throw new RuntimeException("X is not the first header");
				}
				if ((key == 'T' && fieldCount != 2) || (key != 'T' && fieldCount == 2)) {
					throw new RuntimeException("T is not the second header");
				}
				String value = m.group(2).trim();
				ABCToken t = ABCTokenBuilder.createBuilder()
						.setLexeme(ABCToken.Lexeme.HEADER)
						.setHeaderKey(key)
						.setHeaderValue(value)
						.build();
				tokens.add(t);
			} else {
				isHeader = false;
				
			}
		} 
		if (!isHeader) {
			for (int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				
				if (c == 'z') {
					String pattern = "^z(\\d)?(/(\\d)?)?";
					Pattern r = Pattern.compile(pattern);
					String restOfLine = line.substring(i);
					
					Matcher m = r.matcher(restOfLine);
					int numerator = -1;
					int denom = -1;
					if (m.find()) {
						
						//numerator
						if (m.group(1) != null && m.group(1).length() >= 1) {
							numerator = Integer.parseInt(m.group(1));
						}
						
						//denominator
						if (m.group(2) != null && m.group(2).length() >= 1) {
							if (m.group(3) != null && m.group(3).length() >= 1) {
								denom = Integer.parseInt(m.group(3));
							} else {
								denom = 2;
							}
						} else {
							denom = 1;
						}
							
					} else {
						throw new RuntimeException("bad rest");
					}
					Fraction frac = makeFraction(numerator, denom);

					curbuilder = ABCTokenBuilder.createBuilder()
							.setLexeme(ABCToken.Lexeme.REST)
							.setNoteDuration(frac);
					addToTokens(curbuilder);
					
				} else if (startsNote(c)) {
					// This is a note. Look at the entire note and process it.
					String pattern = "^([\\^=_]{0,2})([a-gA-G])([,']*)(\\d+)?(/(\\d+)?)?";
					Pattern r = Pattern.compile(pattern);
					String restOfLine = line.substring(i);
					
					Matcher m = r.matcher(restOfLine);
					String modifiers = "";
					String accModifiers = "";
					int accModifierNum = 0;
					int numerator = -1;
					int denom = -1;
					char noteName;
					char rawNoteName;
					if (m.find()) {
						if (m.group(1) != null) {
							accModifiers = m.group(1); // like ^^ or something
						}
						if (m.group(2).length() != 1) {
							throw new RuntimeException("Wrong note format");
						}
						rawNoteName = m.group(2).charAt(0);
						noteName = Character.toUpperCase(rawNoteName);
						if (m.group(3) != null) {
							modifiers = m.group(3); // like ''' or something
						}
						
						//numerator
						if (m.group(4) != null && m.group(4).length() >= 1) {
							numerator = Integer.parseInt(m.group(4));
						}
						
						//denominator
						if (m.group(5) != null && m.group(5).length() >= 1) {
							if (m.group(6) != null && m.group(6).length() >= 1) {
								denom = Integer.parseInt(m.group(6));
							} else {
								denom = 2;
							}
						} else {
							denom = 1;
						}
						
						
					} else {
						throw new RuntimeException("bad note");
					}
					
					// octave stuff; looking for things like ''' or ,,
					int octave = Character.isLowerCase(rawNoteName) ? 1 : 0;
					for (int j = 0; j < modifiers.length(); j++) {
						octave += modifiers.charAt(j) == ',' ? -1 : 1;
					}
					
					Fraction frac = makeFraction(numerator,denom);

					// accidental stuff
					if (!accModifiers.isEmpty()) {
						for (int j = 0; j < accModifiers.length(); j++) {
							if (accModifiers.charAt(j) == '_') {
								accModifierNum -= 1;
							} else if (accModifiers.charAt(j) == '^') {
								accModifierNum += 1;
							}
						}
						
						ABCToken token = ABCTokenBuilder.createBuilder()
								.setLexeme(ABCToken.Lexeme.ACCIDENTAL)
								.setNoteName(noteName)
								.setNoteOctave(octave)
								.setAccModifier(accModifierNum)
								.build();
						tokens.add(token);
					}
					curbuilder = ABCTokenBuilder.createBuilder()
							.setLexeme(ABCToken.Lexeme.NOTE)
							.setNoteName(noteName)
							.setNoteOctave(octave)
							.setNoteDuration(frac);

					addToTokens(curbuilder);
					
					i += m.group(0).length() - 1; // -1 because 1 is being added at the end
				} else if (c == '[') {
					// chord or multiending
					if (line.length() > i + 1 && Character.isDigit(line.charAt(i+1))) {
						// multiending
						ABCToken token = ABCTokenBuilder.createBuilder()
								.setLexeme(ABCToken.Lexeme.MULTIENDING)
								.setMultiEndingNumber(Integer.parseInt(""+line.charAt(i+1)))
								.build();
						tokens.add(token);
						i += 1;
					} else {
						// chord
						// find the possible scaleing factor to duration of notes in chord
						int j = i;
						try {
							while (line.charAt(j) != ']') {
								j++;
							}
						} catch (Exception e) {
							throw new RuntimeException("No ending to the chord");
						}
						
						String pattern = "^(\\d)?(/(\\d)?)?";
						Pattern r = Pattern.compile(pattern);
						String restOfLine = line.substring(j+1);
						Matcher m = r.matcher(restOfLine);
						int numerator = -1;
						int denom = -1;
						if (m.find()) {
							if (m.group(1) != null && m.group(1).length() >= 1) {
								numerator = Integer.parseInt(m.group(1));
							}
							if (m.group(3) != null && m.group(3).length() >= 1) {
								denom = Integer.parseInt(m.group(3));
							}
						} else {
							throw new RuntimeException("bad chord fraction");
						}

						chordFraction = makeFraction(numerator, denom);
						
						ABCToken token = ABCTokenBuilder.createBuilder()
								.setLexeme(ABCToken.Lexeme.STARTCHORD)
								.build();
						tokens.add(token);
					}
				} else if (c == ']') {
					// process chord
					ABCToken token = ABCTokenBuilder.createBuilder()
							.setLexeme(ABCToken.Lexeme.ENDCHORD)
							.build();
					tokens.add(token);
					chordFraction = null;
				} else if (c == '(') {
					// process tuplet
					try {
						c = line.charAt(i+1);
						int numChars = Integer.parseInt(""+c);
						String tuple = line.substring(i + 2, i + 2 + numChars);
						tupletCount = numChars;
						
						ABCToken token = ABCTokenBuilder.createBuilder()
								.setLexeme(ABCToken.Lexeme.STARTTUPLET)
								.setStartTupletNoteCount(tupletCount)
								.build();
						tokens.add(token);
						
						i += 1;
					} catch (Exception e) {
						throw new RuntimeException("Could not process tuplet");
					}
				} else if (c == ':') {
					// end repeat
					try {
						if (line.charAt(i+1) == '|') {
							ABCToken token = ABCTokenBuilder.createBuilder()
									.setLexeme(ABCToken.Lexeme.ENDREPEAT)
									.build();
							tokens.add(token);
							i += 1;
						}
					} catch (Exception e) {
						throw new RuntimeException("Could not process end repeat");
					}
				} else if (c == '|') {
					// endbar, start repeat, or end section
					if (line.length() > i + 1 && line.charAt(i+1) == ':') {
						ABCToken token = ABCTokenBuilder.createBuilder()
								.setLexeme(ABCToken.Lexeme.STARTREPEAT)
								.build();
						tokens.add(token);
						i += 1;
					} else if (line.length() > i + 1 && (line.charAt(i+1) == '|' || line.charAt(i+1) == ']')){
						ABCToken token = ABCTokenBuilder.createBuilder()
								.setLexeme(ABCToken.Lexeme.ENDSECTION)
								.build();
						tokens.add(token);
						token = ABCTokenBuilder.createBuilder()
								.setLexeme(ABCToken.Lexeme.STARTSECTION)
								.build();
						tokens.add(token);
						i += 1;
					} else {
						ABCToken token = ABCTokenBuilder.createBuilder()
								.setLexeme(ABCToken.Lexeme.ENDBAR)
								.build();
						tokens.add(token);
						token = ABCTokenBuilder.createBuilder()
								.setLexeme(ABCToken.Lexeme.STARTBAR)
								.build();
						tokens.add(token);
					}
				}

			}
		}
	}
	

	/*
	 * Modifies: prevTokenString
	 */
	public void addToTokens(ABCTokenBuilder tokenBuilder) {
		if (tokenBuilder == null) {
			return;
		}
		tokens.add(tokenBuilder.build()); // this does the check to see if everything is ok
	}
	

	public static void main(String [] args) {
		Lexer l = new Lexer("sample_abc/paddy.abc");
		for (ABCToken t : l.tokens) {
			System.out.print(t.toString());
		}
	}
	
	
	public static boolean startsNote(char c) {
		return Character.isLetter(c) || c == '_' || c == '=' || c == '^';
	}
	
	/*
	 * Given numerators and denominators, compute a fraction
	 * Either may be -1, which means it doesn't exist and we have to make defaults
	 */
	public static Fraction makeFraction(int num, int denom) {
		Fraction frac;
		if (num == -1 && denom == -1) {
			frac = new Fraction(1, 1);
		} else {
			num = num == -1 ? 1 : num;
			denom = denom == -1 ? 2 : denom;
			frac = new Fraction(num, denom);
		}
		return frac;
	}
}
