package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.StateMachine.InstructionCycle;

public class LeaInstruction extends Instruction {
  public LeaInstruction(BitWord bitword) {
    super(bitword);
  }

  @Override
  public ControlSet ControlSet(InstructionCycle cycle) {
    switch (cycle) {
      case kFetchInstruction1:
        return FetchInstruction1ControlSet();
      case kFetchInstruction2:
        return FetchInstruction2ControlSet();
      case kDecodeInstruction1:
        return DecodeInstruction1ControlSet();
      case kEvaluateAddress1:
        // Unused
        assert false;
        return null;
      case kFetchOperands1:
        return FetchOperands1ControlSet();
      case kExecuteOperation1:
        return ExecuteOperation1ControlSet();
      case kExecuteOperation2:
        // Unused
        assert false;
        return null;
    }
    assert false;
    return null;
  }
  
  @Override
  protected ControlSet StateIndependentControlSet() {
    ControlSet control_set = super.StateIndependentControlSet(); 
    control_set.gpr_dr_addr = dr();
    control_set.addr1_mux_select = BitWord.FALSE;
    control_set.pc_mux_select = BitWord.FromInt(2, 2);
    control_set.mar_mux_select = BitWord.FALSE;
    return control_set;
  }
  
  private ControlSet FetchOperands1ControlSet() {
    return StateIndependentControlSet();
  }

  private ControlSet ExecuteOperation1ControlSet() {
    return StateIndependentControlSet();
  }
  
  private ControlSet StoreResult1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.gpr_dr_load = BitWord.TRUE;
    return control_set;
  }

  @Override
  public Boolean has_dr() {
    return true;
  }
  
  @Override
  public BitWord dr() {
    return bitword().GetBitRange(kDrHighBit, kDrLowBit);
  }
  
  @Override
  public Boolean has_pcoffset9() {
    return true;
  }
  
  @Override
  public BitWord pcoffset9() {
    return bitword().GetBitRange(kPcOffset9HighBit, kPcOffset9LowBit);
  }

  private final int kDrHighBit = 11;
  private final int kDrLowBit = 9;
  private final int kPcOffset9HighBit = 8;
  private final int kPcOffset9LowBit = 0;
}
