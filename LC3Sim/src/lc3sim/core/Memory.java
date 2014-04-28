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
    RefreshOutput();
  }

  public void RefreshOutput() {
    ForceUpdateOutput(OutputId.Memory);
  }
  
  public BitWord Read(int addr) {
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
      if (!(arg instanceof MemoryStateUpdate)) {
        throw new IllegalArgumentException(
            "Expected MemoryStateUpdate from external sender ID.");
      }
      int address = ((MemoryStateUpdate)arg).address;
      data_[address] =
          BitWord.FromInt(((MemoryStateUpdate)arg).value, kWordSize);
      UpdateOutput(OutputId.Memory);
      SendNotification(null, arg, OutputId.MemoryInternal);
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
          throw new IllegalArgumentException(
              "Unexpected Memory receiver ID: " + receiver);
      }
    }
  }
  
  // Used by external UI to force updates to register state.
  public static class MemoryStateUpdate {
    public MemoryStateUpdate(int addr, int val) {
      address = addr;
      value = val;
    }  
    public int address;
    public int value;
  };
  
  // Synchronized interface
  public void PreClock() {
    if (write_enable_buffer_) {
      BitWord old_data = data_[addr_buffer_.ToInt()];
      data_[addr_buffer_.ToInt()] = data_in_buffer_;
      if (!old_data.IsIdentical(data_in_buffer_)) {
        Memory.MemoryStateUpdate update =
            new Memory.MemoryStateUpdate(addr_buffer_.ToInt(),
                                         data_in_buffer_.ToInt());
        SendNotification(null, update, OutputId.MemoryInternal);
      }
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
