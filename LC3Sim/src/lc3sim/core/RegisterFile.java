package lc3sim.core;

import lc3sim.core.Register16;

// A register file with 8 16-bit entries.
public class RegisterFile {
  
  public RegisterFile() {
    Init();
  }
  
  public void Init() {
    regs_ = new Register16[8];
  }
  
  public short Read(int reg_num) {
    return regs_[reg_num].Read();
  }
  
  public short ReadBitRange(int reg_num, int high_bit, int low_bit) {
    return regs_[reg_num].ReadBitRange(high_bit, low_bit);
  }
  
  public void Write(int reg_num, short data) {
    regs_[reg_num].Write(data);
  }
  
  private Register16[] regs_;
}
