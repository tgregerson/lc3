package lc3sim.core;

import lc3sim.core.instructions.Instruction;

// A state machine that controls the LC3 instruction cycle and generates control
// signals for logic.
public class StateMachine extends AbstractPropagator implements Synchronized {
  
  public StateMachine(CycleClock clock) {
    clock_ = clock;
    clock_.AddSynchronizedElement(this);  // OK if already added.
    Init();
  }
  
  public void Init() {
    Reset();
    instruction_data_buffer_ = BitWord.Zeroes(Instruction.kNumBits);
    instruction_ = Instruction.FromBitWord(instruction_data_buffer_);
    // TODO Check on whether additional mechanism are necessary to ensure the
    // system initializes properly regardless of order of construction and
    // adding of listeners.
  }
  
  public boolean IsRunning() {
    return InstructionCycle.kReset != cycle_;
  }
  
  public void Start() {
    if (IsRunning()) {
      throw new IllegalStateException(
          "Called Start() on already running state machine.");
    }
    cycle_ = InstructionCycle.kFetchInstruction1;
    UpdateOutput(OutputId.StateMachineCycle);
  }
  
  public void Reset() {
    cycle_ = InstructionCycle.kReset;
    UpdateOutput(OutputId.StateMachineCycle);
  }
  
  // Execute current cycle and return next cycle.
  public InstructionCycle ExecuteCurrentCycle() {
    clock_.Tick();
    return cycle_;
  }
  
  // Advances the state machine to start of next phase.
  public void ExecuteInstruction() {
    do {
      ExecuteCurrentCycle();
    } while (cycle_ != InstructionCycle.kFetchInstruction1);
  }

  // Synchronized
  public void PreClock() {
    instruction_ = Instruction.FromBitWord(instruction_data_buffer_);
    cycle_ = instruction_.NextCycle(cycle_);
  }

  public void PostClock() {
    // TODO Add interrupt and exception check?
    UpdateOutput(OutputId.StateMachineInstruction);
    // TODO Currently it is necessary to send Instruction before Cycle, otherwise
    // The control logic will attempt to generate a control set for a cycle that may be invalid
    // for the current instruction it has. This is fragile. Once all instructions are tested,
    // ControlSet should be modified to generate a default control set for in glitch conditions.
    UpdateOutput(OutputId.StateMachineCycle);
  }
  
  // AbstractPropagator
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    if (cycle_ == InstructionCycle.kFetchInstruction3) {
      instruction_data_buffer_ = data;
    }
  }
  
  public BitWord ComputeOutput(OutputId id) {
    if (id == OutputId.StateMachineCycle) {
      return cycle_.as_BitWord();
    } else if (id == OutputId.StateMachineInstruction) {
      return instruction_.bitword();
    } else {
      throw new IllegalArgumentException("Unexpected StateMachine output ID: " + id);
    }
  }
  
  private CycleClock clock_;
  private InstructionCycle cycle_;
  private Instruction instruction_;
  private BitWord instruction_data_buffer_;
}