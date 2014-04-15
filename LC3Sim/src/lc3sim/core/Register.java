package lc3sim.core;

public class Register extends AbstractPropagator
                      implements Synchronized {

  public Register(int num_bits, InputId data_id, InputId enable_id,
                  OutputId output_id) {
    num_bits_ = num_bits;
    data_id_ = data_id;
    out_id_ = output_id;
    enable_id_ = enable_id;
    Init();
  }
  
  public void Init() {
    d_ = q_ = BitWord.Zeroes(num_bits_);
    en_ = false;
    UpdateOutput(out_id_);
  }
  
  public BitWord Read() {
    return CurrentOutput(out_id_);
  }
  
  // BasicPropagator Interface
  public void Notify(BitWord bit_word, OutputId sender,
                     InputId receiver, Object arg) {
    if (sender == OutputId.External) {
      // Asynchronous set command. Immediately update.
      q_ = bit_word.Resize(num_bits_, false);
      UpdateOutput(out_id_);
    } else {
      if (receiver == data_id_) {
        d_ = bit_word.Resize(num_bits_, false);
      } else if (receiver == enable_id_) {
        en_ = bit_word.ToBoolean();
      } else {
        throw new IllegalArgumentException(
            "Unexpected Register receiver ID: " + receiver);
      }
    }
  }
  
  // Synchronized Interface
  public void PreClock() {
    if (en_) {
      q_ = d_;
    }
  }
  
  public void PostClock() {
    UpdateOutput(out_id_);
  }
  
  protected BitWord ComputeOutput(OutputId unused) {
	  return q_;
  }
  
  protected BitWord d_;
  protected Boolean en_;
  protected BitWord q_;
  
  protected final int num_bits_;
  protected final InputId data_id_;
  protected final InputId enable_id_;
  protected final OutputId out_id_; 
}
