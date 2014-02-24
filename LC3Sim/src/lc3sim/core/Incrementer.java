package lc3sim.core;

public class Incrementer extends AbstractPropagator {
  public Incrementer(OutputId out_id, int bitwidth) {
    bitwidth_ = bitwidth;
    out_id_ = out_id;
    Init();
  }
  
  public void Init() {
    input_buffer_ = new BitWord(bitwidth_);
    UpdateOutput(out_id_);
  }
  
  @Override
  public void Notify(BitWord bit_word, OutputId sender,
                     InputId receiver, Object arg) {
    input_buffer_ = bit_word.Resize(bitwidth_, false);
    UpdateOutput(out_id_);
  }
  
  @Override
  protected BitWord ComputeOutput(OutputId unused) {
    return input_buffer_.AddFixedWidth(
        BitWord.FromBoolean(true).Resize(2, false), bitwidth_);
  }
  
  // Buffered inputs
  private BitWord input_buffer_;

  private final int bitwidth_;
  private final OutputId out_id_;
}
