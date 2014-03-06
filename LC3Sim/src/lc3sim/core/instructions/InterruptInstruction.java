package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.StateMachine.InstructionCycle;

// Class that manages the steps of executing an interrupt or exception.
// Interrupts and exceptions are not real instructions, however they
// can leverage the functionality of the Instruction class.
public class InterruptInstruction extends Instruction {
  public enum InterruptType {
    IllegalOpCodeException,
    PrivilegeModeException,
    ExternalInterrupt,
  }
  
  public InterruptInstruction(InterruptType type) {  
    super (new BitWord(Instruction.kNumBits));
  }

  public ControlSet ControlSet(InstructionCycle cycle, BitWord psr) {
    ControlSet control_set = new ControlSet();
    switch (cycle) {
      case kFetchInstruction1:
        return FetchInstruction1ControlSet();
      case kFetchInstruction2:
        return FetchInstruction2ControlSet();
      case kDecodeInstruction1:
        return DecodeInstruction1ControlSet();
      case:
        // Load MAR with R6
        // Load Saved.USP or Saved.SSP with R6, based on privilege bit.
        break;
      case:
        // Load MDR with PSR
        // Set PSR privilege bit to 0.
        // Load PSR priority bits
        break;
      case:
        // Write memory
        // R6 = R6 - 1
      case:
        // Load MAR with R6
      case:
        // Load MDR with PC
      case:
        // Write memory
        // R6 = R6 - 1
      case:
        // PC = Interrupt vector + 256.
      default:
        assert false;
        return null;
    }
    return null;
  }

}