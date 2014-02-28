package lc3sim.core;

import java.util.HashSet;

// A state machine that controls the LC3 instruction cycle and generates control
// signals for logic.
public class StateMachine implements Listenable, Listener {
  // States for the phase state machine. These states may last for multiple
  // clock cycles in the LC3 architecture.
  public enum InstructionPhase {
    kFetchInstruction,
    kDecodeInstruction,
    kEvaluateAddress,
    kFetchOperands,
    kExecuteOperation,
    kStoreResult,
    kInvalid,
  }
  
  // States for the cycle state machine. These states represent one clock cycle
  // in the LC3 architecture.
  public enum InstructionCycle {
    kFetchInstruction1(0),
    kFetchInstruction2(1),
    kDecodeInstruction1(2);
    
    private InstructionCycle(int code) {
      code_as_int_ = code;
      code_as_bit_word_ = BitWord.FromInt(code).Resize(kStateBits, false);
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
    instruction_ = new BitWord(16);
  }
  
  // Executes the phase in 'phase_' and advances it to the next phase.
  public void ExecuteCurrentPhase() {
    switch (phase_) {
      case kFetchInstruction:
        FetchInstruction();
        break;
      case kDecodeInstruction:
        DecodeInstruction();
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
    // TODO Put these magic numbers in a static class somewhere.
    OpCode op_code = OpCode.Lookup(instruction_.GetBitRange(15, 12));
    assert op_code != null;
    switch (phase_) {
      // TODO Incorporate op code into which phases are executed.
      case kFetchInstruction:
        // All ops have DECODE
        return InstructionPhase.kDecodeInstruction;
      case kDecodeInstruction:
        // Ops that have a calculated address go to EvaluateAddress
        
        // Ops with operands in the register file can skip to FetchOperands
        return InstructionPhase.kEvaluateAddress;
      case kEvaluateAddress:
        return InstructionPhase.kFetchOperands;
      case kFetchOperands:
        return InstructionPhase.kExecuteOperation;
      case kExecuteOperation:
        return InstructionPhase.kStoreResult;
      case kStoreResult:
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
  }
  
  private void DecodeInstruction() {
    // IR <= MDR
    // MdrTri <= 1
    // IrLoad <= 1
    cycle_ = InstructionCycle.kDecodeInstruction1;
    clock_.Tick();
  }
  
  private void EvaluateAddress() {
    
  }
  
  private void FetchOperands() {
    
  }
  
  private void ExecuteOperation() {
    
  }
  
  private void StoreResult() {
    
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
    instruction_ = data.Resize(16, false);
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
  private BitWord instruction_;
  
  private HashSet<ListenerCallback> listener_callbacks_;
}