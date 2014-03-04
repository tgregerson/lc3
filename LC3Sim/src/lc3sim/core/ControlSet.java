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
