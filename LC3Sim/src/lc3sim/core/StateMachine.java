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
    instruction_ = Instruction.FromBitWord(new BitWord(Instruction.kNumBits));
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
  
  public void ExecuteCurrentCycle() {
    clock_.Tick();
  }
  
  // Advances the state machine to start of next phase.
  public void ExecuteInstruction() {
    do {
      ExecuteCurrentCycle();
    } while (instruction_.NextCycle(cycle_) !=
             InstructionCycle.kFetchInstruction1);
  }

  // Synchronized
  public void PreClock() {
    instruction_ = instruction_buffer_;
  }

  public void PostClock() {
    // TODO Add interrupt and exception check?
    cycle_ = instruction_.NextCycle(cycle_);
    UpdateOutput(OutputId.StateMachineCycle);
  }
  
  // AbstractPropagator
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    assert sender == OutputId.Ir;
    instruction_buffer_ = Instruction.FromBitWord(data);
  }
  
  public BitWord ComputeOutput(OutputId unused) {
    return cycle_.as_BitWord();
  }
  
  private CycleClock clock_;
  private InstructionCycle cycle_;
  private Instruction instruction_;
  private Instruction instruction_buffer_;  // For implementing Synchronized
}