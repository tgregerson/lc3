package lc3sim.test.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import lc3sim.core.*;

public class MemoryTest {

  @Before
  public void setUp() throws Exception {
    memory_ = new Memory();
    listener_ = new TestListener(InputId.External);
    memory_.RegisterListenerCallback(
        listener_.GetCallback(OutputId.Memory, null));
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void PropagateOnPostClockTest() {
    BitWord value1 = BitWord.FromInt(64, Memory.kWordSize);
    BitWord value2 = BitWord.FromInt(511, Memory.kWordSize);
    
    BitWord addr1 = BitWord.FromInt(4, Memory.kNumAddrBits);
    BitWord addr2 = BitWord.FromInt(30499, Memory.kNumAddrBits);

    OutputId int_sender = OutputId.Bus;

    // Enable signal
    memory_.Notify(BitWord.FALSE, int_sender, InputId.MemoryWriteEnable, null);
    memory_.Notify(value1, int_sender, InputId.MemoryData, null);
    memory_.Notify(addr1, int_sender, InputId.MemoryAddr, null);
    memory_.PreClock();
    memory_.PostClock();
    assertFalse(listener_.last_bitword == value1);

    // Writes are synchronous
    memory_.Notify(BitWord.TRUE, int_sender, InputId.MemoryWriteEnable, null);
    assertFalse(listener_.last_bitword == value1);
    memory_.PreClock();
    memory_.PostClock();
    assertEquals(value1, listener_.last_bitword);
    
    // Reads are asynchronous
    memory_.Notify(addr2, int_sender, InputId.MemoryAddr, null);
    memory_.Notify(value2, int_sender, InputId.MemoryData, null);
    memory_.PreClock();
    memory_.PostClock();
    memory_.Notify(addr2, int_sender, InputId.MemoryAddr, null);
    assertEquals(value2, listener_.last_bitword);
  }

  @Test
  public void ExternalForceValueTest() {
    BitWord value1 = BitWord.FromInt(512, ArchitecturalState.kWordSize);
    
    OutputId int_sender = OutputId.Bus;
    OutputId ext_sender = OutputId.External;
    
    // External ID forced update without needing clock tick.
    BitWord ext_address = BitWord.FromInt(25000, Memory.kNumAddrBits);
    memory_.Notify(BitWord.FALSE, int_sender, InputId.MemoryWriteEnable, null);
    Memory.MemoryStateUpdate update =
        new Memory.MemoryStateUpdate(ext_address, value1);
    memory_.Notify(null, ext_sender, InputId.MemoryData, update);
    memory_.Notify(ext_address, int_sender, InputId.MemoryAddr, null);
    assertEquals(listener_.last_bitword, value1);
  }

  private Memory memory_;
  private TestListener listener_;
}
