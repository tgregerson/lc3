package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.StateMachine.InstructionCycle;

public class BrInstruction extends Instruction {
  public BrInstruction(BitWord bitword) {
    super(bitword);
  }

  @Override
  public ControlSet ControlSet(InstructionCycle cycle) {
    ControlSet control_set = new ControlSet();
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
        return control_set;
      case kFetchOperands1:
        // Unused
        assert false;
        return control_set;
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

  private ControlSet ExecuteOperation1ControlSet() {
    ControlSet control_set = new ControlSet();
    control_set.addr1_mux_select = BitWord.FALSE;
    control_set.addr2_mux_select = BitWord.FromInt(2).Resize(2, false);
    control_set.pc_mux_select = BitWord.FromInt(1).Resize(2, false);
    return control_set;
  }

  @Override
  public Boolean has_pcoffset9() {
    return true;
  }
  
  @Override
  public BitWord pcoffset9() {
    return bitword().GetBitRange(kPcOffset9HighBit, kPcOffset9LowBit);
  }
  
  @Override
  public Boolean has_n() {
    return true;
  }
  
  @Override
  public Boolean n() {
    return bitword().TestBit(kBrNBit);
  }

  @Override
  public Boolean has_z() {
    return true;
  }
  
  @Override
  public Boolean z() {
    return bitword().TestBit(kBrZBit);
  }

  @Override
  public Boolean has_p() {
    return true;
  }
  
  @Override
  public Boolean p() {
    return bitword().TestBit(kBrPBit);
  }

  private final int kPcOffset9HighBit = 8;
  private final int kPcOffset9LowBit = 0;
  private final int kBrNBit = 11;
  private final int kBrZBit = 10;
  private final int kBrPBit = 9;
}
