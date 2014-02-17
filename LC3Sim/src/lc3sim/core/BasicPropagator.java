package lc3sim.core;

import java.util.HashSet;
import java.util.Set;

// Combines the Listener and Listenable classes to provide a basic
// propagation chain.
public abstract class BasicPropagator implements Listener, Listenable {
  protected BasicPropagator() {
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
  
  // This just propagates the input change to the output. Sub-classes will
  // likely override this with some processing of 'bit_word'.
  public void Notify(BitWord bit_word, Object listener_provided) {
    SendNotification(bit_word);
  }
  
  // Executes NotifyUpdate on all listeners.
  protected void SendNotification(BitWord bit_word) {
    for (ListenerCallback cb : listener_bindings_) {
      cb.Run(bit_word);
    }
  }
  
  private Set<ListenerCallback> listener_bindings_;
}
