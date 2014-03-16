package lc3sim.core;

// States for the cycle state machine. These states represent one clock cycle
// in the LC3 architecture.
public enum InstructionCycle {
  // State machine reset state.
  kReset(0),

  // Loads MAR with address of next instruction.
  // MAR <= PC, PC <= PC + 1
  // PcMuxSelect <= 00
  // PcTri <= 1
  // MarLoad <= 1
  // PcLoad <= 1
  kFetchInstruction1(1),

  // Load instruction from memory to MDR.
  // MDR <= m[MAR]
  // MdrMuxSelect <= 1
  // MdrLoad <= 1
  kFetchInstruction2(2),

  // Load instruction from MDR to IR.
  // IR <= MDR
  // MdrTri <= 1
  // IrLoad <= 1
  kFetchInstruction3(3),

  // For instructions that require memory access, load MAR with address.
  // MAR <= computed address
  kEvaluateAddress1(4),

  // For instructions that read memory, load MDR with data from memory.
  // MDR <= mem[MAR]
  kFetchOperands1(5),

  // If instruction directly writes memory, load MDR with data. If instruction
  // indirectly reads/writes memory, load MAR with direct address.
  kExecuteOperation1(6),
  
  // If instruction indirectly reads/writes memory, load MDR with data.
  kExecuteOperation2(7),
  
  // Load destination register, PC, or perform memory write.
  kStoreResult1(8);

  
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
