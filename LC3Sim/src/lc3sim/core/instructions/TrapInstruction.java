package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.StateMachine.InstructionCycle;

public class TrapInstruction extends Instruction{
  public TrapInstruction(BitWord bitword) {
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
      case kEvaluateAddress1:
        return EvaluateAddress1ControlSet();
      case kFetchOperands1:
        return FetchOperands1ControlSet();
      case kExecuteOperation1:
        return ExecuteOperation1ControlSet();
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
    control_set.gpr_dr_addr = BitWord.FromInt(7, 3);
    control_set.mar_mux_select = BitWord.TRUE;
    control_set.mdr_mux_select = BitWord.TRUE;
    return control_set;
  }

  private ControlSet EvaluateAddress1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.mar_mux_tri_enable = BitWord.TRUE;
    control_set.mar_load = BitWord.TRUE;
    return control_set;
  }

  private ControlSet FetchOperands1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.mdr_load = BitWord.TRUE;
    return control_set;
  }

  private ControlSet ExecuteOperation1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.mdr_tri_enable = BitWord.TRUE;
    control_set.pc_mux_select = BitWord.FromInt(2, 2);
    return control_set;
  }
  
  private ControlSet StoreResult1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.pc_load = BitWord.TRUE;
    return control_set;
  }
  
  @Override
  public Boolean has_trapvect8() {
    return true;
  }
  
  @Override
  public BitWord trapvect8() {
    return bitword().GetBitRange(kTrapvect8HighBit, kTrapvect8LowBit);
  }

  private final int kTrapvect8HighBit = 7;
  private final int kTrapvect8LowBit = 0;
}
