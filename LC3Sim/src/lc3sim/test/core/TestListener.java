package lc3sim.test.core;

import static org.junit.Assert.*;
import java.util.List;
import java.util.LinkedList;

import lc3sim.core.*;

public class TestListener implements Listener {
  public TestListener(InputId iid) {
    input_id_ = iid;
    check_sequence_ = new LinkedList<BitWord>();
  }
  
  public TestListener(BitWord bw, OutputId oid, InputId iid, Object arg) {
    input_id_ = iid;
    last_bitword = bw;
    last_output_id = oid;
    last_arg = arg;
    check_sequence_ = new LinkedList<BitWord>();
  }
  
  public void Notify(BitWord bw, OutputId oid, InputId iid, Object arg) {
    last_bitword = bw;
    last_output_id = oid;
    last_input_id = iid;
    last_arg = arg;
    if (!check_sequence_.isEmpty()) {
      assertFalse(bw == null);
      assertEquals(bw, check_sequence_.removeFirst());
    }
  }
  
  // The contents of the check sequence will be compared, in order, to the data
  // received by Notify calls.
  public List<BitWord> CheckSequence() {
    return check_sequence_;
  }

  public void AppendCheckSequence(List<BitWord> sequence) {
    check_sequence_.addAll(sequence);
  }
  
  public void ClearCheckSequence() {
    check_sequence_.clear();
  }
  
  public ListenerCallback GetCallback(OutputId oid) {
    return GetCallback(oid, null);
  }

  public ListenerCallback GetCallback(OutputId oid, Object arg) {
    return new ListenerCallback(this, oid, input_id_, arg);
  }

  public BitWord last_bitword;
  public OutputId last_output_id;
  public Object last_arg;
  public InputId last_input_id;

  private final InputId input_id_;
  private LinkedList<BitWord> check_sequence_;
}
