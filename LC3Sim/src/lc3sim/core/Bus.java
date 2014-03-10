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
    
    // Bus may store null data. This occurs if the previous driver sends null
    // data. This indicates the state of the bus is high impedance.
    if (sender == last_driver_ || data != null) {
      data_ = data;
      last_driver_ = sender;
      UpdateOutput(OutputId.Bus);
    }
  }
  
  public BitWord ComputeOutput(OutputId unused) {
    return data_;
  }
  
  private BitWord data_;
  private OutputId last_driver_;
  
  private final int bitwidth_ = 16;
}
