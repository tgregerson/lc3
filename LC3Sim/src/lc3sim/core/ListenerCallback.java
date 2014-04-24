package lc3sim.core;

public class ListenerCallback {
  // Create a callback, binding 'sender' to 'receiver', and optionally
  // providing additional data through 'arg'.
  //
  // 'sender', 'receiver', and 'arg' will be passed to the listener's Notify()
  // method when the callback is executed.
  //
  // 'sender' indicates the specific output's whose changes should trigger the
  // callback.
  //
  // 'receiver' may be used to indicate to the Notify() method which of a
  // Listener's inputs the notification is directed towards.
  //
  // 'arg' may be NULL if no additional data is needed. 'arg' is stored as a
  // reference, and is therefore mutable after the creation of the callback via
  // the original object.
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

  public void Run(BitWord bit_word, Object arg) {
    listener_.Notify(bit_word, sender_, receiver_, arg);
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
