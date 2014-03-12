package lc3sim.test.core;

import lc3sim.core.*;

public class TestListener implements Listener {
  public TestListener(InputId iid) {
    input_id_ = iid;
  }
  
  public TestListener(BitWord bw, OutputId oid, InputId iid, Object arg) {
    input_id_ = iid;
    last_bitword = bw;
    last_output_id = oid;
    last_arg = arg;
  }
  
  public void Notify(BitWord bw, OutputId oid, InputId iid, Object arg) {
    last_bitword = bw;
    last_output_id = oid;
    last_input_id = iid;
    last_arg = arg;
  }
  
  public ListenerCallback GetCallback(OutputId oid, Object arg) {
    return new ListenerCallback(this, oid, input_id_, arg);
  }

  public BitWord last_bitword;
  public OutputId last_output_id;
  public Object last_arg;
  public InputId last_input_id;

  private final InputId input_id_;
}
