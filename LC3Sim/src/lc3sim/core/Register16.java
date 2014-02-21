package lc3sim.core;

// A 16-bit register.
public class Register16 extends BasicPropagator
                        implements Synchronized {
  public Register16(ArchitecturalId my_id) {
    super(my_id);
    Init();
  }
  
  public void Init() {
    d_ = q_ = new BitWord(16);
    UpdateOutput();
  }
  
  // BasicPropagator Interface
  public void Notify(BitWord bit_word, Object arg, ArchitecturalId sender) {
    assert bit_word.num_bits() == 16;
    if (sender == ArchitecturalId.External) {
      // Asynchronous set command. Immediately update.
      q_ = bit_word;
      UpdateOutput();
    } else {
      // Defer update to clock edge.
      d_ = bit_word;
    }
  }
  
  // Synchronized Interface
  public void PreClock() {
    q_ = d_;
  }
  
  public void PostClock() {
    UpdateOutput();
  }
  
  protected BitWord ComputeOutput() {
	  return q_;
  }
  
  private BitWord d_;
  private BitWord q_;
}
