package lc3sim.core;

import java.util.Set;

public class SimulationController {

  
  // Advances the state machine until the PC is equal to one of the values in
  // 'addresses' (or halt). Stops at the beginning of the next cycle.
  public int AdvanceToAddr(Set<Integer> addresses) {
    while (true) {
      int current_address = architectural_state_.ExecuteInstruction();
      if (addresses.contains(current_address)) {
        return current_address;
      }
    }
  }
  
  private ArchitecturalState architectural_state_;
}
