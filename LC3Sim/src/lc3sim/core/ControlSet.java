package lc3sim.core;

// A structure to hold data for all of the control outputs.
public class ControlSet {
  // All control lines are set to zero.
  public ControlSet() {
    addr1_mux_select = BitWord.Zeroes(ArchitecturalState.kNumAddr1MuxSelectBits);
    addr2_mux_select = BitWord.Zeroes(ArchitecturalState.kNumAddr2MuxSelectBits);
    alu_k = BitWord.Zeroes(ALU.kNumModeBits);
    bus_decrementer_tri_enable = BitWord.FALSE;
    bus_incrementer_tri_enable = BitWord.FALSE;
    alu_tri_enable = BitWord.FALSE;
    gpr_dr_addr = BitWord.Zeroes(RegisterFile.kNumAddrBits);
    gpr_dr_load = BitWord.FALSE;
    gpr_sr1_addr = BitWord.Zeroes(RegisterFile.kNumAddrBits);
    gpr_sr2_addr = BitWord.Zeroes(RegisterFile.kNumAddrBits);
    ir_load = BitWord.FALSE;
    mar_load = BitWord.FALSE;
    mar_mux_select = BitWord.Zeroes(ArchitecturalState.kNumMarMuxSelectBits);
    mar_mux_tri_enable = BitWord.FALSE;
    mdr_load = BitWord.FALSE;
    mdr_mux_select = BitWord.Zeroes(ArchitecturalState.kNumMdrMuxSelectBits);
    mdr_tri_enable = BitWord.FALSE;
    memory_we = BitWord.FALSE;
    pc_load = BitWord.FALSE;
    pc_mux_select = BitWord.Zeroes(ArchitecturalState.kNumPcMuxSelectBits);
    pc_tri_enable = BitWord.FALSE;
    psr_load = BitWord.FALSE;
    saved_sp_mux_select = BitWord.FALSE;
    saved_sp_mux_tri_enable = BitWord.FALSE;
    saved_ssp_load = BitWord.FALSE;
    saved_usp_load = BitWord.FALSE;
    sr2_mux_select = BitWord.Zeroes(ArchitecturalState.kNumSr2MuxSelectBits);
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof ControlSet)) {
      return false;
    } else {
      return ((ControlSet)other).addr1_mux_select == addr1_mux_select &&
             ((ControlSet)other).addr2_mux_select == addr2_mux_select &&
             ((ControlSet)other).bus_decrementer_tri_enable == bus_decrementer_tri_enable &&
             ((ControlSet)other).bus_incrementer_tri_enable == bus_incrementer_tri_enable &&
             ((ControlSet)other).alu_k == alu_k &&
             ((ControlSet)other).alu_tri_enable == alu_tri_enable &&
             ((ControlSet)other).gpr_dr_addr == gpr_dr_addr &&
             ((ControlSet)other).gpr_dr_load == gpr_dr_load &&
             ((ControlSet)other).gpr_sr1_addr == gpr_sr1_addr &&
             ((ControlSet)other).gpr_sr2_addr == gpr_sr2_addr &&
             ((ControlSet)other).ir_load == ir_load &&
             ((ControlSet)other).mar_load == mar_load &&
             ((ControlSet)other).mar_mux_select == mar_mux_select &&
             ((ControlSet)other).mar_mux_tri_enable == mar_mux_tri_enable &&
             ((ControlSet)other).mdr_load == mdr_load &&
             ((ControlSet)other).mdr_mux_select == mdr_mux_select &&
             ((ControlSet)other).mdr_tri_enable == mdr_tri_enable &&
             ((ControlSet)other).memory_we == memory_we &&
             ((ControlSet)other).pc_load == pc_load &&
             ((ControlSet)other).pc_mux_select == pc_mux_select &&
             ((ControlSet)other).pc_tri_enable == pc_tri_enable &&
             ((ControlSet)other).psr_load == psr_load &&
             ((ControlSet)other).saved_sp_mux_select == saved_sp_mux_select &&
             ((ControlSet)other).saved_sp_mux_tri_enable == saved_sp_mux_tri_enable &&
             ((ControlSet)other).saved_ssp_load == saved_ssp_load &&
             ((ControlSet)other).saved_usp_load == saved_usp_load &&
             ((ControlSet)other).sr2_mux_select == sr2_mux_select;
    }
  }
  
  /*
  public void PrintDifferences(ControlSet other) {
    if (other.addr1_mux_select != addr1_mux_select) {
      String o = other.addr1_mux_select.toString();
      String t = addr1_mux_select == null ?
          "null" : addr1_mux_select.toString();
      System.out.println("Difference in addr1_mux_select: " + t + " vs " + o);
    }
    if (other.addr2_mux_select != addr2_mux_select) {
      String o = other.addr2_mux_select.toString();
      String t = addr2_mux_select == null ?
          "null" : addr2_mux_select.toString();
      System.out.println("Difference in addr2_mux_select: " + t + " vs " + o);
    }
    if (other.bus_decrementer_tri_enable != bus_decrementer_tri_enable) {
      String o = other.bus_decrementer_tri_enable.toString();
      String t = bus_decrementer_tri_enable == null ?
          "null" : bus_decrementer_tri_enable.toString();
      System.out.println("Difference in bus_decrementer_tri_enable: " + t + " vs " + o);
    }
    if (other.bus_incrementer_tri_enable != bus_incrementer_tri_enable) {
      String o = other.bus_incrementer_tri_enable.toString();
      String t = bus_incrementer_tri_enable == null ?
          "null" : bus_incrementer_tri_enable.toString();
      System.out.println("Difference in bus_incrementer_tri_enable: " + t + " vs " + o);
    }
    if (other.alu_k != alu_k) {
      String o = other.alu_k.toString();
      String t = alu_k == null ?
          "null" : alu_k.toString();
      System.out.println("Difference in alu_k: " + t + " vs " + o);
    }
    if (other.alu_tri_enable != alu_tri_enable) {
      String o = other.alu_tri_enable.toString();
      String t = alu_tri_enable == null ?
          "null" : alu_tri_enable.toString();
      System.out.println("Difference in alu_tri_enable: " + t + " vs " + o);
    }
    if (other.gpr_dr_addr != gpr_dr_addr) {
      String o = other.gpr_dr_addr.toString();
      String t = gpr_dr_addr == null ?
          "null" : gpr_dr_addr.toString();
      System.out.println("Difference in gpr_dr_addr: " + t + " vs " + o);
    }
    if (other.gpr_dr_load != gpr_dr_load) {
      
    }
    if (other.gpr_sr1_addr != gpr_sr1_addr) {
      
    }
    if (other.gpr_sr2_addr != gpr_sr2_addr) {
      
    }
    if (other.ir_load != ir_load) {
      
    }
    if (other.mar_load != mar_load) {
      
    }
    if (other.mar_mux_select != mar_mux_select) {
      
    }
    if (other.mar_mux_tri_enable != mar_mux_tri_enable) {
      
    }
    if (other.mdr_load != mdr_load) {
      
    }
    if (other.mdr_mux_select != mdr_mux_select) {
      
    }
    if (other.mdr_tri_enable != mdr_tri_enable) {
      
    }
    if (other.memory_we != memory_we) {
      
    }
    if (other.pc_load != pc_load) {
      
    }
    if (other.pc_mux_select != pc_mux_select) {
      
    }
    if (other.pc_tri_enable != pc_tri_enable) {
      
    }
    if (other.psr_load != psr_load) {
      
    }
    if (other.saved_sp_mux_select != saved_sp_mux_select) {
      
    }
    if (other.saved_sp_mux_tri_enable != saved_sp_mux_tri_enable) {
      
    }
    if (other.saved_ssp_load != saved_ssp_load) {
      
    }
    if (other.saved_usp_load != saved_usp_load) {
      
    }
    if (other.sr2_mux_select != sr2_mux_select) {
      
    }
  
  }
  */

  public BitWord addr1_mux_select;
  public BitWord addr2_mux_select;
  public BitWord alu_k;
  public BitWord alu_tri_enable;
  public BitWord bus_decrementer_tri_enable;
  public BitWord bus_incrementer_tri_enable;
  public BitWord gpr_dr_addr;
  public BitWord gpr_dr_load;
  public BitWord gpr_sr1_addr;
  public BitWord gpr_sr2_addr;
  public BitWord ir_load;
  public BitWord mar_load;
  public BitWord mar_mux_select;
  public BitWord mar_mux_tri_enable;
  public BitWord mdr_load;
  public BitWord mdr_mux_select;
  public BitWord mdr_tri_enable;
  public BitWord memory_we;
  public BitWord pc_load;
  public BitWord pc_mux_select;
  public BitWord pc_tri_enable;
  public BitWord psr_load;
  public BitWord saved_sp_mux_select;
  public BitWord saved_sp_mux_tri_enable;
  public BitWord saved_ssp_load;
  public BitWord saved_usp_load;
  public BitWord sr2_mux_select;
}
