package lc3sim.test.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import lc3sim.core.*;

public class RegisterTest {

  @Before
  public void setUp() throws Exception {
    register_ = new Register(
        16, reg_data_input_id_, reg_enable_input_id_, reg_output_id_);
    listener_ = new TestListener(InputId.External);
    register_.RegisterListenerCallback(
        listener_.GetCallback(reg_output_id_, null));
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void PropagateOnPostClockTest() {
    BitWord value1 = BitWord.FromInt(512, 16);
    BitWord value2 = BitWord.FromInt(7800, 16);
    

    // Output does not get propagated until a clock tick.
    register_.Notify(value1, int_id_, reg_data_input_id_, null);
    assertFalse(listener_.last_bitword == value1);
    register_.Notify(BitWord.TRUE, int_id_, reg_enable_input_id_, null);
    assertFalse(listener_.last_bitword == value1);
    
    // Initiate clock tick
    register_.PreClock();
    assertFalse(listener_.last_bitword == value1);
    register_.PostClock();
    assertEquals(listener_.last_bitword, value1);
    
    // Change to output doesn't get propagated until next clock tick
    register_.Notify(value2, int_id_, reg_data_input_id_, null);
    register_.PreClock();
    assertEquals(listener_.last_bitword, value1);
    register_.PostClock();
    assertEquals(listener_.last_bitword, value2);
  }

  @Test
  public void ClockToQTest() {
    BitWord value1 = BitWord.FromInt(512, 16);
    BitWord value2 = BitWord.FromInt(7800, 16);

    // Output does not get propagated until a clock tick.
    register_.Notify(value1, int_id_, reg_data_input_id_, null);
    register_.Notify(BitWord.TRUE, int_id_, reg_enable_input_id_, null);
    
    // Initiate clock tick
    register_.PreClock();
    assertFalse(listener_.last_bitword == value1);
    register_.Notify(value2, int_id_, reg_data_input_id_, null);
    assertFalse(listener_.last_bitword == value1);
    
    // If input value changes between Pre and Post Clock, the Pre-Clock version
    // is the one that gets propagated.
    register_.PostClock();
    assertEquals(listener_.last_bitword, value1);
  }

  @Test
  public void ExternalForceValueTest() {
    BitWord value1 = BitWord.FromInt(512, 16);
    BitWord value2 = BitWord.FromInt(7800, 16);
    BitWord value3 = BitWord.FromInt(5555, 16);

    // External ID forced update without needing clock tick.
    register_.Notify(value1, int_id_, reg_data_input_id_, null);
    register_.Notify(BitWord.TRUE, int_id_, reg_enable_input_id_, null);
    register_.Notify(value2, ext_id_, reg_data_input_id_, null);
    assertEquals(listener_.last_bitword, value2);
    
    // Normal data propagates on clock edge.
    register_.PreClock();
    register_.PostClock();
    assertEquals(listener_.last_bitword, value1);
    
    // Force also works if enable is not set.
    register_.Notify(BitWord.FALSE, int_id_, reg_enable_input_id_, null);
    register_.Notify(value3, ext_id_, reg_data_input_id_, null);
    assertEquals(listener_.last_bitword, value3);
    
    // Value sticks after clock edge.
    register_.PreClock();
    register_.PostClock();
    assertEquals(listener_.last_bitword, value3);
  }

  private Register register_;
  private TestListener listener_;
  private final OutputId reg_output_id_ = OutputId.Mar;
  private final InputId reg_data_input_id_ = InputId.Mar;
  private final InputId reg_enable_input_id_ = InputId.MarLoad;
  private final OutputId int_id_ = OutputId.Alu;
  private final OutputId ext_id_ = OutputId.External;
}
