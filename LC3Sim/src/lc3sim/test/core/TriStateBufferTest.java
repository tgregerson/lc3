package lc3sim.test.core;

import lc3sim.core.*;

import static org.junit.Assert.*;
import lc3sim.core.InputId;
import lc3sim.core.OutputId;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TriStateBufferTest {

  @Before
  public void setUp() throws Exception {
    tristate_ = new TriStateBuffer(
        tri_output_id_, tri_data_id_, tri_enable_id_);
    listener_ = new TestListener(InputId.External);
    tristate_.RegisterListenerCallback(
        listener_.GetCallback(tri_output_id_, null));
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void PropagateOutputTest() {
    BitWord value1 = BitWord.FromInt(12345, kWordSize);
    BitWord value2 = BitWord.FromInt(54321, kWordSize);
    
    // Propagate input to listener on positive enable edge.
    tristate_.Notify(value1, OutputId.External, tri_data_id_, null);
    assertFalse(value1.IsEqual(listener_.last_bitword, false));
    tristate_.Notify(BitWord.TRUE, OutputId.External, tri_enable_id_, null);
    assertEquals(listener_.last_bitword, value1);
    
    // Negative edge of enable updates listener to high impedance (null)
    tristate_.Notify(BitWord.FALSE, OutputId.External, tri_enable_id_, null);
    assertEquals(BitWord.EMPTY, listener_.last_bitword);
    
    // Change while enable is off is not propagated.
    tristate_.Notify(value2, OutputId.External, tri_data_id_, null);
    assertEquals(BitWord.EMPTY, listener_.last_bitword);
    
    // Turning on enable reflects the data change.
    tristate_.Notify(BitWord.TRUE, OutputId.External, tri_enable_id_, null);
    assertEquals(value2, listener_.last_bitword);
    
    // Change of input while enabled is reflected immediately.
    tristate_.Notify(value1, OutputId.External, tri_data_id_, null);
    assertEquals(value1, listener_.last_bitword);
  }
  
  private TriStateBuffer tristate_;
  private TestListener listener_;
  private final OutputId tri_output_id_ = OutputId.AluTri;
  private final InputId tri_data_id_ = InputId.AluTriData;
  private final InputId tri_enable_id_ = InputId.AluTriEnable;
  private final int kWordSize = 16;
}
