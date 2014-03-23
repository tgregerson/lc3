package lc3sim.core;

// Encapsulates the architectural state of the LC3 processor
public class ArchitecturalState {
  public static final int kWordSize = 16;
  
  // Integer address values for multiplexer inputs
  public static final int kData0Addr = 0;
  public static final int kData1Addr = 1;
  public static final int kData00Addr = 0;
  public static final int kData01Addr = 1;
  public static final int kData10Addr = 2;
  public static final int kData11Addr = 3;
  
  // PC Mux
  public static final int kNumPcMuxSelectBits = 2;
  
  // Mar Mux
  public static final int kNumMarMuxSelectBits = 1;
  
  // Mdr Mux
  public static final int kNumMdrMuxSelectBits = 1;
  
  // Sr2 Mux
  public static final int kNumSr2MuxSelectBits = 1;
  
  // Addr1 Mux
  public static final int kNumAddr1MuxSelectBits = 1;
  
  // Addr2 Mux
  public static final int kNumAddr2MuxSelectBits = 2;
  
  public ArchitecturalState() {
    // Multipliers require more complex initialization. All other elements
    // are initialized as they are declared.
    Multiplexer.AddressBinding[] pc_mux_bindings = {
      new Multiplexer.AddressBinding(
          BitWord.FromInt(kData00Addr, kNumPcMuxSelectBits), InputId.PcMuxData00),
      new Multiplexer.AddressBinding(
          BitWord.FromInt(kData01Addr, kNumPcMuxSelectBits), InputId.PcMuxData01),
      new Multiplexer.AddressBinding(
          BitWord.FromInt(kData10Addr, kNumPcMuxSelectBits), InputId.PcMuxData10)
    };
    pc_mux_ = new Multiplexer(
        kNumPcMuxSelectBits, kWordSize, pc_mux_bindings, InputId.PcMuxSel,
        OutputId.PcMux);
    
    Multiplexer.AddressBinding[] mar_mux_bindings = {
        new Multiplexer.AddressBinding(
            BitWord.FromInt(kData0Addr, kNumMarMuxSelectBits), InputId.MarMuxData0),
        new Multiplexer.AddressBinding(
            BitWord.FromInt(kData1Addr, kNumMarMuxSelectBits), InputId.MarMuxData1),
    };
    mar_mux_ = new Multiplexer(
        kNumMarMuxSelectBits, kWordSize, mar_mux_bindings, InputId.MarMuxSel,
        OutputId.MarMux);
    
    Multiplexer.AddressBinding[] mdr_mux_bindings = {
        new Multiplexer.AddressBinding(
            BitWord.FromInt(kData0Addr, kNumMdrMuxSelectBits), InputId.MdrMuxData0),
        new Multiplexer.AddressBinding(
            BitWord.FromInt(kData1Addr, kNumMdrMuxSelectBits), InputId.MdrMuxData1),
    };
    mdr_mux_ = new Multiplexer(
        kNumMdrMuxSelectBits, kWordSize, mdr_mux_bindings, InputId.MdrMuxSel,
        OutputId.MdrMux);
    
    Multiplexer.AddressBinding[] sr2_mux_bindings = {
        new Multiplexer.AddressBinding(
            BitWord.FromInt(kData0Addr, kNumSr2MuxSelectBits), InputId.Sr2MuxData0),
        new Multiplexer.AddressBinding(
            BitWord.FromInt(kData1Addr, kNumSr2MuxSelectBits), InputId.Sr2MuxData1),
    };
    sr2_mux_ = new Multiplexer(
        kNumSr2MuxSelectBits, kWordSize, sr2_mux_bindings, InputId.Sr2MuxSel,
        OutputId.Sr2Mux);
    
    Multiplexer.AddressBinding[] addr1_mux_bindings = {
        new Multiplexer.AddressBinding(
            BitWord.FromInt(kData0Addr, kNumAddr1MuxSelectBits), InputId.AddrAdder1MuxData0),
        new Multiplexer.AddressBinding(
            BitWord.FromInt(kData1Addr, kNumAddr1MuxSelectBits), InputId.AddrAdder1MuxData1),
    };
    addr1_mux_ = new Multiplexer(
        kNumAddr1MuxSelectBits, kWordSize, addr1_mux_bindings, InputId.AddrAdder1MuxSel,
        OutputId.Addr1Mux);
    
    Multiplexer.AddressBinding[] addr2_mux_bindings = {
        new Multiplexer.AddressBinding(
            BitWord.FromInt(kData00Addr, kNumAddr2MuxSelectBits), InputId.AddrAdder2MuxData00),
        new Multiplexer.AddressBinding(
            BitWord.FromInt(kData01Addr, kNumAddr2MuxSelectBits), InputId.AddrAdder2MuxData01),
        new Multiplexer.AddressBinding(
            BitWord.FromInt(kData10Addr, kNumAddr2MuxSelectBits), InputId.AddrAdder2MuxData10),
        new Multiplexer.AddressBinding(
            BitWord.FromInt(kData11Addr, kNumAddr2MuxSelectBits), InputId.AddrAdder2MuxData11),
    };
    addr2_mux_ = new Multiplexer(
        kNumAddr2MuxSelectBits, kWordSize, addr2_mux_bindings, InputId.AddrAdder2MuxSel,
        OutputId.Addr2Mux);
  }
  
  public void Init() {
    RemoveAllListenerBindings();
    AddAllListenerBindings();
  }
  
  // Methods for setting internal state. All setting methods take input as an
  // ints. values should be treated as unsigned.
  public void SetPc(int val) {
    pc_.Notify(BitWord.FromInt(val, kWordSize), OutputId.External, InputId.Pc,
               null);
  }

  public void SetIr(int val) {
    ir_.Notify(BitWord.FromInt(val, kWordSize), OutputId.External, InputId.Ir,
               null);
  }

  public void SetMar(int val) {
    mar_.Notify(BitWord.FromInt(val, kWordSize), OutputId.External, InputId.Mar,
                null);
  }

  public void SetMdr(int val) {
    mdr_.Notify(BitWord.FromInt(val, kWordSize), OutputId.External, InputId.Mdr,
                null);
  }

  public void SetPsr(int val) {
    assert (val == 1 || val == 2 || val == 4);
    psr_.Notify(BitWord.FromInt(val, kWordSize), OutputId.External, InputId.Psr,
                null);
  }

  public void SetGpr(int reg_num, int val) {
    RegisterFile.RegisterStateUpdate update =
        new RegisterFile.RegisterStateUpdate(
            reg_num, BitWord.FromInt(val, kWordSize));
    gpr_.Notify(null, OutputId.External, null, update);
  }
  
  public void SetMemory(int address, int val) {
    Memory.MemoryStateUpdate update = new Memory.MemoryStateUpdate(
        BitWord.FromInt(address, kWordSize), BitWord.FromInt(val, kWordSize));
    memory_.Notify(null, OutputId.External, null, update);
  }

  
  public void RemoveAllListenerBindings() {
    state_machine_.UnregisterAllListenerCallbacks();
    control_logic_.UnregisterAllListenerCallbacks();
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
    state_machine_.RegisterListenerCallback(new ListenerCallback(
        control_logic_, OutputId.StateMachineCycle, InputId.ControlState,
        null));
    state_machine_.RegisterListenerCallback(new ListenerCallback(
        control_logic_, OutputId.StateMachineInstruction,
        InputId.ControlInstruction, null));
    
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        addr1_mux_, OutputId.ControlAddr1MuxSelect, InputId.AddrAdder1MuxSel,
        null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        addr2_mux_, OutputId.ControlAddr2MuxSelect, InputId.AddrAdder2MuxSel,
        null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        alu_, OutputId.ControlAluK, InputId.AluK, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        alu_tri_, OutputId.ControlAluTriEnable, InputId.AluTriEnable, null));
    // TODO bus increment/decrement
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        gpr_, OutputId.ControlGprDrAddr, InputId.GprDrLoad, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        gpr_, OutputId.ControlGprDrLoad, InputId.GprDrAddr, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        gpr_, OutputId.ControlGprSr1Addr, InputId.GprSr1Addr, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        gpr_, OutputId.ControlGprSr2Addr, InputId.GprSr2Addr, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        ir_, OutputId.ControlIrLoad, InputId.IrLoad, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        mar_, OutputId.ControlMarLoad, InputId.MarLoad, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        mar_mux_, OutputId.ControlMarMuxSelect, InputId.MarMuxSel, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        mar_mux_tri_, OutputId.ControlMarMuxTriEnable, InputId.MarMuxTriEnable,
        null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        mdr_, OutputId.ControlMdrLoad, InputId.MdrLoad, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        mdr_tri_, OutputId.ControlMdrTriEnable, InputId.MdrTriEnable, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        mdr_mux_, OutputId.ControlMdrMuxSelect, InputId.MdrMuxSel, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        memory_, OutputId.ControlMemoryWriteEnable, InputId.MemoryWriteEnable,
        null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        pc_, OutputId.ControlPcLoad, InputId.PcLoad, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        pc_mux_, OutputId.ControlPcMuxSelect, InputId.PcMuxSel, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        pc_tri_, OutputId.ControlPcTriEnable, InputId.PcTriEnable, null));
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        psr_, OutputId.ControlPsrLoad, InputId.PsrLoad, null));
    // TODO Saved SP reg + mux + tri
    control_logic_.RegisterListenerCallback(new ListenerCallback(
        sr2_mux_, OutputId.ControlSr2MuxSelect, InputId.Sr2MuxSel, null));
    
    pc_.RegisterListenerCallback(new ListenerCallback(
        pc_incrementer_, OutputId.Pc, InputId.PcIncrementer, null));
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
    ir_.RegisterListenerCallback(new ListenerCallback(
        state_machine_, OutputId.Ir, InputId.StateMachineInstruction, null));
    
    mar_.RegisterListenerCallback(new ListenerCallback(
        memory_, OutputId.Mar, InputId.MemoryAddr, null));
    
    mdr_.RegisterListenerCallback(new ListenerCallback(
        memory_, OutputId.Mdr, InputId.MemoryData, null));
    mdr_.RegisterListenerCallback(new ListenerCallback(
        mdr_tri_, OutputId.Mdr, InputId.MdrTriData, null));
    
    // TODO USP, SSP, SP_Mux, SP_Mux_tri

    branch_flag_logic_.RegisterListenerCallback(
        new ListenerCallback(psr_, OutputId.NzpLogic, InputId.Psr, null));
    
    psr_.RegisterListenerCallback(new ListenerCallback(
        control_logic_, OutputId.Psr, InputId.ControlPsr, null));

    
    gpr_.RegisterListenerCallback(new ListenerCallback(
        alu_, OutputId.GprSr1, InputId.AluA, null));
    gpr_.RegisterListenerCallback(new ListenerCallback(
        addr1_mux_, OutputId.GprSr1, InputId.AddrAdder1MuxData1, null));
    gpr_.RegisterListenerCallback(new ListenerCallback(
        sr2_mux_, OutputId.GprSr2, InputId.Sr2MuxData0, null));
    
    memory_.RegisterListenerCallback(new ListenerCallback(
        mdr_mux_, OutputId.Memory, InputId.MdrMuxData1, null));
    
    alu_.RegisterListenerCallback(new ListenerCallback(
        alu_tri_, OutputId.Alu, InputId.AluTriData, null));
    
    addr_adder_.RegisterListenerCallback(new ListenerCallback(
        mar_mux_, OutputId.AddrAdder, InputId.MarMuxData0, null));
    addr_adder_.RegisterListenerCallback(new ListenerCallback(
        pc_mux_, OutputId.AddrAdder, InputId.PcMuxData01, null));
    
    pc_incrementer_.RegisterListenerCallback(new ListenerCallback(
        pc_mux_, OutputId.PcIncrementer, InputId.PcMuxData00, null));
    
    /*
    bus_incrementer_.RegisterListenerCallback(new ListenerCallback(
        bus_increment_tri_, OutputId.BusIncrementer, InputId.Bus, null));
    bus_incrementer_.RegisterListenerCallback(new ListenerCallback(
        bus_decrement_tri_, OutputId.BusIncrementer, InputId.Bus, null));
        */
    
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
        gpr_, OutputId.Bus, InputId.GprDrData, null));
    bus_.RegisterListenerCallback(new ListenerCallback(
        mar_, OutputId.Bus, InputId.Mar, null));
    bus_.RegisterListenerCallback(new ListenerCallback(
        ir_, OutputId.Bus, InputId.Ir, null));
    bus_.RegisterListenerCallback(new ListenerCallback(
        branch_flag_logic_, OutputId.Bus, InputId.NzpLogic, null));
    bus_.RegisterListenerCallback(new ListenerCallback(
        pc_mux_, OutputId.Bus, InputId.PcMuxData10, null));
    bus_.RegisterListenerCallback(new ListenerCallback(
        mdr_mux_, OutputId.Bus, InputId.MdrMuxData0, null));
    bus_.RegisterListenerCallback(new ListenerCallback(
        branch_flag_logic_, OutputId.Bus, InputId.NzpLogic, null));
    /*
    bus_.RegisterListenerCallback(new ListenerCallback(
        bus_incrementer_, OutputId.Bus, InputId.BusIncrementer, null));
    bus_.RegisterListenerCallback(new ListenerCallback(
        bus_decrementer_, OutputId.Bus, InputId.BusDecrementer, null));
        */
  }
  
  public int ExecuteInstruction() {
    state_machine_.ExecuteInstruction();
    return pc();
  }
  
  public int pc() {
    return pc_.Read().ToInt();
  }
  
  // Control Plane
  private final CycleClock cycle_clock_ = new CycleClock();
  private final StateMachine state_machine_ = new StateMachine(cycle_clock_);
  private final ControlLogic control_logic_ = new ControlLogic();
  
  // Individual registers
  private final Register pc_ =
      new Register(kWordSize, InputId.Pc, InputId.PcLoad, OutputId.Pc);
  private final Register ir_ =
      new Register(kWordSize, InputId.Ir, InputId.IrLoad, OutputId.Ir);
  private final Register mar_ =
      new Register(kWordSize, InputId.Mar, InputId.MarLoad, OutputId.Mar);
  private final Register mdr_ =
      new Register(kWordSize, InputId.Mdr, InputId.MdrLoad, OutputId.Mdr);
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
                OutputId.AddrAdder, kWordSize);
  private final ConstantAdder pc_incrementer_ =
      new ConstantAdder(OutputId.PcIncrementer, 1, kWordSize);
  /*
  // TODO: More logic for supporting interrupts. SavedSP, muxes, etc.
  private final ConstantAdder bus_incrementer_ =
      new ConstantAdder(OutputId.BusIncrementer, 1, kWordSize);
  private final ConstantAdder bus_decrementer_ =
      new ConstantAdder(OutputId.BusDecrementer, -1, kWordSize);
      */
  private final BitExtender sign_extend_11_ =
      new BitExtender(OutputId.IrSext11, 11, kWordSize, true);
  private final BitExtender sign_extend_9_ =
      new BitExtender(OutputId.IrSext9, 9, kWordSize, true);
  private final BitExtender sign_extend_6_ =
      new BitExtender(OutputId.IrSext6, 6, kWordSize, true);
  private final BitExtender sign_extend_5_ =
      new BitExtender(OutputId.IrSext5, 5, kWordSize, true);
  private final BitExtender zero_extend_8_ =
      new BitExtender(OutputId.IrZext8, 8, kWordSize, true);
  
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
  /*
  private final TriStateBuffer bus_increment_tri_ = new TriStateBuffer(
      OutputId.BusIncrementerTri, InputId.BusIncrementerTriData,
      InputId.BusIncrementerTriEnable);
  private final TriStateBuffer bus_decrement_tri_ = new TriStateBuffer(
      OutputId.BusDecrementerTri, InputId.BusDecrementerTriData,
      InputId.BusDecrementerTriEnable);
      */
  
  // Shared Bus
  private final Bus bus_ = new Bus();

}
