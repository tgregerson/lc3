package lc3sim.core;

import lc3sim.core.instructions.Instruction;
import lc3sim.core.instructions.OpCode;

// Outputs control data for Tristate Buffer enable, Multiplexer select,
// register load enable, and register write enable, based on inputs from
// the state machine, instruction register, and processor status register.
//
// The actual logic for which control lines to set is contained within the
// individual Instruction classes. This class performs updates and
// propagation of the information.
public class ControlLogic extends AbstractPropagator {
  public ControlLogic() {
    Init();
  }
  
  public void Init() {
    cycle_ = InstructionCycle.kReset;
    instruction_ = Instruction.FromBitWord(BitWord.Zeroes(Instruction.kNumBits));
    psr_ = BitWord.Zeroes(ProcessorStatusRegister.kNumBits);
    current_control_set_ = new ControlSet();
    RefreshOutput();
  }

  public void RefreshOutput() {
    ForceUpdateOutput(OutputId.ControlAddr1MuxSelect);
    ForceUpdateOutput(OutputId.ControlAddr2MuxSelect);
    ForceUpdateOutput(OutputId.ControlAluK);
    ForceUpdateOutput(OutputId.ControlAluTriEnable);
    ForceUpdateOutput(OutputId.ControlBusDecrementerTriEnable);
    ForceUpdateOutput(OutputId.ControlBusIncrementerTriEnable);
    ForceUpdateOutput(OutputId.ControlGprDrLoad);
    ForceUpdateOutput(OutputId.ControlGprDrAddr);
    ForceUpdateOutput(OutputId.ControlGprSr1Addr);
    ForceUpdateOutput(OutputId.ControlGprSr2Addr);
    ForceUpdateOutput(OutputId.ControlIrLoad);
    ForceUpdateOutput(OutputId.ControlMarLoad);
    ForceUpdateOutput(OutputId.ControlMarMuxSelect);
    ForceUpdateOutput(OutputId.ControlMarMuxTriEnable);
    ForceUpdateOutput(OutputId.ControlMdrLoad);
    ForceUpdateOutput(OutputId.ControlMdrMuxSelect);
    ForceUpdateOutput(OutputId.ControlMdrTriEnable);
    ForceUpdateOutput(OutputId.ControlMemoryWriteEnable);
    ForceUpdateOutput(OutputId.ControlPcLoad);
    ForceUpdateOutput(OutputId.ControlPcTriEnable);
    ForceUpdateOutput(OutputId.ControlPcMuxSelect);
    ForceUpdateOutput(OutputId.ControlPsrLoad);
    ForceUpdateOutput(OutputId.ControlSavedSpMuxSelect);
    ForceUpdateOutput(OutputId.ControlSavedSpMuxTriEnable);
    ForceUpdateOutput(OutputId.ControlSavedSspLoad);
    ForceUpdateOutput(OutputId.ControlSavedUspLoad);
    ForceUpdateOutput(OutputId.ControlSr2MuxSelect);
  }
  
  public void UpdateAllOutputs() {
    UpdateOutput(OutputId.ControlAddr1MuxSelect);
    UpdateOutput(OutputId.ControlAddr2MuxSelect);
    UpdateOutput(OutputId.ControlAluK);
    UpdateOutput(OutputId.ControlAluTriEnable);
    UpdateOutput(OutputId.ControlBusDecrementerTriEnable);
    UpdateOutput(OutputId.ControlBusIncrementerTriEnable);
    UpdateOutput(OutputId.ControlGprDrLoad);
    UpdateOutput(OutputId.ControlGprDrAddr);
    UpdateOutput(OutputId.ControlGprSr1Addr);
    UpdateOutput(OutputId.ControlGprSr2Addr);
    UpdateOutput(OutputId.ControlIrLoad);
    UpdateOutput(OutputId.ControlMarLoad);
    UpdateOutput(OutputId.ControlMarMuxSelect);
    UpdateOutput(OutputId.ControlMarMuxTriEnable);
    UpdateOutput(OutputId.ControlMdrLoad);
    UpdateOutput(OutputId.ControlMdrMuxSelect);
    UpdateOutput(OutputId.ControlMdrTriEnable);
    UpdateOutput(OutputId.ControlMemoryWriteEnable);
    UpdateOutput(OutputId.ControlPcLoad);
    UpdateOutput(OutputId.ControlPcTriEnable);
    UpdateOutput(OutputId.ControlPcMuxSelect);
    UpdateOutput(OutputId.ControlPsrLoad);
    UpdateOutput(OutputId.ControlSavedSpMuxSelect);
    UpdateOutput(OutputId.ControlSavedSpMuxTriEnable);
    UpdateOutput(OutputId.ControlSavedSspLoad);
    UpdateOutput(OutputId.ControlSavedUspLoad);
    UpdateOutput(OutputId.ControlSr2MuxSelect);
  }
  
  private void UpdateCurrentControlSet() {
    // TODO Special-case handling for interrupt and exception
    current_control_set_ = instruction_.ControlSet(cycle_, psr_);
    if (current_control_set_ == null) {
      throw new RuntimeException("Received null control set for instruction " +
                                 instruction_ + " on cycle " + cycle_);
    }
  }
  
  // AbstractPropagator methods
  // If 'receiver' is InputId.ControlState, expects 'arg' to be the current
  // StateMachine.InstructionState.
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    switch (receiver) {
      case ControlState:
        cycle_ = InstructionCycle.Lookup(data);
        if (cycle_ == null) {
          throw new IllegalArgumentException(
              "Cycle lookup failed for: " + data);
        }
        break;
      case ControlInstruction:
        if (Instruction.OpCodeFromBitWord(data) == OpCode.RESERVED) {
          // TODO: Trigger Exception
          throw new UnsupportedOperationException(
              "Exception handling for unsupported op codes not supported.");
        } else {
          instruction_ = Instruction.FromBitWord(data);
        }
        break;
      case ControlPsr:
        psr_ = data;
        break;
      default:
        throw new IllegalArgumentException(
            "Unsupported ControlLogic receiver ID: " + receiver);
    }
    if (cycle_ != InstructionCycle.kReset) {
      UpdateCurrentControlSet();
      UpdateAllOutputs();
    }
  }
  
  public BitWord ComputeOutput(OutputId output_id) {
    switch (output_id) {
      case ControlAddr1MuxSelect: return current_control_set_.addr1_mux_select;
      case ControlAddr2MuxSelect: return current_control_set_.addr2_mux_select;
      case ControlAluK: return current_control_set_.alu_k;
      case ControlAluTriEnable: return current_control_set_.alu_tri_enable;
      case ControlBusDecrementerTriEnable: return current_control_set_.bus_decrementer_tri_enable;
      case ControlBusIncrementerTriEnable: return current_control_set_.bus_incrementer_tri_enable;
      case ControlGprDrLoad: return current_control_set_.gpr_dr_load;
      case ControlGprDrAddr: return current_control_set_.gpr_dr_addr;
      case ControlGprSr1Addr: return current_control_set_.gpr_sr1_addr;
      case ControlGprSr2Addr: return current_control_set_.gpr_sr2_addr;
      case ControlIrLoad: return current_control_set_.ir_load;
      case ControlPcLoad: return current_control_set_.pc_load;
      case ControlMarLoad: return current_control_set_.mar_load;    
      case ControlMdrLoad: return current_control_set_.mdr_load;
      case ControlMarMuxSelect: return current_control_set_.mar_mux_select;
      case ControlMarMuxTriEnable: return current_control_set_.mar_mux_tri_enable;
      case ControlMdrMuxSelect: return current_control_set_.mdr_mux_select;
      case ControlMdrTriEnable: return current_control_set_.mdr_tri_enable;
      case ControlMemoryWriteEnable: return current_control_set_.memory_we;
      case ControlPcMuxSelect: return current_control_set_.pc_mux_select;
      case ControlPcTriEnable: return current_control_set_.pc_tri_enable;
      case ControlPsrLoad: return current_control_set_.psr_load;
      case ControlSavedSpMuxSelect: return current_control_set_.saved_sp_mux_select;
      case ControlSavedSpMuxTriEnable: return current_control_set_.saved_sp_mux_tri_enable;
      case ControlSavedSspLoad: return current_control_set_.saved_ssp_load;
      case ControlSavedUspLoad: return current_control_set_.saved_usp_load;
      case ControlSr2MuxSelect: return current_control_set_.sr2_mux_select;
      default:
        throw new IllegalArgumentException("Unsupported output ID: " + output_id);
    }
  }
  
  private ControlSet current_control_set_;
  
  private InstructionCycle cycle_;
  private Instruction instruction_;
  private BitWord psr_;
}
