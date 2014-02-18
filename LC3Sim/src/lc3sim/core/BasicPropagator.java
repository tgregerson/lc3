package lc3sim.core;

import java.util.HashSet;
import java.util.Set;

// Combines the Listener and Listenable classes to provide a basic
// propagation chain for a logic unit with a single output.
// Subclasses must implement Notify().
public abstract class BasicPropagator implements Listener, Listenable {
  // 'my_id' should correspond to the ID of the output of the propagator.
  protected BasicPropagator(ArchitecturalId my_id) {
    my_id_ = my_id;
    listener_bindings_ = new HashSet<ListenerCallback>();
  }
  
  public void RegisterListenerCallback(ListenerCallback cb) {
    listener_bindings_.add(cb);
  }
  
  // Removes all callbacks for 'listener'.
  public void UnregisterListener(Listener listener) {
    HashSet<ListenerCallback> keys_to_remove = new HashSet<ListenerCallback>();
    for (ListenerCallback cb : listener_bindings_) {
      if (cb.get_listener() == listener) {
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
  
  // Executes NotifyUpdate on all listeners.
  protected void SendNotification(BitWord bit_word) {
    for (ListenerCallback cb : listener_bindings_) {
      cb.Run(bit_word, my_id_);
    }
  }
  
  private Set<ListenerCallback> listener_bindings_;
  private ArchitecturalId my_id_;
}
