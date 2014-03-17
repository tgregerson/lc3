package lc3sim.test.core.instructions;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import lc3sim.core.*;
import lc3sim.core.instructions.*;

public class InstructionTestUtil {
  public static Instruction RandomInstruction() {
    HashSet<OpCode> empty_blacklist = new HashSet<OpCode>();
    return RandomInstructionWithBlacklist(empty_blacklist);
  }

  public static Instruction RandomLegalInstruction() {
    HashSet<OpCode> illegal_blacklist = new HashSet<OpCode>();
    illegal_blacklist.add(OpCode.RESERVED);
    return RandomInstructionWithBlacklist(illegal_blacklist);
  }

  public static Instruction RandomInstructionWithBlacklist(
      Set<OpCode> blacklist) {
    // Avoid possible infinite loop.
    assert blacklist.size() < OpCode.values().length;
    OpCode op_code;
    do {
      op_code = OpCode.Lookup(RandomImm(OpCode.kNumBits));
    } while (blacklist.contains(op_code));
    return RandomInstructionFromOpCode(op_code);
  }

  public static Instruction RandomInstructionWithWhitelist(
      Set<OpCode> whitelist) {
    // Avoid possible infinite loop.
    assert whitelist.size() > 0;
    OpCode op_code;
    do {
      op_code = OpCode.Lookup(RandomImm(OpCode.kNumBits));
    } while (!whitelist.contains(op_code));
    return RandomInstructionFromOpCode(op_code);
  }
  
  public static Instruction RandomInstructionFromOpCode(OpCode op_code) {
    switch (op_code) {
      case ADD: return RandomAddInstruction();
      case AND: return RandomAndInstruction();
      case BR: return RandomBrInstruction();
      case JMP_RET: return RandomJmpRetInstruction();
      case JSR_JSRR: return RandomJsrJsrrInstruction();
      case LD: return RandomLdInstruction();
      case LDI: return RandomLdiInstruction();
      case LDR: return RandomLdrInstruction();
      case LEA: return RandomLeaInstruction();
      case NOT: return RandomNotInstruction();
      case RESERVED:
        assert false : op_code;
        return null;
      case RTI: return RandomRtiInstruction();
      case ST: return RandomStInstruction();
      case STI: return RandomStiInstruction();
      case STR: return RandomStrInstruction();
      case TRAP: return RandomTrapInstruction();
      default:
        assert false : op_code;
        return null;
    }
  }
  
  public static AddInstruction RandomAddInstruction() {
    if (random_.coin_flip()) {
      return RandomAddRegRegInstruction();
    } else {
      return RandomAddRegImmInstruction();
    }
  }

  public static AddInstruction RandomAddRegRegInstruction() {
    BitWord op_code = OpCode.ADD.as_BitWord();
    BitWord dr = RandomReg();
    BitWord sr1 = RandomReg();
    BitWord mode_bit = BitWord.FALSE;
    BitWord fill_bits = BitWord.Zeroes(2);
    BitWord sr2 = RandomReg();
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{sr2, fill_bits, mode_bit, sr1, dr, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof AddInstruction : instruction_bits;
    return (AddInstruction)instruction;
  }

  public static AddInstruction RandomAddRegImmInstruction() {
    BitWord op_code = OpCode.ADD.as_BitWord();
    BitWord dr = RandomReg();
    BitWord sr1 = RandomReg();
    BitWord mode_bit = BitWord.TRUE;
    BitWord imm5 = RandomImm(5);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{imm5, mode_bit, sr1, dr, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof AddInstruction : instruction_bits;
    return (AddInstruction)instruction;
  }

  public static AndInstruction RandomAndInstruction() {
    if (random_.coin_flip()) {
      return RandomAndRegRegInstruction();
    } else {
      return RandomAndRegImmInstruction();
    }
  }

  public static AndInstruction RandomAndRegRegInstruction() {
    BitWord op_code = OpCode.AND.as_BitWord();
    BitWord dr = RandomReg();
    BitWord sr1 = RandomReg();
    BitWord mode_bit = BitWord.FALSE;
    BitWord fill_bits = BitWord.Zeroes(2);
    BitWord sr2 = RandomReg();
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{sr2, fill_bits, mode_bit, sr1, dr, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof AndInstruction : instruction_bits;
    return (AndInstruction)instruction;
  }

  public static AndInstruction RandomAndRegImmInstruction() {
    BitWord op_code = OpCode.AND.as_BitWord();
    BitWord dr = RandomReg();
    BitWord sr1 = RandomReg();
    BitWord mode_bit = BitWord.TRUE;
    BitWord imm5 = RandomImm(5);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{imm5, mode_bit, sr1, dr, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof AndInstruction : instruction_bits;
    return (AndInstruction)instruction;
  }

  public static BrInstruction RandomBrInstruction() {
    BitWord op_code = OpCode.BR.as_BitWord();
    BitWord flags = random_.SetOneOf(3);
    BitWord pc_offset9 = RandomImm(9);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{pc_offset9, flags, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof BrInstruction : instruction_bits;
    return (BrInstruction)instruction;
  }

  public static JmpRetInstruction RandomJmpRetInstruction() {
    BitWord op_code = OpCode.JMP_RET.as_BitWord();
    BitWord fill_bits_h = BitWord.Zeroes(3);
    BitWord base_r = RandomReg();
    BitWord fill_bits_l = BitWord.Zeroes(6);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{fill_bits_l, base_r, fill_bits_h, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof JmpRetInstruction : instruction_bits;
    return (JmpRetInstruction)instruction;
  }

  public static JsrJsrrInstruction RandomJsrJsrrInstruction() {
    if (random_.coin_flip()) {
      return RandomJsrInstruction();
    } else {
      return RandomJsrrInstruction();
    }
  }

  public static JsrJsrrInstruction RandomJsrInstruction() {
    BitWord op_code = OpCode.JSR_JSRR.as_BitWord();
    BitWord mode_bit = BitWord.TRUE;
    BitWord pc_offset11 = RandomImm(11);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{pc_offset11, mode_bit, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof JsrJsrrInstruction : instruction_bits;
    return (JsrJsrrInstruction)instruction;
  }

  public static JsrJsrrInstruction RandomJsrrInstruction() {
    BitWord op_code = OpCode.JSR_JSRR.as_BitWord();
    BitWord mode_bit = BitWord.FALSE;
    BitWord fill_bits_h = BitWord.Zeroes(2);
    BitWord base_r = RandomReg();
    BitWord fill_bits_l = BitWord.Zeroes(6);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{fill_bits_l, base_r, fill_bits_h, mode_bit, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof JsrJsrrInstruction : instruction_bits;
    return (JsrJsrrInstruction)instruction;
  }

  public static LdInstruction RandomLdInstruction() {
    BitWord op_code = OpCode.LD.as_BitWord();
    BitWord dr = RandomReg();
    BitWord pc_offset9 = RandomImm(9);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{pc_offset9, dr, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof LdInstruction : instruction_bits;
    return (LdInstruction)instruction;
  }

  public static LdiInstruction RandomLdiInstruction() {
    BitWord op_code = OpCode.LDI.as_BitWord();
    BitWord dr = RandomReg();
    BitWord pc_offset9 = RandomImm(9);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{pc_offset9, dr, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof LdiInstruction : instruction_bits;
    return (LdiInstruction)instruction;
  }

  public static LdrInstruction RandomLdrInstruction() {
    BitWord op_code = OpCode.LDR.as_BitWord();
    BitWord dr = RandomReg();
    BitWord base_r = RandomReg();
    BitWord offset6 = RandomImm(6);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{offset6, base_r, dr, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof LdrInstruction : instruction_bits;
    return (LdrInstruction)instruction;
  }

  public static LeaInstruction RandomLeaInstruction() {
    BitWord op_code = OpCode.LEA.as_BitWord();
    BitWord dr = RandomReg();
    BitWord pc_offset9 = RandomImm(9);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{pc_offset9, dr, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof LeaInstruction : instruction_bits;
    return (LeaInstruction)instruction;
  }

  public static NotInstruction RandomNotInstruction() {
    BitWord op_code = OpCode.NOT.as_BitWord();
    BitWord dr = RandomReg();
    BitWord sr = RandomReg();
    BitWord fill_bits = BitWord.FromInt(63, 6);  // Six ones.
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{fill_bits, sr, dr, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof NotInstruction : instruction_bits;
    return (NotInstruction)instruction;
  }

  public static RtiInstruction RandomRtiInstruction() {
    // Actually nothing random about it.
    BitWord op_code = OpCode.RTI.as_BitWord();
    BitWord fill_bits = BitWord.Zeroes(12);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{fill_bits, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof RtiInstruction : instruction_bits;
    return (RtiInstruction)instruction;
  }

  public static StInstruction RandomStInstruction() {
    BitWord op_code = OpCode.ST.as_BitWord();
    BitWord sr = RandomReg();
    BitWord pc_offset9 = RandomImm(9);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{pc_offset9, sr, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof StInstruction : instruction_bits;
    return (StInstruction)instruction;
  }

  public static StiInstruction RandomStiInstruction() {
    BitWord op_code = OpCode.STI.as_BitWord();
    BitWord sr = RandomReg();
    BitWord pc_offset9 = RandomImm(9);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{pc_offset9, sr, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof StiInstruction : instruction_bits;
    return (StiInstruction)instruction;
  }

  public static StrInstruction RandomStrInstruction() {
    BitWord op_code = OpCode.STR.as_BitWord();
    BitWord sr = RandomReg();
    BitWord base_r = RandomReg();
    BitWord offset6 = RandomImm(6);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{offset6, base_r, sr, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof StrInstruction : instruction_bits;
    return (StrInstruction)instruction;
  }

  public static TrapInstruction RandomTrapInstruction() {
    // Actually nothing random about it.
    BitWord op_code = OpCode.TRAP.as_BitWord();
    BitWord fill_bits = BitWord.Zeroes(4);
    BitWord trapvect8 = RandomImm(8);
    BitWord instruction_bits = BitWord.Concatenate(
        new BitWord[]{trapvect8, fill_bits, op_code});
    Instruction instruction = Instruction.FromBitWord(instruction_bits);
    assert instruction instanceof TrapInstruction : instruction_bits;
    return (TrapInstruction)instruction;
  }
  
  private static BitWord RandomReg() {
    return random_.random_bitword(kNumRegBits);
  }

  private static BitWord RandomImm(int num_bits) {
    return random_.random_bitword(num_bits);
  }
  
  
  private static class RandomBits extends Random {
    public BitWord random_bitword(int num_bits) {
      int value = next(num_bits);
      return BitWord.FromInt(value, num_bits);
    }
    
    public boolean coin_flip() {
      return nextBoolean();
    }

    public BitWord SetOneOf(int num_bits) {
      BitWord zeroes = BitWord.Zeroes(num_bits);
      return zeroes.SetBit(nextInt(num_bits), true);
    }

    static final long serialVersionUID = 0;
  }
  
  private static final RandomBits random_ = new RandomBits();
  private static final int kNumRegBits = RegisterFile.kNumAddrBits;

}
