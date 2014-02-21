package lc3sim.core;

// Provide unique IDs for all inputs and outputs visible in the architectural
// view.
public enum InputId {
  // Bus
  DataBus,
  
  // Synchronous logic inputs
  Pc,
  Ir,
  PsrN,
  PsrZ,
  PsrP,
  Mar,
  Mdr,
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
  GprSr2MuxData0,
  GprSr2MuxData1,
  GprSr2MuxSel,
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
  MarTriData,
  MarTriEnable,
  MdrTriData,
  MdrTriEnable,
  AluTriData,
  AluTriEnable,

  // Non-Architectural
  External,
}

