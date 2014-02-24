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
  PsrN,
  PsrZ,
  PsrP,
  PsrLoad,
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
  
  // Combinational logic inputs
  AluA,
  AluB,
  AddrAdderA,
  AddrAdderB,
  PcIncrement,
  IrSext5,
  IrSext6,
  IrSext9,
  IrSext11,
  IrZext8,
  NzpLogic,
  
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
  
  // Tristate Buffers
  PcTriData,
  PcTriEnable,
  MarMuxTriData,
  MarMuxTriEnable,
  MdrTriData,
  MdrTriEnable,
  AluTriData,
  AluTriEnable,
  
  // Control Logic
  ControlState,
  ControlInstruction,
  ControlPsr,

  // Non-Architectural
  External,
  
  // Used when value doesn't matter
  DontCare,
}

