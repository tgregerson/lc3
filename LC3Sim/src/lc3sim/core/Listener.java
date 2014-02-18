package lc3sim.core;

// Interface for Observer pattern that returns a BitWord from a
// Listenable object that calls the Notify callback.
public interface Listener {
  // Callback executed by a Listenable object.
  public void Notify(BitWord bit_word, Object listener_provided,
                     ArchitecturalId SenderId);
}
