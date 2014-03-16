package lc3sim.test.core;

import static org.junit.Assert.*;

import lc3sim.core.*;

public class TestControlSet extends ControlSet {
  
  public void AssertEquals(ControlSet other) {
             assertEquals(other.addr1_mux_select, addr1_mux_select);
             assertEquals(other.addr2_mux_select, addr2_mux_select);
             assertEquals(other.bus_decrementer_tri_enable, bus_decrementer_tri_enable);
             assertEquals(other.bus_incrementer_tri_enable, bus_incrementer_tri_enable);
             assertEquals(other.alu_k, alu_k);
             assertEquals(other.alu_tri_enable, alu_tri_enable);
             assertEquals(other.gpr_dr_addr, gpr_dr_addr);
             assertEquals(other.gpr_dr_load, gpr_dr_load);
             assertEquals(other.gpr_sr1_addr, gpr_sr1_addr);
             assertEquals(other.gpr_sr2_addr, gpr_sr2_addr);
             assertEquals(other.ir_load, ir_load);
             assertEquals(other.mar_load, mar_load);
             assertEquals(other.mar_mux_select, mar_mux_select);
             assertEquals(other.mar_mux_tri_enable, mar_mux_tri_enable);
             assertEquals(other.mdr_load, mdr_load);
             assertEquals(other.mdr_mux_select, mdr_mux_select);
             assertEquals(other.mdr_tri_enable, mdr_tri_enable);
             assertEquals(other.memory_we, memory_we);
             assertEquals(other.pc_load, pc_load);
             assertEquals(other.pc_mux_select, pc_mux_select);
             assertEquals(other.pc_tri_enable, pc_tri_enable);
             assertEquals(other.psr_load, psr_load);
             assertEquals(other.saved_sp_mux_select, saved_sp_mux_select);
             assertEquals(other.saved_sp_mux_tri_enable, saved_sp_mux_tri_enable);
             assertEquals(other.saved_ssp_load, saved_ssp_load);
             assertEquals(other.saved_usp_load, saved_usp_load);
             assertEquals(other.sr2_mux_select, sr2_mux_select);
  }
}
