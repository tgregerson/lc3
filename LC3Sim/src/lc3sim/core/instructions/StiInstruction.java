package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.StateMachine.InstructionCycle;

public class StiInstruction extends Instruction {
  public StiInstruction(BitWord bitword) {
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
        return EvaluateAddress1ControlSet();
      case kFetchOperands1:
        return FetchOperands1ControlSet();
      case kExecuteOperation1:
        return ExecuteOperation1ControlSet();
      case kExecuteOperation2:
        return ExecuteOperation2ControlSet();
    }
    assert false;
    return null;
  }
  
  private ControlSet EvaluateAddress1ControlSet() {
    ControlSet control_set = new ControlSet();
    control_set.addr1_mux_select = BitWord.FALSE;
    control_set.addr2_mux_select = BitWord.FromInt(2).Resize(2, false);
    control_set.mar_mux_select = BitWord.FALSE;
    control_set.mar_mux_tri_enable = BitWord.TRUE;
    control_set.mar_load = BitWord.TRUE;
    return control_set;
  }
  
  private ControlSet FetchOperands1ControlSet() {
    ControlSet control_set = new ControlSet();
    control_set.gpr_sr1_addr = sr();
    control_set.mdr_mux_select = BitWord.TRUE;
    control_set.mdr_load = BitWord.TRUE;
    return control_set;
  }

  private ControlSet ExecuteOperation1ControlSet() {
    ControlSet control_set = new ControlSet();
    control_set.gpr_sr1_addr = sr();
    control_set.mdr_tri_enable = BitWord.TRUE;
    control_set.mar_load = BitWord.TRUE;
    return control_set;
  }

  private ControlSet ExecuteOperation2ControlSet() {
    ControlSet control_set = new ControlSet();
    control_set.gpr_sr1_addr = sr();
    control_set.mdr_mux_select = BitWord.FALSE;
    control_set.mdr_load = BitWord.TRUE;
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
  public Boolean has_pcoffset9() {
    return true;
  }
  
  @Override
  public BitWord pcoffset9() {
    return bitword().GetBitRange(kPcOffset9HighBit, kPcOffset9LowBit);
  }

  private final int kSrHighBit = 11;
  private final int kSrLowBit = 9;
  private final int kPcOffset9HighBit = 8;
  private final int kPcOffset9LowBit = 0;
}
