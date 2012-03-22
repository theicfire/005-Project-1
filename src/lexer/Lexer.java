package lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import player.Fraction;

public class Lexer {

	public ArrayList<ABCToken> tokens = new ArrayList<ABCToken>();
	public boolean isHeader = true;
	
	public ABCTokenBuilder curbuilder = null;
	public int tupletCount = -1;
	public int fieldCount = 0;
	public int lastKey = 'Q';
	
	Fraction chordFraction = null;

	public Lexer(String fileName) {

		// read file line by line
		try {
			BufferedReader input = new BufferedReader(new FileReader(fileName));
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
		
		System.out.println("Done" + tokens);
	}

	// given a line from the file, parse it
	public void readLine(String line) {
		// TODO: make sure x is first
		if (!line.isEmpty() && line.charAt(0) == 'V') {
			// voice header
			try {
				String voiceName = line.substring(2);
				ABCToken token = ABCTokenBuilder.createBuilder()
						.setLexeme(ABCToken.Lexeme.VOICE)
						.setVoiceName(voiceName)
						.build();
				tokens.add(token);
				return;
			} catch (Exception e) {
				throw new RuntimeException("Badly formatted voice");
			}
		}
		if (isHeader) {
			
			// use a regex to read the key, value seperated by the :
			String pattern = "^(.):(.*)";

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
				String value = m.group(2);
				ABCToken t = ABCTokenBuilder.createBuilder()
						.setLexeme(ABCToken.Lexeme.HEADER)
						.setHeaderKey(key)
						.setHeaderValue(value)
						.build();
				tokens.add(t);
			} else {
				isHeader = false;
				if (lastKey != 'K') {
					throw new RuntimeException("last header key is not K");
				}
				ABCToken token = ABCTokenBuilder.createBuilder()
						.setLexeme(ABCToken.Lexeme.STARTSECTION)
						.build();
				tokens.add(token);
				
				
				// TAKE OUT
				token = ABCTokenBuilder.createBuilder()
						.build();
				tokens.add(token);
			}
		} 
		if (!isHeader) {
			for (int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				
				// See if we should put an ENDTUPLET in and if states are correct
				updateTuplet(c);
				
				if (startsNote(c)) {
					// This is a note. Look at the entire note and process it.
					String pattern = "^([\\^=_]{0,2})([a-zA-z])([,']*)(\\d)?(/(\\d)?)?";
					Pattern r = Pattern.compile(pattern);
					String restOfLine = line.substring(i);
//					System.out.println("found letter, looking over " + restOfLine);
					
					Matcher m = r.matcher(restOfLine);
					String modifiers = "";
					String accModifiers = "";
					int accModifierNum = 0;
					int numerator = -1;
					int denom = -1;
					char noteName;
					if (m.find()) {
						if (m.group(1) != null) {
							accModifiers = m.group(1); // like ^^ or something
						}
						if (m.group(2).length() != 1) {
							throw new RuntimeException("Wrong note format");
						}
						noteName = m.group(2).charAt(0);
						if (m.group(3) != null) {
							modifiers = m.group(3); // like ''' or something
						}
						if (m.group(4) != null && m.group(4).length() > 1) {
							throw new RuntimeException("Wrong numerator format");
						}
						if (m.group(4) != null && m.group(4).length() >= 1) {
							numerator = Integer.parseInt(m.group(4));
						}
						if (m.group(6) != null && m.group(6).length() >= 1) {
							denom = Integer.parseInt(m.group(6));
						}
//						System.out.println("end result " + noteName + " " + modifiers + " " + numerator + " " + denom);
					} else {
						throw new RuntimeException("bad note");
					}
					
					// octave stuff; looking for things like ''' or ,,
					int octave = Character.isLowerCase(c) ? 1 : 0;
					for (int j = 0; j < modifiers.length(); j++) {
						octave += modifiers.charAt(j) == ',' ? -1 : 1;
					}
					
					Fraction frac;
					if (chordFraction != null) {
						frac = chordFraction;
					} else {
						frac = makeFraction(numerator, denom);
					}
					
					
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
					
//					System.out.println("full group" + m.group(0));
					i += m.group(0).length() - 1; // -1 because 1 is being added at the end
				} else if (c == 'z') {
					
					String pattern = "^z(\\d)?(/(\\d)?)?";
					Pattern r = Pattern.compile(pattern);
					String restOfLine = line.substring(i);
					
					Matcher m = r.matcher(restOfLine);
					int numerator = -1;
					int denom = -1;
					if (m.find()) {
						if (m.group(1) != null && m.group(1).length() >= 1) {
							numerator = Integer.parseInt(m.group(1));
						}
						if (m.group(2) != null && m.group(2).length() >= 1) {
							denom = Integer.parseInt(m.group(2));
						}
					} else {
						throw new RuntimeException("bad rest");
					}
					Fraction frac = makeFraction(numerator, denom);

					curbuilder = ABCTokenBuilder.createBuilder()
							.setLexeme(ABCToken.Lexeme.REST)
							.setNoteDuration(frac);
					addToTokens(curbuilder);
					
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
							if (m.group(2) != null && m.group(2).length() >= 1) {
								denom = Integer.parseInt(m.group(2));
							}
						} else {
							throw new RuntimeException("bad chord");
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
						System.out.println("tuple is " + tuple);
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
						throw new RuntimeException("Could not process tuplet");
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
	
	public void updateTuplet(char c) {
		if (tupletCount > 0 && !startsNote(c)) {
				throw new RuntimeException("Tokens other than notes are in tuplet");
		} else if (tupletCount == 0) {
			ABCToken token = ABCTokenBuilder.createBuilder()
					.setLexeme(ABCToken.Lexeme.ENDTUPLET)
					.build();
			tokens.add(token);
		}
		tupletCount -= 1;
	}
	// TODO: remove
	public static void main(String [] args) {
		Lexer l = new Lexer("sample_abc/paddy.abc");
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
