

public class Bit {
	
	private Boolean data; // 1 - true, 0 - false
	
	Bit(boolean value) {
		data = value;
	}
	
	Bit() {
		data = false;
	}
	
	void set(Boolean value) { // sets the value of the bit
		 data = value;
	}

	void toggle() { // changes the value from true to false or false to true 
		if (data) data = false; // Note: Cannot use logical operators (!) 
		else data = true;
	}

	void set() { // sets the bit to true 
		data = true;
	}

	void clear() { // sets the bit to false
		data = false;
	}

	Boolean getData() { // returns the current value 
		return data;
	}

	Bit and(Bit other) { // performs and on two bits and returns a new bit set to the result 
		if (this.data) { // Note: Cannot use AND operand (&&)
			if (other.getData()) return new Bit(true);
		} 
		return new Bit(false);
	}

	Bit or(Bit other) { // performs or on two bits and returns a new bit set to the result
		if (this.data) return new Bit(true); // Note: Cannot use OR operand (||)
		else if (other.getData()) return new Bit(true);
		else return new Bit(false);
	}

	Bit xor(Bit other) { // performs xor on two bits and returns a new bit set to the result 
		if (this.data) { // Note: Cannot use AND operand (&&)
			if (other.getData() == false) return new Bit(true);
		} else if (other.getData()) return new Bit(true);
		return new Bit(false); // t if only one is t, else false.
	}

	Bit not() { // performs not on the existing bit, returning the result as a new bit
		if (data) return new Bit(false); // Note: Cannot use NOT operand (!)
		else return new Bit(true);
	}

	public String toString() { // returns data as “t” or “f” 
		if (data) return "t";
		else return "f";
	}
	
	public String toStringBinary() { // returns data as 1 (True) or 0 (False)
		if (data) return "1";
		else return "0";
	}

}
