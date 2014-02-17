package lc3sim.util;

// Provides static helper methods for bit manipulation of 16-bit words.
public class BitManipulation {
  // Extracts the bit range data[bit_high:bit_low]. This is zero-padded from
  // most-significant bits to a 16-bit result.
  public static short ExtractRange(short data, int bit_high, int bit_low) {
    int mask = 0;
    for (int i = 0; i < 16; ++i) {
      mask = mask << 1;
      if (i <= bit_high && i >= bit_low) {
        mask |= 1;
      }
    }
    data &= mask;
    for (int i = 0; i < bit_low; ++i) {
      data = (short)(data >>> 1);
    }
    return data;
  }
  
  // Sign extends 'value', which has 'value_num_bits' to 16-bits.
  public static short SignExtend(short value, int value_num_bits) {
    int most_significant_bit = value >> (value_num_bits - 1);
    if (most_significant_bit == 0) {
      // Value is positive or zero. No extension necessary.
      return value;
    } else {
      // Value is negative. Create sign extension mask.
      short mask = Short.MIN_VALUE;
      mask = (short)(mask >> (16 - value_num_bits - 1));
      short ret = (short)(mask | value);
      assert ret < 0;
      return ret;
    }
  }
}
