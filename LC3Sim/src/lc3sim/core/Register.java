package lc3sim.core;

public class Register extends AbstractPropagator
                      implements Synchronized {

  public Register(int num_bits, InputId data_input_id, OutputId my_output_id,
                  Boolean has_enable) {
    num_bits_ = num_bits;
    in_id_ = data_input_id;
    out_id_ = my_output_id;
    has_enable_ = has_enable;
    Init();
  }
  
  public void Init() {
    d_ = q_ = new BitWord(num_bits_);
    en_ = false;
    UpdateOutput(out_id_);
  }
  
  // BasicPropagator Interface
  public void Notify(BitWord bit_word, OutputId sender,
                     InputId receiver, Object arg) {
    if (sender == OutputId.External) {
      // Asynchronous set command. Immediately update.
      q_ = bit_word.Resize(num_bits_, false);
      UpdateOutput(out_id_);
    } else {
      if (receiver == in_id_) {
        // Update to data. Defer update to clock edge.
        d_ = bit_word.Resize(num_bits_, false);
      } else {
        // Assume update to enable.
        en_ = bit_word.ToBoolean();
      }
    }
  }
  
  // Synchronized Interface
  public void PreClock() {
    if (!has_enable_ || en_) {
      q_ = d_;
    }
  }
  
  public void PostClock() {
    UpdateOutput(out_id_);
  }
  
  protected BitWord ComputeOutput(OutputId unused) {
	  return q_;
  }
  
  private BitWord d_;
  private Boolean en_;
  private BitWord q_;
  
  private final Boolean has_enable_;
  private final int num_bits_;
  private final InputId in_id_;
  private final OutputId out_id_; 
}
