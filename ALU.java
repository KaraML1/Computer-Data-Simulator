

public class ALU { // Arthimetic Logic Unit; Handles Math and Boolean Operations for the Processor
    public Word op1; // Note: Public for Testing
    public Word op2;
    public Word result = new Word(); // result OBJECT doesn't get overwritten, only changed so as to not ruin any pointers

    public void doOperation(Bit[] operation) throws Exception { 
    	// Assumes a 4 bit Array to read from. Must compare individual bits.
    	/* Math Operations:
	    	 * 1000 AND
	    	 * 1100 leftShift
	    	 * 1010 XOR
	    	 * 1110 add
	    	 * 1101 rightShift 
	    	 * 1001 OR
	    	 * 1011 NOT
	    	 * 1111 subtract
	    	 * 0111 multiply
    	 * Boolean Operations
	    	 * 0000 equals (eq)
	    	 * 0001 not equal (ne)
	    	 * 0010 less than (lt)
	    	 * 0011 greater than or equal to (ge)
	    	 * 0100 greater than (gt)
	    	 * 0101 less than or equal to (le)
    	 */
    	if (operation[0].getData()) { // XXX1
    		if (operation[1].getData()) { // XX11
    			if (operation[2].getData()) { // X111
    				if (operation[3].getData()) {
    					subtractOperation(); // 1111
    				}
    				else {
    					multiply(op1, op2); // 0111
    				}
    			} else {
    				if (operation[3].getData()) result.copy(op1.not()); // 1011, ignores op2
    				else GEOperation(); // 0011
    			}
    		} else {
    			if (operation[2].getData()) { // X101
    				if (operation[3].getData()) rightShiftOperation(); // 1101
    				else LEOperation(); // 0101
    			} else { // XX01
    				if (operation[3].getData()) result.copy(op1.or(op2)); // 1001
    				else NEOperation(); // 0001
    			}
    		}
    	} else {
    		if (operation[1].getData()) { // XX10
    			if (operation[2].getData()) result.copy(add2(op1, op2)); // 1110
    			else {
    				if (operation[3].getData()) result.copy(op1.xor(op2)); // 1010
    				else LTOperation(); // 0010
    			}
    		} else { // XX00
    			if (operation[2].getData()) { // X100
    				if (operation[3].getData()) leftShiftOperation(); // 1100
    				else GTOperation(); // 0100
    			} else { // X000
    				if (operation[3].getData()) result.copy(op1.and(op2)); // 1000
    				else EQOperation(); // 0000
    			}
    		}
    	}
    }

	private void leftShiftOperation() throws Exception {
    	var temp = new Word(); // Takes the first 5 bits only to shift to
    	result.copy(op1);
		for (int i = 0; i < 5; i++) temp.setBit(i, op2.getBit(i));
		result.leftShift((int) temp.getUnsigned());
	}
	
	private void rightShiftOperation() throws Exception {
    	var temp = new Word(); // Takes the first 5 bits only to shift to
    	result.copy(op1);
		for (int i = 0; i < 5; i++) temp.setBit(i, op2.getBit(i));
		result.rightShift((int) temp.getUnsigned());
	}

	private void subtractOperation() throws Exception {
		var b = new Word(); // (a - b) = (a + -b) | (-b) = not(b) + 1
		b.copy(op2);
		b.invertHelper(); // inverts and adds 1
		result.copy(add2(op1, b));		
	}
	
    private void EQOperation() throws Exception {
		// x - y == 0
    	subtractOperation();
    	if (equalsHelper()) result.setBit(0, new Bit(true));
    	else result.setBit(0, new Bit());
	}
    
    private boolean equalsHelper() throws Exception { // Helper method to determine if two Words are equal
		for (int i = 0; i < 32; i++) {
			if (op1.getBit(i).getData() != op2.getBit(i).getData()) {
				return false;
			}
		}
		return true;
    }


	private void NEOperation() throws Exception {
		// x - y != 0
    	subtractOperation();
    	for (Bit b : result.getData()) 
	    	if (b.getData()) {
	    		result.setBit(0, new Bit(true));
	    		return;
	    	}
    	result.setBit(0, new Bit(false));
	}

	private void LEOperation() throws Exception {
		// x - y; if x is negative or 0 (check MSB) then true
		subtractOperation();
		if (result.getBit(31).getData()) result.setBit(0, new Bit(true));
		else if (equalsHelper()) result.setBit(0, new Bit(true));
		else result.setBit(0, new Bit(false));
	}
	
	private void LTOperation() throws Exception {
		subtractOperation();
		if (result.getBit(31).getData()) result.setBit(0, new Bit(true));
		else if (equalsHelper()) result.setBit(0, new Bit(true));
		else result.setBit(0, new Bit(false));
	}

	private void GEOperation() throws Exception {
		// x - y; if x is positive or 0 (check MSB) then true
		subtractOperation();
		if (!result.getBit(31).getData()) result.setBit(0, new Bit(true));
		else if (equalsHelper()) result.setBit(0, new Bit(true));
		else result.setBit(0, new Bit(false));
	}

	private void GTOperation() throws Exception {
		subtractOperation();
		if (!result.getBit(31).getData()) {
			if (equalsHelper()) result.setBit(0, new Bit());
			else result.setBit(0, new Bit(true));
		}
		else result.setBit(0, new Bit());
	}

	/* Multiplies in 3 rounds 
     * 1) First, Eight 4-way adders 
     * 2) Then two 4-way adders
     * 3) Then one 2-way adder, giving final result
     */
	public void multiply(Word word1, Word word2) throws Exception { // Note: Made public for testing
		Word[] words = new Word[32]; // Hold all equations to be added
		Word[] round1 = new Word[8]; // 8 4-way adders
		Word[] round2 = new Word[2]; // 2 4-way adders
		
		for (int i = 0; i < 32; i++) {
			words[i] = new Word();
			if (word2.getBit(i).getData()) {
				words[i].copy(word1.leftShift(i)); // Need to shift by i for every digit for multiplication
			}
		}
		
		for (int i = 0; i < 32; i += 4) { // Round 1
		    round1[i / 4] = add4(words[i], words[i + 1], words[i + 2], words[i + 3]);
		}	
		for (int i = 0; i < 8; i += 4) { // Round 2
		    round2[i / 4] = add4(round1[i], round1[i + 1], round1[i + 2], round1[i + 3]);
		}	
		result.copy(add2(round2[0], round2[1]));
	}

	public Word add2(Word word1, Word word2) throws Exception { // Adds 2 bits together, Note: Made public for testing
		boolean carry = false; // carry out var
		var word1Data = word1.getData(); // Variables to avoid repeat calling
		var word2Data = word2.getData();
		for (int i = 0; i < 32; i++) {
			var temp = word1Data[i].xor(word2Data[i].xor(new Bit(carry)));
			result.setBit(i, temp);
			carry = ((word1Data[i].and(word2Data[i])) // Get the value of carry (X AND Y OR ((X XOR Y) AND Carry)
					.or((word1Data[i].xor(word2Data[i]))
					.and(new Bit(carry))).getData());
		}
		return result;
	}
	
	public Word add4(Word word1, Word word2, Word word3, Word word4) throws Exception { // Adds 4 words together at the same time, Note: Made public for testing
		Word result = new Word();
		boolean carry1 = false;
		boolean carry2 = false; // There is max 3 carries
		boolean carry3 = false;
		int numOnes = 0; // Number of ones that need to be carried
		
		for (int i = 0; i < 32; i++) {
			if (word1.getBit(i).getData()) numOnes++;
			if (word2.getBit(i).getData()) numOnes++;
			if (word3.getBit(i).getData()) numOnes++;
			if (word4.getBit(i).getData()) numOnes++;
			if (carry1) numOnes++;
			if (carry2) numOnes++;
			if (carry3) numOnes++;

			result.setBit(i, new Bit(carry3).xor(new Bit(carry2).xor(new Bit(carry1).xor(word1.getBit(i).xor(word2.getBit(i).xor(word3.getBit(i).xor(word4.getBit(i))))))));
			
			carry1 = false;
			carry2 = false;
			carry3 = false;
						
			switch (numOnes) { // 3 possible states for carries, either 1, 2, or 3 carries.
				case 2: 
					carry1 = true;
					break;
				case 3:
					carry1 = true;
					break;
				case 4:
					carry1 = true;
					carry2 = true;
					break;
				case 5:
					carry1 = true;
					carry2 = true;
					break;
				case 6: {
					carry1 = true;
					carry2 = true;
					carry3 = true;
					break;
				}
				case 7: {
					carry1 = true;
					carry2 = true;
					carry3 = true;
					break;
				}
			}
			numOnes = 0; // reset
		}
		return result;
	}
	
	public Word getResult() {
		return result;
	}
	
	public void setOp1(Word word) throws Exception {
		op1 = word;
	}
	
	public void setOp2(Word word) throws Exception {
		op2 = word;
	}
}


