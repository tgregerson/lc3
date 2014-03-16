package lc3sim.test.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import lc3sim.core.*;

public class PropagatorTest {

  @Before
  public void setUp() throws Exception {
    test_listener_ = new TestListener(
        test_propagator_initial_value_, other_output_id_,
        test_listener_input_id_, null);
    test_propagator_ = new TestPropagator(test_propagator_initial_value_);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void SinglePortPropagateTest() {
    // Add listener callback
    OutputId propagator_output1 = OutputId.Alu;
    test_propagator_.RegisterListenerCallback(
        test_listener_.GetCallback(propagator_output1, null));
    
    // Notify propagator of new value
    BitWord new_value = BitWord.FromInt(12345, 32);
    assertFalse(new_value.equals(test_propagator_initial_value_));
    assertFalse(new_value.equals(test_listener_.last_bitword));
    test_propagator_.Notify(
        new_value, propagator_output1, other_input_id_, null);
    
    // Propagator should forward the new value on to the listener.
    assertEquals("Check correct input ID",
                 test_listener_input_id_, test_listener_.last_input_id);
    assertEquals("Value should have propagated",
                 new_value, test_listener_.last_bitword);
    
    // Unregister prevents future notifications
    test_propagator_.UnregisterListener(test_listener_);
    BitWord another_value = BitWord.FromInt(90210, 32);
    test_propagator_.Notify(
        another_value, propagator_output1, other_input_id_, null);
    assertEquals("Value not propagated after callback unregistered",
                 new_value, test_listener_.last_bitword);
  }
  
  @Test
  // Tests that the propagator doesn't send notifications if an output hasn't
  // changed.
  public void NoDuplicateNotificationTest() {
    // Add listener callback
    OutputId propagator_output1 = OutputId.Alu;
    test_propagator_.RegisterListenerCallback(
        test_listener_.GetCallback(propagator_output1, null));
    
    // Notify propagator of new value
    BitWord new_value = BitWord.FromInt(12345, 32);
    assertFalse(new_value.equals(test_propagator_initial_value_));
    assertFalse(new_value.equals(test_listener_.last_bitword));
    test_propagator_.Notify(
        new_value, propagator_output1, other_input_id_, null);
    
    // Propagator should forward the new value on to the listener.
    assertEquals("Check correct input ID",
                 test_listener_input_id_, test_listener_.last_input_id);
    assertEquals("Value should have propagated",
                 new_value, test_listener_.last_bitword);
    
    // Override value
    test_listener_.last_bitword = test_propagator_initial_value_;
    test_listener_.last_input_id = other_input_id_;

    test_propagator_.Notify(
        new_value, propagator_output1, other_input_id_, null);
    
    // Value should not have changed.
    assertEquals("Input ID not updated",
                 other_input_id_, test_listener_.last_input_id);
    assertEquals("Value not propagated",
                 test_propagator_initial_value_, test_listener_.last_bitword);
  }

  @Test
  // Test that registering multiple listeners on different outputs results in
  // only getting notifications for the registered output.
  public void MultiPortPropagateTest() {
    // Register two listeners on different output ports.
    OutputId propagator_output1 = OutputId.Alu;
    InputId listener1_input = InputId.AddrAdder1MuxData0;
    TestListener test_listener1 =
        new TestListener(test_propagator_initial_value_, other_output_id_,
                         listener1_input, null);
    test_propagator_.RegisterListenerCallback(
        test_listener1.GetCallback(propagator_output1, null));

    OutputId propagator_output2 = OutputId.Bus;
    InputId listener2_input = InputId.AddrAdder1MuxData1;
    TestListener test_listener2 =
        new TestListener(test_propagator_initial_value_, other_output_id_,
                         listener2_input, null);
    test_propagator_.RegisterListenerCallback(
        test_listener2.GetCallback(propagator_output2, null));
    
    BitWord new_value1 = BitWord.FromInt(12345, 32);
    test_propagator_.Notify(
        new_value1, propagator_output1, other_input_id_, null);
    
    // Update should only have effected listener1.
    assertEquals("Value should have propagated to listener1",
                 new_value1, test_listener1.last_bitword);
    assertFalse("Value should not have propagated to listener2",
                 new_value1.equals(test_listener2.last_bitword));

    BitWord new_value2 = BitWord.FromInt(67890, 32);
    test_propagator_.Notify(
        new_value2, propagator_output2, other_input_id_, null);

    // Update should only have effected listener2.
    assertEquals("Value should not have propagated to listener1",
                 new_value1, test_listener1.last_bitword);
    assertEquals("Value should have propagated to listener1",
                 new_value2, test_listener2.last_bitword);
  }

  private TestListener test_listener_;
  private TestPropagator test_propagator_;
  
  private final BitWord test_propagator_initial_value_ = BitWord.FALSE;
  private final InputId test_listener_input_id_ = InputId.Pc;
  private final InputId other_input_id_ = InputId.External;
  private final OutputId other_output_id_ = OutputId.External;
}
