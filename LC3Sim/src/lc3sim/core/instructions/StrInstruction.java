package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.InstructionCycle;

public class StrInstruction extends Instruction {
  public StrInstruction(BitWord bitword) {
    super(bitword);
  }

  @Override
  public ControlSet ControlSet(InstructionCycle cycle, BitWord psr) {
    switch (cycle) {
      case kEvaluateAddress1: return EvaluateAddress1ControlSet();
      case kFetchOperands1: return FetchOperands1ControlSet();
      case kExecuteOperation1: return ExecuteOperation1ControlSet();
      case kStoreResult1: return StoreResult1ControlSet();
      default: return super.ControlSet(cycle, psr);
    }
  }

  @Override
  public InstructionCycle NextCycle(InstructionCycle current_cycle) {
    switch (current_cycle) {
      case kFetchInstruction1: return InstructionCycle.kFetchInstruction2;
      case kFetchInstruction2: return InstructionCycle.kFetchInstruction3;
      case kFetchInstruction3: return InstructionCycle.kEvaluateAddress1;
      case kEvaluateAddress1: return InstructionCycle.kFetchOperands1;
      case kFetchOperands1: return InstructionCycle.kExecuteOperation1;
      case kExecuteOperation1: return InstructionCycle.kStoreResult1;
      case kStoreResult1: return InstructionCycle.kFetchInstruction1;
      default:
        assert false;
        return null;
    }
  }
  
  @Override
  protected ControlSet StateIndependentControlSet() {
    ControlSet control_set = super.StateIndependentControlSet(); 
    control_set.gpr_sr1_addr = base_r();
    control_set.addr1_mux_select = BitWord.TRUE;
    control_set.addr2_mux_select = BitWord.FromInt(1, 2);
    control_set.mar_mux_select = BitWord.FALSE;
    control_set.mdr_mux_select = BitWord.FALSE;
    return control_set;
  }
  
  private ControlSet EvaluateAddress1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.mar_mux_tri_enable = BitWord.TRUE;
    control_set.mar_load = BitWord.TRUE;
    return control_set;
  }
  
  private ControlSet FetchOperands1ControlSet() {
    return StateIndependentControlSet();
  }

  private ControlSet ExecuteOperation1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.mdr_load = BitWord.TRUE;
    return control_set;
  }
  
  private ControlSet StoreResult1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.memory_we = BitWord.TRUE;
    return control_set;
  }

  public BitWord base_r() {
    return bitword().GetBitRange(kBaseRHighBit, kBaseRLowBit);
  }
  
  public BitWord sr() {
    return bitword().GetBitRange(kSrHighBit, kSrLowBit);
  }
  
  public BitWord offset6() {
    return bitword().GetBitRange(kOffset6HighBit, kOffset6LowBit);
  }
  private final int kSrHighBit = 11;
  private final int kSrLowBit = 9;
  private final int kBaseRHighBit = 8;
  private final int kBaseRLowBit = 6;
  private final int kOffset6HighBit = 5;
  private final int kOffset6LowBit = 0;
}
