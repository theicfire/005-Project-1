package lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

	public ArrayList<ABCToken> tokens = new ArrayList<ABCToken>();
	public boolean isHeader = true;

	public Lexer(String fileName) {
		System.out.println("constructing Lexer");

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
		if (isHeader) {
			// use a regex to read the key, value seperated by the :
			String pattern = "^(.):(.*)";

			// Create a Pattern object
			Pattern r = Pattern.compile(pattern);

			System.out.println("looking over: " + line);
			// Now create matcher object.
			Matcher m = r.matcher(line);
			if (m.find()) {
				ABCToken t = ABCTokenBuilder.createBuilder()
						.setLexeme(ABCToken.Lexeme.HEADER)
						.setHeaderKey(m.group(1))
						.setHeaderValue(m.group(2))
						.build();
				tokens.add(t);
			} else {
				isHeader = false;
			}
		} else {
			
		}

	}
}
