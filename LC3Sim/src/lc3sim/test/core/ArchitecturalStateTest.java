package lc3sim.test.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import lc3sim.core.*;
import lc3sim.core.instructions.*;
import lc3sim.test.core.instructions.*;

public class ArchitecturalStateTest {

  @Before
  public void setUp() throws Exception {
    state_ = new ArchitecturalState();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void AddRegRegInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      AddInstruction instruction =
          InstructionTestUtil.RandomAddRegRegInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      final int sr1_val = InstructionTestUtil.RandomImm(kWordSize).ToInt();
      final int sr2_val = instruction.sr1().IsIdentical(instruction.sr2()) ?
          sr1_val : InstructionTestUtil.RandomImm(kWordSize).ToInt();

      // Load register file
      state_.SetGpr(instruction.sr1().ToInt(), sr1_val);
      state_.SetGpr(instruction.sr2().ToInt(), sr2_val);

      final int expected_result = ModuloSum(sr1_val, sr2_val);

      assertEquals(ModuloSum(instruction_addr, 1), state_.ExecuteInstruction());
      
      // Check correct result has been stored in destination register.
      final int computed_result = state_.ReadGpr(instruction.dr().ToInt());
      assertEquals(expected_result, computed_result);
      CheckConditionCodes(expected_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void AddRegImmInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      AddInstruction instruction =
          InstructionTestUtil.RandomAddRegImmInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      final int sr1_val = InstructionTestUtil.RandomImm(kWordSize).ToInt();
      final int imm5_val = instruction.imm5().Resize(kWordSize, true).ToInt();

      // Load register file
      state_.SetGpr(instruction.sr1().ToInt(), sr1_val);

      final int expected_result = ModuloSum(sr1_val, imm5_val);

      assertEquals(ModuloSum(instruction_addr, 1), state_.ExecuteInstruction());
      
      // Check correct result has been stored in destination register.
      final int computed_result = state_.ReadGpr(instruction.dr().ToInt());
      assertEquals(expected_result, computed_result);
      CheckConditionCodes(expected_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void AndRegRegInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      AndInstruction instruction =
          InstructionTestUtil.RandomAndRegRegInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      final int sr1_val = InstructionTestUtil.RandomImm(kWordSize).ToInt();
      final int sr2_val = instruction.sr1().IsIdentical(instruction.sr2()) ?
          sr1_val : InstructionTestUtil.RandomImm(kWordSize).ToInt();

      // Load register file
      state_.SetGpr(instruction.sr1().ToInt(), sr1_val);
      state_.SetGpr(instruction.sr2().ToInt(), sr2_val);

      final int expected_result = sr1_val & sr2_val;

      assertEquals(ModuloSum(instruction_addr, 1), state_.ExecuteInstruction());
      
      // Check correct result has been stored in destination register.
      final int computed_result = state_.ReadGpr(instruction.dr().ToInt());
      assertEquals(expected_result, computed_result);
      CheckConditionCodes(expected_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void AndRegImmInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      AndInstruction instruction =
          InstructionTestUtil.RandomAndRegImmInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      final int sr1_val = InstructionTestUtil.RandomImm(kWordSize).ToInt();
      final int imm5_val = instruction.imm5().Resize(kWordSize, true).ToInt();

      // Load register file
      state_.SetGpr(instruction.sr1().ToInt(), sr1_val);

      final int expected_result = sr1_val & imm5_val;

      assertEquals(ModuloSum(instruction_addr, 1), state_.ExecuteInstruction());
      
      // Check correct result has been stored in destination register.
      final int computed_result = state_.ReadGpr(instruction.dr().ToInt());
      assertEquals(expected_result, computed_result);
      CheckConditionCodes(expected_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void BrInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      BrInstruction instruction =
          InstructionTestUtil.RandomBrInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);
      
      // Set PSR
      int psr = InstructionTestUtil.RandomPsr().ToInt();
      state_.SetPsr(psr);
      
      final int pc_offset_9 =
          instruction.pcoffset9().Resize(kWordSize, true).ToInt();
      final int pc_prime = ModuloSum(instruction_addr, 1);
      final int branch_target = ModuloSum(pc_prime, pc_offset_9);

      boolean take_branch = (PsrN(psr) && instruction.n()) ||
                            (PsrZ(psr) && instruction.z()) ||
                            (PsrP(psr) && instruction.p());
      final int expected_result = take_branch ? branch_target : pc_prime;

      state_.ExecuteInstruction();
      
      // Check correct result has been stored in PC.
      final int computed_result = state_.ReadPc();
      assertEquals(expected_result, computed_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void JmpInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      JmpRetInstruction instruction =
          InstructionTestUtil.RandomJmpRetInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      final int base_r_val = InstructionTestUtil.RandomImm(kWordSize).ToInt();

      // Load register file
      state_.SetGpr(instruction.base_r().ToInt(), base_r_val);
      
      final int expected_result = base_r_val;

      state_.ExecuteInstruction();
      
      // Check correct result has been stored in PC.
      final int computed_result = state_.ReadPc();
      assertEquals(expected_result, computed_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void JsrInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      JsrJsrrInstruction instruction =
          InstructionTestUtil.RandomJsrInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      int pc_offset_11 =
          instruction.pcoffset11().Resize(kWordSize, true).ToInt();
      
      final int expected_r7 = ModuloSum(instruction_addr, 1);
      final int expected_pc = ModuloSum(expected_r7, pc_offset_11);

      state_.ExecuteInstruction();
      
      // Check correct result has been stored in PC.
      final int computed_pc = state_.ReadPc();
      final int computed_r7 = state_.ReadGpr(7);
      assertEquals(expected_pc, computed_pc);
      assertEquals(expected_r7, computed_r7);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void JsrrInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      JsrJsrrInstruction instruction =
          InstructionTestUtil.RandomJsrrInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      final int base_r = instruction.base_r().ToInt();
      final int base_r_val = InstructionTestUtil.RandomImm(kWordSize).ToInt();
      
      // Set register file.
      state_.SetGpr(base_r, base_r_val);
      
      final int expected_r7 = ModuloSum(instruction_addr, 1);
      final int expected_pc = base_r_val;

      state_.ExecuteInstruction();
      
      // Check correct result has been stored in PC.
      final int computed_pc = state_.ReadPc();
      final int computed_r7 = state_.ReadGpr(7);
      assertEquals(expected_pc, computed_pc);
      assertEquals(expected_r7, computed_r7);
      instruction_addr = state_.ReadPc();
    }
  }
  
  @Test
  public void LdInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      LdInstruction instruction =
          InstructionTestUtil.RandomLdInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      final int pc_prime = ModuloSum(instruction_addr, 1);
      final int pc_offset_9 =
          instruction.pcoffset9().Resize(kWordSize, true).ToInt();
      final int computed_address = ModuloSum(pc_prime, pc_offset_9);
      
      // Store some data in memory.
      final int memory_val = (computed_address == instruction_addr) ?
          instruction_val : InstructionTestUtil.RandomImm(kWordSize).ToInt();
      state_.SetMemory(computed_address, memory_val);
      
      final int dr = instruction.dr().ToInt();
      final int expected_result = memory_val;

      state_.ExecuteInstruction();
      
      // Check correct result has been stored in DR.
      final int computed_dr = state_.ReadGpr(dr);
      assertEquals(expected_result, computed_dr);
      CheckConditionCodes(expected_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void LdiInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      LdiInstruction instruction =
          InstructionTestUtil.RandomLdiInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      final int pc_prime = ModuloSum(instruction_addr, 1);
      final int pc_offset_9 =
          instruction.pcoffset9().Resize(kWordSize, true).ToInt();
      final int address1 = ModuloSum(pc_prime, pc_offset_9);
      final int memory_val1 = (address1 == instruction_addr) ?
          instruction_val : InstructionTestUtil.RandomImm(kWordSize).ToInt();
      
      // Store some data in memory.
      final int address2 = memory_val1;
      final int memory_val2 = (address1 == address2) ?
          memory_val1 : (instruction_addr == address2) ?
          instruction_val : InstructionTestUtil.RandomImm(kWordSize).ToInt();
      state_.SetMemory(address1, memory_val1);
      state_.SetMemory(address2, memory_val2);
      
      final int dr = instruction.dr().ToInt();
      final int expected_result = memory_val2;

      state_.ExecuteInstruction();
      
      // Check correct result has been stored in DR.
      final int computed_dr = state_.ReadGpr(dr);
      assertEquals(expected_result, computed_dr);
      CheckConditionCodes(expected_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void LdrInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      LdrInstruction instruction =
          InstructionTestUtil.RandomLdrInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      // Set register file.
      final int base_r = instruction.base_r().ToInt();
      final int base_r_val = InstructionTestUtil.RandomImm(kWordSize).ToInt();
      state_.SetGpr(base_r, base_r_val);
      
      // Store some data in memory.
      final int memory_val = (instruction_addr == base_r_val) ?
          instruction_val : InstructionTestUtil.RandomImm(kWordSize).ToInt();
      state_.SetMemory(base_r_val, memory_val);
      
      final int dr = instruction.dr().ToInt();
      final int expected_result = memory_val;

      state_.ExecuteInstruction();
      
      // Check correct result has been stored in DR.
      final int computed_dr = state_.ReadGpr(dr);
      assertEquals(expected_result, computed_dr);
      CheckConditionCodes(expected_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void LeaInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      LeaInstruction instruction =
          InstructionTestUtil.RandomLeaInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      final int pc_prime = ModuloSum(instruction_addr, 1);
      final int pc_offset_9 =
          instruction.pcoffset9().Resize(kWordSize, true).ToInt();
      final int computed_address = ModuloSum(pc_prime, pc_offset_9);
      
      final int dr = instruction.dr().ToInt();
      final int expected_result = computed_address;

      state_.ExecuteInstruction();
      
      // Check correct result has been stored in DR.
      final int computed_dr = state_.ReadGpr(dr);
      assertEquals(expected_result, computed_dr);
      CheckConditionCodes(expected_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void NotInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      NotInstruction instruction =
          InstructionTestUtil.RandomNotInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      final int sr_val = InstructionTestUtil.RandomImm(kWordSize).ToInt();

      // Load register file
      state_.SetGpr(instruction.sr().ToInt(), sr_val);

      final int expected_result = (~sr_val) & 0x0FFFF;

      assertEquals(ModuloSum(instruction_addr, 1), state_.ExecuteInstruction());
      
      // Check correct result has been stored in destination register.
      final int computed_result = state_.ReadGpr(instruction.dr().ToInt());
      assertEquals(expected_result, computed_result);
      CheckConditionCodes(expected_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void StInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      StInstruction instruction =
          InstructionTestUtil.RandomStInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      final int pc_prime = ModuloSum(instruction_addr, 1);
      final int pc_offset_9 =
          instruction.pcoffset9().Resize(kWordSize, true).ToInt();
      final int computed_address = ModuloSum(pc_prime, pc_offset_9);
      
      // Store some data in the source register.
      final int register_val = InstructionTestUtil.RandomImm(kWordSize).ToInt();
      state_.SetGpr(instruction.sr().ToInt(), register_val);
      
      final int expected_result = register_val;

      state_.ExecuteInstruction();
      
      // Check correct result has been stored in memory.
      final int computed_result = state_.ReadMemory(computed_address);
      assertEquals(expected_result, computed_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void StiInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      StiInstruction instruction =
          InstructionTestUtil.RandomStiInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      // Store data in register file.
      final int sr = instruction.sr().ToInt();
      final int sr_val = InstructionTestUtil.RandomImm(kWordSize).ToInt();
      state_.SetGpr(sr, sr_val);

      final int pc_prime = ModuloSum(instruction_addr, 1);
      final int pc_offset_9 =
          instruction.pcoffset9().Resize(kWordSize, true).ToInt();
      final int address1 = ModuloSum(pc_prime, pc_offset_9);
      final int memory_val1 = (address1 == instruction_addr) ?
          instruction_val : InstructionTestUtil.RandomImm(kWordSize).ToInt();
      
      // Store indirect address in memory.
      state_.SetMemory(address1, memory_val1);
      final int address2 = memory_val1;

      state_.ExecuteInstruction();
      
      // Check correct result has been stored in DR.
      final int computed_result = state_.ReadMemory(address2);
      final int expected_result = sr_val;
      assertEquals(expected_result, computed_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void StrInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      StrInstruction instruction =
          InstructionTestUtil.RandomStrInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);

      // Set register file.
      final int base_r = instruction.base_r().ToInt();
      final int base_r_val = InstructionTestUtil.RandomImm(kWordSize).ToInt();
      final int sr = instruction.sr().ToInt();
      final int sr_val = (base_r == sr) ?
          base_r_val : InstructionTestUtil.RandomImm(kWordSize).ToInt();
      state_.SetGpr(base_r, base_r_val);
      state_.SetGpr(sr, sr_val);
      
      final int expected_result = sr_val;

      state_.ExecuteInstruction();
      
      // Check correct result has been stored in DR.
      final int computed_result = state_.ReadMemory(base_r_val);
      assertEquals(expected_result, computed_result);
      instruction_addr = state_.ReadPc();
    }
  }

  @Test
  public void TrapInstructionRandomTest() {
    int instruction_addr =
        InstructionTestUtil.RandomImm(kWordSize).ToInt();
    for (int i = 0; i < test_iterations_; ++i) {
      TrapInstruction instruction =
          InstructionTestUtil.RandomTrapInstruction();
      final int instruction_val = instruction.bitword().ToInt();

      // Load instruction into memory.
      state_.SetMemory(instruction_addr, instruction_val);
      state_.SetPc(instruction_addr);
      
      // Set trap target
      final int trapvect8 =
          instruction.trapvect8().Resize(kWordSize, false).ToInt();
      final int trap_target_addr =
          InstructionTestUtil.RandomImm(kWordSize).ToInt();
      state_.SetMemory(trapvect8, trap_target_addr);
      
      final int expected_pc = trap_target_addr;
      final int expected_r7 = ModuloSum(instruction_addr, 1);

      state_.ExecuteInstruction();
      
      // Check correct result has been stored in DR.
      final int computed_pc = state_.ReadPc();
      final int computed_r7 = state_.ReadGpr(7);
      assertEquals(expected_pc, computed_pc);
      assertEquals(expected_r7, computed_r7);
      instruction_addr = state_.ReadPc();
    }
  }
  
  @Test
  public void P1ObjTest() {
    final int os_start_addr = 0x0200;
    try {
      state_.LoadObjFile(GetTestFilePath("lc3os.obj"));
    } catch (IOException e) {
      System.out.println("General I/O Exception:");
      System.out.println(e);
    }

    int program_start_addr = 0x0000;
    try {
      program_start_addr = state_.LoadObjFile(GetTestFilePath("p1.obj"));
    } catch (IOException e) {
      System.out.println("General I/O Exception:");
      System.out.println(e);
    }
    state_.SetPc(os_start_addr);
    
    // Set a sane limit to ensure we don't get stuck in an infinite loop.
    final int max_warmup_instructions = 500;
    int warmup_instructions = 0;
    while (state_.ExecuteInstruction() != program_start_addr &&
           warmup_instructions < max_warmup_instructions) {
      warmup_instructions++;
    }
    
    state_.SetGpr(0, 0x0101);
    state_.SetGpr(1, 0xFF00);
    
    int addr = program_start_addr;
    
    // NOT R2, R0
    state_.ExecuteInstruction();
    assertEquals(++addr, state_.ReadPc());
    assertEquals(0xFEFE, state_.ReadGpr(2));

    // NOT R3, R1
    state_.ExecuteInstruction();
    assertEquals(++addr, state_.ReadPc());
    assertEquals(0x00FF, state_.ReadGpr(3));

    // AND R3, R0, R3
    state_.ExecuteInstruction();
    assertEquals(++addr, state_.ReadPc());
    assertEquals(0x0001, state_.ReadGpr(3));

    // AND R2, R1, R2
    state_.ExecuteInstruction();
    assertEquals(++addr, state_.ReadPc());
    assertEquals(0xFE00, state_.ReadGpr(2));

    // NOT R2, R2
    state_.ExecuteInstruction();
    assertEquals(++addr, state_.ReadPc());
    assertEquals(0x01FF, state_.ReadGpr(2));

    // NOT R3, R3
    state_.ExecuteInstruction();
    assertEquals(++addr, state_.ReadPc());
    assertEquals(0xFFFE, state_.ReadGpr(3));

    // AND R2, R2, R3
    state_.ExecuteInstruction();
    assertEquals(++addr, state_.ReadPc());
    assertEquals(0x01FE, state_.ReadGpr(2));

    // NOT R2, R2
    state_.ExecuteInstruction();
    assertEquals(++addr, state_.ReadPc());
    assertEquals(0xFE01, state_.ReadGpr(2));
  }
  
  private int ModuloSum(int a, int b) {
    return (a + b) % (1 << kWordSize);
  }
  
  private void CheckConditionCodes(int written_value) {
    final boolean expected_n = written_value > 0x7FFF;
    final boolean expected_z = written_value == 0;
    final boolean expected_p = !(expected_n || expected_z);
    final boolean computed_n = (state_.ReadPsr() & 0x4) != 0;
    final boolean computed_z = (state_.ReadPsr() & 0x2) != 0;
    final boolean computed_p = (state_.ReadPsr() & 0x1) != 0;
    assertEquals(expected_n, computed_n);
    assertEquals(expected_z, computed_z);
    assertEquals(expected_p, computed_p);
  }
  
  private boolean PsrN(int psr) {
    return (psr & 0x4) != 0;
  }

  private boolean PsrZ(int psr) {
    return (psr & 0x2) != 0;
  }

  private boolean PsrP(int psr) {
    return (psr & 0x1) != 0;
  }

  private Path GetTestFilePath(String filename) {
    return Paths.get(System.getProperty("user.dir") + "/src/lc3sim/test/core/" + filename);
  }

  private final int kWordSize = ArchitecturalState.kWordSize;
  
  private final int test_iterations_ = 1000;

  private ArchitecturalState state_;
}
