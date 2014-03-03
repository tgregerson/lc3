package lc3sim.core.instructions;

import lc3sim.core.BitWord;

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

/*
public class Instruction {
  public static final int kNumBits = 16;

  public Instruction(BitWord bitword) {
    assert bitword.num_bits() == kNumBits;
    bitword_ = bitword;
    op_code_ = OpCode.Lookup(
        bitword_.GetBitRange(kOpCodeHighBit, kOpCodeLowBit));
    assert op_code_ != null;
  }
  
  // Below are convenience methods for accessing named fields from the LC3 ISA.
  // A given instruction only has access to some named fields, as specified by
  // the has_<field_name>() functions. Attempting to access a named field not
  // present in the instruction results in undefined behavior.
  public OpCode op_code() {
    return op_code_;
  }
  
  public Boolean has_sr1() {
    return op_code_ == OpCode.ADD ||
           op_code_ == OpCode.AND;
  }
  
  public BitWord sr1() {
    switch (op_code_) {
      case ADD:
        return bitword_.GetBitRange(kSr1HighBit, kSr1LowBit);
      case AND:
        return bitword_.GetBitRange(kSr1HighBit, kSr1LowBit);
      default:
        assert false;
        return null;
    }
  }
  
  public Boolean has_sr2() {
    return (op_code_ == OpCode.ADD && !mode_bit()) ||
           (op_code_ == OpCode.AND && !mode_bit());
  }
  
  public BitWord sr2() {
    switch (op_code_) {
      case ADD:
        assert !mode_bit();
        return bitword_.GetBitRange(kSr2HighBit, kSr2LowBit);
      case AND:
        assert !mode_bit();
        return bitword_.GetBitRange(kSr2HighBit, kSr2LowBit);
      default:
        assert false;
        return null;
    }
  }
  
  public Boolean has_sr() {
    return op_code_ == OpCode.NOT ||
           op_code_ == OpCode.ST  ||
           op_code_ == OpCode.STI ||
           op_code_ == OpCode.STR;
  }
  
  public BitWord sr() {
    switch (op_code_) {
      case NOT:
        // For NOT, SR is in the location normally SR1.
        return bitword_.GetBitRange(kSr1HighBit, kSrLowBit);
      case ST:
        return bitword_.GetBitRange(kSrHighBit, kSrLowBit);
      case STI:
        return bitword_.GetBitRange(kSrHighBit, kSrLowBit);
      case STR:
        return bitword_.GetBitRange(kSrHighBit, kSrLowBit);
      default:
        assert false;
        return null;
    }
  }
  
  public Boolean has_base_r() {
    return  op_code_ == OpCode.JMP_RET ||
           (op_code_ == OpCode.JSR_JSRR && !mode_bit()) ||
            op_code_ == OpCode.LDR ||
            op_code_ == OpCode.STR;
  }

  public BitWord base_r() {
    switch (op_code_) {
      case JMP_RET:
        return bitword_.GetBitRange(kBaseRHighBit, kBaseRLowBit);
      case JSR_JSRR:
        assert !mode_bit();
        return bitword_.GetBitRange(kBaseRHighBit, kBaseRLowBit);
      case LDR:
        return bitword_.GetBitRange(kBaseRHighBit, kBaseRLowBit);
      case STR:
        return bitword_.GetBitRange(kBaseRHighBit, kBaseRLowBit);
      default:
        assert false;
        return null;
    }
  }
  
  public Boolean has_dr() {
    return op_code_ == OpCode.ADD ||
           op_code_ == OpCode.AND ||
           op_code_ == OpCode.LD  ||
           op_code_ == OpCode.LDI ||
           op_code_ == OpCode.LDR ||
           op_code_ == OpCode.LEA ||
           op_code_ == OpCode.NOT;
  }
  
  public BitWord dr() {
    switch (op_code_) {
      case ADD:
        return bitword_.GetBitRange(kDrHighBit, kDrLowBit);
      case AND:
        return bitword_.GetBitRange(kDrHighBit, kDrLowBit);
      case LD:
        return bitword_.GetBitRange(kDrHighBit, kDrLowBit);
      case LDI:
        return bitword_.GetBitRange(kDrHighBit, kDrLowBit);
      case LDR:
        return bitword_.GetBitRange(kDrHighBit, kDrLowBit);
      case LEA:
        return bitword_.GetBitRange(kDrHighBit, kDrLowBit);
      case NOT:
        return bitword_.GetBitRange(kDrHighBit, kDrLowBit);
      default:
        assert false;
        return null;
    }
  }
  
  public Boolean has_imm() {
    return (op_code_ == OpCode.ADD && mode_bit()) ||
           (op_code_ == OpCode.AND && mode_bit()) ||
            op_code_ == OpCode.BR  ||
           (op_code_ == OpCode.JSR_JSRR && mode_bit()) ||
            op_code_ == OpCode.LD  ||
            op_code_ == OpCode.LDI ||
            op_code_ == OpCode.LDR ||
            op_code_ == OpCode.LEA ||
            op_code_ == OpCode.ST  ||
            op_code_ == OpCode.STI ||
            op_code_ == OpCode.STR ||
            op_code_ == OpCode.TRAP;
  }
  
  // Used for all immediate value fields.
  public BitWord imm() {
    switch (op_code_) {
      case ADD:
        assert mode_bit();
        return bitword_.GetBitRange(kImm5HighBit, kImm5LowBit);
      case AND:
        assert mode_bit();
        return bitword_.GetBitRange(kImm5HighBit, kImm5LowBit);
      case BR:
        return bitword_.GetBitRange(kPcOffset9HighBit, kPcOffset9LowBit);
      case JSR_JSRR:
        assert mode_bit();
        return bitword_.GetBitRange(kPcOffset11HighBit, kPcOffset11LowBit);
      case LD:
        return bitword_.GetBitRange(kPcOffset9HighBit, kPcOffset9LowBit);
      case LDI:
        return bitword_.GetBitRange(kPcOffset9HighBit, kPcOffset9LowBit);
      case LDR:
        return bitword_.GetBitRange(kOffset6HighBit, kOffset6LowBit);
      case LEA:
        return bitword_.GetBitRange(kPcOffset9HighBit, kPcOffset9LowBit);
      case ST:
        return bitword_.GetBitRange(kPcOffset9HighBit, kPcOffset9LowBit);
      case STI:
        return bitword_.GetBitRange(kPcOffset9HighBit, kPcOffset9LowBit);
      case STR:
        return bitword_.GetBitRange(kOffset6HighBit, kOffset6LowBit);
      case TRAP:
        return bitword_.GetBitRange(kTrapvect8HighBit, kTrapvect8LowBit);
      default:
        assert false;
        return null;
    }
  }
  
  public Boolean has_n() {
    return op_code_ == OpCode.BR;
  }
  
  public Boolean n() {
    assert op_code_ == OpCode.BR;
    return bitword_.TestBit(kBrNBit);
  }

  public Boolean has_z() {
    return op_code_ == OpCode.BR;
  }
  
  public Boolean z() {
    assert op_code_ == OpCode.BR;
    return bitword_.TestBit(kBrZBit);
  }

  public Boolean has_p() {
    return op_code_ == OpCode.BR;
  }
  
  public Boolean p() {
    assert op_code_ == OpCode.BR;
    return bitword_.TestBit(kBrPBit);
  }

  public Boolean has_mode_bit() {
    return op_code_ == OpCode.AND ||
           op_code_ == OpCode.ADD ||
           op_code_ == OpCode.JSR_JSRR;
  }
  
  public Boolean mode_bit() {
    switch (op_code_) {
      case ADD:
        return bitword_.TestBit(kAddAndModeBit);
      case AND:
        return bitword_.TestBit(kAddAndModeBit);
      case JSR_JSRR:
        return bitword_.TestBit(kJsrJsrrModeBit);
      default:
        assert false;
        return false;
    }
  }

  private final BitWord bitword_;
  private final OpCode op_code_;

  // Op Code: Valid for all instructions.
  private final int kOpCodeHighBit = 15;
  private final int kOpCodeLowBit = 12;
  
  // Dr: Valid for ADD, AND, LD, LDI, LDR, LEA, NOT
  private final int kDrHighBit = 11;
  private final int kDrLowBit = 9;
  
  // Sr: Valid for NOT, ST, STI, STR
  // NOTE: For NOT, Sr uses the bitrange of Sr1, unlike all other instructions
  // using Sr.
  private final int kSrHighBit = 11;
  private final int kSrLowBit = 9;
  
  // Sr1: Valid for ADD, AND
  private final int kSr1HighBit = 8;
  private final int kSr1LowBit = 6;
  
  // Sr2: Valid for ADD, AND (when mode = 0)
  private final int kSr2HighBit = 2;
  private final int kSr2LowBit = 0;

  // BaseR: Valid for JMP, JSRR, LDR, STR
  private final int kBaseRHighBit = 8;
  private final int kBaseRLowBit = 6;
  
  // Imm5: Valid for ADD, AND (when mode = 1)
  private final int kImm5HighBit = 4;
  private final int kImm5LowBit = 0;

  // PCoffset9: Valid for BR, LD, LDI, LEA, ST, STI
  private final int kPcOffset9HighBit = 8;
  private final int kPcOffset9LowBit = 0;

  // PCoffset11: Valid for JSR
  private final int kPcOffset11HighBit = 10;
  private final int kPcOffset11LowBit = 0;

  // Offset6: Valid for LDR, STR
  private final int kOffset6HighBit = 5;
  private final int kOffset6LowBit = 0;

  // Trapvect8: Valid for TRAP
  private final int kTrapvect8HighBit = 7;
  private final int kTrapvect8LowBit = 0;
  
  // Flag bits for BR
  private final int kBrNBit = 11;
  private final int kBrZBit = 10;
  private final int kBrPBit = 9;
  
  // Mode bits to differentiate between versions of ADD, AND, and JSR/JSRR
  private final int kAddAndModeBit = 5;
  private final int kJsrJsrrModeBit = 11;
}*/