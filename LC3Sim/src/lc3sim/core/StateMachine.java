package lc3sim.core;

import lc3sim.core.instructions.Instruction;
import lc3sim.core.instructions.OpCode;

// A state machine that controls the LC3 instruction cycle and generates control
// signals for logic.
public class StateMachine extends AbstractPropagator implements Synchronized {
  // States for the phase state machine. These states may last for multiple
  // clock cycles in the LC3 architecture.
  public enum InstructionPhase {
    kReset,
    kFetchInstruction,
    kEvaluateAddress,
    kFetchOperands,
    kExecuteOperation,
    kStoreResult,
    kDispatchInterrupt,
    kReturnFromInterrupt,
    kInvalid,
  }
  
  // States for the cycle state machine. These states represent one clock cycle
  // in the LC3 architecture.
  public enum InstructionCycle {
    kReset(0),
    kFetchInstruction1(1),
    kFetchInstruction2(2),
    kFetchInstruction3(3),
    kEvaluateAddress1(4),
    kFetchOperands1(5),
    kExecuteOperation1(6),
    kExecuteOperation2(7),
    kStoreResult1(8),
    kDispatchInterrupt1(9);
    
    private InstructionCycle(int code) {
      code_as_int_ = code;
      code_as_bit_word_ = BitWord.FromInt(code, kStateBits);
    }
    
    public int as_int() {
      return code_as_int_;
    }
    
    public BitWord as_BitWord() {
      return code_as_bit_word_;
    }
    
    public static InstructionCycle Lookup(int code) {
      for (InstructionCycle cycle : InstructionCycle.values()) {
        if (code == cycle.as_int()) {
          return cycle;
        }
      }
      return null;
    }
    
    public static InstructionCycle Lookup(BitWord code) {
      for (InstructionCycle cycle : InstructionCycle.values()) {
        if (code.IsEqual(cycle.as_BitWord(), false)) {
          return cycle;
        }
      }
      return null;
    }
    
    private final int code_as_int_;
    private final BitWord code_as_bit_word_;
    private final int kStateBits = 4;
  }
  
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
    phase_ = InstructionPhase.kFetchInstruction;
    cycle_ = InstructionCycle.kFetchInstruction1;
    UpdateOutput(OutputId.StateMachineCycle);
  }
  
  public void Reset() {
    phase_ = InstructionPhase.kReset;
    cycle_ = InstructionCycle.kReset;
    UpdateOutput(OutputId.StateMachineCycle);
  }
  
  // Executes the phase in 'phase_' and advances it to the next phase.
  public void ExecuteCurrentPhase() {
    switch (phase_) {
      case kReset:
        assert false;
        break;
      case kFetchInstruction:
        FetchInstruction();
        break;
      case kEvaluateAddress:
        EvaluateAddress();
        break;
      case kFetchOperands:
        FetchOperands();
        break;
      case kExecuteOperation:
        ExecuteOperation();
        break;
      case kStoreResult:
        StoreResult();
        break;
        // TODO: Add support for interrupt special phases.
      default:
        assert false;
        break;
    }
    phase_ = NextPhase();
  }
  
  // Advances the state machine to start of next phase.
  public void ExecuteInstruction() {
    do {
      ExecuteCurrentPhase(); 
    } while (phase_ != InstructionPhase.kFetchInstruction);
  }
  
  private InstructionPhase NextPhase() {
    switch (phase_) {
      case kReset:
        return InstructionPhase.kReset;
      case kFetchInstruction:
        // Instructions that do not access memory can skip phases.
        switch (instruction_.op_code()) {
          case ADD:
            return InstructionPhase.kStoreResult;
          case AND:
            return InstructionPhase.kStoreResult;
          case BR:
            return InstructionPhase.kStoreResult;
          case JMP_RET:
            return InstructionPhase.kStoreResult;
          case JSR_JSRR:
            return InstructionPhase.kStoreResult;
          case LEA:
            return InstructionPhase.kStoreResult;
          case NOT:
            return InstructionPhase.kStoreResult;
          case RTI:
            return InstructionPhase.kReturnFromInterrupt;
          default:
            return InstructionPhase.kEvaluateAddress;
        }
      case kEvaluateAddress:
        return InstructionPhase.kFetchOperands;
      case kFetchOperands:
        return InstructionPhase.kExecuteOperation;
      case kExecuteOperation:
        return InstructionPhase.kStoreResult;
      case kStoreResult:
        // TODO Check for interrupt.
        return InstructionPhase.kFetchInstruction;
      default:
        return InstructionPhase.kInvalid;
    }
  }

  private InstructionCycle NextCycle() {
    if (cycle_ != null) {
      System.out.println(cycle_.name());
    }
    switch (cycle_) {
      case kFetchInstruction1:
        return InstructionCycle.kFetchInstruction2;
      case kFetchInstruction2:
        return InstructionCycle.kFetchInstruction3;
      case kFetchInstruction3:
        switch (NextPhase()) {
          case kEvaluateAddress:
            return InstructionCycle.kEvaluateAddress1;
          case kStoreResult:
            return InstructionCycle.kStoreResult1;
          case kReturnFromInterrupt:
            assert false;
            return null;
          default:
            return InstructionCycle.kStoreResult1;
        }
      case kEvaluateAddress1:
        return InstructionCycle.kFetchOperands1;
      case kFetchOperands1:
        return InstructionCycle.kExecuteOperation1;
      case kExecuteOperation1:
        if (instruction_.op_code() == OpCode.LDI ||
            instruction_.op_code() == OpCode.STI) {
          return InstructionCycle.kExecuteOperation2;
        } else {
          return InstructionCycle.kStoreResult1;
        }
      case kStoreResult1:
        if (NextPhase() == InstructionPhase.kFetchInstruction) {
          return InstructionCycle.kFetchInstruction1;
        } else {
          // Add interrupt phase handling.
          assert false;
          return null;
        }
      default:
        assert false;
        return null;
    }
  }
  
  private void FetchInstruction() {
    // MAR <= PC, PC <= PC + 1
    // PcMuxSelect <= 00
    // PcTri <= 1
    // MarLoad <= 1
    // PcLoad <= 1
    clock_.Tick();
    
    // MDR <= m[MAR]
    // MdrMuxSelect <= 1
    // MdrLoad <= 1
    clock_.Tick();

    // IR <= MDR
    // MdrTri <= 1
    // IrLoad <= 1
    clock_.Tick();
  }
  
  private void EvaluateAddress() {
    // MAR <= computed address
    clock_.Tick();
  }
  
  private void FetchOperands() {
    // For instructions that read memory:
    // MDR <= mem[MAR]
    clock_.Tick();
  }
  
  private void ExecuteOperation() {
    // Used by instructions that write memory, or do indirect reads.
    clock_.Tick();
    
    if (instruction_.op_code() == OpCode.LDI ||
        instruction_.op_code() == OpCode.STI) {
      clock_.Tick();
    }
  }
  
  private void StoreResult() {
    // Write to memory, write to the register file, or write to the PC.
    clock_.Tick();
  }
  
  // Synchronized
  public void PreClock() {
    instruction_ = instruction_buffer_;
  }

  public void PostClock() {
    cycle_ = NextCycle();
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
  private InstructionPhase phase_;
  private InstructionCycle cycle_;
  private Instruction instruction_;
  private Instruction instruction_buffer_;  // For implementing Synchronized
}