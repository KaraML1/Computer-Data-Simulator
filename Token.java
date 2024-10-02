
public class Token {
	
	private TokenType type;
	
	enum TokenType { // Key Words
		math, branch, jump, call, push, load, pop, returnT, store, peek, interrupt, shift, halt, // Operation Tokens
		add, subtract, multiply, copy, // Math Tokens
		and, or, not, xor,
		equal, unequal, greater, less, greaterOrEqual, lessOrEqual, // Boolean Operator Tokens
		left, right, // Shift Direction Tokens
		num, register, // Number Tokens
		noR, destOnly, threeR, twoR; // Needs to be specified since some instructions are not distinct enough
	}
	
	Token (TokenType type) {
		this.type = type;
	}
	
	public TokenType getTokenType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return type.toString();
	}
}
