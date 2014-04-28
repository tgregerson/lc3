package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.InstructionCycle;

public class AndInstruction extends Instruction {
  public AndInstruction(BitWord bitword) {
    super(bitword);
  }

  @Override
  public String toString() {
    final String op_name = "AND";
    final String operand1 = "R" + dr().ToInt();
    final String operand2 = "R" + sr1().ToInt();
    final String operand3 = has_sr2() ?
        "R" + sr2().ToInt() : "#" + imm5().toSignedDecString();
    return op_name + " " + operand1 + ", " + operand2 + ", " + operand3;
  }

  @Override
  public ControlSet ControlSet(InstructionCycle cycle, BitWord psr) {
    switch (cycle) {
      case kStoreResult1: return StoreResult1ControlSet();
      default: return super.ControlSet(cycle, psr);
    }
  }

  @Override
  public InstructionCycle NextCycle(InstructionCycle current_cycle) {
    switch (current_cycle) {
      case kFetchInstruction1: return InstructionCycle.kFetchInstruction2;
      case kFetchInstruction2: return InstructionCycle.kFetchInstruction3;
      case kFetchInstruction3: return InstructionCycle.kStoreResult1;
      case kStoreResult1: return InstructionCycle.kFetchInstruction1;
      default:
        assert false;
        return null;
    }
  }

  @Override
  protected ControlSet StateIndependentControlSet() {
    ControlSet control_set = super.StateIndependentControlSet();        
    control_set.gpr_sr1_addr = sr1();
    control_set.gpr_sr2_addr = sr2();
    control_set.gpr_dr_addr = dr();
    return control_set;
  }

  private ControlSet StoreResult1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.gpr_dr_load = BitWord.TRUE;
    control_set.psr_load = BitWord.TRUE;
    control_set.alu_tri_enable = BitWord.TRUE;
    return control_set;
  }

  public BitWord sr1() {
    return bitword().GetBitRange(kSr1HighBit, kSr1LowBit);
  }
  
  public Boolean has_sr2() {
    return !mode_bit();
  }
  
  public BitWord sr2() {
    return bitword().GetBitRange(kSr2HighBit, kSr2LowBit);
  }
  
  public BitWord dr() {
    return bitword().GetBitRange(kDrHighBit, kDrLowBit);
  }
  
  public Boolean has_imm5() {
    return mode_bit();
  }
  
  public BitWord imm5() {
    return bitword().GetBitRange(kImm5HighBit, kImm5LowBit);
  }
  
  public Boolean mode_bit() {
    return bitword().TestBit(kModeBit);
  }
  
  private final int kDrHighBit = 11;
  private final int kDrLowBit = 9;
  private final int kSr1HighBit = 8;
  private final int kSr1LowBit = 6;
  private final int kSr2HighBit = 2;
  private final int kSr2LowBit = 0;
  private final int kImm5HighBit = 4;
  private final int kImm5LowBit = 0;
  private final int kModeBit = 5;
}