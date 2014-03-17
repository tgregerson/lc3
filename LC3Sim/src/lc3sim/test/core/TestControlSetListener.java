package lc3sim.test.core;

import java.util.ArrayList;
import java.util.List;

import lc3sim.core.*;

public class TestControlSetListener extends TestListener {
  public TestControlSetListener() {
    super(InputId.DontCare);
    current_control_set_ = new TestControlSet();
  }
  
  public List<ListenerCallback> GetCallbacks() {
    ArrayList<ListenerCallback> callbacks = new ArrayList<ListenerCallback>();
    callbacks.add(super.GetCallback(OutputId.ControlAddr1MuxSelect));
    callbacks.add(super.GetCallback(OutputId.ControlAddr2MuxSelect));
    callbacks.add(super.GetCallback(OutputId.ControlAluK));
    callbacks.add(super.GetCallback(OutputId.ControlAluTriEnable));
    callbacks.add(super.GetCallback(OutputId.ControlBusDecrementerTriEnable));
    callbacks.add(super.GetCallback(OutputId.ControlBusIncrementerTriEnable));
    callbacks.add(super.GetCallback(OutputId.ControlGprDrAddr));
    callbacks.add(super.GetCallback(OutputId.ControlGprDrLoad));
    callbacks.add(super.GetCallback(OutputId.ControlGprSr1Addr));
    callbacks.add(super.GetCallback(OutputId.ControlGprSr2Addr));
    callbacks.add(super.GetCallback(OutputId.ControlIrLoad));
    callbacks.add(super.GetCallback(OutputId.ControlMarLoad));
    callbacks.add(super.GetCallback(OutputId.ControlMarMuxSelect));
    callbacks.add(super.GetCallback(OutputId.ControlMarMuxTriEnable));
    callbacks.add(super.GetCallback(OutputId.ControlMdrLoad));
    callbacks.add(super.GetCallback(OutputId.ControlMdrMuxSelect));
    callbacks.add(super.GetCallback(OutputId.ControlMdrTriEnable));
    callbacks.add(super.GetCallback(OutputId.ControlMemoryWriteEnable));
    callbacks.add(super.GetCallback(OutputId.ControlPcLoad));
    callbacks.add(super.GetCallback(OutputId.ControlPcMuxSelect));
    callbacks.add(super.GetCallback(OutputId.ControlPcTriEnable));
    callbacks.add(super.GetCallback(OutputId.ControlPsrLoad));
    callbacks.add(super.GetCallback(OutputId.ControlSavedSpMuxSelect));
    callbacks.add(super.GetCallback(OutputId.ControlSavedSpMuxTriEnable));
    callbacks.add(super.GetCallback(OutputId.ControlSavedSspLoad));
    callbacks.add(super.GetCallback(OutputId.ControlSavedUspLoad));
    callbacks.add(super.GetCallback(OutputId.ControlSr2MuxSelect));
    return callbacks;
  }
  
  @Override
  public void Notify(BitWord bw, OutputId sender, InputId receiver, Object arg) {
    super.Notify(bw, sender, receiver, arg);
    switch (sender) {
      case ControlAddr1MuxSelect:
        current_control_set_.addr1_mux_select = bw;
        break;
      case ControlAddr2MuxSelect:
        current_control_set_.addr2_mux_select = bw;
        break;
      case ControlAluK:
        current_control_set_.alu_k = bw;
        break;
      case ControlAluTriEnable:
        current_control_set_.alu_tri_enable = bw;
        break;
      case ControlBusDecrementerTriEnable:
        current_control_set_.bus_decrementer_tri_enable = bw;
        break;
      case ControlBusIncrementerTriEnable:
        current_control_set_.bus_incrementer_tri_enable = bw;
        break;
      case ControlGprDrAddr:
        current_control_set_.gpr_dr_addr = bw;
        break;
      case ControlGprDrLoad:
        current_control_set_.gpr_dr_load = bw;
        break;
      case ControlGprSr1Addr:
        current_control_set_.gpr_sr1_addr = bw;
        break;
      case ControlGprSr2Addr:
        current_control_set_.gpr_sr2_addr = bw;
        break;
      case ControlIrLoad:
        current_control_set_.ir_load = bw;
        break;
      case ControlMarLoad:
        current_control_set_.mar_load = bw;
        break;
      case ControlMarMuxSelect:
        current_control_set_.mar_mux_select = bw;
        break;
      case ControlMarMuxTriEnable:
        current_control_set_.mar_mux_tri_enable = bw;
        break;
      case ControlMdrLoad:
        current_control_set_.mdr_load = bw;
        break;
      case ControlMdrMuxSelect:
        current_control_set_.mdr_mux_select = bw;
        break;
      case ControlMdrTriEnable:
        current_control_set_.mdr_tri_enable = bw;
        break;
      case ControlMemoryWriteEnable:
        current_control_set_.memory_we = bw;
        break;
      case ControlPcLoad:
        current_control_set_.pc_load = bw;
        break;
      case ControlPcMuxSelect:
        current_control_set_.pc_mux_select = bw;
        break;
      case ControlPcTriEnable:
        current_control_set_.pc_tri_enable = bw;
        break;
      case ControlPsrLoad:
        current_control_set_.psr_load = bw;
        break;
      case ControlSavedSpMuxSelect:
        current_control_set_.saved_sp_mux_select = bw;
        break;
      case ControlSavedSpMuxTriEnable:
        current_control_set_.saved_sp_mux_tri_enable = bw;
        break;
      case ControlSavedSspLoad:
        current_control_set_.saved_ssp_load = bw;
        break;
      case ControlSavedUspLoad:
        current_control_set_.saved_usp_load = bw;
        break;
      case ControlSr2MuxSelect:
        current_control_set_.sr2_mux_select = bw;
        break;
      default:
        assert false;
    }
  }
  
  public TestControlSet CurrentControlSet() {
    return current_control_set_;
  }
  
  private TestControlSet current_control_set_;
}
