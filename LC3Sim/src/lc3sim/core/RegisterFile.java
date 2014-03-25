package lc3sim.core;

// A register file with 8 entries, two read ports, and one write port.
public class RegisterFile extends AbstractPropagator implements Synchronized {
  public static final int kNumAddrBits = 3;
  public static final int kWordSize = ArchitecturalState.kWordSize;
  
  public RegisterFile() {
    Init();
  }
  
  public void Init() {
    regs_ = new BitWord[num_entries_];
    for (int i = 0; i < num_entries_; ++i) {
      regs_[i] = BitWord.Zeroes(kWordSize);
    }
    sr1_addr_ = sr2_addr_ = dr_addr_ = BitWord.Zeroes(kNumAddrBits);
    dr_load_enable_ = false;
    dr_in_ = BitWord.Zeroes(kWordSize);
  }
  
  public BitWord Read(int reg_num) {
    assert reg_num < num_entries_ : reg_num;
    return regs_[reg_num];
  }
  
  // Basic Propagator interface
  
  public BitWord ComputeOutput(OutputId output_id) {
    if (output_id == OutputId.GprSr1) {
      return regs_[sr1_addr_.ToInt()];
    } else if (output_id == OutputId.GprSr2) {
      return regs_[sr2_addr_.ToInt()];
    } else {
      assert false;
      return null;
    }
  }
  
  // External senders may pass a RegisterStateUpdate as 'arg'.
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    if (sender == OutputId.External) {
      // Change to internal state from external source.
      assert arg instanceof RegisterStateUpdate;
      int register = ((RegisterStateUpdate)arg).register_number;
      assert register < num_entries_;
      regs_[register] = ((RegisterStateUpdate)arg).value.Resize(
          ArchitecturalState.kWordSize, false);
    } else {
      // Change to one of the inputs.
      switch (receiver) {
        case GprSr1Addr:
          sr1_addr_ = data.Resize(kWordSize, false);
          break;
        case GprSr2Addr:
          sr2_addr_ = data.Resize(kWordSize, false);
          break;
        case GprDrAddr:
          dr_addr_ = data.Resize(kWordSize, false);
          break;
        case GprDrData:
          dr_in_ = data.Resize(kWordSize, false);
          break;
        case GprDrLoad:
          dr_load_enable_ = data.ToBoolean();
          break;
        default:
          assert false;
      }
    }
    UpdateOutput(OutputId.GprSr1);
    UpdateOutput(OutputId.GprSr2);
  }
  
  // Used by external UI to force updates to register state.
  public static class RegisterStateUpdate {
    public RegisterStateUpdate(int reg_num, BitWord val) {
      register_number = reg_num;
      value = val;
    }
    public int register_number;
    public BitWord value;
  };
  
  // Synchronized interface
  public void PreClock() {
    if (dr_load_enable_) {
      regs_[dr_addr_.ToInt()] = dr_in_;
    }
  }
  
  public void PostClock() {
    UpdateOutput(OutputId.GprSr1);
    UpdateOutput(OutputId.GprSr2);
  }

  private BitWord[] regs_;
  
  // Input signal buffers
  private BitWord sr1_addr_;
  private BitWord sr2_addr_;
  private BitWord dr_addr_;
  private Boolean dr_load_enable_;
  private BitWord dr_in_;
  
  private final int num_entries_ = 1 << kNumAddrBits;
}
