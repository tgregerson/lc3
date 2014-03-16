package lc3sim.core;

// Adds a constant to the connected input. Input is assumed to be in
// 2's complement format.
public class ConstantAdder extends AbstractPropagator {
  public ConstantAdder(OutputId out_id, int constant,
                       int output_bitwidth) {
    assert output_bitwidth <= 32;
    bitwidth_ = output_bitwidth;
    constant_ = constant;
    out_id_ = out_id;
    Init();
  }
  
  public void Init() {
    input_buffer_ = BitWord.Zeroes(bitwidth_);
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
    int new_value =
        input_buffer_.Resize(bitwidth_, true).ToInt() + constant_;
    return BitWord.FromInt(new_value, bitwidth_);
  }
  
  // Buffered inputs
  private BitWord input_buffer_;

  private final int bitwidth_;
  private final int constant_;
  private final OutputId out_id_;
}
