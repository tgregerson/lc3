package lc3sim.core;

public class ListenerCallback {
  // Create a callback, binding 'sender' to 'receiver', and optionally
  // providing additional data through 'arg', which may be set to
  // NULL if unused.
  //
  // 'listener' must be capable of servicing the notification, and is
  // typically the owner of 'receiver'. 
  public ListenerCallback(Listener listener, OutputId sender,
		                      InputId receiver, Object arg) {
    listener_ = listener;
    sender_ = sender;
    receiver_ = receiver;
    arg_ = arg;
  }
  
  public void Run(BitWord bit_word) {
    listener_.Notify(bit_word, sender_, receiver_, arg_);
  }
  
  public Listener listener() {
    return listener_;
  }
  
  public OutputId sender() {
    return sender_;
  }
  
  public InputId receiver() {
    return receiver_;
  }
  
  public Object arg() {
    return arg_;
  }
  
  public void set_arg(Object arg) {
    arg_ = arg;
  }
  
  private Listener listener_;
  private OutputId sender_;
  private InputId receiver_;
  private Object arg_;
}
