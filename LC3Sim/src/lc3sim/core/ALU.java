package lc3sim.core;

import java.util.BitSet;

public class ALU extends AbstractPropagator {
  // ALU Interface
  public ALU() {
    Init();
  }
  
  public void Init() {
    op_a_ = new BitWord(16);
    op_b_ = new BitWord(16);
    op_code_ = new BitWord(4);
    SetCurrentOutput(out_id_, new BitWord(16));
  }
  
  @Override
  public void Notify(BitWord bit_word, OutputId sender,
                     InputId receiver, Object arg) {
    if (sender == OutputId.Sr2Mux) {
      if (!op_b_.IsEqual(bit_word)) {
        op_b_ = bit_word;
        UpdateOutput(out_id_);
      }
    } else if (sender == OutputId.GprSr1){
      if (!op_a_.IsEqual(bit_word)) {
        op_a_ = bit_word;
        UpdateOutput(out_id_);
      }
    }
  }
  
  @Override
  protected BitWord ComputeOutput(OutputId unused) {
    if (op_code_.IsEqual(OpCode.kADD.as_BitWord())) {
      return Add(op_a_, op_b_);
    } else {
      return null; // TODO add other ALU functions.
    }
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
  
  // Buffered inputs
  private BitWord op_a_;
  private BitWord op_b_;
  private BitWord op_code_;

  private final int kBitWidth = 16;
  private final OutputId out_id_ = OutputId.Alu;
}
