package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.InstructionCycle;

// Class that manages the steps of executing an interrupt or exception.
// Interrupts and exceptions are not real instructions, however they
// can leverage the functionality of the Instruction class.
public class InterruptInstruction extends Instruction {
  public enum InterruptType {
    IllegalOpCodeException,
    PrivilegeModeException,
    ExternalInterrupt,
  }

  @Override
  public String toString() {
    return "INTERRUPT";
  }
  
  public InterruptInstruction(InterruptType type) {  
    super (BitWord.Zeroes(Instruction.kNumBits));
  }

  public ControlSet ControlSet(InstructionCycle cycle, BitWord psr) {
    switch (cycle) {
      case kFetchInstruction1:
        return FetchInstruction1ControlSet();
      case kFetchInstruction2:
        return FetchInstruction2ControlSet();
      case kFetchInstruction3:
        return FetchInstruction3ControlSet();
      //case:
        // Load MAR with R6
        // Load Saved.USP or Saved.SSP with R6, based on privilege bit.
      //case:
        // Load MDR with PSR
        // Set PSR privilege bit to 0.
        // Load PSR priority bits
      //case:
        // Write memory
        // R6 = R6 - 1
      //case:
        // Load MAR with R6
      //case:
        // Load MDR with PC
      //case:
        // Write memory
        // R6 = R6 - 1
      //case:
        // PC = Interrupt vector + 256.
      default:
        assert false;
        return null;
    }
  }

  @Override
  public InstructionCycle NextCycle(InstructionCycle current_cycle) {
    assert false;
    return null;
  }
}
