package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.StateMachine.InstructionCycle;

public class AddInstruction extends Instruction {
  public AddInstruction(BitWord bitword) {
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
      case kStoreResult1:
        return StoreResult1ControlSet();
    }
    assert false;
    return null;
  }
  
  @Override
  protected ControlSet StateIndependentControlSet() {
    ControlSet control_set = super.StateIndependentControlSet();        
    control_set.gpr_sr1_addr = sr1();
    control_set.gpr_dr_addr = dr();
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
  public Boolean has_sr1() {
    return true;
  }
  
  @Override
  public BitWord sr1() {
    return bitword().GetBitRange(kSr1HighBit, kSr1LowBit);
  }
  
  @Override
  public Boolean has_sr2() {
    return !mode_bit();
  }
  
  @Override
  public BitWord sr2() {
    return bitword().GetBitRange(kSr2HighBit, kSr2LowBit);
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
  public Boolean has_imm5() {
    return mode_bit();
  }
  
  @Override
  public BitWord imm5() {
    return bitword().GetBitRange(kImm5HighBit, kImm5LowBit);
  }
  
  @Override
  public Boolean has_mode_bit() {
    return true;
  }
  
  @Override
  public Boolean mode_bit() {
    return bitword().TestBit(kModeBit);
  }
  
  private final int kDrHighBit = 11;
  private final int kDrLowBit = 9;
  private final int kSr1HighBit = 8;
  private final int kSr1LowBit = 6;
  private final int kSr2HighBit = 2;
  private final int kSr2LowBit = 0;
  private final int kImm5HighBit = 4;
  private final int kImm5LowBit = 0;
  private final int kModeBit = 5;
}