package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.StateMachine.InstructionCycle;;

public abstract class Instruction {
  public static final int kNumBits = 16;

  protected Instruction(BitWord bitword) {
    assert bitword.num_bits() == kNumBits;
    bitword_ = bitword;
    op_code_ = OpCodeFromBitWord(bitword);
    assert op_code_ != null;
  }
  
  public OpCode op_code() {
    return op_code_;
  }
  
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
        assert false;
        return null;
    }
  }
  
  public abstract ControlSet ControlSet(InstructionCycle cycle);
  
  // FetchInstruction1, FetchInstruction2, and DecodeInstruction1 are the same
  // for all instructions, so they are provided here.
  protected ControlSet FetchInstruction1ControlSet() {
    ControlSet control_set = new ControlSet();
    control_set.pc_load = BitWord.TRUE;
    control_set.pc_tri_enable = BitWord.TRUE;
    control_set.pc_mux_select = BitWord.FromInt(0).Resize(2, false);
    control_set.mar_load = BitWord.TRUE;
    return control_set;
  }

  protected ControlSet FetchInstruction2ControlSet() {
    ControlSet control_set = new ControlSet();
    control_set.mdr_mux_select = BitWord.TRUE;
    control_set.mdr_load = BitWord.TRUE;
    return control_set;
  }
  
  protected ControlSet DecodeInstruction1ControlSet() {
    ControlSet control_set = new ControlSet();
    control_set.mdr_tri_enable = BitWord.TRUE;
    control_set.ir_load = BitWord.TRUE;
    return control_set;
  }
  
  // Below are convenience methods for accessing named fields from the LC3 ISA.
  // A given instruction only has access to some named fields. If a child
  // instruction class has the field, it should override the corresponding
  // methods in its implementation.
  public Boolean has_sr1() {
    return false;
  }
  
  public BitWord sr1() {
    assert false;
    return null;
  }
  
  public Boolean has_sr2() {
    return false;
  }
  
  public BitWord sr2() {
    assert false;
    return null;
  }
  
  public Boolean has_sr() {
    return false;
  }
  
  public BitWord sr() {
    assert false;
    return null;
  }
  
  public Boolean has_base_r() {
    return false;
  }

  public BitWord base_r() {
    assert false;
    return null;
  }
  
  public Boolean has_dr() {
    return false;
  }
  
  public BitWord dr() {
    assert false;
    return null;
  }
  
  public Boolean has_imm5() {
    return false;
  }
  
  public BitWord imm5() {
    assert false;
    return null;
  }
  
  public Boolean has_pcoffset9() {
    return false;
  }
  
  public BitWord pcoffset9() {
    assert false;
    return null;
  }

  public Boolean has_pcoffset11() {
    return false;
  }
  
  public BitWord pcoffset11() {
    assert false;
    return null;
  }

  public Boolean has_offset6() {
    return false;
  }
  
  public BitWord offset6() {
    assert false;
    return null;
  }

  public Boolean has_trapvect8() {
    return false;
  }
  
  public BitWord trapvect8() {
    assert false;
    return null;
  }
  
  public Boolean has_n() {
    return false;
  }
  
  public Boolean n() {
    assert false;
    return false;
  }

  public Boolean has_z() {
    return false;
  }
  
  public Boolean z() {
    assert false;
    return false;
  }

  public Boolean has_p() {
    return false;
  }
  
  public Boolean p() {
    assert false;
    return false;
  }

  public Boolean has_mode_bit() {
    return false;
  }
  
  public Boolean mode_bit() {
    assert false;
    return null;
  }
  
  protected BitWord bitword() {
    return bitword_;
  }
  
  private static OpCode OpCodeFromBitWord(BitWord bitword) {
    return OpCode.Lookup(
        bitword.GetBitRange(kOpCodeHighBit, kOpCodeLowBit));
  }

  private final BitWord bitword_;
  private final OpCode op_code_;

  private static final int kOpCodeHighBit = 15;
  private static final int kOpCodeLowBit = 12;
  
}