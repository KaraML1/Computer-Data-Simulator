
public class NumToken extends Token {

	private int data = -1;
	
	NumToken (Token.TokenType type, int data) {
		super(type);
		this.data = data;
	}
	
	public int getData() {
		return this.data;
	}
	
	@Override
	public String toString() {
		return String.valueOf(data);
	}
}
