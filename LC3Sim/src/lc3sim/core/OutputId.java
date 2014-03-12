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
	SavedUsp,
	SavedSsp,
	
	// Outputs of Combinational logic
	PcIncrementer,
	BusIncrementer,
	BusDecrementer,
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
	SavedSpMux,
	NzpLogic,
	
	// Tristate Buffers
	MarMuxTri,
	PcTri,
	MdrTri,
	AluTri,
	BusIncrementerTri,
	BusDecrementerTri,
	SavedSpMuxTri,

	// Control drivers
	ControlState,

	// Control signals
	ControlPcLoad,
	ControlIrLoad,
	ControlMarLoad,
	ControlMdrLoad,
	ControlPsrLoad,
	ControlGprDrLoad,
	ControlUspLoad,
	ControlSspLoad,
	ControlGprDrAddr,
	ControlGprSr1Addr,
	ControlGprSr2Addr,
	ControlMemoryWriteEnable,
	ControlPcMuxSelect,
	ControlMarMuxSelect,
	ControlMdrMuxSelect,
	ControlSr2MuxSelect,
	ControlAddr1MuxSelect,
	ControlAddr2MuxSelect,
	ControlSavedSpMuxSelect,
	ControlPcTriEnable,
	ControlMarMuxTriEnable,
	ControlMdrTriEnable,
	ControlAluTriEnable,
	ControlBusIncrementerTriEnable,
	ControlBusDecrementerTriEnable,
	ControlSavedSpMuxTriEnable,
	
	// Reserved for UI
	External,
	
	// Used when value doesn't matter
	DontCare,
}
