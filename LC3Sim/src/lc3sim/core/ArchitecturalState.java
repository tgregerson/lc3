package lc3sim.core;

// Encapulates the architectural state of the LC3 processor
public class ArchitecturalState {
  public ArchitecturalState() {
    Init();
  }
  
  public void Init();
  
  // Logic elements
  
  // Individual registers
  private final Register pc_ =
      new Register(16, InputId.Pc, OutputId.Pc, false);
  private final Register ir_ =
      new Register(16, InputId.Ir, OutputId.Ir, false);
  private final Register psr_ =
      new Register(16, InputId.Psr, OutputId.Psr);
  private final Register mar_ =
      new Register(16, InputId.Mar, OutputId.Mar, true);
  private final Register mdr_ =
      new Register(16, InputId.Mdr, OutputId.Mdr, true);
  
  // Register file
  private final RegisterFile gpr_ = new RegisterFile();
  
  // Memory
  private final Memory memory_ = new Memory();
  
  // Arithmetic units
  private final ALU alu_ = new ALU();
  private final Adder addr_adder_1_ = new Adder();
  private final Adder addr_adder_2_ = new Adder();
  private final Incrementer pc_incrementer_ = new Incrementer();
  private final BitExtender sign_extend_11_ =
      new BitExtender(OutputId.IrSext11, 11, 16, true);

}
