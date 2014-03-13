package lc3sim.test.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import lc3sim.core.*;

public class RegisterFileTest {

  @Before
  public void setUp() throws Exception {
    register_file_ = new RegisterFile();
    sr1_listener_ = new TestListener(InputId.External);
    sr2_listener_ = new TestListener(InputId.External);
    register_file_.RegisterListenerCallback(
        sr1_listener_.GetCallback(OutputId.GprSr1, null));
    register_file_.RegisterListenerCallback(
        sr2_listener_.GetCallback(OutputId.GprSr2, null));
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void PropagateOnPostClockTest() {
    BitWord value1 = BitWord.FromInt(64, RegisterFile.kWordSize);
    BitWord value2 = BitWord.FromInt(511, RegisterFile.kWordSize);
    
    BitWord addr1 = BitWord.FromInt(4, RegisterFile.kNumAddrBits);
    BitWord addr2 = BitWord.FromInt(1, RegisterFile.kNumAddrBits);

    OutputId int_sender = OutputId.Bus;

    // Enable signal
    register_file_.Notify(BitWord.FALSE, int_sender, InputId.GprDrLoad, null);
    register_file_.Notify(value1, int_sender, InputId.GprDrData, null);
    register_file_.Notify(addr1, int_sender, InputId.GprDrAddr, null);
    register_file_.Notify(addr1, int_sender, InputId.GprSr1Addr, null);
    register_file_.PreClock();
    register_file_.PostClock();
    assertFalse(sr1_listener_.last_bitword == value1);

    // Writes are synchronous
    register_file_.Notify(BitWord.TRUE, int_sender, InputId.GprDrLoad, null);
    assertFalse(sr1_listener_.last_bitword == value1);
    register_file_.PreClock();
    register_file_.PostClock();
    assertEquals(value1, sr1_listener_.last_bitword);
    
    // Reads are asynchronous
    register_file_.Notify(addr2, int_sender, InputId.GprDrAddr, null);
    register_file_.Notify(value2, int_sender, InputId.GprDrData, null);
    register_file_.PreClock();
    register_file_.PostClock();
    register_file_.Notify(addr2, int_sender, InputId.GprSr1Addr, null);
    assertEquals(value2, sr1_listener_.last_bitword);
    
    // Can read two values at once.
    register_file_.Notify(addr1, int_sender, InputId.GprSr2Addr, null);
    assertEquals(value2, sr1_listener_.last_bitword);
    assertEquals(value1, sr2_listener_.last_bitword);
  }

  @Test
  public void ExternalForceValueTest() {
    BitWord value1 = BitWord.FromInt(512, ArchitecturalState.kWordSize);
    
    OutputId int_sender = OutputId.Bus;
    OutputId ext_sender = OutputId.External;
    
    // External ID forced update without needing clock tick.
    register_file_.Notify(BitWord.FALSE, int_sender, InputId.GprDrLoad, null);
    RegisterFile.RegisterStateUpdate update =
        new RegisterFile.RegisterStateUpdate(7, value1);
    register_file_.Notify(null, ext_sender, InputId.GprDrData, update);
    register_file_.Notify(
        BitWord.FromInt(7, RegisterFile.kNumAddrBits), int_sender,
                        InputId.GprSr1Addr, null);
    assertEquals(sr1_listener_.last_bitword, value1);
  }

  private RegisterFile register_file_;
  private TestListener sr1_listener_;
  private TestListener sr2_listener_;
}
