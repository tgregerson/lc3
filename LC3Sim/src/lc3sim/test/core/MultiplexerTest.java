package lc3sim.test.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import lc3sim.core.*;

public class MultiplexerTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void PropagateOutput4to1Test() {
    // Create mux
    final int num_address_bits = 2;
    final InputId data00id = InputId.PcMuxData00;
    final InputId data01id = InputId.PcMuxData01;
    final InputId data10id = InputId.PcMuxData10;
    final InputId data11id = InputId.Bus;
    Multiplexer.AddressBinding[] bindings = {
        new Multiplexer.AddressBinding(addr00_, data00id),
        new Multiplexer.AddressBinding(addr01_, data01id),
        new Multiplexer.AddressBinding(addr10_, data10id),
        new Multiplexer.AddressBinding(addr11_, data11id),
    };
    Multiplexer mux4to1 = new Multiplexer(
        num_address_bits, kWordSize, bindings, select_id_, output_id_);
    TestListener listener = new TestListener(InputId.External);
    mux4to1.RegisterListenerCallback(listener.GetCallback(output_id_, null));
    
    // Set input data values
    final BitWord value00 = BitWord.FromInt(3000, kWordSize);
    final BitWord value01 = BitWord.FromInt(3111, kWordSize);
    final BitWord value10 = BitWord.FromInt(3222, kWordSize);
    final BitWord value11 = BitWord.FromInt(3333, kWordSize);
    mux4to1.Notify(value00, null, data00id, null);
    mux4to1.Notify(value01, null, data01id, null);
    mux4to1.Notify(value10, null, data10id, null);
    mux4to1.Notify(value11, null, data11id, null);
    
    // Cycle through addresses
    mux4to1.Notify(addr00_, null, select_id_, null);
    assertEquals(value00, listener.last_bitword);
    mux4to1.Notify(addr01_, null, select_id_, null);
    assertEquals(value01, listener.last_bitword);
    mux4to1.Notify(addr10_, null, select_id_, null);
    assertEquals(value10, listener.last_bitword);
    mux4to1.Notify(addr11_, null, select_id_, null);
    assertEquals(value11, listener.last_bitword);
  }

  private final int kWordSize = 16;
  private final InputId select_id_ = InputId.PcMuxSel;
  private final OutputId output_id_ = OutputId.PcMux;
  private final BitWord addr00_ = BitWord.FromInt(0, 2);
  private final BitWord addr01_ = BitWord.FromInt(1, 2);
  private final BitWord addr10_ = BitWord.FromInt(2, 2);
  private final BitWord addr11_ = BitWord.FromInt(3, 2);
}
