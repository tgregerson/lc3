package lc3sim.core;

// A state machine that controls the LC3 instruction cycle.
public class StateMachine {
  public enum InstructionState {
    kFetchInstruction,
    kDecodeInstruction,
    kEvaluateAddress,
    kFetchOperands,
    kExecute,
    kStore,
    kInvalidState
  }
  
  public StateMachine() {
    Init();
  }
  
  public void Init() {
    current_state_ = InstructionState.kFetchInstruction;
  }
  
  // Advances the state machine by one state.
  public void AdvanceState() {
    RunCurrentState();
    switch (current_state_) {
      case kFetchInstruction:
        current_state_ = InstructionState.kDecodeInstruction;
        break;
      case kDecodeInstruction:
        current_state_ = InstructionState.kEvaluateAddress;
        break;
      case kEvaluateAddress:
        current_state_ = InstructionState.kFetchOperands;
        break;
      case kFetchOperands:
        current_state_ = InstructionState.kExecute;
        break;
      case kExecute:
        current_state_ = InstructionState.kStore;
        break;
      case kStore:
        current_state_ = InstructionState.kFetchInstruction;
        break;
      default:
        assert false;
        break;
    }
  }
  
  // Advances the state machine to start of next cycle.
  public void AdvanceCycle() {
    do {
      AdvanceState(); 
    } while (current_state_ != InstructionState.kFetchInstruction);
  }
  
  // Advances the state machine until the PC is equal to one of the values in
  // 'addresses' (or halt). Stops at the beginning of the next cycle.
  public void AdvanceToAddr(short[] addresses) {
    while (true) {
      for (short address : addresses) {
        if (address == architectural_state_.GetPC()) {
          return;
        }
      }
      AdvanceCycle();
      // TODO: Deal with program halt.
    }
  }
  
  private void RunCurrentState() {
    
  }
  
  // The current state is pre-execution of that state.
  private InstructionState current_state_;

}
