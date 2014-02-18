package lc3sim.core;

// An interface for synchronous logic. Provides a pre-clock and post-clock
// method interface.
public interface Synchronized {
  
  // Takes actions immediately before the synchronization point
  public void PreClock();
  
  // Takes actions after the synchronization point.
  public void PostClock();
}
