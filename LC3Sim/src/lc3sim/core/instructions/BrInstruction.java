package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.ProcessorStatusRegister;
import lc3sim.core.StateMachine.InstructionCycle;

public class BrInstruction extends Instruction {
  public BrInstruction(BitWord bitword) {
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
        return StoreResult1ControlSet(psr);
      default:
        // Unused
        assert false;
        return null;
    }
  }

  private ControlSet StoreResult1ControlSet(BitWord psr) {
    ControlSet control_set = StateIndependentControlSet();
    control_set.addr1_mux_select = BitWord.FALSE;
    control_set.addr2_mux_select = BitWord.FromInt(2, 2);
    control_set.pc_mux_select = BitWord.FromInt(1, 2);
    // Note: By convention, the case where none of the flags is used in the
    // instruction is considered to be an unconditional branch, rather than a
    // NOP.
    if ((psr.TestBit(ProcessorStatusRegister.kNBit) && n()) ||
        (psr.TestBit(ProcessorStatusRegister.kZBit) && z()) ||
        (psr.TestBit(ProcessorStatusRegister.kPBit) && p()) ||
        (!n() && !z() && !p())){
      control_set.pc_load = BitWord.TRUE;
    }
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
