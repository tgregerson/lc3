package lc3sim.core;

import lc3sim.core.Register16;

// A register file with 8 16-bit entries, two read ports, and one write port.
public class RegisterFile implements Synchronized {
  
  public RegisterFile() {
    Init();
  }
  
  public void Init() {
    regs_ = new Register16[8];
    sr1_addr_ = sr2_addr_ = dr_addr_ = new BitWord(3);
  }
  
  // Synchronized interface
  public void PreClock() {
    
  }
  
  public void PostClock() {
    
  }

  private BitWord[] regs_;
  
  // Input signals
  private BitWord sr1_addr_;
  private BitWord sr2_addr_;
  private BitWord dr_addr_;
  private Boolean dr_load_enable_;
  private BitWord dr_in_;
  
  // Output signals
  private BitWord sr1_out_;
  private BitWord sr2_out_;
}
