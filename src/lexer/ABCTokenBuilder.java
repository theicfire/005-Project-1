package lexer;

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
	
	// TODO: more
	
	private void check() {
        if (done) {
        	throw new IllegalArgumentException("Do use other builder to create new instance");
        }
	}
}
