package lc3sim.core;

// A memory with 64K entries and 16-bit word size.
public class Memory extends AbstractPropagator implements Synchronized {
  public Memory() {
    Init();
  }
  
  public void Init() {
    data_ = new BitWord[num_entries_];
    for (int i = 0; i < num_entries_; ++i) {
      data_[i] = new BitWord(word_size_);
    }
    addr_buffer_ = new BitWord(addr_size_);
    write_enable_buffer_ = false;
    data_in_buffer_ = new BitWord(word_size_);
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
          ((MemoryStateUpdate)arg).value.Resize(word_size_, false);
    } else {
      // Change to one of the inputs.
      switch (receiver) {
        case MemoryData:
          data_in_buffer_ = data.Resize(word_size_, false);
          break;
        case MemoryAddr:
          addr_buffer_ = data.Resize(addr_size_, false);
          break;
        case MemoryWriteEnable:
          write_enable_buffer_ = data.ToBoolean();
          break;
        default:
          assert false;
      }
    }
    UpdateOutput(OutputId.Memory);
  }
  
  // Used by external UI to force updates to register state.
  public class MemoryStateUpdate {
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
    UpdateOutput(OutputId.Memory);
  }

  // Input signal buffers
  private BitWord data_in_buffer_;
  private BitWord addr_buffer_;
  private Boolean write_enable_buffer_;
   
  private BitWord[] data_;
  private final int word_size_ = 16;
  private final int addr_size_ = 16;
  private static final int num_entries_ = 65536;
}
