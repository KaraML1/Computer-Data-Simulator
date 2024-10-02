import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ProcessorJUnit {
	/**
	 * Note: Decrement Testing is in {@code as1JUNIT}
	 */

    @Test
    public void ProcessorBranchNoR() throws Exception
    {
    	String[] arr = new String[8];
        arr[0] = "0000000000000 00101 0000 00001 000 01"; // MATH DestOnly 5 R1
        arr[1] = "0000000000000 00000 0000 00101 001 00"; // JUMP: PC <= imm (jump to 5)
        arr[2] = "0000000000000 00110 0000 00010 000 01"; // MATH DestOnly 6 R2
        arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3
        arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
        arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5, Expect to jump here
        arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
        arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
        
        for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
        
        MainMemory.load(arr);

        Processor process = new Processor();
        process.run();
        
        assertEquals(0, process.getRegisters()[2].getUnsigned());
        assertEquals(0, process.getRegisters()[3].getUnsigned());
        assertEquals(0, process.getRegisters()[4].getUnsigned());
        assertEquals(9, process.getRegisters()[5].getUnsigned());
        assertEquals(10, process.getRegisters()[6].getUnsigned());

        MainMemory.clearData();
    }
    
    @Test
    public void ProcessorBranchDestOnly() throws Exception
    {
    	String[] arr = new String[8];
        arr[0] = "0000000000000 00101 0000 00001 000 01"; // MATH DestOnly 5 R1 
        arr[1] = "0000000000000 00011 0000 00000 001 01"; // JUMP: PC <= PC + imm (jump to 5) (2 + 3)
        arr[2] = "0000000000000 00110 0000 00010 000 01"; // MATH DestOnly 6 R2
        arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3
        arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
        arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5 // Expect to jump here
        arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
        arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
        
        for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
        
        MainMemory.load(arr);

        Processor process = new Processor();
        process.run();
                
        assertEquals(0, process.getRegisters()[2].getUnsigned());
        assertEquals(0, process.getRegisters()[3].getUnsigned());
        assertEquals(0, process.getRegisters()[4].getUnsigned());
        assertEquals(9, process.getRegisters()[5].getUnsigned());
        assertEquals(10, process.getRegisters()[6].getUnsigned());

        MainMemory.clearData();
    }
    
    @Test
    public void ProcessorBranch2REquals() throws Exception
    {
    	String[] arr = new String[8];
        arr[0] = "0000000000000 00001 0000 00001 000 01"; // MATH DestOnly 1 R1
        arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
        arr[2] = "0000000000010 00010 0000 00001 001 11"; // 2R: PC <= r1 BOP rD ? PC + imm : PC (2 == 1, expect PC)
        arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 // Expect to continue
        arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
        arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5
        arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
        arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
        
        for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
        
        MainMemory.load(arr);

        Processor process = new Processor();
        process.run();
        
        assertEquals(1, process.getRegisters()[1].getUnsigned());
        assertEquals(2, process.getRegisters()[2].getUnsigned());
        assertEquals(7, process.getRegisters()[3].getUnsigned());
        assertEquals(8, process.getRegisters()[4].getUnsigned());
        assertEquals(9, process.getRegisters()[5].getUnsigned());
        assertEquals(10, process.getRegisters()[6].getUnsigned());

        MainMemory.clearData();
    }
	
    @Test
    public void ProcessorBranch2RNotEquals() throws Exception
    {
    	String[] arr = new String[8];
        arr[0] = "0000000000000 00001 0000 00001 000 01"; // MATH DestOnly 1 R1
        arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
        arr[2] = "0000000000010 00010 0001 00001 001 11"; // 2R: PC <= r1 BOP rD ? PC + imm : PC (r1 == rD, expect PC + imm)
        arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3
        arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
        arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5 // Expect to jump here
        arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
        arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
        
        for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
        
        MainMemory.load(arr);

        Processor process = new Processor();
        process.run();
        
        assertEquals(1, process.getRegisters()[1].getUnsigned());
        assertEquals(2, process.getRegisters()[2].getUnsigned());
        assertEquals(0, process.getRegisters()[3].getUnsigned());
        assertEquals(0, process.getRegisters()[4].getUnsigned());
        assertEquals(9, process.getRegisters()[5].getUnsigned());
        assertEquals(10, process.getRegisters()[6].getUnsigned());

        MainMemory.clearData();
    }
	
    @Test
    public void ProcessorBranch2RLTFalse() throws Exception
    {
    	String[] arr = new String[8];
        arr[0] = "0000000000000 00001 0000 00001 000 01"; // MATH DestOnly 1 R1
        arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
        arr[2] = "0000000000010 00010 0010 00001 001 11"; // 2R: PC <= r1 BOP rD ? PC + imm : PC (r1 < rD, expect false)
        arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 // Expect to continue
        arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
        arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5
        arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
        arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
        
        for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
        
        MainMemory.load(arr);

        Processor process = new Processor();
        process.run();
        
        assertEquals(1, process.getRegisters()[1].getUnsigned());
        assertEquals(2, process.getRegisters()[2].getUnsigned());
        assertEquals(7, process.getRegisters()[3].getUnsigned());
        assertEquals(8, process.getRegisters()[4].getUnsigned());
        assertEquals(9, process.getRegisters()[5].getUnsigned());
        assertEquals(10, process.getRegisters()[6].getUnsigned());

        MainMemory.clearData();
    }
    
    @Test
    public void ProcessorBranch2RLTTrue() throws Exception
    {
    	String[] arr = new String[8];
        arr[0] = "0000000000000 00001 0000 00001 000 01"; // MATH DestOnly 1 R1
        arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
        arr[2] = "0000000000010 00001 0010 00010 001 11"; // 2R: PC <= r1 BOP rD ? PC + imm : PC (r1 < rD, expect false)
        arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 // Expect to continue
        arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
        arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5
        arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
        arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
        
        for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
        
        MainMemory.load(arr);

        Processor process = new Processor();
        process.run();
        
        assertEquals(1, process.getRegisters()[1].getUnsigned());
        assertEquals(2, process.getRegisters()[2].getUnsigned());
        assertEquals(0, process.getRegisters()[3].getUnsigned());
        assertEquals(0, process.getRegisters()[4].getUnsigned());
        assertEquals(9, process.getRegisters()[5].getUnsigned());
        assertEquals(10, process.getRegisters()[6].getUnsigned());

        MainMemory.clearData();
    }
	
	   @Test
	    public void ProcessorBranch2RLETrue() throws Exception
	    {
	    	String[] arr = new String[8];
	        arr[0] = "0000000000000 00001 0000 00001 000 01"; // MATH DestOnly 1 R1
	        arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
	        arr[2] = "0000000000010 00001 0101 00010 001 11"; // 2R: PC <= r1 BOP rD ? PC + imm : PC (1 <= 2, expect true)
	        arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 // Expect to Skip
	        arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
	        arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5
	        arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
	        arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
	        
	        for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
	        
	        MainMemory.load(arr);
	
	        Processor process = new Processor();
	        process.run();
	        
	        assertEquals(1, process.getRegisters()[1].getUnsigned());
	        assertEquals(2, process.getRegisters()[2].getUnsigned());
	        assertEquals(0, process.getRegisters()[3].getUnsigned());
	        assertEquals(0, process.getRegisters()[4].getUnsigned());
	        assertEquals(9, process.getRegisters()[5].getUnsigned());
	        assertEquals(10, process.getRegisters()[6].getUnsigned());
	
	        MainMemory.clearData();
	    }
	
	   @Test
    public void ProcessorBranch2RLEEqTrue() throws Exception
    {
    	String[] arr = new String[8];
        arr[0] = "0000000000000 00010 0000 00001 000 01"; // MATH DestOnly 2 R1
        arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
        arr[2] = "0000000000010 00001 0101 00010 001 11"; // 2R: PC <= r1 BOP rD ? PC + imm : PC (2 <= 2, expect true)
        arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 // Expect to Skip
        arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
        arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5
        arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
        arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
        
        for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
        
        MainMemory.load(arr);

        Processor process = new Processor();
        process.run();
        
        assertEquals(2, process.getRegisters()[1].getUnsigned());
        assertEquals(2, process.getRegisters()[2].getUnsigned());
        assertEquals(0, process.getRegisters()[3].getUnsigned());
        assertEquals(0, process.getRegisters()[4].getUnsigned());
        assertEquals(9, process.getRegisters()[5].getUnsigned());
        assertEquals(10, process.getRegisters()[6].getUnsigned());

        MainMemory.clearData();
    }
	
  @Test
  public void ProcessorBranch2RLEFalse() throws Exception
  {
  	String[] arr = new String[8];
      arr[0] = "0000000000000 00001 0000 00001 000 01"; // MATH DestOnly 1 R1
      arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
      arr[2] = "0000000000010 00010 0101 00001 001 11"; // 2R: PC <= r1 BOP rD ? PC + imm : PC (2 < 1, expect false)
      arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 // Expect to continue
      arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
      arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5
      arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
      arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
      
      for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
      
      MainMemory.load(arr);

      Processor process = new Processor();
      process.run();
      
      assertEquals(1, process.getRegisters()[1].getUnsigned());
      assertEquals(2, process.getRegisters()[2].getUnsigned());
      assertEquals(7, process.getRegisters()[3].getUnsigned());
      assertEquals(8, process.getRegisters()[4].getUnsigned());
      assertEquals(9, process.getRegisters()[5].getUnsigned());
      assertEquals(10, process.getRegisters()[6].getUnsigned());

      MainMemory.clearData();
  }
	
  @Test
  public void ProcessorBranch2RGTTrue() throws Exception
  {
  	String[] arr = new String[8];
      arr[0] = "0000000000000 00001 0000 00001 000 01"; // MATH DestOnly 1 R1
      arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
      arr[2] = "0000000000010 00010 0100 00001 001 11"; // 2R: PC <= r1 BOP rD ? PC + imm : PC (2 > 1, expect true)
      arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 
      arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
      arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5 // Expect to Jump here
      arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
      arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
      
      for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
      
      MainMemory.load(arr);

      Processor process = new Processor();
      process.run();
      
      assertEquals(1, process.getRegisters()[1].getUnsigned());
      assertEquals(2, process.getRegisters()[2].getUnsigned());
      assertEquals(0, process.getRegisters()[3].getUnsigned());
      assertEquals(0, process.getRegisters()[4].getUnsigned());
      assertEquals(9, process.getRegisters()[5].getUnsigned());
      assertEquals(10, process.getRegisters()[6].getUnsigned());

      MainMemory.clearData();
  }
	
	  @Test
	  public void ProcessorBranch2RGTFalse() throws Exception
	  {
	  	String[] arr = new String[8];
	      arr[0] = "0000000000000 00001 0000 00001 000 01"; // MATH DestOnly 1 R1
	      arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
	      arr[2] = "0000000000010 00001 0100 00010 001 11"; // 2R: PC <= r1 BOP rD ? PC + imm : PC (1 > 2, expect false)
	      arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 // Expect to continue
	      arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
	      arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5
	      arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
	      arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
	      
	      for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
	      
	      MainMemory.load(arr);

	      Processor process = new Processor();
	      process.run();
	      
	      assertEquals(1, process.getRegisters()[1].getUnsigned());
	      assertEquals(2, process.getRegisters()[2].getUnsigned());
	      assertEquals(7, process.getRegisters()[3].getUnsigned());
	      assertEquals(8, process.getRegisters()[4].getUnsigned());
	      assertEquals(9, process.getRegisters()[5].getUnsigned());
	      assertEquals(10, process.getRegisters()[6].getUnsigned());

	      MainMemory.clearData();
	  }
	
	  @Test
	  public void ProcessorBranch2RGEFalse() throws Exception
	  {
	  	String[] arr = new String[8];
	      arr[0] = "0000000000000 00001 0000 00001 000 01"; // MATH DestOnly 1 R1
	      arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
	      arr[2] = "0000000000010 00001 0011 00010 001 11"; // 2R: PC <= r1 BOP rD ? PC + imm : PC (1 >= 2, expect false)
	      arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 // Expect to continue
	      arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
	      arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5
	      arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
	      arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
	      
	      for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
	      
	      MainMemory.load(arr);

	      Processor process = new Processor();
	      process.run();
	      
	      assertEquals(1, process.getRegisters()[1].getUnsigned());
	      assertEquals(2, process.getRegisters()[2].getUnsigned());
	      assertEquals(7, process.getRegisters()[3].getUnsigned());
	      assertEquals(8, process.getRegisters()[4].getUnsigned());
	      assertEquals(9, process.getRegisters()[5].getUnsigned());
	      assertEquals(10, process.getRegisters()[6].getUnsigned());

	      MainMemory.clearData();
	  }
	  
	  @Test
	  public void ProcessorBranch2RGETrue() throws Exception
	  {
	  	String[] arr = new String[8];
	      arr[0] = "0000000000000 00010 0000 00001 000 01"; // MATH DestOnly 2 R1
	      arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
	      arr[2] = "0000000000010 00001 0011 00010 001 11"; // 2R: PC <= r1 BOP rD ? PC + imm : PC (2 >= 2, expect true)
	      arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 // Expect to continue
	      arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
	      arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5
	      arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
	      arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
	      
	      for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
	      
	      MainMemory.load(arr);

	      Processor process = new Processor();
	      process.run();
	      
	      assertEquals(2, process.getRegisters()[1].getUnsigned());
	      assertEquals(2, process.getRegisters()[2].getUnsigned());
	      assertEquals(0, process.getRegisters()[3].getUnsigned());
	      assertEquals(0, process.getRegisters()[4].getUnsigned());
	      assertEquals(9, process.getRegisters()[5].getUnsigned());
	      assertEquals(10, process.getRegisters()[6].getUnsigned());

	      MainMemory.clearData();
	  }
	   
	  @Test
	  public void ProcessorBranch3RBOPTrue() throws Exception
	  {
     	  String[] arr = new String[8];
	      arr[0] = "0000000000000 00010 0000 00001 000 01"; // MATH DestOnly 2 R1
	      arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
	      arr[2] = "00000010 00010 00001 0000 00000 001 10"; // 2R: PC <= r1 BOP r2 ? PC + imm : PC (2 == 2, expect true)
	      arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 // Expect to jump
	      arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
	      arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5
		  arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
		  arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
		      
	      for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
		      
	      MainMemory.load(arr);
	
	      Processor process = new Processor();
	      process.run();
	      
	      assertEquals(2, process.getRegisters()[1].getUnsigned());
	      assertEquals(2, process.getRegisters()[2].getUnsigned());
	      assertEquals(0, process.getRegisters()[3].getUnsigned());
	      assertEquals(0, process.getRegisters()[4].getUnsigned());
	      assertEquals(9, process.getRegisters()[5].getUnsigned());
	      assertEquals(10, process.getRegisters()[6].getUnsigned());
	
	      MainMemory.clearData();
	}
	
	  @Test
	  public void ProcessorBranch3RBOPFalse() throws Exception
	  {
     	  String[] arr = new String[8];
	      arr[0] = "0000000000000 00011 0000 00001 000 01"; // MATH DestOnly 3 R1
	      arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
	      arr[2] = "00000010 00010 00001 0000 00000 001 10"; // 3R: PC <= r1 BOP r2 ? PC + imm : PC (3 == 2, expect false)
	      arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 // Expect to continue
	      arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
	      arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5
		  arr[6] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6
		  arr[7] = "0000000000000 00000 0000 00000 000 00"; // HALT
		      
	      for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
		      
	      MainMemory.load(arr);
	
	      Processor process = new Processor();
	      process.run();
	      
	      assertEquals(3, process.getRegisters()[1].getUnsigned());
	      assertEquals(2, process.getRegisters()[2].getUnsigned());
	      assertEquals(7, process.getRegisters()[3].getUnsigned());
	      assertEquals(8, process.getRegisters()[4].getUnsigned());
	      assertEquals(9, process.getRegisters()[5].getUnsigned());
	      assertEquals(10, process.getRegisters()[6].getUnsigned());
	
	      MainMemory.clearData();
	}
	  
	  @Test
	  public void ProcessorCall2RFunctionReturn() throws Exception
	  {
     	  String[] arr = new String[9];
	      arr[0] = "0000000000000 00010 0000 00001 000 01"; // MATH DestOnly 2 R1
	      arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
	      arr[2] = "0000000000100 00010 0000 00001 010 11"; // 2R: PC <= Rs BOP Rd ? push pc; pc + imm : pc (2 == 2, expect call and then return);
	      arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 | Return here
	      arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
	      arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5
		  arr[6] = "0000000000000 00000 0000 00000 000 00"; // HALT
		  arr[7] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6 | Jump here
		  arr[8] = "0000000000000 00000 0000 00000 100 00"; // RETURN
				  
		      
	      for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
		      
	      MainMemory.load(arr);
	
	      Processor process = new Processor();
	      process.run();
	      
	      assertEquals(2, process.getRegisters()[1].getUnsigned());
	      assertEquals(2, process.getRegisters()[2].getUnsigned());
	      assertEquals(7, process.getRegisters()[3].getUnsigned());
	      assertEquals(8, process.getRegisters()[4].getUnsigned());
	      assertEquals(9, process.getRegisters()[5].getUnsigned());
	      assertEquals(10, process.getRegisters()[6].getUnsigned());
	
	      MainMemory.clearData();
	}
	  
	  @Test
	  public void ProcessorCall3RFunctionReturn() throws Exception
	  {
     	  String[] arr = new String[9];
	      arr[0] = "0000000000000 00010 0000 00001 000 01"; // MATH DestOnly 2 R1
	      arr[1] = "0000000000000 00010 0000 00010 000 01"; // MATH DestOnly 2 R2 
	      arr[2] = "00000101 00001 00010 0000 00010 010 10"; // 3R: PC <= r1 BOP r2 ? push pc; rD + imm : pc (2 == 2, expect call and then return);
	      arr[3] = "0000000000000 00111 0000 00011 000 01"; // MATH DestOnly 7 R3 | Return here
	      arr[4] = "0000000000000 01000 0000 00100 000 01"; // MATH DestOnly 8 R4
	      arr[5] = "0000000000000 01001 0000 00101 000 01"; // MATH DestOnly 9 R5
		  arr[6] = "0000000000000 00000 0000 00000 000 00"; // HALT
		  arr[7] = "0000000000000 01010 0000 00110 000 01"; // MATH DestOnly 10 R6 | Jump here
		  arr[8] = "0000000000000 00000 0000 00000 100 00"; // RETURN
				  
		      
	      for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
		      
	      MainMemory.load(arr);
	
	      Processor process = new Processor();
	      process.run();
	      
	      assertEquals(2, process.getRegisters()[1].getUnsigned());
	      assertEquals(2, process.getRegisters()[2].getUnsigned());
	      assertEquals(7, process.getRegisters()[3].getUnsigned());
	      assertEquals(8, process.getRegisters()[4].getUnsigned());
	      assertEquals(9, process.getRegisters()[5].getUnsigned());
	      assertEquals(10, process.getRegisters()[6].getUnsigned());
	
	      MainMemory.clearData();
	}
	  
	  @Test // Tests Jumps, Conditionals, and Push
	  public void ProcessorFactorial() throws Exception
	  {
     	  String[] arr = new String[13];
	      arr[0] = "0000000000000 00110 0000 00001 000 01"; // MATH DestOnly 6 R1
	      arr[1] = "0000000000000 00110 0000 00010 000 01"; // MATH DestOnly 6 R2 
	      arr[2] = "0000000000000 00001 0000 00011 000 01"; // MATH DestOnly 1 R3
	      arr[3] = "0000000000011 00010 0100 00011 001 11"; // BRANCH PC <= r2 > 1 ? push pc; pc + imm : pc (Check if greater than 1)
	      arr[4] = "0000000000000 00000 1110 00011 011 01"; // Push r2 (Number is 1, push onto stack)
	      arr[5] = "0000000000100 00001 0100 00011 001 11"; // BRANCH: PC <= r1 > 1 ? push pc; pc + imm : pc (Loop while popping)
	      arr[6] = "0000000000000 00000 0000 00000 000 00"; // HALT 
		  arr[7] = "0000000000000 00000 1110 00010 011 01"; // Push r2 onto stack
		  arr[8] = "0000000000000 00011 1111 00010 000 11"; // MATH 2R r2 <= (R2 - R3)
		  arr[9] = "0000000000000 00000 0000 00011 001 00"; // Jump to Instruction 3
		  arr[10] = "0000000000000 00000 0000 00011 110 01"; // r3 <= Pop Stack
		  arr[11] = "0000000000000 00011 0111 00010 000 11"; // MATH 2R r2 <= (r2 * r3)
		  arr[12] = "0000000000000 00000 0000 00101 001 00"; // Jump to Instruction 5
		      
	      for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
		      
	      MainMemory.load(arr);
	
	      Processor process = new Processor();
	      process.run();
	      
	      assertEquals(720, process.getRegisters()[2].getUnsigned());
	
	      MainMemory.clearData();
	}
	  
	  @Test
	  public void ProcessorFactorialZero() throws Exception
	  {
     	  String[] arr = new String[13];
	      arr[0] = "0000000000000 00000 0000 00001 000 01"; // MATH DestOnly 0 R1
	      arr[1] = "0000000000000 00000 0000 00010 000 01"; // MATH DestOnly 0 R2 
	      arr[2] = "0000000000000 00001 0000 00011 000 01"; // MATH DestOnly 1 R3
	      arr[3] = "0000000000011 00010 0100 00011 001 11"; // BRANCH PC <= r2 > 1 ? push pc; pc + imm : pc (Check if greater than 1)
	      arr[4] = "0000000000000 00000 1110 00011 011 01"; // Push r2 (Number is 1, push onto stack)
	      arr[5] = "0000000000100 00001 0100 00011 001 11"; // BRANCH: PC <= r1 > 1 ? push pc; pc + imm : pc (Loop while popping)
	      arr[6] = "0000000000000 00000 0000 00000 000 00"; // HALT 
		  arr[7] = "0000000000000 00000 1110 00010 011 01"; // Push r2 onto stack
		  arr[8] = "0000000000000 00011 1111 00010 000 11"; // MATH 2R r2 <= (R2 - R3)
		  arr[9] = "0000000000000 00000 0000 00011 001 00"; // Jump to Instruction arr[3]
		  arr[10] = "0000000000000 00000 0000 00011 110 01"; // r3 <= Pop Stack
		  arr[11] = "0000000000000 00011 0111 00010 000 11"; // MATH 2R r2 <= (r2 * r3)
		  arr[12] = "0000000000000 00000 0000 00101 001 00"; // Jump to Instruction arr[5]
		      
	      for (int i = 0; i < arr.length; i++) arr[i] = arr[i].replaceAll("\\s", ""); // Note: This is done to help with JUNIT readability
		      
	      MainMemory.load(arr);
	
	      Processor process = new Processor();
	      process.run();
	      
	      assertEquals(0, process.getRegisters()[2].getUnsigned());
	
	      MainMemory.clearData();
	}
}
