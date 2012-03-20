package lexer;

import lexer.Lexer;

/**
 * Main entry point of your application.
 */
public class Main {

	public static void play(String file) {
		Lexer l = new Lexer(file);
	}
	
	public static void main(String [] args) {
		play("sample_abc/paddy.abc");
	}
}
