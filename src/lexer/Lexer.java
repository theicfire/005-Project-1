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
		if (isHeader) {
			// use a regex to read the key, value seperated by the :
			String pattern = "^(.):(.*)";

			// Create a Pattern object
			Pattern r = Pattern.compile(pattern);

			// Now create matcher object.
			Matcher m = r.matcher(line);
			if (m.find()) {
				String key = m.group(1);
				String value = m.group(2);
				ABCToken t = ABCTokenBuilder.createBuilder()
						.setLexeme(ABCToken.Lexeme.HEADER)
						.setHeaderKey(key)
						.setHeaderValue(value)
						.build();
				tokens.add(t);
			} else {
				isHeader = false;
			}
		} else {
			for (int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				
				// See if we should put an ENDTUPLET in and if states are correct
				updateTuplet(c);
				
				if (startsNote(c)) {
					// TODO: worry about accidentals
					// This is a note. Look at the entire note and process it.
					String pattern = "^([a-zA-z])([,']*)(\\d)?(/(\\d)?)?";
					Pattern r = Pattern.compile(pattern);
					String restOfLine = line.substring(i);
//					System.out.println("found letter, looking over " + restOfLine);
					
					Matcher m = r.matcher(restOfLine);
					String modifiers = "";
					int numerator = -1;
					int denom = -1;
					char noteName;
					if (m.find()) {
						if (m.group(1).length() != 1) {
							throw new RuntimeException("Wrong note format");
						}
						noteName = m.group(1).charAt(0);
						if (m.group(2) != null) {
							modifiers = m.group(2); // like ''' or something
						}
						if (m.group(3) != null && m.group(3).length() > 1) {
							throw new RuntimeException("Wrong numerator format");
						}
						if (m.group(3) != null && m.group(3).length() >= 1) {
							numerator = Integer.parseInt(m.group(3));
						}
						if (m.group(5) != null && m.group(5).length() >= 1) {
							denom = Integer.parseInt(m.group(5));
						}
//						System.out.println("end result " + noteName + " " + modifiers + " " + numerator + " " + denom);
					} else {
						throw new RuntimeException("bad fraction");
					}
					int octave = Character.isLowerCase(c) ? 1 : 0;
					for (int j = 0; j < modifiers.length(); j++) {
						octave += modifiers.charAt(j) == ',' ? -1 : 1;
					}
					Fraction frac;
					if (numerator == -1 && denom == -1) {
						frac = new Fraction(1, 1);
					} else {
						numerator = numerator == -1 ? 1 : numerator;
						denom = denom == -1 ? 2 : denom;
						frac = new Fraction(numerator, denom);
					}
					curbuilder = ABCTokenBuilder.createBuilder()
							.setLexeme(ABCToken.Lexeme.NOTE)
							.setNoteName(Character.toUpperCase(c))
							.setNoteOctave(octave)
							.setNoteDuration(frac);

					addToTokens(curbuilder);
					
//					System.out.println("full group" + m.group(0));
					i += m.group(0).length() - 1; // -1 because 1 is being added at the end
				} else if (c == '[') {
					// process chord
					ABCToken token = ABCTokenBuilder.createBuilder()
							.setLexeme(ABCToken.Lexeme.STARTCHORD)
							.build();
					tokens.add(token);
				} else if (c == ']') {
					// process chord
					ABCToken token = ABCTokenBuilder.createBuilder()
							.setLexeme(ABCToken.Lexeme.ENDCHORD)
							.build();
					tokens.add(token);
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
					// endbar or start repeat
					if (line.length() > i + 1 && line.charAt(i+1) == ':') {
						ABCToken token = ABCTokenBuilder.createBuilder()
								.setLexeme(ABCToken.Lexeme.STARTREPEAT)
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
}
