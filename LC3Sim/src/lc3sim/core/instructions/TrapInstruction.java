package lc3sim.core.instructions;

import lc3sim.core.BitWord;

public class TrapInstruction extends Instruction{
  public TrapInstruction(BitWord bitword) {
    super(bitword);
  }
  
  @Override
  public Boolean has_trapvect8() {
    return true;
  }
  
  @Override
  public BitWord trapvect8() {
    return bitword().GetBitRange(kTrapvect8HighBit, kTrapvect8LowBit);
  }

  private final int kTrapvect8HighBit = 7;
  private final int kTrapvect8LowBit = 0;
}
