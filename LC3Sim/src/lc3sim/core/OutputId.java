package lc3sim.core;

// All architecturally visible logic outputs.
public enum OutputId {
	// Bus
	Bus,
	
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
	IrSext9,
	IrSext11,
	IrZext8,
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

	// Control drivers
	ControlState,

	// Control signals
	ControlPcLoad,
	ControlIrLoad,
	ControlMarLoad,
	ControlMdrLoad,
	ControlPsrLoad,
	ControlGprDrLoad,
	ControlMemoryWriteEnable,
	ControlPcMuxSelect,
	ControlMarMuxSelect,
	ControlMdrMuxSelect,
	ControlSr2MuxSelect,
	ControlAddr1MuxSelect,
	ControlAddr2MuxSelect,
	ControlPcTriEnable,
	ControlMarMuxTriEnable,
	ControlMdrTriEnable,
	ControlAluTriEnable,
	
	// Reserved for UI
	External,
	
	// Used when value doesn't matter
	DontCare,
}
