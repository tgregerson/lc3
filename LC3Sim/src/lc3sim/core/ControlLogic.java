package lc3sim.core;

import lc3sim.core.StateMachine.InstructionCycle;
import lc3sim.core.instructions.Instruction;

// Outputs control data for Tristate Buffer enable, Multiplexer select,
// register load enable, and register write enable, based on inputs from
// the state machine, instruction register, and processor status register.
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
  
  // A structure to hold data for all of the control outputs.
  private class ControlSet {
    // All control lines are set to zero.
    public ControlSet() {
      pc_load = BitWord.FALSE;
      ir_load = BitWord.FALSE;
      mar_load = BitWord.FALSE;
      mdr_load = BitWord.FALSE;
      psr_load = BitWord.FALSE;
      gpr_dr_load = BitWord.FALSE;
      memory_we = BitWord.FALSE;
      pc_mux_select = BitWord.FALSE;
      mar_mux_select = BitWord.FALSE;
      mdr_mux_select = BitWord.FALSE;
      sr2_mux_select = BitWord.FALSE;
      addr1_mux_select = BitWord.FALSE;
      addr2_mux_select = new BitWord(2);
      // TODO get rid of magic numbers
      gpr_dr_addr = new BitWord(3);
      gpr_sr1_addr = new BitWord(3);
      gpr_sr2_addr = new BitWord(3);
      pc_tri_enable = BitWord.FALSE;
      mar_mux_tri_enable = BitWord.FALSE;
      mdr_tri_enable = BitWord.FALSE;
      alu_tri_enable = BitWord.FALSE;
    }

    public BitWord pc_load;
    public BitWord ir_load;
    public BitWord mar_load;
    public BitWord mdr_load;
    public BitWord psr_load;
    public BitWord gpr_dr_load;
    public BitWord memory_we;
    public BitWord pc_mux_select;
    public BitWord mar_mux_select;
    public BitWord mdr_mux_select;
    public BitWord sr2_mux_select;
    public BitWord addr1_mux_select;
    public BitWord addr2_mux_select;
    public BitWord gpr_dr_addr;
    public BitWord gpr_sr1_addr;
    public BitWord gpr_sr2_addr;
    public BitWord pc_tri_enable;
    public BitWord mar_mux_tri_enable;
    public BitWord mdr_tri_enable;
    public BitWord alu_tri_enable;
  }

  private void ClearAllControlSignals() {
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
  
  private ControlSet FetchInstruction1ControlSet() {
    ControlSet control_set = new ControlSet();
    control_set.pc_load = BitWord.TRUE;
    control_set.pc_tri_enable = BitWord.TRUE;
    control_set.pc_mux_select = BitWord.FromInt(0).Resize(2, false);
    control_set.mar_load = BitWord.TRUE;
    return control_set;
  }

  private ControlSet FetchInstruction2ControlSet() {
    ControlSet control_set = new ControlSet();
    control_set.mdr_mux_select = BitWord.TRUE;
    control_set.mdr_load = BitWord.TRUE;
    return control_set;
  }

  private ControlSet DecodeInstruction1ControlSet() {
    ControlSet control_set = new ControlSet();
    control_set.mdr_tri_enable = BitWord.TRUE;
    control_set.ir_load = BitWord.TRUE;
    return control_set;
  }

  private ControlSet EvaluateAddress1ControlSet(Instruction instruction) {
    ControlSet control_set = new ControlSet();
    control_set.mar_load = BitWord.TRUE;
    control_set.mar_mux_tri_enable = BitWord.TRUE;
    
    if (instruction.has_trapvect8()) {
      // TRAP
      control_set.mar_mux_select = BitWord.TRUE;
    } else {
      if (instruction.has_pcoffset9()) {
        // LD, LDI, ST, STI
        control_set.addr2_mux_select = BitWord.FromInt(2).Resize(2, false);
      } else if (instruction.has_offset6()) {
        // LDR, STR
        control_set.addr2_mux_select = BitWord.FromInt(1).Resize(2, false);
      }

      if (instruction.has_base_r()) {
        // LDR, STR
        control_set.addr1_mux_select = BitWord.TRUE;
        control_set.gpr_sr1_addr = instruction.base_r();
      }
    }


    switch (instruction.op_code()) {
      case LD:
        control_set.addr2_mux_select = BitWord.FromInt(2).Resize(2, false); // 9-bit imm
        break;
      case LDI:
        control_set.addr2_mux_select = BitWord.FromInt(2).Resize(2, false); // 9-bit imm
        break;
      case LDR:
        control_set.gpr_sr1_addr = instruction_.base_r();
        control_set.addr1_mux_select = BitWord.TRUE; // SR1 is base register rather than PC
        control_set.addr2_mux_select = BitWord.FromInt(1).Resize(2, false); // 6-bit imm
        break;
      case ST:
        control_set.addr2_mux_select = BitWord.FromInt(2).Resize(2, false); // 9-bit imm
        break;
      case STI:
        control_set.addr2_mux_select = BitWord.FromInt(2).Resize(2, false); // 9-bit imm
        break;
      case STR:
        control_set.gpr_sr1_addr = instruction_.base_r();
        control_set.addr1_mux_select = BitWord.TRUE; // SR1 is base register rather than PC
        control_set.addr2_mux_select = BitWord.FromInt(1).Resize(2, false); // 6-bit imm
        break;
      case TRAP:
        control_set.mar_mux_select = BitWord.TRUE; // Zero-extended 8-bit immediate
        break;
      default:
        assert false;
    }
    return control_set;
  }
  
  private ControlSet FetchOperands1ControlSet(Instruction instruction) {
    ControlSet control_set = new ControlSet();
    if (instruction.has_sr()) {
      control_set.gpr_sr1_addr = instruction.sr1();
    } else if (instruction.has_sr1()) {
      control_set.gpr_sr1_addr = instruction.sr1();
    }
    // TODO finish
    return control_set;
  }
  
  private ControlSet GetControlSet(InstructionCycle cycle, Instruction instruction) {
    switch (cycle) {
      case kFetchInstruction1: return FetchInstruction1ControlSet();
      case kFetchInstruction2: return FetchInstruction2ControlSet();
      case kDecodeInstruction1: return DecodeInstruction1ControlSet();
    }
    return null;
  }
  
  private void UpdateCurrentControlSet() {
    current_control_set_ = GetControlSet(cycle_, instruction_);
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
        instruction_ = Instruction.FromBitWord(data);
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
