package lc3sim.core;

public class ListenerCallback {
  public ListenerCallback(Listener listener, Object arg) {
    listener_ = listener;
    arg_ = arg;
  }
  
  public void Run(BitWord bit_word, ArchitecturalId sender_id) {
    listener_.Notify(bit_word, arg_, sender_id);
  }
  
  public Listener get_listener() {
    return listener_;
  }
  
  public Object get_arg() {
    return arg_;
  }
  
  private Listener listener_;
  private Object arg_;
}
