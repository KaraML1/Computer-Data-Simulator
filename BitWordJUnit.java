
import static org.junit.Assert.*;
import org.junit.Test;

public class BitWordJUnit {
	
	//Bit Class JUNIT tests
	@Test
	public void bitToggle() {
		var posBit = new Bit(true);
		var negBit = new Bit(false);
		posBit.toggle();
		negBit.toggle();
		assertEquals(false, posBit.getData());
		assertEquals(true, negBit.getData());
	}
	
	@Test
	public void bitAND() {
		var posBit = new Bit(true);
		var posBitTwo = new Bit(true);
		var negBit = new Bit(false);
		var negBitTwo = new Bit(false);
		assertEquals(true, posBit.and(posBitTwo).getData());
		assertEquals(false, posBit.and(negBitTwo).getData());
		assertEquals(false, negBit.and(negBitTwo).getData());
	}
	
	@Test
	public void bitOR() {
		var posBit = new Bit(true);
		var posBitTwo = new Bit(true);
		var negBit = new Bit(false);
		var negBitTwo = new Bit(false);
		assertEquals(true, posBit.or(posBitTwo).getData());
		assertEquals(true, posBit.or(negBitTwo).getData());
		assertEquals(false, negBit.or(negBitTwo).getData());
	}
	
	@Test
	public void bitXOR() { // t if only one is true, else false
		var posBit = new Bit(true);
		var posBitTwo = new Bit(true);
		var negBit = new Bit(false);
		var negBitTwo = new Bit(false);
		assertEquals(false, posBit.xor(posBitTwo).getData());
		assertEquals(true, posBit.xor(negBitTwo).getData());
		assertEquals(false, negBit.xor(negBitTwo).getData());
	}
	
	@Test
	public void bitNOT() {
		var posBit = new Bit(true);
		var negBit = new Bit(false);
		assertEquals(false, posBit.not().getData());
		assertEquals(true, negBit.not().getData());
	}
	
	// Word Class JUNIT Tests
	// Note: getUnsigned(), getSigned(), and set() are tested thru out
	
	@Test
	public void wordRightShifting() throws Exception {
		var temp = new Word();
		temp.set(8); // 0000 1000 -> 0000 0010 (2)
		var newWord = temp.rightShift(2); // Shift it right 2 spaces
		assertEquals(2, newWord.getUnsigned());
	}
	
	@Test
	public void wordLeftShifting() throws Exception {
		var temp = new Word();
		temp.set(8); // 0000 1000 -> 0010 0000 (32)
		var newWord = temp.leftShift(2); // Shift it right 2 spaces
		assertEquals(32, newWord.getUnsigned());
	}
	
	@Test
	public void wordSimpleSet() throws Exception {
		var temp = new Word();
		temp.set(5);
		assertEquals(5, temp.getUnsigned());
	}
	
	@Test
	public void wordComplexSet() throws Exception {
		var temp = new Word();
		temp.set(184); // 184 is 10111000 in Binary
		assertEquals(184, temp.getUnsigned());
		
	}
	
	
	@Test
	public void wordSimpleSetNegative() throws Exception {
		var temp = new Word();
		temp.set(-12); // -12 is 1111 1111 0100 in Binary
		assertEquals(-12, temp.getSigned());
	}
	
	@Test
	public void wordComplexSetNegative() throws Exception {
		var temp = new Word();
		temp.set(-204);
		assertEquals(-204, temp.getSigned());
	}
	
	@Test
	public void wordCopy() throws Exception {
		var wordOne = new Word();
		var wordTwo = new Word();
		wordOne.set(32);
		wordTwo.copy(wordOne);
		assertEquals(32, wordTwo.getUnsigned());
	}
	
	@Test // Tests both setBit() and getBit()
	public void setGetBit() throws Exception {
		var wordOne = new Word();
		wordOne.setBit(3, new Bit(true));
		assertEquals(8, wordOne.getUnsigned());
		assertEquals(true, wordOne.getBit(3).getData());
	}
	
	@Test
	public void wordAND() throws Exception {
		var wordOne = new Word();
		var wordTwo = new Word();
		wordOne.set(9); // 0000 1001
		wordTwo.set(5); // 0000 0101 -> expect completely false, except for one's place digit
		var temp = wordOne.and(wordTwo);
		assertEquals(1, temp.getUnsigned());
	}
	
	@Test
	public void wordOR() throws Exception {
		var wordOne = new Word();
		var wordTwo = new Word();
		wordOne.set(9); // 0000 1001
		wordTwo.set(5); // 0000 0101 -> 0000 1101
		var temp = wordOne.or(wordTwo);
		assertEquals(13, temp.getUnsigned());
	}
	
	@Test
	public void wordXOR() throws Exception { // t if only one is true, else f
		var wordOne = new Word();
		var wordTwo = new Word();
		wordOne.set(9); // 0000 1001
		wordTwo.set(5); // 0000 0101 -> 0000 1100
		var temp = wordOne.xor(wordTwo);
		assertEquals(12, temp.getUnsigned());
	}
	
	@Test
	public void wordNOT() throws Exception { // t if only one is true, else f
		var wordOne = new Word();
		wordOne.set(9); // 0000 1001 -> 1111 0110
		var temp = wordOne.not();
		assertEquals("t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,f,t,t,f,", temp.toString());
	}
	
	@Test // Tests Increment Function
	public void incrementFunc() throws Exception {
		var wordOne = new Word();
		wordOne.set(8);
		assertEquals(8, wordOne.getUnsigned());
		wordOne.increment();
		assertEquals(9, wordOne.getUnsigned());
	}
	
	@Test // Tests Increment Function
	public void incrementFuncZero() throws Exception {
		var wordOne = new Word();
		assertEquals(0, wordOne.getUnsigned());
		wordOne.increment();
		assertEquals(1, wordOne.getUnsigned());
	}
	
	@Test // Tests Increment Function
	public void incrementFuncLargeNum() throws Exception {
		var wordOne = new Word();
		wordOne.set(400201);
		assertEquals(400201, wordOne.getUnsigned());
		wordOne.increment();
		assertEquals(400202, wordOne.getUnsigned());
	}
	
	@Test // Tests Decrement Function
	public void DecrementFunc() throws Exception {
		var wordOne = new Word();
		wordOne.set(8);
		assertEquals(8, wordOne.getUnsigned());
		wordOne.decrement();
		assertEquals(7, wordOne.getUnsigned());
	}
	
	@Test // Tests Decrement Function
	public void decrementFuncZero() throws Exception {
		var wordOne = new Word();
		assertEquals(0, wordOne.getUnsigned());
		wordOne.decrement();
		assertEquals(-1, wordOne.getSigned());
	}
	
	@Test // Tests Decrement Function
	public void decrementFuncNeg() throws Exception {
		var wordOne = new Word();
		wordOne.set(-10);
		assertEquals(-10, wordOne.getSigned());
		wordOne.decrement();
		assertEquals(-11, wordOne.getSigned());
	}
	
	@Test // Tests Decrement Function
	public void decrementFuncLargeNum() throws Exception {
		var wordOne = new Word();
		wordOne.set(400201);
		assertEquals(400201, wordOne.getUnsigned());
		wordOne.decrement();
		assertEquals(400200, wordOne.getUnsigned());
	}
}
