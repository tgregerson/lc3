package lc3sim.core;

// A state machine that controls the LC3 instruction cycle and generates control
// signals for logic.
public class StateMachine {
  public enum InstructionPhase {
    kFetchInstruction,
    kDecodeInstruction,
    kEvaluateAddress,
    kFetchOperands,
    kExecuteOperation,
    kStoreResult,
    kInvalidState
  }
  
  public enum InstructionState {
    
  }
  
  public StateMachine() {
    Init();
  }
  
  public void Init() {
    next_phase_ = InstructionPhase.kFetchInstruction;
  }
  
  // Advances the state machine by one state.
  public void ExecuteCurrentPhase() {
    switch (next_phase_) {
      case kFetchInstruction:
        FetchInstruction();
        next_phase_ = InstructionPhase.kDecodeInstruction;
        break;
      case kDecodeInstruction:
        DecodeInstruction();
        next_phase_ = InstructionPhase.kEvaluateAddress;
        break;
      case kEvaluateAddress:
        EvaluateAddress();
        next_phase_ = InstructionPhase.kFetchOperands;
        break;
      case kFetchOperands:
        FetchOperands();
        next_phase_ = InstructionPhase.kExecuteOperation;
        break;
      case kExecuteOperation:
        ExecuteOperation();
        next_phase_ = InstructionPhase.kStoreResult;
        break;
      case kStoreResult:
        StoreResult();
        next_phase_ = InstructionPhase.kFetchInstruction;
        break;
      default:
        assert false;
        break;
    }
  }
  
  // Advances the state machine to start of next phase.
  public void ExecuteInstruction() {
    do {
      ExecuteCurrentPhase(); 
    } while (next_phase_ != InstructionPhase.kFetchInstruction);
  }
  
  private void FetchInstruction() {
    // MAR <= PC, PC <= PC + 1
    // PCMux Select <= 00
    // PCTri <= 1
    
    // MDR <= m[MAR]
    
    // IR <= MDR
    
  }
  
  private void DecodeInstruction() {
    
  }
  
  private void EvaluateAddress() {
    
  }
  
  private void FetchOperands() {
    
  }
  
  private void ExecuteOperation() {
    
  }
  
  private void StoreResult() {
    
  }
  
  // The current state is the state that the processor is about to execute.
  private InstructionPhase next_phase_;


}
