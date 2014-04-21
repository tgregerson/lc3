package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.InstructionCycle;;

// Instructions are an abstraction for state machine logic based on the
// contents of the instruction register. Their purpose is to simplify and
// modularize the logic for determining the next state and the output of the
// control signals.
//
// Children of Instruction must implement methods for determining the next state
// based on the current state and for generating a control set based on the
// current state.
public abstract class Instruction {
  public static final int kNumBits = 16;

  // Public methods
  // Factory method to create a child instruction from a 16-bit BitWord.
  public static Instruction FromBitWord(BitWord bitword) {
    OpCode op_code = OpCodeFromBitWord(bitword);
    switch (op_code) {
      case ADD:      return new AddInstruction(bitword);
      case AND:      return new AndInstruction(bitword);
      case BR:       return new BrInstruction(bitword);
      case JMP_RET:  return new JmpRetInstruction(bitword);
      case JSR_JSRR: return new JsrJsrrInstruction(bitword);
      case LD:       return new LdInstruction(bitword);
      case LDI:      return new LdiInstruction(bitword);
      case LDR:      return new LdrInstruction(bitword);
      case LEA:      return new LeaInstruction(bitword);
      case NOT:      return new NotInstruction(bitword);
      case RTI:      return new RtiInstruction(bitword);
      case ST:       return new StInstruction(bitword);
      case STI:      return new StiInstruction(bitword);
      case STR:      return new StrInstruction(bitword);
      case TRAP:     return new TrapInstruction(bitword);
      default:
        assert false : op_code;
        return null;
    }
  }

  public BitWord bitword() {
    return bitword_;
  }

  public OpCode op_code() {
    return op_code_;
  }

  public static OpCode OpCodeFromBitWord(BitWord bitword) {
    assert bitword.num_bits() == kNumBits : bitword.num_bits();
    return OpCode.Lookup(
        bitword.GetBitRange(kOpCodeHighBit, kOpCodeLowBit));
  }
  
  public abstract String toString();

  public abstract InstructionCycle NextCycle(
      InstructionCycle current_cycle);

  // Children should override to handle non-fetch states.
  public ControlSet ControlSet(InstructionCycle cycle, BitWord psr) {
    switch (cycle) {
      case kFetchInstruction1:
        return FetchInstruction1ControlSet();
      case kFetchInstruction2:
        return FetchInstruction2ControlSet();
      case kFetchInstruction3:
        return FetchInstruction3ControlSet();
      default:
        // Unused
        assert false;
        return null;
    }
  }

  protected Instruction(BitWord bitword) {
    assert bitword.num_bits() == kNumBits;
    bitword_ = bitword;
    op_code_ = OpCodeFromBitWord(bitword);
    assert op_code_ != null;
  }
  
  // Sets control values that can be safely assigned based on IR bits regardless
  // instruction or state context.
  protected ControlSet StateIndependentControlSet() {
    ControlSet control_set = new ControlSet();
    control_set.sr2_mux_select = bitword_.GetBitRange(kSr2ModeBit, kSr2ModeBit);
    control_set.alu_k = bitword_.GetBitRange(kAluKHighBit, kAluKLowBit);
    return control_set;
  }
  
  // FetchInstruction1, FetchInstruction2, and DecodeInstruction1 are the same
  // for all instructions, so they are provided here.
  protected ControlSet FetchInstruction1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.pc_load = BitWord.TRUE;
    control_set.pc_tri_enable = BitWord.TRUE;
    control_set.pc_mux_select = BitWord.FromInt(0, 2);
    control_set.mar_load = BitWord.TRUE;
    return control_set;
  }

  protected ControlSet FetchInstruction2ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.mdr_mux_select = BitWord.TRUE;
    control_set.mdr_load = BitWord.TRUE;
    return control_set;
  }
  
  protected ControlSet FetchInstruction3ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.mdr_tri_enable = BitWord.TRUE;
    control_set.ir_load = BitWord.TRUE;
    return control_set;
  }
  
  private final BitWord bitword_;
  private final OpCode op_code_;

  private static final int kOpCodeHighBit = 15;
  private static final int kOpCodeLowBit = 12;
  private static final int kSr2ModeBit = 5;
  private static final int kAluKHighBit = 15;
  private static final int kAluKLowBit = 14;
}