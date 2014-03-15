package lc3sim.core;

// An interface for synchronous logic. Provides a pre-clock and post-clock
// method interface. The purpose of the Synchronized interface is to avoid race
// conditions when updating outputs. Typically a synchronized element will
// buffer the inputs to its synchronous logic on PreClock, then update its
// output on PostClock.
public interface Synchronized {
  
  // Takes actions immediately before the synchronization point.
  public void PreClock();
  
  // Takes actions after the synchronization point.
  public void PostClock();
}
