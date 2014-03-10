package lc3sim.core.instructions;

import lc3sim.core.BitWord;

public enum OpCode {
  ADD(1),
  AND(3),
  BR(0),
  JMP_RET(12),
  JSR_JSRR(4),
  LD(2),
  LDI(10),
  LDR(6),
  LEA(14),
  NOT(9),
  RTI(8),
  ST(3),
  STI(11),
  STR(7),
  TRAP(15),
  RESERVED(13);
  
  private OpCode(int code) {
    code_as_int_ = code;
    code_as_bit_word_ = BitWord.FromInt(code, kOpCodeLength);
  }
  
  public int as_int() {
    return code_as_int_;
  }
  
  public BitWord as_BitWord() {
    return code_as_bit_word_;
  }
  
  public static OpCode Lookup(int code) {
    for (OpCode op_code : OpCode.values()) {
      if (code == op_code.as_int()) {
        return op_code;
      }
    }
    return null;
  }
  
  public static OpCode Lookup(BitWord code) {
    for (OpCode op_code : OpCode.values()) {
      if (code.IsEqual(op_code.as_BitWord(), false)) {
        return op_code;
      }
    }
    return null;
  }
  
  private final int code_as_int_;
  private final BitWord code_as_bit_word_;
  private final int kOpCodeLength = 4;
}
