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
      /*
      System.out.println(
        " SR1: "  + BitWord.FromInt(sr1_val, kWordSize) +
        " SR2: " + BitWord.FromInt(sr2_val, kWordSize) + 
        " DR: "   + BitWord.FromInt(computed_result, kWordSize) +
        " EXPECTED: " + BitWord.FromInt(expected_result, kWordSize));
      */
      assertEquals(expected_result, computed_result);
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
  
  private final int test_iterations_ = 50;

  private ArchitecturalState state_;
}
