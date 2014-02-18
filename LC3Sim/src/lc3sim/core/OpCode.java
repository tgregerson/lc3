package lc3sim.core;

public enum OpCode {
  kADD(1),
  kAND(3),
  kBR(0),
  kJMP_RET(12),
  kJSR_JSRR(4),
  kLD(2),
  kLDI(10),
  kLDR(6),
  kLEA(14),
  kNOT(9),
  kRTI(8),
  kST(3),
  kSTI(11),
  kSTR(7),
  kTRAP(15),
  kRESERVED(13);
  
  private OpCode(int code) {
    code_as_int_ = code;
    code_as_bit_word_ = BitWord.FromInt(code).Resize(kOpCodeLength, false);
  }
  
  public int as_int() {
    return code_as_int_;
  }
  
  public BitWord as_BitWord() {
    return code_as_bit_word_;
  }
  
  private int code_as_int_;
  private BitWord code_as_bit_word_;
  private final int kOpCodeLength = 4;
}
