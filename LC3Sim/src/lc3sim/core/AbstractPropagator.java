package lc3sim.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// The AbstractPropagator provides most of the implementation of a logic
// element that can both respond to changes in its inputs (via Listener)
// and broadcast changes in its output (via Listenable).
//
// Children must implement Notify() (from Listener), which implements the
// actions to take on a change in input, and ComputeOutput(), which
// calculates the output of the logic based on the current input values.
//
// Changes to the output should occur via calls to UpdateOutput(), which
// handles computing the output, comparing it to the previous output,
// and broadcasting notifications to listeners if necessary.
public abstract class AbstractPropagator implements Listener, Listenable {
  protected AbstractPropagator() {
    listener_bindings_ = new HashSet<ListenerCallback>();
    current_output_ = new HashMap<OutputId, BitWord>();
  }
  
  public void RegisterListenerCallback(ListenerCallback cb) {
    listener_bindings_.add(cb);
  }
  
  // Removes all callbacks for 'listener'.
  public void UnregisterListener(Listener listener) {
    HashSet<ListenerCallback> keys_to_remove = new HashSet<ListenerCallback>();
    for (ListenerCallback cb : listener_bindings_) {
      if (cb.listener() == listener) {
        keys_to_remove.add(cb);
      }
    }
    for (ListenerCallback key : keys_to_remove) {
      listener_bindings_.remove(key);
    }
  }
  
  public void UnregisterListenerCallback(ListenerCallback cb) {
    listener_bindings_.remove(cb);
  }
  
  public void UnregisterAllListenerCallbacks() {
    listener_bindings_.clear();
  }
  
  // Executes NotifyUpdate on listeners with associated with 'sender_id'.
  protected void SendNotification(BitWord bit_word, OutputId sender_id) {
    for (ListenerCallback cb : listener_bindings_) {
      if (cb.sender() == sender_id) {
        cb.Run(bit_word);
      }
    }
  }
  
  protected void UpdateOutput(OutputId id) {
    BitWord old_output = CurrentOutput(id);
    BitWord new_output = ComputeOutput(id);
    assert (new_output != null);
    if (old_output == null || !old_output.IsEqual(old_output, false)) {
      SetCurrentOutput(id, new_output);
      SendNotification(new_output, id);
    }
  }
  
  protected BitWord CurrentOutput(OutputId id) {
	  return current_output_.get(id);
  }
  
  abstract protected BitWord ComputeOutput(OutputId id);
  
  protected void SetCurrentOutput(OutputId id, BitWord bit_word) {
	  current_output_.put(id, bit_word);
  }
  
  private Set<ListenerCallback> listener_bindings_;
  private Map<OutputId, BitWord> current_output_;
}
