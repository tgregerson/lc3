package lc3sim.core;

public interface Listenable {

  public void RegisterListenerCallback(ListenerCallback cb);
  
  public void UnregisterListener(Listener listener);
  public void UnregisterListenerCallback(ListenerCallback cb);
  public void UnregisterAllListenerCallbacks();
}
