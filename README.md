# Computer-Data-Simulator

The **Computer-Data-Simulator** is a Java-based simulation of a basic computer assembler and architecture. This project models key computer operations like mathematical calculations, boolean logic, and bit manipulation, similar to what happens in a real CPU. It includes an assembler that converts instructions into binary format to be loaded onto a simulated memory.

## Features

### 1. Arithmetic Logic Unit (ALU)
- **Handles Core Operations**: Performs basic arithmetic and logical operations such as addition, subtraction, multiplication, and bit shifting.
- **Boolean Logic Support**: Includes operations like AND, OR, NOT, XOR, and comparisons (e.g., greater than, equal to).

### 2. Assembler
- **Tokenization**: Converts input lines into tokens, such as operations (math, copy, jump), registers, or values, and processes these to form instructions.
- **Binary Conversion**: Transforms the parsed instructions into 32-bit binary words, which are then loaded into a simulated memory.

### 3. Base-2 Mathematical Conversions
- Handles conversion between different numerical bases (specifically binary).

### 4. Simulated Memory
- **Main Memory Interface**: Load assembled binary instructions into simulated memory to execute operations.

### 5. JUnit Testing
- Provides a suite of unit tests for validating the correctness of core features such as math operations, tokenization, and memory loading.

## Example Usage
#### Binary is demonstrated below, but the Assembler can convert plain language into 32 bit Instructions.

### 1. Basic Number Storage

0) "000000000000000101 0000 00001 000 01"; // MATH DestOnly 5 => R1
1) "000000000000000000 0000 00000 000 00"; // HALT

Stores 5 into Register 1

### 2. Factorial

0) "0000000000000 00110 0000 00001 000 01"; // MATH DestOnly 6 R1
1) "0000000000000 00110 0000 00010 000 01"; // MATH DestOnly 6 R2 
2) "0000000000000 00001 0000 00011 000 01"; // MATH DestOnly 1 R3
3) "0000000000011 00010 0100 00011 001 11"; // BRANCH PC <= r2 > 1 ? push pc; pc + imm : pc (Check if greater than 1)
4) "0000000000000 00000 1110 00011 011 01"; // Push r2 (Number is 1, push onto stack)
5) "0000000000100 00001 0100 00011 001 11"; // BRANCH: PC <= r1 > 1 ? push pc; pc + imm : pc (Loop while popping)
6) "0000000000000 00000 0000 00000 000 00"; // HALT 
7) "0000000000000 00000 1110 00010 011 01"; // Push r2 onto stack
8) "0000000000000 00011 1111 00010 000 11"; // MATH 2R r2 <= (R2 - R3)
9) "0000000000000 00000 0000 00011 001 00"; // Jump to Instruction 3
10) "0000000000000 00000 0000 00011 110 01"; // r3 <= Pop Stack
11) "0000000000000 00011 0111 00010 000 11"; // MATH 2R r2 <= (r2 * r3)
12) "0000000000000 00000 0000 00101 001 00"; // Jump to Instruction 5
		      
Returns 6! or 720

#### Note: See the SIA 32 Document for all Operation Codes
