package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.StateMachine.InstructionCycle;

public class RtiInstruction extends Instruction {
  public RtiInstruction(BitWord bitword) {
    super(bitword);
  }

  @Override
  public ControlSet ControlSet(InstructionCycle cycle, BitWord psr) {
    switch (cycle) {
      case kFetchInstruction1:
        return FetchInstruction1ControlSet();
      case kFetchInstruction2:
        return FetchInstruction2ControlSet();
      case kFetchInstruction3:
        return FetchInstruction3ControlSet();
      //case:
        // MAR <= R6
      //case:
        // MDR <= mem[MAR]
        // R6 <= R6 + 1
      //case:
        // PC <= MDR
      //case:
        // MAR <= R6
      //case:
        // MDR <= mem[MAR]
        // R6 <= R6 + 1
        // SSP.Saved <= R6 + 1
      //case:
        // PSR <= MDR
      //case:
        // R6 <= SSP.Saved or USP.Saved based on new privilege bit.
      //case:
      default:
        // Unused
        assert false;
        return null;
    }
  }
}
