package lc3sim.core;

public class TriStateBuffer extends AbstractPropagator {
  public TriStateBuffer(OutputId output_id, InputId data_id,
                        InputId enable_id) {
    data_id_ = data_id;
    enable_id_ = enable_id;
    output_id_ = output_id;
    Init();
  }
  
  public void Init() {
    enable_ = false;
    data_ = new BitWord(16);
  }
  
  // AbstractPropagator methods
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    boolean prev_enable_ = enable_;
    if (receiver == data_id_) {
      System.out.println("Processing data update");
      data_ = data;
    } else if (receiver == enable_id_) {
      System.out.println("Processing enable update");
      enable_ = data.ToBoolean();
    }
    // Update output if enable is set or on negative enable edge.
    if (enable_ || prev_enable_) {
      UpdateOutput(output_id_);
    }
  }
  
  public BitWord ComputeOutput(OutputId unused) {
    if (enable_) {
      System.out.println("Computing enabled data");
      return data_;
    } else {
      System.out.println("Computing non-enabled null data");
      // Tristate buffer sends null as its output data to indicate high impedance.
      return null;
    }
  }
  
  private Boolean enable_;
  private BitWord data_;

  private InputId data_id_;
  private InputId enable_id_;
  private OutputId output_id_;
}
