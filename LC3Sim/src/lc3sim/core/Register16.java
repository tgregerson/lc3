package lc3sim.core;

// A 16-bit register.
public class Register16 extends BasicPropagator
                        implements Synchronized {
  public Register16(InputId my_input_id,
                    OutputId my_output_id) {
    super();
    in_id_ = my_input_id;
    out_id_ = my_output_id;
    Init();
  }
  
  public void Init() {
    d_ = q_ = new BitWord(16);
    UpdateOutput(out_id_);
  }
  
  // BasicPropagator Interface
  public void Notify(BitWord bit_word, OutputId sender,
                     InputId receiver, Object arg) {
    assert bit_word.num_bits() == 16;
    if (sender == OutputId.External) {
      // Asynchronous set command. Immediately update.
      q_ = bit_word;
      UpdateOutput(out_id_);
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
    UpdateOutput(out_id_);
  }
  
  protected BitWord ComputeOutput(OutputId unused) {
	  return q_;
  }
  
  private BitWord d_;
  private BitWord q_;
  
  private final InputId in_id_;
  private final OutputId out_id_; 
}
