package lc3sim.core;

// Interface for Observer pattern that returns a BitWord from a
// Listenable object that calls the Notify callback. 
public interface Listener {
  // Callback executed by a Listenable object. 'sender' should refer to an
  // output on the Listenable object, and 'receiver' should refer to an input
  // on the Listener object. 'arg' may be used to pass additional information
  // or may be NULL.
  //
  // If the Listenable is tristate logic, the BitWord may be null, indicating
  // high impedance.
  public void Notify(BitWord bit_word, OutputId sender,
                     InputId receiver, Object arg);
}
