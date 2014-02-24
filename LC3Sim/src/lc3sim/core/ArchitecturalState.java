package lc3sim.core;

// Encapulates the architectural state of the LC3 processor
public class ArchitecturalState {
  public ArchitecturalState() {
    Multiplexer.AddressBinding[] pc_mux_bindings = {
      new Multiplexer.AddressBinding(BitWord.FromInt(0), InputId.PcMuxData00),
      new Multiplexer.AddressBinding(BitWord.FromInt(1), InputId.PcMuxData01),
      new Multiplexer.AddressBinding(BitWord.FromInt(2), InputId.PcMuxData10)
    };
    pc_mux_ = new Multiplexer(2, 16, pc_mux_bindings, InputId.PcMuxSel,
                              OutputId.PcMux);
    
    Multiplexer.AddressBinding[] mar_mux_bindings = {
        new Multiplexer.AddressBinding(BitWord.FromInt(0), InputId.MarMuxData0 ),
        new Multiplexer.AddressBinding(BitWord.FromInt(1), InputId.MarMuxData1),
    };
    mar_mux_ = new Multiplexer(1, 16, mar_mux_bindings, InputId.MarMuxSel,
                               OutputId.MarMux);
    
    Multiplexer.AddressBinding[] mdr_mux_bindings = {
        new Multiplexer.AddressBinding(BitWord.FromInt(0), InputId.MdrMuxData0 ),
        new Multiplexer.AddressBinding(BitWord.FromInt(1), InputId.MdrMuxData1),
    };
    mdr_mux_ = new Multiplexer(1, 16, mdr_mux_bindings, InputId.MdrMuxSel,
                               OutputId.MdrMux);
    
    Multiplexer.AddressBinding[] sr2_mux_bindings = {
        new Multiplexer.AddressBinding(BitWord.FromInt(0), InputId.Sr2MuxData0),
        new Multiplexer.AddressBinding(BitWord.FromInt(1), InputId.Sr2MuxData1),
    };
    sr2_mux_ = new Multiplexer(1, 16, sr2_mux_bindings, InputId.Sr2MuxSel,
                               OutputId.Sr2Mux);
    
    Multiplexer.AddressBinding[] addr1_mux_bindings = {
        new Multiplexer.AddressBinding(BitWord.FromInt(0), InputId.AddrAdder1MuxData0),
        new Multiplexer.AddressBinding(BitWord.FromInt(1), InputId.AddrAdder1MuxData1),
    };
    addr1_mux_ = new Multiplexer(1, 16, addr1_mux_bindings, InputId.AddrAdder1MuxSel,
                                 OutputId.Addr1Mux);
    
    Multiplexer.AddressBinding[] addr2_mux_bindings = {
        new Multiplexer.AddressBinding(BitWord.FromInt(0), InputId.AddrAdder2MuxData00),
        new Multiplexer.AddressBinding(BitWord.FromInt(1), InputId.AddrAdder2MuxData01),
        new Multiplexer.AddressBinding(BitWord.FromInt(2), InputId.AddrAdder2MuxData10),
        new Multiplexer.AddressBinding(BitWord.FromInt(3), InputId.AddrAdder2MuxData11),
    };
    addr2_mux_ = new Multiplexer(2, 16, addr2_mux_bindings, InputId.AddrAdder2MuxSel,
                                 OutputId.Addr2Mux);
  }
  
  public void Init() {
    RemoveAllListenerBindings();
    AddAllListenerBindings();
  }
  
  public void RemoveAllListenerBindings() {
    
  }
  
  public void AddAllListenerBindings() {
    
  }
  
  public int ExecuteInstruction() {
    state_machine_.ExecuteInstruction();
    return pc();
  }
  
  public int pc() {
    return pc_.Read().ToInt();
  }
  
  // State Machine
  private final StateMachine state_machine_ = new StateMachine();
  
  // Logic elements
  
  // Individual registers
  private final Register pc_ =
      new Register(16, InputId.Pc, OutputId.Pc, false);
  private final Register ir_ =
      new Register(16, InputId.Ir, OutputId.Ir, false);
  private final Register mar_ =
      new Register(16, InputId.Mar, OutputId.Mar, true);
  private final Register mdr_ =
      new Register(16, InputId.Mdr, OutputId.Mdr, true);
  private final ProcessorStatusRegister psr_ =
      new ProcessorStatusRegister();
  
  // Register file
  private final RegisterFile gpr_ = new RegisterFile();
  
  // Memory
  private final Memory memory_ = new Memory();
  
  // Arithmetic units
  private final ALU alu_ = new ALU();
  private final Adder addr_adder_1_ =
      new Adder(InputId.AddrAdder1MuxData0, InputId.AddrAdder1MuxData1,
                OutputId.AddrAdder, 16);
  private final Adder addr_adder_2_ =
      new Adder(InputId.AddrAdder1MuxData0, InputId.AddrAdder1MuxData1,
          OutputId.AddrAdder, 16);
  private final Incrementer pc_incrementer_ =
      new Incrementer(OutputId.PcIncrement, 16);
  private final BitExtender sign_extend_11_ =
      new BitExtender(OutputId.IrSext11, 11, 16, true);
  private final BitExtender sign_extend_9_ =
      new BitExtender(OutputId.IrSext9, 9, 16, true);
  private final BitExtender sign_extend_6_ =
      new BitExtender(OutputId.IrSext6, 6, 16, true);
  private final BitExtender sign_extend_5_ =
      new BitExtender(OutputId.IrSext5, 5, 16, true);
  private final BitExtender zero_extend_8_ =
      new BitExtender(OutputId.IrZext8, 8, 16, true);
  
  // Multiplexers - Initialized in constructor
  private final Multiplexer pc_mux_;
  private final Multiplexer mar_mux_;
  private final Multiplexer mdr_mux_;
  private final Multiplexer sr2_mux_;
  private final Multiplexer addr1_mux_;
  private final Multiplexer addr2_mux_;
  
  // Tristate Buffers
  private final TriStateBuffer pc_tri_ = new TriStateBuffer(
      OutputId.PcTri, InputId.PcTriData, InputId.PcTriEnable);
  private final TriStateBuffer mar_mux_tri_ = new TriStateBuffer(
      OutputId.MarMuxTri, InputId.MarMuxTriData, InputId.MarMuxTriEnable);
  private final TriStateBuffer mdr_tri_ = new TriStateBuffer(
      OutputId.MdrTri, InputId.MdrTriData, InputId.MdrTriEnable);
  private final TriStateBuffer alu_tri_ = new TriStateBuffer(
      OutputId.AluTri, InputId.AluTriData, InputId.AluTriEnable);
  
  // Shared Bus


}
