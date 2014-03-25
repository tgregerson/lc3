package lc3sim.test.core;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import lc3sim.core.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArchitecturalStateTest {

  @Before
  public void setUp() throws Exception {
    state_ = new ArchitecturalState();

    pc_listener_ = new TestListener(InputId.DontCare);
    ir_listener_ = new TestListener(InputId.DontCare);

    pc_binding_ = new ArchitecturalState.ListenerBinding(
        pc_listener_, OutputId.Pc);
    ir_binding_ = new ArchitecturalState.ListenerBinding(
        ir_listener_, OutputId.Ir);

    pc_checksequence_ = new ArrayList<BitWord>();
    ir_checksequence_ = new ArrayList<BitWord>();
  }

  @After
  public void tearDown() throws Exception {
    assertEquals(0, pc_listener_.CheckSequence().size());
    assertEquals(0, ir_listener_.CheckSequence().size());
  }

  @Test
  public void AddRegRegInstructionTest() {
    // R1 = 0x0137
    // R4 = 0x0420
    // R5 = R1 + R4
    
    // R5 = R1 + R4
    final int instruction_val = 0x1A44;
    final int instruction_addr = 0x0005;
    final int r1_val = 0x0137;
    final int r4_val = 0x0420;
    
    // Load instruction into memory.
    state_.SetMemory(instruction_addr, instruction_val);
    state_.SetPc(instruction_addr);
    
    // Load register file
    state_.SetGpr(1, r1_val);
    state_.SetGpr(4, r4_val);
    
    List<ArchitecturalState.ListenerBinding> bindings =
        new ArrayList<ArchitecturalState.ListenerBinding>();

    pc_checksequence_.add(BitWord.FromInt((instruction_addr + 1), kWordSize));
    pc_listener_.AppendCheckSequence(pc_checksequence_);
    bindings.add(pc_binding_);
    
    ir_checksequence_.add(BitWord.FromInt(instruction_val, kWordSize));
    ir_listener_.AppendCheckSequence(ir_checksequence_);
    bindings.add(ir_binding_);
    
    state_.AddExternalListenerBindings(bindings);
    
    assertEquals(instruction_addr + 1, state_.ExecuteInstruction());
  }
  
  private final int kWordSize = ArchitecturalState.kWordSize;

  private ArchitecturalState state_;

  private TestListener pc_listener_;
  private TestListener ir_listener_;

  private ArchitecturalState.ListenerBinding ir_binding_;
  private ArchitecturalState.ListenerBinding pc_binding_;

  private ArrayList<BitWord> pc_checksequence_;
  private ArrayList<BitWord> ir_checksequence_;
}
