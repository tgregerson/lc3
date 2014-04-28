package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.InstructionCycle;

public class LeaInstruction extends Instruction {
  public LeaInstruction(BitWord bitword) {
    super(bitword);
  }

  @Override
  public String toString() {
    final String op_name = "LDI";
    final String operand1 = "R" + dr().ToInt();
    final String operand2 = "#" + pcoffset9().toSignedDecString();
    return op_name + " " + operand1 + ", " + operand2;
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
    control_set.gpr_dr_addr = dr();
    control_set.addr1_mux_select = BitWord.FALSE;
    control_set.addr2_mux_select = BitWord.FromInt(2, 2);
    control_set.mar_mux_select = BitWord.FALSE;
    return control_set;
  }
  
  private ControlSet StoreResult1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.mar_mux_tri_enable = BitWord.TRUE;
    control_set.gpr_dr_load = BitWord.TRUE;
    control_set.psr_load = BitWord.TRUE;
    return control_set;
  }

  public BitWord dr() {
    return bitword().GetBitRange(kDrHighBit, kDrLowBit);
  }
  
  public BitWord pcoffset9() {
    return bitword().GetBitRange(kPcOffset9HighBit, kPcOffset9LowBit);
  }

  private final int kDrHighBit = 11;
  private final int kDrLowBit = 9;
  private final int kPcOffset9HighBit = 8;
  private final int kPcOffset9LowBit = 0;
}
