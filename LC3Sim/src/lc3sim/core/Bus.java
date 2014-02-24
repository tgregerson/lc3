package lc3sim.core;

public class Bus extends AbstractPropagator {
  public Bus() {
    data_ = new BitWord(bitwidth_);
  }
  
  // AbstractPropagator methods
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    assert receiver == InputId.Bus;
    assert data.num_bits() == bitwidth_;
    data_ = data;
    UpdateOutput(OutputId.Bus);
  }
  
  public BitWord ComputeOutput(OutputId unused) {
    return data_;
  }
  
  private BitWord data_;
  
  private final int bitwidth_ = 16;
}
