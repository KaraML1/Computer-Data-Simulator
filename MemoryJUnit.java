
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MemoryJUnit {
	/* Assignment 3 JUNIT tests (MainMemory, Processor, Word)
	 * Note: MainMemory's static data is cleared after every test to avoid data collision
	 */
		
	@Test
	public void testMainMemoryLoad() throws Exception {
		var mem = MainMemory.data; // Note: Only used to see debugger value, otherwise useless in test
		var addr = new Word();
		String[] arr = new String[3];
		
		arr[0] = "00000000000000000000000000001000"; // 8
		arr[1] = "00000000000000000000000000000100"; // 4
		arr[2] = "00000000000000000000000000000001"; // 1
		
		addr.set(0);
		assertEquals(0, addr.getUnsigned());
		MainMemory.load(arr);
		assertEquals(8, MainMemory.read(addr).getUnsigned());
		
		addr.set(1);
		assertEquals(1, addr.getUnsigned());
		assertEquals(4, MainMemory.read(addr).getUnsigned());

		addr.set(2);
		assertEquals(2, addr.getUnsigned());
		assertEquals(1, MainMemory.read(addr).getUnsigned());
		MainMemory.clearData();
	}
	
	@Test
	public void testMainMemoryLoadZero() throws Exception {
		var mem = MainMemory.data; // Note: Only used to see debugger value, otherwise useless in test
		var addr = new Word();
		String[] arr = new String[3];
		
		arr[0] = "00000000000000000000000000000000"; // 0
		arr[1] = "00000000000000000000000000001111"; // 15
		arr[2] = "00000000000000000000000000010001"; // 17
		
		addr.set(0);
		assertEquals(0, addr.getUnsigned());
		MainMemory.load(arr);
		assertEquals(0, MainMemory.read(addr).getUnsigned());
		
		addr.set(1);
		assertEquals(1, addr.getUnsigned());
		assertEquals(15, MainMemory.read(addr).getUnsigned());

		addr.set(2);
		assertEquals(2, addr.getUnsigned());
		assertEquals(17, MainMemory.read(addr).getUnsigned());
		MainMemory.clearData();
	}
	
	@Test
	public void testMainMemoryWrite() throws Exception {
		var mem = MainMemory.data; // Note: Only used to see debugger value, otherwise useless in test
		var addr = new Word();
		var newValue = new Word();
		String[] arr = new String[3];
		
		arr[0] = "00000000000000000000000000000000"; // 0
		arr[1] = "00000000000000000000000000001111"; // 15
		arr[2] = "00000000000000000000000000010001"; // 17
		newValue.set(13);
		
		addr.set(0);
		assertEquals(0, addr.getUnsigned());
		MainMemory.load(arr);
		assertEquals(0, MainMemory.read(addr).getUnsigned());
		
		addr.set(1);
		assertEquals(1, addr.getUnsigned());
		assertEquals(15, MainMemory.read(addr).getUnsigned());

		addr.set(2);
		assertEquals(2, addr.getUnsigned());
		assertEquals(17, MainMemory.read(addr).getUnsigned());
		
		MainMemory.write(addr, newValue); // Value #2 / 17 -> 13
		assertEquals(13, MainMemory.read(addr).getUnsigned());

		addr.set(1);
		assertEquals(1, addr.getUnsigned());
		newValue.set(0);
		MainMemory.write(addr, newValue); // Value #1 / 15 -> 0
		assertEquals(0, MainMemory.read(addr).getUnsigned());
		MainMemory.clearData();
	}
	
	@Test
	public void testMainMemoryReadException() throws Exception {
		var mem = MainMemory.data; // Note: Only used to see debugger value, otherwise useless in test
		var addr = new Word();
		var newValue = new Word();
		String[] arr = new String[3];
		
		arr[0] = "00000000000000000000000000000000"; // 0
		arr[1] = "00000000000000000000000000001111"; // 15
		arr[2] = "00000000000000000000000000010001"; // 17
		newValue.set(13);
		
		addr.set(0);
		assertEquals(0, addr.getUnsigned());
		MainMemory.load(arr);
		assertEquals(0, MainMemory.read(addr).getUnsigned());
		
		addr.set(1);
		assertEquals(1, addr.getUnsigned());
		assertEquals(15, MainMemory.read(addr).getUnsigned());

		addr.set(2);
		assertEquals(2, addr.getUnsigned());
		assertEquals(17, MainMemory.read(addr).getUnsigned());
		
		try {
			addr.set(1500);
			MainMemory.read(addr); // Expect error to be thrown since address does not exist
			assertEquals(true, false); // Test Case failed
		} catch (Exception e) {
			assertEquals(true, true); // Test Case success
		}
		MainMemory.clearData();
	}
	
	@Test
	public void testMainMemoryFullLoad() throws Exception {
		var mem = MainMemory.data; // Note: Only used to see debugger value, otherwise useless in test
		var addr = new Word();
		var newValue = new Word();
		String[] arr = new String[1024];
		
		for (int i = 0; i < 1024; i++) arr[i] = "00000000000000000000000000000000"; 
		MainMemory.load(arr);
		
		addr.set(0);
		assertEquals(0, addr.getUnsigned());
		assertEquals(0, MainMemory.read(addr).getUnsigned());
		
		addr.set(500);
		assertEquals(500, addr.getUnsigned());
		newValue.set(500);
		MainMemory.write(addr, newValue);
		assertEquals(500, MainMemory.read(addr).getUnsigned());
		
		addr.set(1023);
		assertEquals(1023, addr.getUnsigned());
		newValue.set(1024);
		MainMemory.write(addr, newValue);
		assertEquals(1024, MainMemory.read(addr).getUnsigned());
		MainMemory.clearData();
	}
}