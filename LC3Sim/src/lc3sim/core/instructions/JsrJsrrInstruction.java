package lc3sim.core.instructions;

import lc3sim.core.BitWord;

public class JsrJsrrInstruction extends Instruction {
  public JsrJsrrInstruction(BitWord bitword) {
    super(bitword);
  }

  @Override
  public Boolean has_base_r() {
    return !mode_bit();
  }

  @Override
  public BitWord base_r() {
    assert !mode_bit();
    return bitword().GetBitRange(kBaseRHighBit, kBaseRLowBit);
  }
  
  @Override
  public Boolean has_pcoffset11() {
    return mode_bit();
  }
  
  @Override
  public BitWord pcoffset11() {
    assert mode_bit();
    return bitword().GetBitRange(kPcOffset11HighBit, kPcOffset11LowBit);
  }

  @Override
  public Boolean has_mode_bit() {
    return true;
  }
  
  @Override
  public Boolean mode_bit() {
    return bitword().TestBit(kModeBit);
  }
  
  private final int kBaseRHighBit = 8;
  private final int kBaseRLowBit = 6;
  private final int kPcOffset11HighBit = 10;
  private final int kPcOffset11LowBit = 0;
  private final int kModeBit = 11;
}
