package lc3sim.core;

import java.util.BitSet;

// A 16-bit version of BitWord. For use where static type-checking is desired.
public class BitWord16 extends BitWord {
  public BitWord16() {
    super(16);
  }
  
  public BitWord16(BitSet bit_set, Boolean sign_extend) {
    super(bit_set);
    Resize(16, sign_extend);
  }
  
  public BitWord16(BitWord bit_word) {
    super(bit_word);
    Resize(16, false);
  }
  
  public BitWord16(BitWord bit_word, Boolean sign_extend) {
    super(bit_word);
    Resize(16, sign_extend);
  }
}
