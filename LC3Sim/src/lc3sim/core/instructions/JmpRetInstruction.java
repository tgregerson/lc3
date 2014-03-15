package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.StateMachine.InstructionCycle;

public class JmpRetInstruction extends Instruction {
  public JmpRetInstruction(BitWord bitword) {
    super(bitword);
  }

  @Override
  public ControlSet ControlSet(InstructionCycle cycle, BitWord psr) {
    switch (cycle) {
      case kFetchInstruction1:
        return FetchInstruction1ControlSet();
      case kFetchInstruction2:
        return FetchInstruction2ControlSet();
      case kFetchInstruction3:
        return FetchInstruction3ControlSet();
      case kStoreResult1:
        return StoreResult1ControlSet();
      default:
        // Unused
        assert false;
        return null;
    }
  }

  @Override
  protected ControlSet StateIndependentControlSet() {
    ControlSet control_set = super.StateIndependentControlSet();        
    control_set.gpr_sr1_addr = base_r();
    control_set.addr1_mux_select = BitWord.TRUE;
    control_set.addr2_mux_select = BitWord.FromInt(0, 2);
    control_set.pc_mux_select = BitWord.FromInt(1, 2);
    return control_set;
  }

  private ControlSet StoreResult1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.pc_load = BitWord.TRUE;
    return control_set;
  }

  @Override
  public Boolean has_base_r() {
    return true;
  }

  @Override
  public BitWord base_r() {
    return bitword().GetBitRange(kBaseRHighBit, kBaseRLowBit);
  }

  private final int kBaseRHighBit = 8;
  private final int kBaseRLowBit = 6;
}
