package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.StateMachine.InstructionCycle;

public class NotInstruction extends Instruction {
  public NotInstruction(BitWord bitword) {
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
  
  private ControlSet FetchOperands1ControlSet() {
    ControlSet control_set = new ControlSet();
    control_set.gpr_dr_addr = dr();
    control_set.gpr_sr1_addr = sr();
    return control_set;
  }

  private ControlSet ExecuteOperation1ControlSet() {
    ControlSet control_set = FetchOperands1ControlSet();
    // TODO Set ALU Mode
    return control_set;
  }

  @Override
  public Boolean has_sr() {
    return true;
  }
  
  @Override
  public BitWord sr() {
    return bitword().GetBitRange(kSrHighBit, kSrLowBit);
  }
  
  @Override
  public Boolean has_dr() {
    return true;
  }
  
  @Override
  public BitWord dr() {
    return bitword().GetBitRange(kDrHighBit, kDrLowBit);
  }
  private final int kDrHighBit = 11;
  private final int kDrLowBit = 9;
  private final int kSrHighBit = 8;
  private final int kSrLowBit = 6;
}
