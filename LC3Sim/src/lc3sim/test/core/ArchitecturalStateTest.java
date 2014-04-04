package lc3sim.test.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

  // TODO Add test of condition codes for instruction with DR
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
  
  // TODO For memory-related operations, add check to ensure instruction address
  // does not collide with memory addresses.

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

      
      final int trapvect8 =
          instruction.trapvect8().Resize(kWordSize, false).ToInt();
      final int expected_pc = trapvect8;
      final int expected_r7 = ModuloSum(instruction_addr, 1);

      state_.ExecuteInstruction();
      
      // Check correct result has been stored in DR.
      final int computed_pc = state_.ReadPc();
      final int computed_r7 = state_.ReadGpr(7);
      /*
      System.out.println(
        " PC_VAL: "  + BitWord.FromInt(computed_pc, kWordSize) +
        " EXPECTED_PC_VAL: "  + BitWord.FromInt(expected_pc, kWordSize) +
        " R7_VAL: "  + BitWord.FromInt(computed_r7, kWordSize) +
        " EXPECTED_R7_VAL: "  + BitWord.FromInt(expected_r7, kWordSize));
        */
      assertEquals(expected_pc, computed_pc);
      assertEquals(expected_r7, computed_r7);
      instruction_addr = state_.ReadPc();
    }
  }
  
  private int ModuloSum(int a, int b) {
    return (a + b) % (1 << kWordSize);
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
  
  private final int kWordSize = ArchitecturalState.kWordSize;
  
  private final int test_iterations_ = 1000;

  private ArchitecturalState state_;
}
