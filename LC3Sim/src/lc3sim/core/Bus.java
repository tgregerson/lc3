package lc3sim.core;

public class Bus extends AbstractPropagator {
  public static final int kNumBits = ArchitecturalState.kWordSize;

  public Bus() {
    Init();
  }
  
  public void Init() {
    data_ = null;
  }
  
  // AbstractPropagator methods
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    assert receiver == InputId.Bus;
    if (data != BitWord.EMPTY) {
      assert data.num_bits() == kNumBits;
    }
    
    // Bus may store null data. This occurs if the previous driver sends null
    // data. This indicates the state of the bus is high impedance.
    if (sender == last_driver_ || !BitWord.EMPTY.IsIdentical(data)) {
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
  
}
