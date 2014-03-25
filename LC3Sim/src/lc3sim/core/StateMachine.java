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
    instruction_ = Instruction.FromBitWord(BitWord.Zeroes(Instruction.kNumBits));
    // TODO Check on whether additional mechanism are necessary to ensure the
    // system initializes properly regardless of order of construction and
    // adding of listeners.
  }
  
  public boolean IsRunning() {
    return InstructionCycle.kReset != cycle_;
  }
  
  public void Start() {
    assert !(IsRunning());
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
    cycle_buffer_ = instruction_.NextCycle(cycle_);
  }

  public void PostClock() {
    // TODO Add interrupt and exception check?
    cycle_ = cycle_buffer_;
    UpdateOutput(OutputId.StateMachineCycle);
  }
  
  // AbstractPropagator
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    // TODO create an input id instead. This is bad for test mode.
    assert sender == OutputId.Ir;
    instruction_ = Instruction.FromBitWord(data);
    UpdateOutput(OutputId.StateMachineInstruction);
  }
  
  public BitWord ComputeOutput(OutputId id) {
    if (id == OutputId.StateMachineCycle) {
      return cycle_.as_BitWord();
    } else if (id == OutputId.StateMachineInstruction) {
      return instruction_.bitword();
    } else {
      assert false : id;
      return null;
    }
  }
  
  private CycleClock clock_;
  private InstructionCycle cycle_;
  private InstructionCycle cycle_buffer_;  // For Synchronized
  private Instruction instruction_;
}