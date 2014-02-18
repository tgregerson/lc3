package lc3sim.core;

// Provide unique IDs for architectural features.
public enum ArchitecturalId {
  // Bus
  DataBus,
  // Outputs of Synchronous logic
  PcOut,
  IrOut,
  MarOut,
  MdrOut,
  PsrOut,
  GprSr1Out,
  GprSr2Out,
  MemoryOut,
  // Outputs of Combinational logic
  PcPlusOneOut,
  AddrAdderOut,
  AluOut,
  IrSext5Out,
  IrSext6Out,
  IrSext8Out,
  IrSext9Out,
  IrSext11Out,
  PcMuxOut,
  MarMuxOut,
  MdrMuxOut,
  Sr2MuxOut,
  Addr1MuxOut,
  Addr2MuxOut,
  NzpLogicOut,
  // Tristate Buffers
  MarMuxTri,
  PcTri,
  MdrTri,
  AluTri,
  // Non-Architectural
  External,
}
