

public class Word {
	
	private Bit[] data;
	
	Word() {
		data = new Bit[32];
		for (int i = 0; i < 32; i++) {
			data[i] = new Bit();
		}
	}
	
	public Bit[] getData() {
		return this.data;
	}
	
    Bit getBit(int i) throws Exception { // Get a new Bit that has the same value as bit i 
    	if (i >= 0 && i < 32) { // Checks bounds
        	return new Bit(data[i].getData()); 
    	} else throw new Exception("Error: getBit() - Index Out Of Bounds");
    }

    void setBit(int i, Bit value) throws Exception { // set bit i's value 
    	if (i >= 0 && i < 32) { // Checks bounds
        	data[i] = value; // Note: The array is right to left ie) 8 = 0000 0000 1000
    	} else throw new Exception("Error: setBit() - Index Out Of Bounds");
    }

    Word and(Word other) { // AND two words, returning a new Word
    	var otherData = other.getData(); // Variables to hold the different objects to avoid calling 32 times
    	var result = new Word();
    	var resultData = result.getData();
    	for (int i = 0; i < 32; i++) {
    		resultData[i] = data[i].and(otherData[i]);
    	}
		return result; 
    }

    Word or(Word other) { // OR two words, returning a new Word 
    	var otherData = other.getData(); // Variables to hold the different objects to avoid calling 32 times
    	var result = new Word();
    	var resultData = result.getData();
    	for (int i = 0; i < 32; i++) {
    		resultData[i] = data[i].or(otherData[i]);
    	}
		return result;
	}

    Word xor(Word other) { // XOR two words, returning a new Word
    	// xor: t if only one is true, else f
    	var otherData = other.getData(); // Variables to hold the different objects to avoid calling 32 times
    	var result = new Word();
    	var resultData = result.getData();
    	for (int i = 0; i < 32; i++) {
    		resultData[i] = data[i].xor(otherData[i]);
    	}
		return result;    	 
    }

    Word not() { // negate this word, returning a new Word 
    	var result = new Word(); // Variables to hold the different objects to avoid calling 32 times
    	var resultData = result.getData();
    	for (int i = 0; i < 32; i++) {
    		resultData[i] = data[i].not();
    	}
		return result;
    }

    Word rightShift(int amount) throws Exception { // right shift this word by amount bits, creating a new Word
    	if (amount < 0) throw new Exception("Right Shift Error: Cannot shift by " + amount);
        var result = new Word();
        var resultData = result.getData();
        for (int i = 31 - amount; i >= 0; i--) { // Note: Starts at amount b/c new Bits are false at start. 
            if (data[i + amount].getData()) {
                resultData[i].set();
            }
        }
        return result;
    }

    Word leftShift(int amount) throws Exception { // left shift this word by amount bits, creating a new Word 
    	if (amount < 0) throw new Exception("Left Shift Error: Cannot shift by " + amount);
        var result = new Word();
        var resultData = result.getData();
        for (int i = amount; i < 32; i++) { // Note: Starts at amount b/c new Bits are false at start. 
            if (data[i - amount].getData()) {
                resultData[i].set();
            }
        }
        return result;
    }

    @Override
    public String toString() { // returns a comma separated string of t’s and f’s 
    	StringBuilder result = new StringBuilder();
    	for (int i = 31; i >= 0; i--) {
    		result.append(data[i].toString() + ",");
    	}
		return result.toString();
    }
    
    public String toStringBinary() { // returns a string of 0's and 1's
    	StringBuilder result = new StringBuilder();
    	for (int i = 31; i >= 0; i--) {
    		result.append(data[i].toStringBinary());
    	}
		return result.toString();
    }

    // Note: getUnsigned() and getSigned() use base-2 conversions
    long getUnsigned() { // returns the value of this word as a long (pos only)
    	long result = 0;	
    	for (int i = 0; i < 32; i++) {
    		if (data[i].getData()) {
        		result += Math.pow(2, i);
    		}
    	}
		return result;
    }

    int getSigned() throws Exception { // returns the value of this word as an int (pos and neg)    	
    	if (data[31].getData()) { 
    		// Dealing with negative number since most significant digit is 1
    		// Therefore need to invert and add 1.
    		var result = new Word();
        	result.copy(this);
        	result.invertHelper();
        	return (int) result.getUnsigned() * -1;
    	} else return (int) getUnsigned();
	}

    void copy(Word other) throws Exception { // copies the values of the bits from another Word into this one 
    	for (int i = 0; i < 32; i++) {
    		data[i] = new Bit(other.getBit(i).getData());
    	}
    }
    
    void clear() {
    	for (Bit b : data) {
    		b.clear();
    	}
    }

    void set(int value) throws Exception {
    	clear(); // To ensure an empty word
    	boolean flag = false; // t if negative
    	if (value < 0) {
    		flag = true;
    		value = value * -1;
    	}
       	int target = 0;
       	for (int i = 31; i >= 0; i--) {
        	if (Math.pow(2, i) > value - target) continue; // Find the largest power of 2 that can fit
        	else {
       			data[i].set();
       			target += Math.pow(2, i);
       			if (target == value) break;
       		}
       	}
       	if (flag) invertHelper();
    }
    
    void increment() { // Increments this by one
    	boolean carry = true; // The 1 to be carried
    	int i = 0;
    	while (carry && i < 32) {
			var temp = data[i].xor(new Bit(carry));
			data[i] = temp;
			if (temp.getData()) break; // Only true if the carried variable gets swallowed, else it hit a 1 and needs to be carried more
			i++;
    	}
    }
    
    void decrement() { // Decrement this by one
       	boolean carry = true;
       	int i = 0;
       	while (carry && i < 32) {
   			var temp = data[i].xor(new Bit(carry));
  			data[i] = temp;
   			if (!temp.getData()) break;
    		i++;
        }
    }
    
    void invertHelper() throws Exception { // Helper method; inverts and adds 1 to the data
    	for (Bit bit : data) bit.toggle();
    	increment();
    }
    
    void setData(Word word) throws Exception {
    	this.copy(word);
    }
}
