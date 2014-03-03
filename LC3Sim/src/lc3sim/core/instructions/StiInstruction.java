package lc3sim.core.instructions;

import lc3sim.core.BitWord;

public class StiInstruction extends Instruction {
  public StiInstruction(BitWord bitword) {
    super(bitword);
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
  public Boolean has_pcoffset9() {
    return true;
  }
  
  @Override
  public BitWord pcoffset9() {
    return bitword().GetBitRange(kPcOffset9HighBit, kPcOffset9LowBit);
  }

  private final int kSrHighBit = 11;
  private final int kSrLowBit = 9;
  private final int kPcOffset9HighBit = 8;
  private final int kPcOffset9LowBit = 0;
}
