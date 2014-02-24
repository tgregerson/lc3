package lc3sim.core;

// Encapsulates the architectural state of the LC3 processor
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
    pc_.UnregisterAllListenerCallbacks();
    ir_.UnregisterAllListenerCallbacks();
    mar_.UnregisterAllListenerCallbacks();
    mdr_.UnregisterAllListenerCallbacks();
    psr_.UnregisterAllListenerCallbacks();
    gpr_.UnregisterAllListenerCallbacks();
    memory_.UnregisterAllListenerCallbacks();
    alu_.UnregisterAllListenerCallbacks();
    addr_adder_.UnregisterAllListenerCallbacks();
    pc_incrementer_.UnregisterAllListenerCallbacks();
    sign_extend_11_.UnregisterAllListenerCallbacks();
    sign_extend_9_.UnregisterAllListenerCallbacks();
    sign_extend_6_.UnregisterAllListenerCallbacks();
    sign_extend_5_.UnregisterAllListenerCallbacks();
    zero_extend_8_.UnregisterAllListenerCallbacks();
    pc_mux_.UnregisterAllListenerCallbacks();
    mar_mux_.UnregisterAllListenerCallbacks();
    mdr_mux_.UnregisterAllListenerCallbacks();
    sr2_mux_.UnregisterAllListenerCallbacks();
    addr1_mux_.UnregisterAllListenerCallbacks();
    addr2_mux_.UnregisterAllListenerCallbacks();
    pc_tri_.UnregisterAllListenerCallbacks();
    mar_mux_tri_.UnregisterAllListenerCallbacks();
    mdr_tri_.UnregisterAllListenerCallbacks();
    alu_tri_.UnregisterAllListenerCallbacks();
    bus_.UnregisterAllListenerCallbacks();
  }
  
  public void AddAllListenerBindings() {
    // Architectural connections
    pc_.RegisterListenerCallback(new ListenerCallback(
        pc_incrementer_, OutputId.Pc, InputId.PcIncrement, null));
    pc_.RegisterListenerCallback(new ListenerCallback(
        addr1_mux_, OutputId.Pc, InputId.AddrAdder1MuxData0, null));
    pc_.RegisterListenerCallback(new ListenerCallback(
        pc_tri_, OutputId.Pc, InputId.PcTriData, null));
    
    ir_.RegisterListenerCallback(new ListenerCallback(
        sign_extend_11_, OutputId.Ir, InputId.IrSext11, null));
    ir_.RegisterListenerCallback(new ListenerCallback(
        sign_extend_9_, OutputId.Ir, InputId.IrSext9, null));
    ir_.RegisterListenerCallback(new ListenerCallback(
        sign_extend_6_, OutputId.Ir, InputId.IrSext6, null));
    ir_.RegisterListenerCallback(new ListenerCallback(
        sign_extend_5_, OutputId.Ir, InputId.IrSext5, null));
    ir_.RegisterListenerCallback(new ListenerCallback(
        zero_extend_8_, OutputId.Ir, InputId.IrZext8, null));
    
    mar_.RegisterListenerCallback(new ListenerCallback(
        memory_, OutputId.Mar, InputId.MemoryAddr, null));
    
    mdr_.RegisterListenerCallback(new ListenerCallback(
        memory_, OutputId.Mdr, InputId.MemoryData, null));
    mdr_.RegisterListenerCallback(new ListenerCallback(
        mdr_tri_, OutputId.Mdr, InputId.MdrTriData, null));
    
    // TODO PSR
    
    gpr_.RegisterListenerCallback(new ListenerCallback(
        alu_, OutputId.GprSr1, InputId.AluA, null));
    gpr_.RegisterListenerCallback(new ListenerCallback(
        addr1_mux_, OutputId.GprSr1, InputId.AddrAdder1MuxData1, null));
    gpr_.RegisterListenerCallback(new ListenerCallback(
        sr2_mux_, OutputId.GprSr2, InputId.AluA, null));
    
    memory_.RegisterListenerCallback(new ListenerCallback(
        mdr_mux_, OutputId.Memory, InputId.MdrMuxData1, null));
    
    alu_.RegisterListenerCallback(new ListenerCallback(
        alu_tri_, OutputId.Alu, InputId.AluTriData, null));
    
    addr_adder_.RegisterListenerCallback(new ListenerCallback(
        mar_mux_, OutputId.AddrAdder, InputId.MarMuxData0, null));
    addr_adder_.RegisterListenerCallback(new ListenerCallback(
        pc_mux_, OutputId.AddrAdder, InputId.PcMuxData01, null));
    
    pc_incrementer_.RegisterListenerCallback(new ListenerCallback(
        pc_mux_, OutputId.PcIncrement, InputId.PcMuxData00, null));
    
    sign_extend_11_.RegisterListenerCallback(new ListenerCallback(
        addr2_mux_, OutputId.IrSext11, InputId.AddrAdder2MuxData11, null));
    sign_extend_9_.RegisterListenerCallback(new ListenerCallback(
        addr2_mux_, OutputId.IrSext9, InputId.AddrAdder2MuxData10, null));
    sign_extend_6_.RegisterListenerCallback(new ListenerCallback(
        addr2_mux_, OutputId.IrSext6, InputId.AddrAdder2MuxData01, null));
    sign_extend_5_.RegisterListenerCallback(new ListenerCallback(
        sr2_mux_, OutputId.IrSext5, InputId.Sr2MuxData1, null));
    zero_extend_8_.RegisterListenerCallback(new ListenerCallback(
        mar_mux_, OutputId.IrZext8, InputId.MarMuxData1, null));
    
    pc_mux_.RegisterListenerCallback(new ListenerCallback(
        pc_, OutputId.PcMux, InputId.Pc, null));
    
    mar_mux_.RegisterListenerCallback(new ListenerCallback(
        mar_mux_tri_, OutputId.MarMux, InputId.MarMuxTriData, null));
    
    mdr_mux_.RegisterListenerCallback(new ListenerCallback(
        mdr_, OutputId.MdrMux, InputId.Mdr, null));
    
    sr2_mux_.RegisterListenerCallback(new ListenerCallback(
        alu_, OutputId.Sr2Mux, InputId.AluB, null));
    
    addr1_mux_.RegisterListenerCallback(new ListenerCallback(
        addr_adder_, OutputId.Addr1Mux, InputId.AddrAdderA, null));
    
    addr2_mux_.RegisterListenerCallback(new ListenerCallback(
        addr_adder_, OutputId.Addr2Mux, InputId.AddrAdderB, null));
    
    pc_tri_.RegisterListenerCallback(new ListenerCallback(
        bus_, OutputId.PcTri, InputId.Bus, null));
    
    mar_mux_tri_.RegisterListenerCallback(new ListenerCallback(
        bus_, OutputId.MarMuxTri, InputId.Bus, null));
    
    mdr_tri_.RegisterListenerCallback(new ListenerCallback(
        bus_, OutputId.MdrTri, InputId.Bus, null));
    
    alu_tri_.RegisterListenerCallback(new ListenerCallback(
        bus_, OutputId.AluTri, InputId.Bus, null));
    
    bus_.RegisterListenerCallback(new ListenerCallback(
        mar_, OutputId.Bus, InputId.Mar, null));
    bus_.RegisterListenerCallback(new ListenerCallback(
        branch_flag_logic_, OutputId.Bus, InputId.NzpLogic, null));
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
      new Register(16, InputId.Pc, InputId.PcLoad, OutputId.Pc);
  private final Register ir_ =
      new Register(16, InputId.Ir, InputId.IrLoad, OutputId.Ir);
  private final Register mar_ =
      new Register(16, InputId.Mar, InputId.MarLoad, OutputId.Mar);
  private final Register mdr_ =
      new Register(16, InputId.Mdr, InputId.MdrLoad, OutputId.Mdr);
  private final ProcessorStatusRegister psr_ =
      new ProcessorStatusRegister();
  
  // Register file
  private final RegisterFile gpr_ = new RegisterFile();
  
  // Memory
  private final Memory memory_ = new Memory();
  
  // Arithmetic units
  private final ALU alu_ = new ALU();
  private final Adder addr_adder_ =
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
  
  // Additional logic
  private final BranchFlagLogic branch_flag_logic_ = new BranchFlagLogic();
  
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
  private final Bus bus_ = new Bus();

}
