package lc3sim.core;

// Provides a static method for sign extension to 16 bits.
public class SignExtender extends BasicPropagator {
  
  Notify(BitWord bit_word, Object arg, ArchitecturalId sender) {
    
  }
  
  // Sign extends 'in_value', which has 'in_bits' to 16-bits.
  public static short SignExtend16(short in_value, int in_bits) {
    int most_significant_bit = in_value >> (in_bits - 1);
    if (most_significant_bit == 0) {
      // Value is positive or zero. No extension necessary.
      return in_value;
    } else {
      // Value is negative. Create sign extension mask.
      short mask = Short.MIN_VALUE;
      mask = (short)(mask >> (16 - in_bits - 1));
      short ret = (short)(mask | in_value);
      assert ret < 0;
      return ret;
    }
  }

}
