package lc3sim.test.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.LinkedList;

import lc3sim.core.*;
import lc3sim.core.StateMachine.InstructionCycle;
import lc3sim.core.instructions.*;

public class ControlPlaneTest {

  @Before
  public void setUp() throws Exception {
    cycle_clock_ = new CycleClock();
    state_machine_ = new StateMachine(cycle_clock_);
    control_logic_ = new ControlLogic();
    cycle_listener_ = new TestListener(InputId.DontCare);
    control_set_listener_ = new TestControlSetListener();
    prior_instruction_ = Instruction.FromBitWord(new BitWord(kWordSize));
    psr_ = new ProcessorStatusRegister();

    List<ListenerCallback> control_set_callbacks =
        control_set_listener_.GetCallbacks();
    for (ListenerCallback cb : control_set_callbacks) {
      control_logic_.RegisterListenerCallback(cb);
    }
    
    state_machine_.RegisterListenerCallback(
        new ListenerCallback(control_logic_, OutputId.StateMachineCycle,
                             InputId.ControlState, null));
    state_machine_.RegisterListenerCallback(
        cycle_listener_.GetCallback(OutputId.StateMachineCycle, null));
    state_machine_.Reset();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void AddInstructionTest() {
    assertTrue(cycle_listener_.CheckSequence().isEmpty());
    LinkedList<BitWord> check_sequence = new LinkedList<BitWord>();
    check_sequence.add(InstructionCycle.kFetchInstruction1.as_BitWord());
    check_sequence.add(InstructionCycle.kFetchInstruction2.as_BitWord());
    check_sequence.add(InstructionCycle.kFetchInstruction3.as_BitWord());
    cycle_listener_.AppendCheckSequence(check_sequence);

    // DR = 3, SR1 = 1, SR2 = 2
    BitWord bits = BitWord.FromInt(0x1642, kWordSize);
    Instruction instruction = Instruction.FromBitWord(bits);
    assertEquals(OpCode.ADD, instruction.op_code());
    assertTrue(instruction instanceof AddInstruction);
    state_machine_.Notify(bits, OutputId.Ir, null, null);
    
    TestFetchInstruction();

    assertTrue(cycle_listener_.CheckSequence().isEmpty());
  }
  
  private void TestFetchInstruction() {
    // Go to FetchInstruction1
    if (!state_machine_.IsRunning()) {
      state_machine_.Start();
    } else {
      cycle_clock_.Tick();
    }
    control_set_listener_.CurrentControlSet().AssertEquals(
        prior_instruction_.ControlSet(
            InstructionCycle.kFetchInstruction1, psr_.Read()));

    // Go to FetchInstruction2
    cycle_clock_.Tick();
    control_set_listener_.CurrentControlSet().AssertEquals(
        prior_instruction_.ControlSet(
            InstructionCycle.kFetchInstruction2, psr_.Read()));

    // Go to FetchInstruction3
    cycle_clock_.Tick();
    control_set_listener_.CurrentControlSet().AssertEquals(
        prior_instruction_.ControlSet(
            InstructionCycle.kFetchInstruction3, psr_.Read()));
  }
  
  private CycleClock cycle_clock_;
  private StateMachine state_machine_;
  private ControlLogic control_logic_;
  private TestListener cycle_listener_;
  private TestControlSetListener control_set_listener_;
  private Instruction prior_instruction_;
  private ProcessorStatusRegister psr_;
  
  private final int kWordSize = 16;

}
