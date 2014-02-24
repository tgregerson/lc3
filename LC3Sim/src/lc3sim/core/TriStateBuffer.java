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
    if (receiver == data_id_) {
      data_ = data;
    } else if (receiver == enable_id_) {
      enable_ = data.ToBoolean();
    }
    if (enable_) {
      UpdateOutput(output_id_);
    }
  }
  
  public BitWord ComputeOutput(OutputId unused) {
    return data_;
  }
  
  private Boolean enable_;
  private BitWord data_;

  private InputId data_id_;
  private InputId enable_id_;
  private OutputId output_id_;
}
