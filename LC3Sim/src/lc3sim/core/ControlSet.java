package lc3sim.core;

// A structure to hold data for all of the control outputs.
public class ControlSet {
  // All control lines are set to zero.
  public ControlSet() {
    pc_load = BitWord.FALSE;
    ir_load = BitWord.FALSE;
    mar_load = BitWord.FALSE;
    mdr_load = BitWord.FALSE;
    psr_load = BitWord.FALSE;
    gpr_dr_load = BitWord.FALSE;
    memory_we = BitWord.FALSE;
    pc_mux_select = new BitWord(ArchitecturalState.kNumPcMuxSelectBits);
    mar_mux_select = new BitWord(ArchitecturalState.kNumMarMuxSelectBits);
    mdr_mux_select = new BitWord(ArchitecturalState.kNumMdrMuxSelectBits);
    sr2_mux_select = new BitWord(ArchitecturalState.kNumSr2MuxSelectBits);
    addr1_mux_select = new BitWord(ArchitecturalState.kNumAddr1MuxSelectBits);
    addr2_mux_select = new BitWord(ArchitecturalState.kNumAddr2MuxSelectBits);
    alu_k = new BitWord(ALU.kNumModeBits);
    gpr_dr_addr = new BitWord(RegisterFile.kNumAddrBits);
    gpr_sr1_addr = new BitWord(RegisterFile.kNumAddrBits);
    gpr_sr2_addr = new BitWord(RegisterFile.kNumAddrBits);
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
  public BitWord saved_usp_load;
  public BitWord saved_ssp_load;
  public BitWord memory_we;
  public BitWord pc_mux_select;
  public BitWord mar_mux_select;
  public BitWord mdr_mux_select;
  public BitWord sr2_mux_select;
  public BitWord addr1_mux_select;
  public BitWord addr2_mux_select;
  public BitWord saved_sp_mux_select;
  public BitWord alu_k;
  public BitWord gpr_dr_addr;
  public BitWord gpr_sr1_addr;
  public BitWord gpr_sr2_addr;
  public BitWord pc_tri_enable;
  public BitWord mar_mux_tri_enable;
  public BitWord mdr_tri_enable;
  public BitWord alu_tri_enable;
  public BitWord bus_incrementer_tri_enable;
  public BitWord bus_decrementer_tri_enable;
  public BitWord saved_sp_mux_tri_enable;
}
