package lc3sim.core.instructions;

import lc3sim.core.BitWord;

public class StrInstruction extends Instruction {
  public StrInstruction(BitWord bitword) {
    super(bitword);
  }

  @Override
  public Boolean has_base_r() {
    return true;
  }

  @Override
  public BitWord base_r() {
    return bitword().GetBitRange(kBaseRHighBit, kBaseRLowBit);
  }
  
  @Override
  public Boolean has_sr() {
    return true;
  }
  
  @Override
  public BitWord sr() {
    return bitword().GetBitRange(kSrHighBit, kSrLowBit);
  }
  
  @Override
  public Boolean has_offset6() {
    return true;
  }
  
  @Override
  public BitWord offset6() {
    return bitword().GetBitRange(kOffset6HighBit, kOffset6LowBit);
  }
  private final int kSrHighBit = 11;
  private final int kSrLowBit = 9;
  private final int kBaseRHighBit = 8;
  private final int kBaseRLowBit = 6;
  private final int kOffset6HighBit = 5;
  private final int kOffset6LowBit = 0;
}
