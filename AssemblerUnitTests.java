import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class AssemblerUnitTests {
	
	@Test
	public void opCopy() throws Exception {
		String test = "Copy R1 5"
				+ "\nHalt";
				
		var assembler = new Assembler(test);
		String[] result = assembler.getOutput();
		
		// Note: This test was taken from {@code ProcessorMathUnitTests}
		assertEquals("00000000000000010100000000100001", result[0]); // Copy R1 5
		assertEquals("00000000000000000000000000000000", result[1]); // Halt
	}
	
	@Test
	public void MathAdd() throws Exception {
		String test = "Copy R1 5\n"
				+ "Math add r1 r1\n"
				+ "Halt";
				
		var assembler = new Assembler(test);
		String[] result = assembler.getOutput();
		
		// Note: This test was taken from {@code ProcessorMathUnitTests}
		assertEquals("00000000000000010100000000100001", result[0]); // Copy R1 5 | copy 5 into r1
		assertEquals("00000000000000000111100000100011", result[1]); // Math add R1 R1 | add r1 to r1
		assertEquals("00000000000000000000000000000000", result[2]); // Halt
	}
	
	@Test
	public void MathAdd3Registers() throws Exception {
		String test = 
				  "Copy R1 6\n"
				+ "Copy R2 6\n"
				+ "Math add r1 r2 r3\n"
				+ "Halt";
				
		var assembler = new Assembler(test);
		String[] result = assembler.getOutput();
		
		// Note: This test was taken from {@code ProcessorMathUnitTests}
		assertEquals("00000000000000011000000000100001", result[0]); // Copy R1 6 | copy 6 into r1
		assertEquals("00000000000000011000000001000001", result[1]); // Copy R2 6 | copy 6 into r2
		assertEquals("00000000000010001011100001100010", result[2]); // Math add R1 R2 R3 | add r1 to r2 into r3
		assertEquals("00000000000000000000000000000000", result[3]); // Halt
	}
	
	@Test
	public void MathAdd3RegistersNegative() throws Exception {
		String test = "Copy R1 6\n"
				+ "Copy R2 -6\n" // Expect Two's complement negative | Ones should extend to the 31st bit
				+ "Math add r1 r2 r3\n"
				+ "Halt";
				
		var assembler = new Assembler(test);
		String[] result = assembler.getOutput();
		
		// Note: This test was taken from {@code ProcessorMathUnitTests}
		assertEquals("00000000000000011000000000100001", result[0]); // Copy R1 6 | copy 6 into r1
		assertEquals("11111111111111101000000001000001", result[1]); // Copy R2 6 | copy -6 into r2
		assertEquals("00000000000010001011100001100010", result[2]); // Math add R1 R2 R3 | add r1 to r2 into r3
		assertEquals("00000000000000000000000000000000", result[3]); // Halt
	}
	
	@Test
	public void MathAdd2R0() throws Exception {
		String test = "Copy R1 6\n"
				+ "Math add r1 r0\n"
				+ "Halt";
				
		var assembler = new Assembler(test);
		String[] result = assembler.getOutput();
		
		// Note: This test was taken from {@code ProcessorMathUnitTests}
		assertEquals("00000000000000011000000000100001", result[0]); // Copy R1 6 | copy 6 into r1
		assertEquals("00000000000000000111100000000011", result[1]); // Math add R1 R0
		assertEquals("00000000000000000000000000000000", result[2]); // Halt
	}
	
	@Test
	public void MathMultiply3Registers() throws Exception {
		String test = "Copy R1 6\n"
				+ "Copy R2 6\n"
				+ "Math multiply r1 r2 r3\n"
				+ "Halt";
				
		var assembler = new Assembler(test);
		String[] result = assembler.getOutput();
		
		// Note: This test was taken from {@code ProcessorMathUnitTests}
		assertEquals("00000000000000011000000000100001", result[0]); // Copy R1 6 | copy 6 into r1
		assertEquals("00000000000000011000000001000001", result[1]); // Copy R2 6 | copy 6 into r2
		assertEquals("00000000000010001001110001100010", result[2]); // Math multiply R1 R2 R3 | add r1 to r2 into r3
		assertEquals("00000000000000000000000000000000", result[3]); // Halt
	}
	
	@Test
	public void testBooleanOps3R() throws Exception {
		String test = "Copy R1 6\n"
				+ "Copy R2 1\n"
				+ "Math or r1 r2 r3\n"
				+ "Halt";
				
		var assembler = new Assembler(test);
		String[] result = assembler.getOutput();
		
		assertEquals("00000000000000011000000000100001", result[0]); // Copy R1 6 
		assertEquals("00000000000000000100000001000001", result[1]); // Copy R2 1
		assertEquals("00000000000010001010010001100010", result[2]); // Math OR R1 R2 R3
		assertEquals("00000000000000000000000000000000", result[3]); // Halt
	}
	
	@Test
	public void testBooleanOps2R() throws Exception {
		String test = "Copy R1 6\n"
				+ "Copy R2 1\n"
				+ "Math not r1 r2 r3\n"
				+ "Halt";
				
		var assembler = new Assembler(test);
		String[] result = assembler.getOutput();
		
		assertEquals("00000000000000011000000000100001", result[0]); // Copy R1 6 
		assertEquals("00000000000000000100000001000001", result[1]); // Copy R2 1
		assertEquals("00000000000010001010110001100010", result[2]); // Math not R1 R2 R3
		assertEquals("00000000000000000000000000000000", result[3]); // Halt
	}
	
	@Test
	public void MathAdd2R01() throws Exception {
		String test = "Copy R1 6\n"
				+ "Math add r1 r0\n"
				+ "Halt";
				
		var assembler = new Assembler(test);
		String[] result = assembler.getOutput();
		
		// Note: This test was taken from {@code ProcessorMathUnitTests}
		assertEquals("00000000000000011000000000100001", result[0]); // Copy R1 6 | copy 6 into r1
		assertEquals("00000000000000000111100000000011", result[1]); // Math add R1 R0
		assertEquals("00000000000000000000000000000000", result[2]); // Halt
	}
	
	@Test
	public void testBranchOp() throws Exception {
		// Taken from Assignment 5 JUNITs
		String test = "Copy R1 5\n"
				+ "Jump DestOnly 3\n" // Need to specify since DestOnly and NoR are the exact same in required specification (requires only immediate)
				+ "Copy R2 6\n"
				+ "Copy R3 7\n"
				+ "Copy R4 8\n"
				+ "Copy R5 9\n"
				+ "Copy R6 10\n"
				+ "Halt";
				
		var assembler = new Assembler(test);
		String[] result = assembler.getOutput();
		
		// Note: This test was taken from {@code ProcessorMathUnitTests}
		assertEquals("00000000000000010100000000100001", result[0]); // Copy R1 5
		assertEquals("00000000000000001100000000000101", result[1]); // Jump to 5 (PC + imm, 2 + 3 in this case)
		assertEquals("00000000000000011000000001000001", result[2]); // Copy R2 6
		assertEquals("00000000000000011100000001100001", result[3]); // Copy R3 7
		assertEquals("00000000000000100000000010000001", result[4]); // Copy R4 8
		assertEquals("00000000000000100100000010100001", result[5]); // Copy R5 9
		assertEquals("00000000000000101000000011000001", result[6]); // Copy R6 10
		assertEquals("00000000000000000000000000000000", result[7]); // Halt
	}
	
	@Test
	public void testBranchOpNoR() throws Exception {
		// Taken from Assignment 5 JUNITs
		String test = "Copy R1 5\n"
				+ "Jump NoR 3\n" // Need to specify since DestOnly and NoR are the exact same in required specification (requires only immediate)
				+ "Copy R2 6\n"
				+ "Copy R3 7\n"
				+ "Copy R4 8\n"
				+ "Copy R5 9\n"
				+ "Copy R6 10\n"
				+ "Halt";
				
		var assembler = new Assembler(test);
		String[] result = assembler.getOutput();
		
		assertEquals("00000000000000010100000000100001", result[0]); // Copy R1 5
		assertEquals("00000000000000000000000001100100", result[1]); // Jump to 3 (jump to immediate, 3 in this case)
		assertEquals("00000000000000011000000001000001", result[2]); // Copy R2 6
		assertEquals("00000000000000011100000001100001", result[3]); // Copy R3 7
		assertEquals("00000000000000100000000010000001", result[4]); // Copy R4 8
		assertEquals("00000000000000100100000010100001", result[5]); // Copy R5 9
		assertEquals("00000000000000101000000011000001", result[6]); // Copy R6 10
		assertEquals("00000000000000000000000000000000", result[7]); // Halt
	}
	
	@Test
	public void testBranchOp2REquals() throws Exception {
		// Taken from Assignment 5 JUNITs
		String test = "Copy R1 1\n"
				+ "Copy R2 2\n"
				+ "branch equal r1 r2 2\n"
				+ "Copy R3 3\n"
				+ "Copy R4 4\n"
				+ "Halt";
				
		var assembler = new Assembler(test);
		String[] result = assembler.getOutput();
		
		assertEquals("00000000000000000100000000100001", result[0]); // Copy R1 1
		assertEquals("00000000000000001000000001000001", result[1]); // Copy R2 2
		assertEquals("00000000000100000100000001000111", result[2]); // Branch equals r1 r2 2
		assertEquals("00000000000000001100000001100001", result[3]); // Copy R3 3
		assertEquals("00000000000000010000000010000001", result[4]); // Copy R4 4
		assertEquals("00000000000000000000000000000000", result[5]); // Halt
	}
	
	@Test
	public void testLoadStore() throws Exception {
		// Taken from Assignment 5 JUNITs
		String test = "Copy R1 20\n"
				+ "store r1 2\n" // Store 2 into mem[20]
				+ "load r2 2\n" // load 2 and put into r2
				+ "Halt";
				
		var assembler = new Assembler(test);
		String[] result = assembler.getOutput();
		
		assertEquals("00000000000001010000000000100001", result[0]); // Copy
		assertEquals("00000000000000001000000000110101", result[1]); // Store
		assertEquals("00000000000000001000000001010001", result[2]); // load
		assertEquals("00000000000000000000000000000000", result[3]); // Halt
	}
}
