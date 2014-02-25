package lc3sim.core;

import java.util.Vector;

public class CycleClock {
  public void Init() {
    RemoveAllElements();
  }
  
  public void AddSynchronizedElement(Synchronized s) {
    elements_.add(s);
  }
  
  public void RemoveAllElements() {
    elements_.clear();
  }
  
  public void Tick() {
    for (Synchronized element : elements_) {
      element.PreClock();
    }
    for (Synchronized element : elements_) {
      element.PostClock();
    }
  }

  private Vector<Synchronized> elements_;
}
