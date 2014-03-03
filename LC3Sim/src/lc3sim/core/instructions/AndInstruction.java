package lc3sim.core.instructions;

import lc3sim.core.BitWord;

public class AndInstruction extends Instruction {
  public AndInstruction(BitWord bitword) {
    super(bitword);
  }

  @Override
  public Boolean has_sr1() {
    return true;
  }
  
  @Override
  public BitWord sr1() {
    return bitword().GetBitRange(kSr1HighBit, kSr1LowBit);
  }
  
  @Override
  public Boolean has_sr2() {
    return !mode_bit();
  }
  
  @Override
  public BitWord sr2() {
    assert !mode_bit();
    return bitword().GetBitRange(kSr2HighBit, kSr2LowBit);
  }
  
  @Override
  public Boolean has_dr() {
    return true;
  }
  
  @Override
  public BitWord dr() {
    return bitword().GetBitRange(kDrHighBit, kDrLowBit);
  }
  
  @Override
  public Boolean has_imm5() {
    return mode_bit();
  }
  
  @Override
  public BitWord imm5() {
    assert mode_bit();
    return bitword().GetBitRange(kImm5HighBit, kImm5LowBit);
  }
  
  @Override
  public Boolean has_mode_bit() {
    return true;
  }
  
  @Override
  public Boolean mode_bit() {
    return bitword().TestBit(kModeBit);
  }
  
  private final int kDrHighBit = 11;
  private final int kDrLowBit = 9;
  private final int kSr1HighBit = 8;
  private final int kSr1LowBit = 6;
  private final int kSr2HighBit = 2;
  private final int kSr2LowBit = 0;
  private final int kImm5HighBit = 4;
  private final int kImm5LowBit = 0;
  private final int kModeBit = 5;
}