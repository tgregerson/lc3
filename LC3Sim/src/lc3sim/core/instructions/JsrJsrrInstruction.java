package lc3sim.core.instructions;

import lc3sim.core.BitWord;
import lc3sim.core.ControlSet;
import lc3sim.core.InstructionCycle;

public class JsrJsrrInstruction extends Instruction {
  public JsrJsrrInstruction(BitWord bitword) {
    super(bitword);
  }

  @Override
  public String toString() {
    final String op_name = mode_bit() ? "JSR" : "JSRR";
    final String operand1 = mode_bit() ? "#" + pcoffset11().ToInt() :
                                         "R" + base_r().ToInt();
    return op_name + " " + operand1;
  }

  @Override
  public ControlSet ControlSet(InstructionCycle cycle, BitWord psr) {
    switch (cycle) {
      case kStoreResult1: return StoreResult1ControlSet();
      default: return super.ControlSet(cycle, psr);
    }
  }

  @Override
  public InstructionCycle NextCycle(InstructionCycle current_cycle) {
    switch (current_cycle) {
      case kFetchInstruction1: return InstructionCycle.kFetchInstruction2;
      case kFetchInstruction2: return InstructionCycle.kFetchInstruction3;
      case kFetchInstruction3: return InstructionCycle.kStoreResult1;
      case kStoreResult1: return InstructionCycle.kFetchInstruction1;
      default:
        assert false;
        return null;
    }
  }

  @Override
  protected ControlSet StateIndependentControlSet() {
    ControlSet control_set = super.StateIndependentControlSet();        
    control_set.gpr_sr1_addr = base_r();
    control_set.gpr_dr_addr = BitWord.FromInt(7, 3);
    if (mode_bit()) {
      // PC + offset
      control_set.addr1_mux_select = BitWord.FALSE;
      control_set.addr2_mux_select = BitWord.FromInt(3, 2);
    } else {
      // Base register
      control_set.addr1_mux_select = BitWord.TRUE;
      control_set.addr2_mux_select = BitWord.FromInt(0, 2);
    }
    control_set.pc_mux_select = BitWord.FromInt(1, 2);
    return control_set;
  }
  
  // TODO According to the ISA, R7 should be loaded with the PC prior to
  // loading the PC rather than in parallel as done here. This could be
  // accomplished by adding an execute state and moving the dr_load and
  // pc_tri_enable signals there.
  private ControlSet StoreResult1ControlSet() {
    ControlSet control_set = StateIndependentControlSet();
    control_set.pc_tri_enable = BitWord.TRUE;
    control_set.gpr_dr_load = BitWord.TRUE;
    control_set.pc_load = BitWord.TRUE;
    return control_set;
  }

  public Boolean has_base_r() {
    return !mode_bit();
  }

  public BitWord base_r() {
    return bitword().GetBitRange(kBaseRHighBit, kBaseRLowBit);
  }
  
  public Boolean has_pcoffset11() {
    return mode_bit();
  }
  
  public BitWord pcoffset11() {
    return bitword().GetBitRange(kPcOffset11HighBit, kPcOffset11LowBit);
  }

  public Boolean mode_bit() {
    return bitword().TestBit(kModeBit);
  }
  
  private final int kBaseRHighBit = 8;
  private final int kBaseRLowBit = 6;
  private final int kPcOffset11HighBit = 10;
  private final int kPcOffset11LowBit = 0;
  private final int kModeBit = 11;
}
