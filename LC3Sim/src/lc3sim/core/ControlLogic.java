package lc3sim.core;

import lc3sim.core.StateMachine.InstructionCycle;

// Outputs control data for Tristate Buffer enable, Multiplexer select,
// register load enable, and register write enable, based on inputs from
// the state machine, instruction register, and processor status register.
public class ControlLogic extends AbstractPropagator {
  public ControlLogic() {
    Init();
  }
  
  public void Init() {
    cycle_ = InstructionCycle.kFetchInstruction1;
    instruction_ = new BitWord(16);
    psr_ = new BitWord(16);
    ClearAllControlSignals();
  }

  private void ClearAllControlSignals() {
    pc_load_ = BitWord.FALSE;
    ir_load_ = BitWord.FALSE;
    mar_load_ = BitWord.FALSE;
    mdr_load_ = BitWord.FALSE;
    psr_load_ = BitWord.FALSE;
    gpr_dr_load_ = BitWord.FALSE;
    memory_we_ = BitWord.FALSE;
    pc_mux_select_ = BitWord.FALSE;
    mar_mux_select_ = BitWord.FALSE;
    mdr_mux_select_ = BitWord.FALSE;
    sr2_mux_select_ = BitWord.FALSE;
    addr1_mux_select_ = BitWord.FALSE;
    addr2_mux_select_ = BitWord.FALSE;
    pc_tri_enable_ = BitWord.FALSE;
    mar_mux_tri_enable_ = BitWord.FALSE;
    mdr_tri_enable_ = BitWord.FALSE;
    alu_tri_enable_ = BitWord.FALSE;
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
    UpdateOutput(OutputId.ControlPcTriEnable);
    UpdateOutput(OutputId.ControlMarMuxTriEnable);
    UpdateOutput(OutputId.ControlMdrTriEnable);
    UpdateOutput(OutputId.ControlAluTriEnable);
  }
  
  private void SetControlSignals() {
    ClearAllControlSignals();
    switch (cycle_) {
      case kFetchInstruction1:
        pc_load_ = BitWord.TRUE;
        pc_tri_enable_ = BitWord.FALSE;
        pc_mux_select_ = BitWord.FromInt(2).Resize(2, false);
        mar_load_ = BitWord.FALSE;
        break;
      case kFetchInstruction2:
        mdr_mux_select_ = BitWord.TRUE;
        mdr_load_ = BitWord.TRUE;
        break;
      case kFetchInstruction3:
        mdr_tri_enable_ = BitWord.TRUE;
        ir_load_ = BitWord.TRUE;
        break;
    }
    
    // TODO Instruction decode
    
    // TODO Psr
  }
  
  // AbstractPropagator methods
  // If 'receiver' is InputId.ControlState, expects 'arg' to be the current
  // StateMachine.InstructionState.
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    switch (receiver) {
      case ControlState:
        assert arg instanceof InstructionCycle;
        cycle_ = (InstructionCycle)arg;
        break;
      case ControlInstruction:
        assert(data.num_bits() == 16);
        instruction_ = data;
        break;
      case ControlPsr:
        assert(data.num_bits() == 16);
        psr_ = data;
        break;
      default:
        assert false;
    }
    SetControlSignals();
    UpdateAllOutputs();
  }
  
  public BitWord ComputeOutput(OutputId output_id) {
    switch (output_id) {
      case ControlPcLoad: return pc_load_;
      case ControlIrLoad: return ir_load_;
      case ControlMarLoad: return mar_load_;    
      case ControlMdrLoad: return mdr_load_;
      case ControlPsrLoad: return psr_load_;
      case ControlGprDrLoad: return gpr_dr_load_;
      case ControlMemoryWriteEnable: return memory_we_;
      case ControlPcMuxSelect: return pc_mux_select_;
      case ControlMarMuxSelect: return mar_mux_select_;
      case ControlMdrMuxSelect: return mdr_mux_select_;
      case ControlSr2MuxSelect: return sr2_mux_select_;
      case ControlAddr1MuxSelect: return addr1_mux_select_;
      case ControlAddr2MuxSelect: return addr2_mux_select_;
      case ControlPcTriEnable: return pc_tri_enable_;
      case ControlMarMuxTriEnable: return mar_mux_tri_enable_;
      case ControlMdrTriEnable: return mdr_tri_enable_;
      case ControlAluTriEnable: return alu_tri_enable_;
      default:
        assert false;
        return null;
    }
  }
  
  private BitWord pc_load_;
  private BitWord ir_load_;
  private BitWord mar_load_;
  private BitWord mdr_load_;
  private BitWord psr_load_;
  private BitWord gpr_dr_load_;
  private BitWord memory_we_;
  private BitWord pc_mux_select_;
  private BitWord mar_mux_select_;
  private BitWord mdr_mux_select_;
  private BitWord sr2_mux_select_;
  private BitWord addr1_mux_select_;
  private BitWord addr2_mux_select_;
  private BitWord pc_tri_enable_;
  private BitWord mar_mux_tri_enable_;
  private BitWord mdr_tri_enable_;
  private BitWord alu_tri_enable_;
  
  private InstructionCycle cycle_;
  private BitWord instruction_;
  private BitWord psr_;

}
