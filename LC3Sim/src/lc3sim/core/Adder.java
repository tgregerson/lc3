package lc3sim.core;

public class Adder extends AbstractPropagator {
  public Adder(InputId a, InputId b, OutputId out, int bitwidth) {
    bitwidth_ = bitwidth;
    in_id_a_ = a;
    in_id_b_ = b;
    out_id_ = out;
    Init();
  }
  
  public void Init() {
    op_a_ = BitWord.Zeroes(bitwidth_);
    op_b_ = BitWord.Zeroes(bitwidth_);
    UpdateOutput(out_id_);
  }
  
  @Override
  public void Notify(BitWord bit_word, OutputId sender,
                     InputId receiver, Object arg) {
    if (receiver == in_id_a_) {
      op_a_ = bit_word;
    } else if (receiver == in_id_b_){
      op_b_ = bit_word;
    }
    UpdateOutput(out_id_);
  }
  
  @Override
  protected BitWord ComputeOutput(OutputId unused) {
    return op_a_.AddFixedWidth(op_b_, bitwidth_);
  }
  
  // Buffered inputs
  private BitWord op_a_;
  private BitWord op_b_;

  private final int bitwidth_;
  private final OutputId out_id_;
  private final InputId in_id_a_;
  private final InputId in_id_b_;
}
