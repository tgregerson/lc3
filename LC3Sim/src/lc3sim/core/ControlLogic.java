package lc3sim.core;

import lc3sim.core.StateMachine.InstructionCycle;
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
    cycle_ = InstructionCycle.kFetchInstruction1;
    instruction_ = Instruction.FromBitWord(new BitWord(Instruction.kNumBits));
    psr_ = new BitWord(ProcessorStatusRegister.kNumBits);
    current_control_set_ = new ControlSet();
  }
  
  public void UpdateAllOutputs() {
    UpdateOutput(OutputId.ControlAddr1MuxSelect);
    UpdateOutput(OutputId.ControlAddr2MuxSelect);
    UpdateOutput(OutputId.ControlGprDrLoad);
    UpdateOutput(OutputId.ControlGprDrAddr);
    UpdateOutput(OutputId.ControlGprSr1Addr);
    UpdateOutput(OutputId.ControlGprSr2Addr);
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
  
  private void UpdateCurrentControlSet() {
    // TODO Special-case handling for interrupt and exception
    current_control_set_ = instruction_.ControlSet(cycle_, psr_);
  }
  
  // AbstractPropagator methods
  // If 'receiver' is InputId.ControlState, expects 'arg' to be the current
  // StateMachine.InstructionState.
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    switch (receiver) {
      case ControlState:
        cycle_ = InstructionCycle.Lookup(data);
        assert cycle_ != null;
        break;
      case ControlInstruction:
        assert(data.num_bits() == 16);
        if (Instruction.OpCodeFromBitWord(data) == OpCode.RESERVED) {
          // TODO: Trigger Exception
          assert false;
        } else {
          instruction_ = Instruction.FromBitWord(data);
        }
        break;
      case ControlPsr:
        assert(data.num_bits() == 16);
        psr_ = data;
        break;
      default:
        assert false;
    }
    UpdateCurrentControlSet();
    UpdateAllOutputs();
  }
  
  public BitWord ComputeOutput(OutputId output_id) {
    switch (output_id) {
      case ControlPcLoad: return current_control_set_.pc_load;
      case ControlIrLoad: return current_control_set_.ir_load;
      case ControlMarLoad: return current_control_set_.mar_load;    
      case ControlMdrLoad: return current_control_set_.mdr_load;
      case ControlPsrLoad: return current_control_set_.psr_load;
      case ControlGprDrLoad: return current_control_set_.gpr_dr_load;
      case ControlGprDrAddr: return current_control_set_.gpr_dr_addr;
      case ControlGprSr1Addr: return current_control_set_.gpr_sr1_addr;
      case ControlGprSr2Addr: return current_control_set_.gpr_sr2_addr;
      case ControlMemoryWriteEnable: return current_control_set_.memory_we;
      case ControlPcMuxSelect: return current_control_set_.pc_mux_select;
      case ControlMarMuxSelect: return current_control_set_.mar_mux_select;
      case ControlMdrMuxSelect: return current_control_set_.mdr_mux_select;
      case ControlSr2MuxSelect: return current_control_set_.sr2_mux_select;
      case ControlAddr1MuxSelect: return current_control_set_.addr1_mux_select;
      case ControlAddr2MuxSelect: return current_control_set_.addr2_mux_select;
      case ControlPcTriEnable: return current_control_set_.pc_tri_enable;
      case ControlMarMuxTriEnable: return current_control_set_.mar_mux_tri_enable;
      case ControlMdrTriEnable: return current_control_set_.mdr_tri_enable;
      case ControlAluTriEnable: return current_control_set_.alu_tri_enable;
      default:
        assert false;
        return null;
    }
  }
  
  private ControlSet current_control_set_;
  
  private InstructionCycle cycle_;
  private Instruction instruction_;
  private BitWord psr_;
}
