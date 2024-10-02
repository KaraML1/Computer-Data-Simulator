import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ProcessorMathJUnit {
	
    @Test
    public void ProcessorMathDest() throws Exception
    {
    	var MainMem = MainMemory.data; // Note: Used for Debugging Purposes
        String[] arr = new String[2];
        arr[0] = "00000000000000010100000000100001"; // MATH DestOnly 5 => R1
        arr[1] = "00000000000000000000000000000000"; // HALT
        MainMemory.load(arr);

        Processor process = new Processor();
        process.run();

        Word answer = new Word();
        answer.set(5);
        assertEquals(answer.getUnsigned(), process.getRegisters()[1].getUnsigned());
        MainMemory.clearData();
    }
	
  @Test
  public void ProcessorMathDestR0() throws Exception
  {
  	var MainMem = MainMemory.data; // Note: Used for Debugging Purposes
      String[] arr = new String[2];
      arr[0] = "00000000000000010100000000000001"; // MATH DestOnly 5 => R0
      arr[1] = "00000000000000000000000000000000"; // HALT
      MainMemory.load(arr);

      Processor process = new Processor();
      process.run();

      Word answer = new Word();
      answer.set(0);
      assertEquals(answer.getUnsigned(), process.getRegisters()[0].getUnsigned());
      MainMemory.clearData();
  }
    
    @Test
    public void ProcessorMathR2() throws Exception
    {
    	var MainMem = MainMemory.data; // Note: Used for Debugging Purposes
        String[] arr = new String[3];
        arr[0] = "00000000000000010100000000100001"; // MATH DestOnly 5 R1
        arr[1] = "00000000000000000111100000100011"; // MATH ADD R1 R1 R1
        arr[2] = "00000000000000000000000000000000"; // HALT
        
        MainMemory.load(arr);

        Processor process = new Processor();
        process.run();

        Word answer = new Word();
        answer.set(10);
        assertEquals(answer.getUnsigned(), process.getRegisters()[1].getUnsigned());
        MainMemory.clearData();
    }
	
  @Test // R0 should stay 0 and cannot be altered. It is an operation pass.
  public void ProcessorMathR0() throws Exception 
  { 
	  var MainMem = MainMemory.data; // Note: Used for Debugging Purposes
      String[] arr = new String[3];
      arr[0] = "00000000000000101000000000100001"; // MATH DestOnly 10 R1
      arr[1] = "00000000000000000111100000000011"; // MATH ADD R1 R0 R0
      arr[2] = "00000000000000000000000000000000"; // HALT
      
      MainMemory.load(arr);

      Processor process = new Processor();
      process.run();

      Word answer = new Word(); // 0 by default
      assertEquals(answer.getUnsigned(), process.getRegisters()[0].getUnsigned());
      Word regOneVal = new Word();
      regOneVal.set(10);
      assertEquals(regOneVal.getUnsigned(), process.getRegisters()[1].getUnsigned());
      MainMemory.clearData();
  }
    
    @Test
    public void ProcessorMathR3() throws Exception
    {
    	var MainMem = MainMemory.data; // Note: Used for Debugging Purposes
        String[] arr = new String[4];
        arr[0] = "00000000000000011000000000100001"; // MATH DestOnly 6 R1
        arr[1] = "00000000000000011000000001000001"; // MATH DestOnly 6 R2
        arr[2] = "00000000000100000111100001100010"; // MATH ADD R1 R2 R3 | 00000000 00010 00001 1110 00011 00010
        arr[3] = "00000000000000000000000000000000"; // HALT
        
        MainMemory.load(arr);

        Processor process = new Processor();
        process.run();

        Word answer = new Word();
        answer.set(12);
        assertEquals(answer.getUnsigned(), process.getRegisters()[3].getUnsigned());
        MainMemory.clearData();
    }
    
    @Test
    public void ProcessorMathR3Multiply() throws Exception
    {
    	var MainMem = MainMemory.data; // Note: Used for Debugging Purposes
        String[] arr = new String[4];
        arr[0] = "00000000000000011000000000100001"; // MATH DestOnly 6 R1
        arr[1] = "00000000000000011000000001000001"; // MATH DestOnly 6 R2
        arr[2] = "00000000000100000101110001100010"; // MATH multiply R1 R2 R3 | 00000000 00010 00001 0111 00011 00010
        arr[3] = "00000000000000000000000000000000"; // HALT
        
        MainMemory.load(arr);

        Processor process = new Processor();
        process.run();

        Word answer = new Word();
        answer.set(36);
        assertEquals(answer.getUnsigned(), process.getRegisters()[3].getUnsigned());
        MainMemory.clearData();
    }
    
    @Test
    public void ProcessorMathR3OR() throws Exception
    { // Test OR (0110 OR 0001 => Expect 0111 or 7 in R3)
    	var MainMem = MainMemory.data; // Note: Used for Debugging Purposes
        String[] arr = new String[4]; 
        arr[0] = "00000000000000011000000000100001"; // MATH DestOnly 6 R1
        arr[1] = "00000000000000000100000001000001"; // MATH DestOnly 1 R2
        arr[2] = "00000000000100000110010001100010"; // MATH OR R1 R2 R3
        arr[3] = "00000000000000000000000000000000"; // HALT
        
        MainMemory.load(arr);

        Processor process = new Processor();
        process.run();

        Word answer = new Word();
        answer.set(7);
        assertEquals(answer.getUnsigned(), process.getRegisters()[3].getUnsigned());
        MainMemory.clearData();
    }
	
  @Test
  public void ProcessorMathR2SubtractSelf() throws Exception
  {
  	var MainMem = MainMemory.data; // Note: Used for Debugging Purposes
      String[] arr = new String[3];
      arr[0] = "00000000000000010100000000100001"; // MATH DestOnly 5 R1
      arr[1] = "00000000000000000111110000100011"; // MATH ADD R1 R1 R1
      arr[2] = "00000000000000000000000000000000"; // HALT
      
      MainMemory.load(arr);

      Processor process = new Processor();
      process.run();

      Word answer = new Word();
      answer.set(0);
      assertEquals(answer.getUnsigned(), process.getRegisters()[1].getUnsigned());
      MainMemory.clearData();
  }
  
  @Test
  public void ProcessorMathR3MultiplyByR0Self() throws Exception
  {
  	var MainMem = MainMemory.data; // Note: Used for Debugging Purposes
      String[] arr = new String[3];
      arr[0] = "00000000000000010100000000100001"; // MATH DestOnly 5 R1
      arr[1] = "00000000000000000001110000100010"; // MATH MULTIPLY R1 R0 R1
      arr[2] = "00000000000000000000000000000000"; // HALT
      
      MainMemory.load(arr);

      Processor process = new Processor();
      process.run();

      Word answer = new Word();
      answer.set(0);
      assertEquals(answer.getUnsigned(), process.getRegisters()[1].getUnsigned());
      MainMemory.clearData();
  }
  
  @Test
  public void ProcessorMathR3MultiplyNegatives() throws Exception
  {
  	var MainMem = MainMemory.data; // Note: Used for Debugging Purposes
      String[] arr = new String[3];
      // -5 is 1111 1011
      arr[0] = "11111111111111101100000000100001"; // MATH DestOnly -5 R1
      arr[1] = "00000000000010000101110001000010"; // MATH Multiply R1 R1 R2
      arr[2] = "00000000000000000000000000000000"; // HALT
      
      MainMemory.load(arr);

      Processor process = new Processor();
      process.run();

      Word answer = new Word();
      answer.set(25);
      assertEquals(answer.getUnsigned(), process.getRegisters()[2].getUnsigned());
      MainMemory.clearData();
  }
  
  @Test
  public void ProcessorMathR3AddNegatives() throws Exception
  {
  	var MainMem = MainMemory.data; // Note: Used for Debugging Purposes
      String[] arr = new String[3];
      // -5 is 1111 1011
      arr[0] = "11111111111111101100000000100001"; // MATH DestOnly -5 R1
      arr[1] = "00000000000010000111100001000010"; // MATH ADD R1 R1 R2
      arr[2] = "00000000000000000000000000000000"; // HALT
      
      MainMemory.load(arr);

      Processor process = new Processor();
      process.run();

      Word answer = new Word();
      answer.set(-10);
      assertEquals(answer.getSigned(), process.getRegisters()[2].getSigned());
      MainMemory.clearData();
  }
  
  @Test
  public void ProcessorMathR3MultNegativeAndR0() throws Exception
  {
  	var MainMem = MainMemory.data; // Note: Used for Debugging Purposes
      String[] arr = new String[3];
      // -5 is 1111 1011
      arr[0] = "11111111111111101100000000100001"; // MATH DestOnly -5 R1
      arr[1] = "00000000000000000101110001000010"; // MATH Multiply R1 R0 R2
      arr[2] = "00000000000000000000000000000000"; // HALT
      
      MainMemory.load(arr);

      Processor process = new Processor();
      process.run();

      Word answer = new Word();
      answer.set(0);
      assertEquals(answer.getUnsigned(), process.getRegisters()[2].getUnsigned());
      MainMemory.clearData();
  }
}