
public class OperationToken extends Token {
	// math, branch, jump, call, push, load, popReturn, store, peek, interrupt, shift, halt

	OperationToken(Token.TokenType type) {
		super(type);
	}
}
