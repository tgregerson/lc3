package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.InstructionCycle;

public class NotInstruction extends Instruction {
  public NotInstruction(BitWord bitword) {
    super(bitword);
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
    control_set.gpr_sr1_addr = sr();
    control_set.alu_tri_enable = BitWord.TRUE;
    return control_set;
  }
  
  private ControlSet StoreResult1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.gpr_dr_load = BitWord.TRUE;
    return control_set;
  }

  public BitWord sr() {
    return bitword().GetBitRange(kSrHighBit, kSrLowBit);
  }
  
  public BitWord dr() {
    return bitword().GetBitRange(kDrHighBit, kDrLowBit);
  }
  private final int kDrHighBit = 11;
  private final int kDrLowBit = 9;
  private final int kSrHighBit = 8;
  private final int kSrLowBit = 6;
}
