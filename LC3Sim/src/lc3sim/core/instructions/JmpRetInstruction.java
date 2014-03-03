package lc3sim.core.instructions;

import lc3sim.core.BitWord;

public class JmpRetInstruction extends Instruction {
  public JmpRetInstruction(BitWord bitword) {
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

  private final int kBaseRHighBit = 8;
  private final int kBaseRLowBit = 6;
}
