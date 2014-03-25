package lc3sim.core;

import java.util.HashSet;

public class CycleClock {
  public CycleClock() {
    elements_ = new HashSet<Synchronized>();
  }

  public void Init() {
    RemoveAllElements();
  }
  
  // Elements are stored in a set, so adding the same element more than once
  // does nothing.
  public void AddSynchronizedElement(Synchronized s) {
    elements_.add(s);
  }
  
  public void RemoveAllElements() {
    elements_.clear();
  }
  
  public void Tick() {
    //System.out.println("----------------------------------TICK---------------------------------");
    for (Synchronized element : elements_) {
      element.PreClock();
    }
    for (Synchronized element : elements_) {
      element.PostClock();
    }
  }

  private HashSet<Synchronized> elements_;
}
