package lc3sim.core.instructions;

import lc3sim.core.BitWord;

public class BrInstruction extends Instruction {
  public BrInstruction(BitWord bitword) {
    super(bitword);
  }

  @Override
  public Boolean has_pcoffset9() {
    return true;
  }
  
  @Override
  public BitWord pcoffset9() {
    return bitword().GetBitRange(kPcOffset9HighBit, kPcOffset9LowBit);
  }
  
  @Override
  public Boolean has_n() {
    return true;
  }
  
  @Override
  public Boolean n() {
    return bitword().TestBit(kBrNBit);
  }

  @Override
  public Boolean has_z() {
    return true;
  }
  
  @Override
  public Boolean z() {
    return bitword().TestBit(kBrZBit);
  }

  @Override
  public Boolean has_p() {
    return true;
  }
  
  @Override
  public Boolean p() {
    return bitword().TestBit(kBrPBit);
  }

  private final int kPcOffset9HighBit = 8;
  private final int kPcOffset9LowBit = 0;
  private final int kBrNBit = 11;
  private final int kBrZBit = 10;
  private final int kBrPBit = 9;
}
