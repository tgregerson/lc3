package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.StateMachine.InstructionCycle;

public class JsrJsrrInstruction extends Instruction {
  public JsrJsrrInstruction(BitWord bitword) {
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
    if (has_base_r()) {
      control_set.gpr_sr1_addr = base_r();
    }
    return control_set;
  }

  private ControlSet ExecuteOperation1ControlSet() {
    ControlSet control_set = FetchOperands1ControlSet();
    if (mode_bit()) {
      // PC + offset
      control_set.addr1_mux_select = BitWord.FALSE;
      control_set.addr2_mux_select = BitWord.FromInt(3).Resize(2, false);
    } else {
      // Base register
      control_set.addr1_mux_select = BitWord.TRUE;
      control_set.addr2_mux_select = BitWord.FromInt(0).Resize(2, false);
    }
    control_set.pc_mux_select = BitWord.FromInt(1).Resize(2, false);
    return control_set;
  }

  @Override
  public Boolean has_base_r() {
    return !mode_bit();
  }

  @Override
  public BitWord base_r() {
    assert !mode_bit();
    return bitword().GetBitRange(kBaseRHighBit, kBaseRLowBit);
  }
  
  @Override
  public Boolean has_pcoffset11() {
    return mode_bit();
  }
  
  @Override
  public BitWord pcoffset11() {
    assert mode_bit();
    return bitword().GetBitRange(kPcOffset11HighBit, kPcOffset11LowBit);
  }

  @Override
  public Boolean has_mode_bit() {
    return true;
  }
  
  @Override
  public Boolean mode_bit() {
    return bitword().TestBit(kModeBit);
  }
  
  private final int kBaseRHighBit = 8;
  private final int kBaseRLowBit = 6;
  private final int kPcOffset11HighBit = 10;
  private final int kPcOffset11LowBit = 0;
  private final int kModeBit = 11;
}
