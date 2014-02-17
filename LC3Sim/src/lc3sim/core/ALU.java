package lc3sim.core;

import java.util.BitSet;

public class ALU extends BasicPropagator {
  // ALU Interface
  public ALU() {
    op_a_listener_callback_ = new ListenerCallback(this, kOpAId);
  }
  
  public ListenerCallback OpAListenerCallback() {
    
  }
  
  // Add treats operands as 2's complement and sign extends to 16-bit.
  public BitWord16 Add(BitWord op1, BitWord op2) {
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
  
  private ListenerCallback op_a_listener_callback_;
  private final String kOpAId = "op_a";
  private final int kBitWidth = 16;
}
