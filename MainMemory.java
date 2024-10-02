

public class MainMemory {
	
	public static Word[] data = new Word[1024]; // Must check that the memory is constructed before each read and write
	
	// Returns a new Word Object at data location
	public static Word read(Word address) throws Exception {
		int addr = (int) address.getUnsigned();
		if (addr >= 1024) throw new Exception("MainMemory - Cannot read from address " + addr);
		if (data[addr] == null) {
			data[addr] = new Word();
			return new Word(); // Auto set to 0.
		}
		var result = new Word();
		result.copy(data[addr]);
		return result;
	}
	
	// Does not replace the Word object in memory to avoid memory issues
	public static void write(Word address, Word value) throws Exception {
		int addr = (int) address.getUnsigned();
		if (addr >= 1024) throw new Exception("MainMemory - Cannot write from address " + addr);
		if (data[addr] == null) data[addr] = new Word();
		data[addr].copy(value);
	}
	
	public static void load(String[] data) throws Exception { // Processes an array of Strings into DRAM array
		// Each String object are 32 ones and zeroes
		if (data.length > 1024) throw new Exception("MainMemory - Too much data to load!");
		for (int i = 0; i < data.length; i++) {
			MainMemory.data[i] = new Word();
			for (int z = 31; z >= 0; z--) { // Initialize word bits to T or F based off of String
				if (data[i].charAt(31 - z) == '1') MainMemory.data[i].setBit(z, new Bit(true));
				else MainMemory.data[i].setBit(z, new Bit());
			}
		}
	}
	
	public static void clearData() { // Note: Made for JUnitTesting
		data = new Word[1024];
	}
}
