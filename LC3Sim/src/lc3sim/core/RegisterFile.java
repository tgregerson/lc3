package lc3sim.core;

// A register file with 8 16-bit entries.
public class RegisterFile {
  
  public RegisterFile() {
    Init();
  }
  
  public void Init() {
    regs = new short[10];
  }
  
  public short Read(int reg_num) {
    return regs[reg_num];
  }
  
  public void Write(int reg_num, short data) {
    regs[reg_num] = data;
  }
  
  private short[] regs;
}
