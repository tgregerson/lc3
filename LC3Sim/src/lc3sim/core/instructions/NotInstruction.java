package lc3sim.core.instructions;

import lc3sim.core.BitWord;

public class NotInstruction extends Instruction {
  public NotInstruction(BitWord bitword) {
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
  public Boolean has_dr() {
    return true;
  }
  
  @Override
  public BitWord dr() {
    return bitword().GetBitRange(kDrHighBit, kDrLowBit);
  }
  private final int kDrHighBit = 11;
  private final int kDrLowBit = 9;
  private final int kSrHighBit = 8;
  private final int kSrLowBit = 6;
}
