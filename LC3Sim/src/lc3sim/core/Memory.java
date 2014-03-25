package lc3sim.core;

// A memory with 64K entries and 16-bit word size.
public class Memory extends AbstractPropagator implements Synchronized {
  public static final int kWordSize = ArchitecturalState.kWordSize;
  public static final int kNumAddrBits = kWordSize;

  public Memory() {
    Init();
  }
  
  public void Init() {
    data_ = new BitWord[num_entries_];
    for (int i = 0; i < num_entries_; ++i) {
      data_[i] = BitWord.Zeroes(kWordSize);
    }
    addr_buffer_ = BitWord.Zeroes(kNumAddrBits);
    write_enable_buffer_ = false;
    data_in_buffer_ = BitWord.Zeroes(kWordSize);
  }
  
  public BitWord Read(int addr) {
    assert addr < num_entries_ : addr;
    return data_[addr];
  }
  
  // Basic Propagator interface
  public BitWord ComputeOutput(OutputId unused) {
    return data_[addr_buffer_.ToInt()];
  }
  
  // External senders may pass a MemoryStateUpdate as 'arg'.
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    if (sender == OutputId.External) {
      // Change to internal state from external source.
      assert arg instanceof MemoryStateUpdate;
      int address = ((MemoryStateUpdate)arg).address.ToInt();
      assert address < num_entries_;
      data_[address] =
          ((MemoryStateUpdate)arg).value.Resize(kWordSize, false);
      UpdateOutput(OutputId.Memory);
    } else {
      // Change to one of the inputs.
      switch (receiver) {
        case MemoryData:
          data_in_buffer_ = data.Resize(kWordSize, false);
          break;
        case MemoryAddr:
          addr_buffer_ = data.Resize(kNumAddrBits, false);
          UpdateOutput(OutputId.Memory);
          break;
        case MemoryWriteEnable:
          write_enable_buffer_ = data.ToBoolean();
          break;
        default:
          assert false;
      }
    }
  }
  
  // Used by external UI to force updates to register state.
  public static class MemoryStateUpdate {
    public MemoryStateUpdate(BitWord addr, BitWord val) {
      address = addr;
      value = val;
    }  
    public BitWord address;
    public BitWord value;
  };
  
  // Synchronized interface
  public void PreClock() {
    if (write_enable_buffer_) {
      data_[addr_buffer_.ToInt()] = data_in_buffer_;
    }
  }
  
  public void PostClock() {
    if (write_enable_buffer_) {
      UpdateOutput(OutputId.Memory);
    }
  }

  // Input signal buffers
  private BitWord data_in_buffer_;
  private BitWord addr_buffer_;
  private Boolean write_enable_buffer_;
   
  private BitWord[] data_;
  private static final int num_entries_ = 1 << kNumAddrBits;
}
