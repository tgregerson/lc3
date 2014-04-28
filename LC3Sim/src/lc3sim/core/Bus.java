package lc3sim.core;

public class Bus extends AbstractPropagator {
  public static final int kNumBits = ArchitecturalState.kWordSize;

  public Bus() {
    Init();
  }
  
  public void Init() {
    data_ = null;
    RefreshOutput();
  }

  public void RefreshOutput() {
    ForceUpdateOutput(OutputId.Bus);
  }
  
  // AbstractPropagator methods
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    if (receiver != InputId.Bus) {
      throw new IllegalStateException(
          "Update to Bus contained non-Bus receiver ID: " + receiver);
    }
    if (data != BitWord.EMPTY) {
      if (data.num_bits() != kNumBits) {
        throw new IllegalArgumentException(
            "Mismatch between bus width and update width: " + kNumBits +
            " vs " + data.num_bits());
      }
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
