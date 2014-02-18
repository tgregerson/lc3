package lc3sim.core;

import java.util.BitSet;

public class ALU extends BasicPropagator {
  // ALU Interface
  public ALU() {
    super(ArchitecturalId.AluOut);
    Init();
  }
  
  public void Init() {
    op_a_ = new BitWord(16);
    op_b_ = new BitWord(16);
    op_code_ = new BitWord(4);
    current_output_ = new BitWord(16);
  }
  
  public void Notify(BitWord bit_word, Object arg, ArchitecturalId sender) {
    assert arg != null;
    assert arg instanceof String;
    if (sender == ArchitecturalId.Sr2MuxOut) {
      if (!op_b_.IsEqual(bit_word)) {
        op_b_ = bit_word;
        UpdateOutput();
      }
    } else if (sender == ArchitecturalId.GprSr1Out){
      if (!op_a_.IsEqual(bit_word)) {
        op_a_ = bit_word;
        UpdateOutput();
      }
    }
  }
  
  public void UpdateOutput() {
    // TODO
  }
  
  // Add treats operands as 2's complement and sign extends to 16-bit.
  private BitWord16 Add(BitWord op1, BitWord op2) {
    // Avoid issues with type conversion by doing bit-wise addition rather than
    // converting to intermediate int/long types.
    op1 = op1.Resize(16, true);
    op2 = op2.Resize(16, true);
    BitSet sum = new BitSet(kBitWidth);
    Boolean carry = false;
    for (int i = 0; i < kBitWidth; ++ i) {
      Boolean bit_sum = op1.TestBit(i) ^ op2.TestBit(i) ^ carry;
      sum.set(i, bit_sum);
      carry = (op1.TestBit(i) && op2.TestBit(i)) ||
              (op1.TestBit(i) && carry) ||
              (op2.TestBit(i) && carry);
    }
    return new BitWord16(sum, true);
  }
  
  private BitWord op_a_;
  private BitWord op_b_;
  private BitWord op_code_;
  private BitWord current_output_;
  private final int kBitWidth = 16;
}
