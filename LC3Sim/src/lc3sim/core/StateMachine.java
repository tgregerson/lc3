package lc3sim.core;

// A state machine that controls the LC3 instruction cycle.
public class StateMachine {
  public enum InstructionState {
    kFetchInstruction,
    kDecodeInstruction,
    kEvaluateAddress,
    kFetchOperands,
    kExecute,
    kStore
  }
  
  // Advances the state machine by one state.
  public void AdvanceState() {
    
  }
  
  // Advances the state machine to start of next cycle.
  public void AdvanceCycle() {
    
  }
  
  // Advances the state machine until the PC is equal to one of the values in
  // 'addresses' (or halt). Stops at the beginning of the next cycle.
  public void AdvanceToAddr(short[] addresses) {
    
  }

}
