package lc3sim.core;

// Provide unique IDs for all inputs and outputs visible in the architectural
// view.
public enum InputId {
  // Bus
  Bus,
  
  // Synchronous logic inputs
  Pc,
  PcLoad,
  Ir,
  IrLoad,
  Mar,
  MarLoad,
  Mdr,
  MdrLoad,
  GprSr1Addr,
  GprSr2Addr,
  GprDrAddr,
  GprDrData,
  GprDrLoad,
  MemoryData,
  MemoryAddr,
  MemoryWriteEnable,
  Psr,
  PsrLoad,
  SavedUsp,
  SavedUspLoad,
  SavedSsp,
  SavedSspLoad,
  
  // Combinational logic inputs
  AddrAdderA,
  AddrAdderB,
  AluA,
  AluB,
  AluK,
  BusIncrementer,
  BusDecrementer,
  IrSext5,
  IrSext6,
  IrSext9,
  IrSext11,
  IrZext8,
  NzpLogic,
  PcIncrementer,
  
  // Multiplexers
  PcMuxData00,
  PcMuxData01,
  PcMuxData10,
  PcMuxSel,
  MarMuxData0,
  MarMuxData1,
  MarMuxSel,
  MdrMuxData0,
  MdrMuxData1,
  MdrMuxSel,
  Sr2MuxData0,
  Sr2MuxData1,
  Sr2MuxSel,
  AddrAdder1MuxData0,
  AddrAdder1MuxData1,
  AddrAdder1MuxSel,
  AddrAdder2MuxData00,
  AddrAdder2MuxData01,
  AddrAdder2MuxData10,
  AddrAdder2MuxData11,
  AddrAdder2MuxSel,
  SavedSpMuxData0,
  SavedSpMuxData1,
  SavedSpMuxSel,
  
  // Tristate Buffers
  PcTriData,
  PcTriEnable,
  MarMuxTriData,
  MarMuxTriEnable,
  MdrTriData,
  MdrTriEnable,
  AluTriData,
  AluTriEnable,
  BusIncrementerTriEnable,
  BusIncrementerTriData,
  BusDecrementerTriEnable,
  BusDecrementerTriData,
  SavedSpMuxTriData,
  SavedSpMuxTriEnable,
  
  // Control Logic
  ControlState,
  ControlInstruction,
  ControlPsr,
  StateMachineInstruction,

  // Non-Architectural
  External,
  
  // Used when value doesn't matter
  DontCare,
}

