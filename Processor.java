
public class Processor {

	private Word[] registers = new Word[32]; // R0 is hard-coded to 0, writing to it leaves it unchanged.
	private Word PC = new Word(); // Program Counter (what instruction we're working on)
	private Word SP = new Word(); // Stack Pointer (starts at 1024)
	private Word currentInstruction = new Word();
	private Bit halted = new Bit(); // op that stops runtime
	private ALU processorALU = new ALU();
	private InstructionCache cache = new InstructionCache();
	private static int clockCycles = 0; // # of current Clock Cycles

	
	// Temporary Storage between decode() and execute()
	private Word r1; // Source Registers
	private Word r2; 
	private Word dest; // Destination Register
	private Word immediate; // Immediate Value (Varies from 8 to 27 bits, based off operation)
	private int op = 0; // num to communicate what the Register Operation was (0, 1, 2, 3 Registers)
	private int storeType = 0; // num to communicate where to store during store() (1 = dest, 2 = PC, 3 = Main Memory)
	private Word executionResult; // This is a pointer that contains the result of the execution() phase, allowing store() to easily store the result
	
	Processor() throws Exception {
		SP.set(1023); // Initialize Stack Size
		for (int i = 0; i < 32; i++) registers[i] = new Word();
	}
	
	public void run() throws Exception {
		while (!halted.getData()) { // Loop until halted
			fetch();
			System.out.println("Working on " + currentInstruction.toStringBinary());
			decode();
			execute();
			store();
		}
	}
	
	/* Determine the Operation and gets data from registers, sending it to ALU
	 * 	10 - 3 Register // Get the Rs1, Rs2 and dest values from the Registers and the immediate value from the instruction
	 * 	11 - 2 Register // Get the Rs and dest values from the Registers and the immediate value from the instruction
	 * 	01 - Destination Only // Get the dest value from the Registers and the immediate value from the instruction
	 * 	00 - No Register // Get the dest value from the Registers and the immediate value from the instruction
	 */
	private void decode() throws Exception {	
		if (currentInstruction.getBit(0).getData()) {
			if (currentInstruction.getBit(1).getData()) twoRegisterOp();
			else destinationOp();
		} else {
			if (currentInstruction.getBit(1).getData()) threeRegisterOp();			
			else noRegisterOp();
		}
	}
	
	/* Checks the 5 Bit Operation
	 * 000 00 HALT
	 * 
	 * 000 Math
	 * 001 Branch
	 * 010 Call
	 * 011 Push
	 * 100 Load
	 * 101 Store
	 * 110 Pop
	 */
	private void execute() throws Exception { // Compares by bit
		if (currentInstruction.getBit(2).getData()) {
			if (currentInstruction.getBit(3).getData()) pushOperation();
			else {
				if (currentInstruction.getBit(4).getData()) storeOperation();
				else branchOperation();
			}
		} else {
			if (currentInstruction.getBit(3).getData()) {
				if (currentInstruction.getBit(4).getData()) peekPopOperation(); // Can include Peek
				else callOperation();
			} else {
				if (currentInstruction.getBit(4).getData()) loadOperation();
				else {
					if (!currentInstruction.getBit(0).getData() && !currentInstruction.getBit(1).getData()) setHalt(true);
					else mathOperation();
				}
			}
		}
	}
	
	private void threeRegisterOp() throws Exception {
		var data = new Word();
		for (int i = 5; i < 10; i++) data.setBit(i, new Bit(true)); // Masking to receive just the location we need
		data = data.and(currentInstruction).rightShift(5); // Receive just the data we need, and shift it to the front
		this.dest = findRegister(data); // Find what register pointer is needed, set Register Destination
		
		for (int i = 14; i < 19; i++) data.setBit(i, new Bit(true));
		data = data.and(currentInstruction).rightShift(14);
		this.r2 = findRegister(data); // Set Register Source One
		
		for (int i = 19; i < 24; i++) data.setBit(i, new Bit(true));
		data = data.and(currentInstruction).rightShift(19);
		this.r1 = findRegister(data); // Set Register Source Two
		
		for (int i = 24; i < 32; i++) data.setBit(i, new Bit(true));
		this.immediate = data.and(currentInstruction).rightShift(24);
		this.op = 3; // Set flag for Execute() 
		if (currentInstruction.getBit(31).getData()) { // Dealing with negative number, must continue the most significant bit
			for (int i = 31; i > 24; i--) immediate.setBit(i, new Bit(true));
		}
	}
	
	private void twoRegisterOp() throws Exception {
		var data = new Word();
		for (int i = 5; i < 10; i++) data.setBit(i, new Bit(true));
		data = data.and(currentInstruction).rightShift(5);
		this.dest = findRegister(data);
		
		for (int i = 14; i < 19; i++) data.setBit(i, new Bit(true));
		data = data.and(currentInstruction).rightShift(14);
		this.r1 = findRegister(data);
		
		for (int i = 19; i < 32; i++) data.setBit(i, new Bit(true)); 
		this.immediate = data.and(currentInstruction).rightShift(19);
		this.op = 2;
		if (currentInstruction.getBit(31).getData()) { // Dealing with negative number, must continue the most significant bit
			for (int i = 31; i > 12; i--) immediate.setBit(i, new Bit(true));
		}
	}
	
	private void destinationOp() throws Exception {
		var data = new Word();
		for (int i = 5; i < 10; i++) data.setBit(i, new Bit(true));
		data = data.and(currentInstruction).rightShift(5);
		this.dest = findRegister(data);
		
		for (int i = 14; i < 32; i++) data.setBit(i, new Bit(true));
		this.immediate = data.and(currentInstruction).rightShift(14);
		this.op = 1;
		if (currentInstruction.getBit(31).getData()) { // Dealing with negative number, must continue the most significant bit
			for (int i = 31; i > 14; i--) immediate.setBit(i, new Bit(true));
		}
	}
	
	private void noRegisterOp() throws Exception {
		var data = new Word();
		for (int i = 5; i < 32; i++) data.setBit(i, new Bit(true));
		this.immediate = data.and(currentInstruction).rightShift(5);
		if (currentInstruction.getBit(31).getData()) { // Dealing with negative number, must continue the most significant bit
			for (int i = 31; i > 26; i--) immediate.setBit(i, new Bit(true));
		}
		this.op = 0;
	}
	
	private void mathOperation() throws Exception {
		switch (op) {
			case 1: { // Destination Register
				// Rest Case : Set dest to the Immediate Value (27 Bits)
				this.executionResult = immediate;
				break;
			}
			case 2: { // Two Register
				var data = new Bit[4];
				for (int i = 10, z = 0; i < 14; i++, z++) data[z] = currentInstruction.getBit(i); // Get the Function Code
				processorALU.setOp1(dest);
				processorALU.setOp2(r1);	
				processorALU.doOperation(data);
				this.executionResult = processorALU.getResult();
				break;
			}
			case 3: { // Three Register
				var data = new Bit[4];
				for (int i = 10, z = 0; i < 14; i++, z++) data[z] = currentInstruction.getBit(i); // Get the Function Code
				processorALU.setOp1(r1);
				processorALU.setOp2(r2);	
				processorALU.doOperation(data);
				this.executionResult = processorALU.getResult();
				break;
			}
		}
		storeType = 1;
	}
	
	private void branchOperation() throws Exception {
		// No R and DestOnly branch always jump (aren't conditional)
		switch (op) {
			case 0: { // No R, [ JUMP: PC <- immediateValue ]
				this.executionResult = immediate;
				break;
			}
			case 1: { // Dest Only, [ JUMP: PC <- PC + immediateValue ]
				processorALU.setOp1(PC);
				processorALU.setOp2(immediate); // Add 1110
				processorALU.doOperation(new Bit[] {new Bit(), new Bit(true), new Bit(true), new Bit(true)});
				this.executionResult = processorALU.getResult();
				break;
			}
			case 2: { // 2R, [ PC <- r1 BOP dest ? PC + immediateValue : PC ]
				processorALU.setOp1(r1);
				processorALU.setOp2(dest);
				var data = new Bit[4];
				for (int i = 10, z = 0; i < 14; i++, z++) data[z] = currentInstruction.getBit(i); // Get the Function Code
				processorALU.doOperation(data);
				if (processorALU.getResult().getBit(0).getData()) {
					processorALU.setOp1(PC);
					processorALU.setOp2(immediate);
					processorALU.doOperation(new Bit[] {new Bit(), new Bit(true), new Bit(true), new Bit(true)});
					this.executionResult = processorALU.getResult();
				} else this.executionResult = PC;
				break;
			}
			case 3: { // 3R, [ PC <- Rs BOP dest ? PC + immediateValue : PC ]
				processorALU.setOp1(r1);
				processorALU.setOp2(r2);
				var data = new Bit[4];
				for (int i = 10, z = 0; i < 14; i++, z++) data[z] = currentInstruction.getBit(i); // Get the Function Code
				processorALU.doOperation(data);
				if (processorALU.getResult().getBit(0).getData()) {
					processorALU.setOp1(PC);
					processorALU.setOp2(immediate);
					processorALU.doOperation(new Bit[] {new Bit(), new Bit(true), new Bit(true), new Bit(true)});
					this.executionResult = processorALU.getResult();
				} else this.executionResult = PC;
				break;
			}
		}
		storeType = 2;
	}
	
	private void callOperation() throws Exception {
		/*
		 * Push the old PC onto stack
		 * Need to implement a decrement() on Word Class
		 * 3R and destOnly don't calculate based off PC, instead based on the dest 
		 */
		
		switch (op) {
			case 0: {
				MainMemory.write(SP, PC); // Push PC
				SP.decrement(); // Decrement the stack
				this.executionResult = immediate;
				break;
			}
			case 1: {
				MainMemory.write(SP, PC); // Push PC
				SP.decrement(); // Decrement the stack
				processorALU.setOp1(dest);
				processorALU.setOp2(immediate); // Add 1110
				processorALU.doOperation(new Bit[] {new Bit(), new Bit(true), new Bit(true), new Bit(true)});
				this.executionResult = processorALU.getResult();
				break;	
			}
			case 2: { // pc <== Rs BOP dest ? push pc; pc + imm : pc
				processorALU.setOp1(r1);
				processorALU.setOp2(dest);
				var data = new Bit[4];
				for (int i = 10, z = 0; i < 14; i++, z++) data[z] = currentInstruction.getBit(i); // Get the Function Code
				processorALU.doOperation(data);
				if (processorALU.getResult().getBit(0).getData()) {
					MainMemory.write(SP, PC); // Push PC
					SP.decrement(); // Decrement the stack
					processorALU.setOp1(PC);
					processorALU.setOp2(immediate);
					processorALU.doOperation(new Bit[] {new Bit(), new Bit(true), new Bit(true), new Bit(true)});
					this.executionResult = processorALU.getResult();
				} else this.executionResult = PC;
				break;	
			}
			case 3: { // pc <== Rs1 BOP Rs2 ? push pc; dest + imm : pc
				processorALU.setOp1(r1);
				processorALU.setOp2(r2);
				var data = new Bit[4];
				for (int i = 10, z = 0; i < 14; i++, z++) data[z] = currentInstruction.getBit(i); // Get the Function Code
				processorALU.doOperation(data);
				if (processorALU.getResult().getBit(0).getData()) {
					MainMemory.write(SP, PC); // Push PC
					SP.decrement(); // Decrement the stack
					processorALU.setOp1(dest);
					processorALU.setOp2(immediate);
					processorALU.doOperation(new Bit[] {new Bit(), new Bit(true), new Bit(true), new Bit(true)});
					this.executionResult = processorALU.getResult();
				} else this.executionResult = PC;
				break;
			}
		}
		storeType = 2;
	}
	
	private void returnOperation() throws Exception {
		//Pop and set the PC

		
		if (SP.getUnsigned() == 1023) {
			storeType = 0;
			return;
		}
		
		SP.increment();
		this.executionResult = MainMemory.read(SP);
		storeType = 2;
	}
	
	private void pushOperation() throws Exception {
		//Push and pop are “just” memory accesses out to our main memory, as are load and store. Note that push can do math! 

		storeType = 3;
		switch (op) {
			case 0: {
				storeType = 0;
				break; // Unused
			}
			case 1: {
				processorALU.setOp1(dest);
				processorALU.setOp2(immediate);
				var data = new Bit[4];
				for (int i = 10, z = 0; i < 14; i++, z++) data[z] = currentInstruction.getBit(i); // Get the Function Code
				processorALU.doOperation(data);
				this.executionResult = processorALU.getResult();
				break;
			}
			case 2: {
				processorALU.setOp1(dest);
				processorALU.setOp2(r1);
				var data = new Bit[4];
				for (int i = 10, z = 0; i < 14; i++, z++) data[z] = currentInstruction.getBit(i); // Get the Function Code
				processorALU.doOperation(data);
				this.executionResult = processorALU.getResult();
				break;			
				}
			case 3: {
				processorALU.setOp1(r1);
				processorALU.setOp2(r2);
				var data = new Bit[4];
				for (int i = 10, z = 0; i < 14; i++, z++) data[z] = currentInstruction.getBit(i); // Get the Function Code
				processorALU.doOperation(data);
				this.executionResult = processorALU.getResult();
				break;
			}
		}
	}
	
	private void loadOperation() throws Exception {
		// Load something from MainMemory
		switch (op) {
			case 0: {
				returnOperation();
				break;
			}
			case 1: {
				processorALU.setOp1(dest);
				processorALU.setOp2(immediate);
				processorALU.doOperation(new Bit[] {new Bit(), new Bit(true), new Bit(true), new Bit(true)});
				this.executionResult = processorALU.getResult();
				this.executionResult = MainMemory.read(executionResult);
				storeType = 1;
				break;
			}
			case 2: {
				processorALU.setOp1(r1);
				processorALU.setOp2(immediate);
				processorALU.doOperation(new Bit[] {new Bit(), new Bit(true), new Bit(true), new Bit(true)});
				this.executionResult = processorALU.getResult();
				this.executionResult = MainMemory.read(executionResult);
				storeType = 1;
				break;			
				}
			case 3: {
				processorALU.setOp1(r1);
				processorALU.setOp2(r2);
				processorALU.doOperation(new Bit[] {new Bit(), new Bit(true), new Bit(true), new Bit(true)});
				this.executionResult = processorALU.getResult();
				this.executionResult = MainMemory.read(executionResult);
				storeType = 1;
				break;	
			}
		}
	}
	
	private void storeOperation() throws Exception {
		// Store something into Main Memory
		storeType = 3;
		switch (op) { // Calculates the Main Memory Address, and then completes in store()
			case 0: { // Unused
				storeType = 0;
				break;
			}
			case 1: {
				this.executionResult = dest;
				storeType = 3;
				break;
			}
			case 2: {
				processorALU.setOp1(dest);
				processorALU.setOp2(immediate);
				processorALU.doOperation(new Bit[] {new Bit(), new Bit(true), new Bit(true), new Bit(true)});
				this.executionResult = processorALU.getResult();
				storeType = 3;
				break;			
				}
			case 3: {
				processorALU.setOp1(dest);
				processorALU.setOp2(r1);
				processorALU.doOperation(new Bit[] {new Bit(), new Bit(true), new Bit(true), new Bit(true)});
				this.executionResult = processorALU.getResult();
				storeType = 3;
				break;	
			}
		}
	}
	
	private void peekPopOperation() throws Exception {
		// Includes both Peek and Pop. No Register case is INTERRUPT (not implemented);
		storeType = 1;
		switch (op) {
			case 0: {
				storeType = 0;
				break; // Unused
			}
			case 1: { // put MainMemory[SP++] into dest
				SP.increment();
				this.executionResult = MainMemory.read(SP);
				break;
			}
			case 2: {
				processorALU.setOp1(r1);
				processorALU.setOp2(immediate);
				processorALU.doOperation(new Bit[] {new Bit(), new Bit(true), new Bit(true), new Bit(true)});
				processorALU.setOp1(SP);
				processorALU.setOp2(processorALU.getResult()); // dest <= MainMemory[SP - (r1 + immediate)]
				processorALU.doOperation(new Bit[] {new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
				this.executionResult = MainMemory.read(processorALU.getResult());
				break;				
				}
			case 3: {
				processorALU.setOp1(r1);
				processorALU.setOp2(r2);
				processorALU.doOperation(new Bit[] {new Bit(), new Bit(true), new Bit(true), new Bit(true)});
				processorALU.setOp1(SP);
				processorALU.setOp2(processorALU.getResult()); // dest <= MainMemory[SP - (r1 + r2)]
				processorALU.doOperation(new Bit[] {new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
				this.executionResult = MainMemory.read(processorALU.getResult());
				break;		
			}
		}
	}
		
	private void store() throws Exception {
		if (this.halted.getData()) return; // End if Halted
		
		switch (storeType) {
			case 1: { // Storing to destination
				dest.copy(executionResult);
				return;
			}
			case 2: { // Storing to PC
				PC.copy(executionResult);
				return;
			}
			case 3: { // Storing to some spot in MainMemory
				if (currentInstruction.getBit(4).getData()) { // Store Operation
					switch (op) {
						case 1: {
							MainMemory.write(executionResult, immediate);
							break;
						}
						case 2: {
							MainMemory.write(executionResult, r1);
							break;
						}
						case 3: {
							MainMemory.write(executionResult, r2);
							break;
						}
					}
				} else { // Push Operation
					MainMemory.write(SP, executionResult);
					SP.decrement();
				}
				return;
			}
			default: {
				halted.set();
				return; // Hit an unused operation; This shouldn't happen!
			}
		}
	}
	
	private void fetch() throws Exception { // Get the next instruction then increments
		currentInstruction.copy(MainMemory.read(PC));
		PC.increment();
	}
	
	private Word findRegister(Word reg) throws Exception {
		int num = 0;
		if (reg != null) {
			for (int i = 0; i < 5; i++) {
				if (reg.getBit(i).getData()) num += Math.pow(2, i);
			}
		}
		if (num == 0) return new Word(); // To ensure R0 is unaffected; R0 is a NO OPERATION value. Hard-coded to 0 and will stay 0. Return temporary Word
		return registers[num];
	}
	
	public Word getPC() { // Note: Made for JUnit Testing
		return this.PC;
	}
	
	public Word getSP() { // Note: Made for JUnit Testing
		return this.SP;
	}
	
	public Word[] getRegisters() { // Note: Made for JUnit Testing
		return this.registers;
	}
	
	public Word getCurrentInstruction() { // Note: Made for JUnit Testing
		return this.currentInstruction;
	}
	
	public void setHalt(boolean value) { // Note: Made for JUnit Testing
		this.halted.set(value);
	}
	
	public static void increaseClockCycle(int num) {
		clockCycles += num;
	}
	
	public static int getClockCycles() {
		return clockCycles;
	}
}


