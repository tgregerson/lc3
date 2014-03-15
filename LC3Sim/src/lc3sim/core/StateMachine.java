package lc3sim.core;

import java.util.HashSet;

import lc3sim.core.instructions.Instruction;
import lc3sim.core.instructions.OpCode;

// A state machine that controls the LC3 instruction cycle and generates control
// signals for logic.
public class StateMachine implements Listenable, Listener {
  // States for the phase state machine. These states may last for multiple
  // clock cycles in the LC3 architecture.
  public enum InstructionPhase {
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
    kFetchInstruction1(0),
    kFetchInstruction2(1),
    kFetchInstruction3(2),
    kEvaluateAddress1(3),
    kFetchOperands1(4),
    kExecuteOperation1(5),
    kExecuteOperation2(6),
    kStoreResult1(7),
    kDispatchInterrupt1(8);
    
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
    Init();
  }
  
  public void Init() {
    phase_ = InstructionPhase.kFetchInstruction;
    instruction_ = Instruction.FromBitWord(new BitWord(Instruction.kNumBits));
  }
  
  // Executes the phase in 'phase_' and advances it to the next phase.
  public void ExecuteCurrentPhase() {
    switch (phase_) {
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
  
  private void FetchInstruction() {
    // MAR <= PC, PC <= PC + 1
    // PcMuxSelect <= 00
    // PcTri <= 1
    // MarLoad <= 1
    // PcLoad <= 1
    // TODO: Also set PSR privilege bit based on address on bus?
    cycle_ = InstructionCycle.kFetchInstruction1;
    clock_.Tick();
    
    // MDR <= m[MAR]
    // MdrMuxSelect <= 1
    // MdrLoad <= 1
    cycle_ = InstructionCycle.kFetchInstruction2;
    clock_.Tick();

    // IR <= MDR
    // MdrTri <= 1
    // IrLoad <= 1
    cycle_ = InstructionCycle.kFetchInstruction3;
    clock_.Tick();
  }
  
  private void EvaluateAddress() {
    // MAR <= computed address
    cycle_ = InstructionCycle.kEvaluateAddress1;
    clock_.Tick();
  }
  
  private void FetchOperands() {
    // For instructions that read memory:
    // MDR <= mem[MAR]
    cycle_ = InstructionCycle.kFetchOperands1;
    clock_.Tick();
  }
  
  private void ExecuteOperation() {
    // Used by instructions that write memory, or do indirect reads.
    cycle_ = InstructionCycle.kExecuteOperation1;
    clock_.Tick();
    
    if (instruction_.op_code() == OpCode.LDI ||
        instruction_.op_code() == OpCode.STI) {
      cycle_ = InstructionCycle.kExecuteOperation2;
      clock_.Tick();
    }
  }
  
  private void StoreResult() {
    // Write to memory, write to the register file, or write to the PC.
    cycle_ = InstructionCycle.kStoreResult1;
    clock_.Tick();
  }
  
  public void SendNotification() {
    for (ListenerCallback cb : listener_callbacks_) {
      cb.set_arg(cycle_);
      cb.Run(BitWord.FALSE);
    }
  }
  
  // Listener
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    assert sender == OutputId.Ir;
    instruction_ = Instruction.FromBitWord(data);
  }
  
  // Listenable
  public void RegisterListenerCallback(ListenerCallback cb) {
    listener_callbacks_.add(cb);
  }

  public void UnregisterListener(Listener listener) {
    HashSet<ListenerCallback> keys_to_remove = new HashSet<ListenerCallback>();
    for (ListenerCallback cb : listener_callbacks_) {
      if (cb.listener() == listener) {
        keys_to_remove.add(cb);
      }
    }
    for (ListenerCallback key : keys_to_remove) {
      listener_callbacks_.remove(key);
    }
  }
  
  public void UnregisterListenerCallback(ListenerCallback cb) {
    listener_callbacks_.remove(cb);
  } 

  public void UnregisterAllListenerCallbacks() {
    listener_callbacks_.clear();
  }
  
  private CycleClock clock_;
  private InstructionPhase phase_;
  private InstructionCycle cycle_;
  private Instruction instruction_;
  
  private HashSet<ListenerCallback> listener_callbacks_;
}