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
    q_changed_ = false;
  }
  
  public BitWord Get() {
    return q_;
  }
  
  public void Set(short value) {
    Set(BitWord.FromShort(value));
  }
  
  public void Set(BitWord bit_word) {
    d_ = bit_word.Resize(16, true);
  }
  
  // BasicPropagator Interface
  public void Notify(BitWord bit_word, Object arg, ArchitecturalId sender) {
    assert bit_word.num_bits() == 16;
    if (sender == ArchitecturalId.External) {
      // Force immediate update and propagation
      Boolean changed = q_.IsEqual(bit_word);
      if (changed) {
        q_ = bit_word;
        SendNotification(q_);
      }
    } else {
      // Defer update to clock edge.
      d_ = bit_word;
    }
  }
  
  // Synchronized Interface
  public void PreClock() {
    q_changed_ = !d_.IsEqual(q_);
    q_ = d_;
  }
  
  public void PostClock() {
    if (q_changed_) {
      SendNotification(q_);
      q_changed_ = false;
    }
  }
  
  private BitWord d_;
  private BitWord q_;
  private Boolean q_changed_;
}
