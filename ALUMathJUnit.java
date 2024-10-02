
import static org.junit.Assert.*;
import org.junit.Test;

public class ALUMathJUnit {
	
	@Test
	public void ALUAdd2() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();	
		word1.set(7);
		word2.set(2);
		var temp = ALU.add2(word1, word2);
		assertEquals(9, temp.getUnsigned());
	}
	
	@Test
	public void ALUAdd2_3000() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();	
		word1.set(1000);
		word2.set(2000);
		var temp = ALU.add2(word1, word2);
		assertEquals(3000, temp.getUnsigned());
	}
	
	@Test
	public void ALUAdd4() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();
		var word3 = new Word();
		var word4 = new Word();
		
		word1.set(2);
		word2.set(2);
		word3.set(2);
		word4.set(2);
		var temp = ALU.add4(word1, word2, word3, word4);
		assertEquals(8, temp.getUnsigned());
	}
	
	@Test
	public void ALUAdd4_0() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();
		var word3 = new Word();
		var word4 = new Word();
		
		word1.set(0);
		word2.set(0);
		word3.set(0);
		word4.set(0);
		var temp = ALU.add4(word1, word2, word3, word4);
		assertEquals(0, temp.getUnsigned());
	}
	
	@Test
	public void ALUAdd4_One0() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();
		var word3 = new Word();
		var word4 = new Word();
		
		word1.set(0);
		word2.set(40);
		word3.set(50);
		word4.set(60);
		var temp = ALU.add4(word1, word2, word3, word4);
		assertEquals(150, temp.getUnsigned());
	}
	
	@Test
	public void ALUMultiply_64() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();	
		word1.set(8);
		word2.set(8);
		ALU.multiply(word1, word2);
		assertEquals(64, ALU.result.getUnsigned());
	}
	
	@Test
	public void ALUMultiply_100() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();	
		word1.set(100);
		word2.set(100);
		ALU.multiply(word1, word2);
		assertEquals(10000, ALU.result.getUnsigned());
	}
	
	@Test
	public void ALUMultiply_Zero() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();	
		word1.set(0);
		word2.set(100);
		ALU.multiply(word1, word2);
		assertEquals(0, ALU.result.getUnsigned());
	}
	
	@Test
	public void ALUMultiply_75625() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();	
		word1.set(275);
		word2.set(275);
		ALU.multiply(word1, word2);
		assertEquals(75625, ALU.result.getUnsigned());
	}
	
	@Test
	public void ALUMultiply_Neg() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();	
		word1.set(-8);
		word2.set(-8);
		ALU.multiply(word1, word2);
		assertEquals(64, ALU.result.getSigned());
	}
	
	@Test
	public void ALUAdd4_160() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();
		var word3 = new Word();
		var word4 = new Word();
		
		word1.set(40);
		word2.set(40);
		word3.set(40);
		word4.set(40);
		var temp = ALU.add4(word1, word2, word3, word4);
		assertEquals(160, temp.getUnsigned());
	}
	
	@Test
	public void ALUAdd4_10000() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();
		var word3 = new Word();
		var word4 = new Word();
		
		word1.set(1000);
		word2.set(2000);
		word3.set(3000);
		word4.set(4000);
		var temp = ALU.add4(word1, word2, word3, word4);
		assertEquals(10000, temp.getUnsigned());
	}
	
	@Test
	public void ALUAdd4Neg() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();
		var word3 = new Word();
		var word4 = new Word();
		
		word1.set(-3);
		word2.set(-3);
		word3.set(-3);
		word4.set(-3);
		var temp = ALU.add4(word1, word2, word3, word4);
		assertEquals(-12, temp.getSigned());
	}
	
	@Test
	public void ALUAdd2PosToNeg() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();
		var word3 = new Word();
		var word4 = new Word();
		
		word1.set(-3);
		word2.set(10);
		var temp = ALU.add2(word1, word2);
		assertEquals(7, temp.getSigned());
		
		word3.set(10);
		word4.set(-3);
		var temp2 = ALU.add2(word3, word4);
		assertEquals(7, temp2.getSigned());
	}
	
	@Test
	public void ALUMultiplyNeg() throws Exception {
		var ALU = new ALU();
		var word1 = new Word();
		var word2 = new Word();
		word1.set(-5);
		word2.set(-5);
		ALU.multiply(word1, word2);
		
		assertEquals(25, ALU.getResult().getSigned());
	}
	
	
}