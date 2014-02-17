package lc3sim.core;

import lc3sim.util.BitManipulation;

// A 16-bit register.
public class Register16 extends BasicPropagator {
  public Register16() {
    Init();
  }
  
  public void Init() {
    data_ = 0;
  }
  
  public short Read() {
    return data_;
  }
  
  // Extracts the bit range [bit_high:bit_low], and pads with zeroes from the
  // most-significant bits.
  public short ReadBitRange(int bit_high, int bit_low) {
    return BitManipulation.ExtractRange(data_, bit_high, bit_low);
  }
  
  public void Write(short data) {
    data_ = data;
  }
  
  private short data_;
}
