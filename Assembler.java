import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Assembler {
	private Word convertedWord; // Representation of the parsed currentStatement
	private LinkedList<Token> currentStatement;
	private List<String> outcome;
	
	private String[] data; // An array of the data, split by "\n" to be converted into tokens
	private String [] currentString;
	private int index = 0; // Index of data
	
	Assembler(String data) throws Exception {
		this.currentStatement = new LinkedList<Token>();
		this.data = data.toLowerCase().split("\n");
		this.outcome = new ArrayList<String>();
		for (String s : this.data) {
			lex();
		}
		load();
	}
	
	/* Tokenizes each statement into a series of Tokens
	 * Ends a statement when encountering a newLine ("\n")
	 */
	private void lex() throws Exception {
		this.currentString = data[index].split("\s");
		for (int i = 0; i < currentString.length; i++) {
			switch (currentString[i]) {
				case ("math"): {
					currentStatement.add(new OperationToken(Token.TokenType.math));
					break;
				}
				case ("add"): {
					currentStatement.add(new MathToken(Token.TokenType.add));
					break;
				}
				case ("subtract"): {
					currentStatement.add(new MathToken(Token.TokenType.subtract));
					break;
				}
				case ("multiply"): {
					currentStatement.add(new MathToken(Token.TokenType.multiply));
					break;
				}
				case ("and"): {
					currentStatement.add(new MathToken(Token.TokenType.and));
					break;
				}
				case ("or"): {
					currentStatement.add(new MathToken(Token.TokenType.or));
					break;
				}
				case ("not"): {
					currentStatement.add(new MathToken(Token.TokenType.not));
					break;
				}
				case ("xor"): {
					currentStatement.add(new MathToken(Token.TokenType.xor));
					break;
				}
				case ("copy"): {
					currentStatement.add(new OperationToken(Token.TokenType.copy));
					break;
				}
				case ("halt"): {
					currentStatement.add(new OperationToken(Token.TokenType.halt));
					break;
				}
				case ("branch"): {
					currentStatement.add(new OperationToken(Token.TokenType.branch));
					break;
				}
				case ("jump"): {
					currentStatement.add(new OperationToken(Token.TokenType.jump));
					break;
				}
				case ("call"): {
					currentStatement.add(new OperationToken(Token.TokenType.call));
					break;
				}
				case ("push"): {
					currentStatement.add(new OperationToken(Token.TokenType.push));
					break;
				}
				case ("load"): {
					currentStatement.add(new OperationToken(Token.TokenType.load));
					break;
				}
				case ("pop"): {
					currentStatement.add(new OperationToken(Token.TokenType.pop));
					break;
				}
				case ("return"): {
					currentStatement.add(new OperationToken(Token.TokenType.returnT));
					break;
				}
				case ("store"): {
					currentStatement.add(new OperationToken(Token.TokenType.store));
					break;
				}
				case ("peek"): {
					currentStatement.add(new OperationToken(Token.TokenType.peek));
					break;
				}
				case ("interrupt"): {
					currentStatement.add(new OperationToken(Token.TokenType.interrupt));
					break;
				}
				case ("equal"): {
					currentStatement.add(new BooleanToken(Token.TokenType.equal));
					break;
				}
				case ("unequal"): {
					currentStatement.add(new BooleanToken(Token.TokenType.unequal));
					break;
				}
				case ("greater"): {
					currentStatement.add(new Token(Token.TokenType.greater));
					break;
				}
				case ("less"): {
					currentStatement.add(new BooleanToken(Token.TokenType.less));
					break;
				}
				case ("greaterorequal"): {
					currentStatement.add(new BooleanToken(Token.TokenType.greaterOrEqual));
					break;
				}
				case ("lessorequal"): {
					currentStatement.add(new BooleanToken(Token.TokenType.lessOrEqual));
					break;
				}
				case ("shift"): {
					currentStatement.add(new OperationToken(Token.TokenType.shift));
					break;
				}
				case ("left"): {
					currentStatement.add(new ShiftToken(Token.TokenType.left));
					break;
				}
				case ("right"): {
					currentStatement.add(new ShiftToken(Token.TokenType.right));
					break;
				}
				/* Some Instructions are too vague to be classified without specific mention of number of Registers
				 * ie) Branch's Jump where noR and destOnly only use immediateValue
				 * Thus Tokens to include the number of registers are included to clarify when needed so they're defaulted to 0
				 */
				case ("twor"): {
					currentStatement.add(new RegisterToken(Token.TokenType.register, 0));
					break;
				}
				case ("threer"): {
					currentStatement.add(new RegisterToken(Token.TokenType.register, 0));
					currentStatement.add(new RegisterToken(Token.TokenType.register, 0));
					break;
				}
				case ("destonly"): {
					currentStatement.add(new RegisterToken(Token.TokenType.register, 0));
					break;
				}
				case ("nor"): {
					break;
				}
				default: // Expect Only Registers or Numbers at this point
					try {
						if (currentString[i].charAt(0) == 'r') currentStatement.add(new RegisterToken(Token.TokenType.register, Integer.parseInt(currentString[i].substring(1))));
						else currentStatement.add(new NumToken(Token.TokenType.num, Integer.parseInt(currentString[i])));
					} catch (Exception e) {
						throw new Exception("Illegal Character(s) " + currentString[i] + ", " + e);
					}
			}
		}
		index++;
		convertToBits();
	}

	private void load() throws Exception {
		System.out.println("Loading these statements:");
		var output = this.getOutput();
		for (int i = 0; i < output.length; i++) {
			System.out.println(output[i]);
		}
		MainMemory.load(this.getOutput());
	}
	
	private void convertToBits() throws Exception {
		// Convert the current statement into a line of bits. Add to output list. Continue until all statements have been converted.
		convertedWord = new Word();
		parseStatement();
		outcome.add(convertedWord.toStringBinary()); // Add to the outcome 
	}

	private void parseStatement() throws Exception {
		System.out.println(this.currentStatement);
		parseNoReg();
	}
	
	private Token parseOperation() throws Exception {
		if (currentStatement.isEmpty()) return null;
		var type = currentStatement.pop();
		switch (type.getTokenType()) {
			case halt: // 000 00 opCode
			case copy: // 000 01 opCode
			case math: {
				// 000 opCode
				return type;
			}
			case jump: // 001 01 or 001 00 opCode
			case branch:  {
				// 001 opCode
				this.convertedWord.setBit(2, new Bit(true));
				return type;
			}
			case call: {
				// 010 opCode
				this.convertedWord.setBit(3, new Bit(true));
				return type;
			}
			case push: {
				// 011 opCode
				this.convertedWord.setBit(2, new Bit(true));
				this.convertedWord.setBit(3, new Bit(true));
				return type;
			}
			case returnT: // 100 00 opCode
			case load: {
				// 100 opCode
				this.convertedWord.setBit(4, new Bit(true));
				return type;
			}
			case store: {
				// 101 opCode
				this.convertedWord.setBit(2, new Bit(true));
				this.convertedWord.setBit(4, new Bit(true));
				return type;
			}
			case peek: // 110 01 and 110 10 opCode
			case interrupt: // Unused - 110 00 opCode
			case pop: {
				// 110 01 opCode
				this.convertedWord.setBit(3, new Bit(true));
				this.convertedWord.setBit(4, new Bit(true));
				return type;
			}
			default: {
				return type;
			}
		}
	}
	
	private Token parseMath() throws Exception {
		var result = parseOperation();
		if (result.getTokenType() == Token.TokenType.math || result.getTokenType() == Token.TokenType.push) {
			switch (currentStatement.pop().getTokenType()) {
				case add: { // 1110
					this.convertedWord.setBit(13, new Bit(true));
					this.convertedWord.setBit(12, new Bit(true));
					this.convertedWord.setBit(11, new Bit(true));
					break;
				}
				case subtract: { // 1111
					this.convertedWord.setBit(13, new Bit(true));
					this.convertedWord.setBit(12, new Bit(true));
					this.convertedWord.setBit(11, new Bit(true));
					this.convertedWord.setBit(10, new Bit(true));
					break;
				}
				case multiply: { // 0111
					this.convertedWord.setBit(12, new Bit(true));
					this.convertedWord.setBit(11, new Bit(true));
					this.convertedWord.setBit(10, new Bit(true));
					break;
				}
				case or: { // 1001
					this.convertedWord.setBit(13, new Bit(true));
					this.convertedWord.setBit(10, new Bit(true));
					break;
				}
				case and: { // 1000
					this.convertedWord.setBit(13, new Bit(true));
					break;
				}
				case xor: { // 1010
					this.convertedWord.setBit(13, new Bit(true));
					this.convertedWord.setBit(11, new Bit(true));
					break;
				}
				case not: { // 1011
					this.convertedWord.setBit(13, new Bit(true));
					this.convertedWord.setBit(11, new Bit(true));
					this.convertedWord.setBit(10, new Bit(true));
					break;
				}
				default: {
					throw new Exception("parseMath: " + result.getTokenType() + "Illegal argument used");
				}
			}
		}
		return result;
	}
	
	private Token parseBool() throws Exception {
		var result = parseMath();
		if (result.getTokenType() == Token.TokenType.branch || result.getTokenType() == Token.TokenType.call) {
			var temp = currentStatement.pop();
			switch (temp.getTokenType()) {
				case equal: {
					// 0000
					break;
				}
				case unequal: {
					// 0001
					this.convertedWord.setBit(10, new Bit(true));
					break;
				}
				case greater: {
					// 0100
					this.convertedWord.setBit(12, new Bit(true));
					break;
				}
				case less: {
					// 0010
					this.convertedWord.setBit(11, new Bit(true));
					break;
				}
				case greaterOrEqual: {
					// 0011
					this.convertedWord.setBit(11, new Bit(true));
					this.convertedWord.setBit(10, new Bit(true));
					break;
				}
				case lessOrEqual: {
					// 0101
					this.convertedWord.setBit(12, new Bit(true));
					this.convertedWord.setBit(10, new Bit(true));
					break;
				}
				default: {
					return temp; // Else likely encountered a JUMP or callPush
				}
			}
		}
		return result;
	}
	
	private Token parseShift() throws Exception { // [Left/Right] -> expect some Registers
		var result = parseBool();
		if (result instanceof ShiftToken) {
			var leftOrRight = this.currentStatement.pop().getTokenType();
			if (leftOrRight == Token.TokenType.left) { // Set the 4 bit Function code
				this.convertedWord.setBit(13, new Bit(true));
				this.convertedWord.setBit(12, new Bit(true));
			} else if (leftOrRight == Token.TokenType.right) {
				this.convertedWord.setBit(13, new Bit(true));
				this.convertedWord.setBit(12, new Bit(true));
				this.convertedWord.setBit(10, new Bit(true));
			} else throw new Exception("parseShift: Expected a Left or Right");
		}
		return result;
	}
	
	private Token parseThreeRegisters() throws Exception { 
		// Expect Register (r1) Register (r2) Register (rD)
		// Once this point is reached, expect only registers and the immediate value to be left
		var result = parseShift();
		if (currentStatement.isEmpty()) return result;
		var temp = currentStatement.pop();
		if (currentStatement.size() < 2) return temp;
		if (currentStatement.peek().getTokenType() == Token.TokenType.register) {
			if (currentStatement.peekLast().getTokenType() == Token.TokenType.num) return temp; // Last value is actually the immediateValue so it is likely a 2R
				for (int i = 23, k = 0; i > 18; i--, k++) { // Rs1 Spot
					this.convertedWord.setBit(i, ((RegisterToken) temp).getBinaryData()[k]);
				}
				temp = currentStatement.pop();
				for (int i = 18, k = 0; i > 13; i--, k++) { // Rs2 Spot
					this.convertedWord.setBit(i, ((RegisterToken) temp).getBinaryData()[k]);
				}
				temp = currentStatement.pop();
				for (int i = 9, k = 0; i > 4; i--, k++) { // Rd Spot
					this.convertedWord.setBit(i, ((RegisterToken) temp).getBinaryData()[k]);
				}	
				convertedWord.setBit(1, new Bit(true));
				if (currentStatement.isEmpty()) return null;
		}
		return temp;
	}
	
	private Token parseTwoRegisters() throws Exception { // Expect Register Register
		var result = parseThreeRegisters();
		if (result instanceof RegisterToken) {
			if (currentStatement.peek() != null && currentStatement.peek().getTokenType() == Token.TokenType.register) {
				for (int i = 18, k = 0; i > 13; i--, k++) {
					this.convertedWord.setBit(i, ((RegisterToken) result).getBinaryData()[k]);
				}
				result = currentStatement.pop();
				for (int i = 9, k = 0; i > 4; i--, k++) {
					this.convertedWord.setBit(i, ((RegisterToken) result).getBinaryData()[k]);
				}
				convertedWord.setBit(0, new Bit(true));
				convertedWord.setBit(1, new Bit(true));
				if (currentStatement.isEmpty()) return null;
				else return currentStatement.pop();
			}
		}
		return result;
	}
	
	private Token parseDestOnly() throws Exception { // Expect Register NUM
		var result = parseTwoRegisters();
		if (result == null) return result;
		if (result instanceof RegisterToken) {
				for (int i = 9, k = 0; i > 4; i--, k++) {
					this.convertedWord.setBit(i, ((RegisterToken) result).getBinaryData()[k]);
				}
				convertedWord.setBit(0, new Bit(true));
				if (currentStatement.isEmpty()) return result;
				result = currentStatement.pop();
		}
		return result;
	}
	
	private void parseNoReg() throws Exception { // Expect NUM
		var result = parseDestOnly();
		if (result == null) return;
		if (result instanceof NumToken) {
			var temp = ((NumToken) result).getData();
			var word = new Word();
			word.set(temp); 
			// Accounts for negative and pos numbers. Expect to be filled within the ranges
			// Expect Registers to be marked. Check what Register Operation then set the immediateValue
			if (this.convertedWord.getBit(0).getData()) { // X1
				if (this.convertedWord.getBit(1).getData()) { // 11
					for (int i = 19, k = 0; i < 32; i++, k++) {
						convertedWord.setBit(i, word.getBit(k)); // Set immediate Value
					}
				} else { // 01
					for (int i = 14, k = 0; i < 32; i++, k++) {
						convertedWord.setBit(i, word.getBit(k)); // Set immediate Value
					}
				}
			} else if (this.convertedWord.getBit(1).getData()) { // 10
				for (int i = 24, k = 0; i < 32; i++, k++) {
					convertedWord.setBit(i, word.getBit(k)); // Set immediate Value
				}
			} else { // 00
				for (int i = 5, k = 0; i < 32; i++, k++) {
					convertedWord.setBit(i, word.getBit(k)); // Set immediate Value
				}
			}
		}
	}
	
	public String[] getOutput() { // A format acceptable to the MainMemory
		return this.outcome.toArray(new String[0]);
	}
}
