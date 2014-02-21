package lc3sim.core;

// All architecturally visible logic outputs.
public enum OutputId {
	// Bus
	DataBus,
	
	// Outputs of Synchronous logic
	Pc,
	Ir,
	Mar,
	Mdr,
	Psr,
	GprSr1,
	GprSr2,
	Memory,
	
	// Outputs of Combinational logic
	PcIncrement,
	AddrAdder,
	Alu,
	IrSext5,
	IrSext6,
	IrSext8,
	IrSext9,
	IrSext11,
	PcMux,
	MarMux,
	MdrMux,
	Sr2Mux,
	Addr1Mux,
	Addr2Mux,
	NzpLogic,
	
	// Tristate Buffers
	MarMuxTri,
	PcTri,
	MdrTri,
	AluTri,
	
	// Reserved for UI
	External,
}
