package lc3sim.core;

// Outputs control data for Tristate Buffer enable, Multiplexer select,
// register load enable, and register write enable, based on inputs from
// the state machine, instruction register, and processor status register.
public class ControlLogic extends AbstractPropagator {
  public ControlLogic() {
    Init();
  }
  
  void Init() {
    pc_load_ = false;
    ir_load_ = false;
    mar_load_ = false;
    mdr_load_ = false;
    psr_load_ = false;
    gpr_dr_load_ = false;
    memory_we_ = false;
    pc_mux_select_ = false;
    mar_mux_select_ = false;
    mdr_mux_select_ = false;
    sr2_mux_select_ = false;
    addr1_mux_select_ = false;
    addr2_mux_select_ = false;
  }
  
  public void UpdateAllOutputs() {
    UpdateOutput(OutputId.ControlAddr1MuxSelect);
    UpdateOutput(OutputId.ControlAddr2MuxSelect);
    UpdateOutput(OutputId.ControlGprDrLoad);
    UpdateOutput(OutputId.ControlIrLoad);
    UpdateOutput(OutputId.ControlMarLoad);
    UpdateOutput(OutputId.ControlMarMuxSelect);
    UpdateOutput(OutputId.ControlMdrLoad);
    UpdateOutput(OutputId.ControlMdrMuxSelect);
    UpdateOutput(OutputId.ControlMemoryWriteEnable);
    UpdateOutput(OutputId.ControlPcLoad);
    UpdateOutput(OutputId.ControlPcMuxSelect);
    UpdateOutput(OutputId.ControlPsrLoad);
    UpdateOutput(OutputId.ControlSr2MuxSelect);
  }
  
  // AbstractPropagator methods
  // If 'receiver' is InputId.ControlState, expects 'arg' to be the current
  // StateMachine.InstructionState.
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    switch (receiver) {
      case ControlState:
        assert arg instanceof StateMachine.InstructionState;
        StateMachine.InstructionState state =
            (StateMachine.InstructionState)arg;
        switch (state) {
        
        }
        // TODO
        break;
      case ControlInstruction:
        // TODO
        break;
      case ControlPsr:
        // TODO
        break;
      default:
        assert false;
    }
    UpdateAllOutputs();
  }
  
  public BitWord ComputeOutput(OutputId output_id) {
    switch (output_id) {
      case ControlPcLoad:
        return BitWord.FromBoolean(pc_load_);
      case ControlIrLoad:
        return BitWord.FromBoolean(ir_load_);
      case ControlMarLoad:
        return BitWord.FromBoolean(mar_load_);    
      case ControlMdrLoad:
        return BitWord.FromBoolean(mdr_load_);
      case ControlPsrLoad:
        return BitWord.FromBoolean(psr_load_);
      case ControlGprDrLoad:
        return BitWord.FromBoolean(gpr_dr_load_);
      case ControlMemoryWriteEnable:
        return BitWord.FromBoolean(memory_we_);
      case ControlPcMuxSelect:
        return BitWord.FromBoolean(pc_mux_select_);
      case ControlMarMuxSelect:
        return BitWord.FromBoolean(mar_mux_select_);
      case ControlMdrMuxSelect:
        return BitWord.FromBoolean(mdr_mux_select_);
      case ControlSr2MuxSelect:
        return BitWord.FromBoolean(sr2_mux_select_);
      case ControlAddr1MuxSelect:
        return BitWord.FromBoolean(addr1_mux_select_);
      case ControlAddr2MuxSelect:
        return BitWord.FromBoolean(addr2_mux_select_);
      default:
        assert false;
        return null;
    }
  }
  
  private Boolean pc_load_;
  private Boolean ir_load_;
  private Boolean mar_load_;
  private Boolean mdr_load_;
  private Boolean psr_load_;
  private Boolean gpr_dr_load_;
  private Boolean memory_we_;
  private Boolean pc_mux_select_;
  private Boolean mar_mux_select_;
  private Boolean mdr_mux_select_;
  private Boolean sr2_mux_select_;
  private Boolean addr1_mux_select_;
  private Boolean addr2_mux_select_;

}
