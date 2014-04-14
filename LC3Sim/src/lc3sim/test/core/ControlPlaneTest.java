package lc3sim.test.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import lc3sim.core.*;
import lc3sim.core.instructions.*;
import lc3sim.test.core.instructions.*;

public class ControlPlaneTest {

  @Before
  public void setUp() throws Exception {
    cycle_clock_ = new CycleClock();
    state_machine_ = new StateMachine(cycle_clock_);
    control_logic_ = new ControlLogic();
    cycle_listener_ = new TestListener(InputId.DontCare);
    control_set_listener_ = new TestControlSetListener();
    prior_instruction_ = Instruction.FromBitWord(BitWord.Zeroes(kWordSize));
    psr_ = new ProcessorStatusRegister();

    List<ListenerCallback> control_set_callbacks =
        control_set_listener_.GetCallbacks();
    for (ListenerCallback cb : control_set_callbacks) {
      control_logic_.RegisterListenerCallback(cb);
    }
    control_logic_.Notify(psr_.Read(), OutputId.Psr, InputId.ControlPsr, null);
    
    state_machine_.RegisterListenerCallback(
        new ListenerCallback(control_logic_, OutputId.StateMachineCycle,
                             InputId.ControlState, null));
    state_machine_.RegisterListenerCallback(
        new ListenerCallback(control_logic_, OutputId.StateMachineInstruction,
                             InputId.ControlInstruction, null));
    state_machine_.RegisterListenerCallback(
        cycle_listener_.GetCallback(OutputId.StateMachineCycle, null));
    state_machine_.Reset();

    state_machine_.Notify(
        prior_instruction_.bitword(), OutputId.Ir, null, null);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void AddInstructionTest() {
    BitWord bits = BitWord.FromInt(0x1642, kWordSize);
    Instruction instruction = Instruction.FromBitWord(bits);
    assertEquals(OpCode.ADD, instruction.op_code());
    assertTrue(instruction instanceof AddInstruction);

    TestAllInstructionCycles(instruction);
  }
  
  @Test
  public void RandomInstructionStream() {
    final int kRandomInstructionIterations = 1000;
    assertTrue(cycle_listener_.CheckSequence().isEmpty());
    HashSet<OpCode> blacklist = new HashSet<OpCode>();
    blacklist.add(OpCode.RESERVED);
    blacklist.add(OpCode.RTI);  // TODO remove from blacklist when ready.
    for (int i = 0; i < kRandomInstructionIterations; ++i) {
      Instruction instruction = 
          InstructionTestUtil.RandomInstructionWithBlacklist(blacklist);
      TestAllInstructionCycles(instruction);
    }
  }
  
  private void TestAllInstructionCycles(Instruction instruction) {
    TestFetchInstruction();

    // Give the state machine the new instruction that should be loaded from
    // FetchInstruction.
    state_machine_.Notify(instruction.bitword(), OutputId.Ir,
                          InputId.DontCare, null);

    // Optional cycles.
    // TODO RESERVED should go directly to interrupt at this point.
    switch (instruction.op_code()) {
      case LD:    // Fallthrough intended.
      case LDR:
        TestEvaluateAddress(instruction);
        TestFetchOperands(instruction);
        break;
      case ST:    // Fallthrough intended.
      case STR:
        TestEvaluateAddress(instruction);
        TestExecuteOperation(instruction);
        break;
      case TRAP:
        TestExecuteOperation(instruction);
        break;
      case LDI:   // Fallthrough intended.
      case STI:
        TestEvaluateAddress(instruction);
        TestFetchOperands(instruction);
        TestExecuteOperation(instruction);
        break;
      case RTI:   // Fallthrough intended.
      case RESERVED:
        assert false;
      default:
        break;
    }
    TestStoreResult(instruction);
    prior_instruction_ = instruction;
  }
  
  private void TestFetchInstruction() {
    // Go to FetchInstruction1
    if (!state_machine_.IsRunning()) {
      state_machine_.Start();
    } else {
      state_machine_.ExecuteCurrentCycle();
    }
    control_set_listener_.CurrentControlSet().AssertEquals(
        prior_instruction_.ControlSet(
            InstructionCycle.kFetchInstruction1, psr_.Read()));

    // Go to FetchInstruction2
    state_machine_.ExecuteCurrentCycle();
    control_set_listener_.CurrentControlSet().AssertEquals(
        prior_instruction_.ControlSet(
            InstructionCycle.kFetchInstruction2, psr_.Read()));

    // Go to FetchInstruction3
    state_machine_.ExecuteCurrentCycle();
    control_set_listener_.CurrentControlSet().AssertEquals(
        prior_instruction_.ControlSet(
            InstructionCycle.kFetchInstruction3, psr_.Read()));

  }
  
  private void TestEvaluateAddress(Instruction current_instruction) {
    assertEquals(true, state_machine_.IsRunning());
    // Go to EvaluateAddress1
    state_machine_.ExecuteCurrentCycle();
    control_set_listener_.CurrentControlSet().AssertEquals(
        current_instruction.ControlSet(
            InstructionCycle.kEvaluateAddress1, psr_.Read()));
  }

  private void TestFetchOperands(Instruction current_instruction) {
    assertEquals(true, state_machine_.IsRunning());
    // Go to FetchOperands1
    state_machine_.ExecuteCurrentCycle();
    control_set_listener_.CurrentControlSet().AssertEquals(
        current_instruction.ControlSet(
            InstructionCycle.kFetchOperands1, psr_.Read()));
  }

  private void TestExecuteOperation(Instruction current_instruction) {
    assertEquals(true, state_machine_.IsRunning());
    // Go to ExecuteOperation1
    state_machine_.ExecuteCurrentCycle();
    control_set_listener_.CurrentControlSet().AssertEquals(
        current_instruction.ControlSet(
            InstructionCycle.kExecuteOperation1, psr_.Read()));
    if (current_instruction.op_code() == OpCode.LDI ||
        current_instruction.op_code() == OpCode.STI) {
      // Go to ExecuteOperation2
      cycle_clock_.Tick();
      control_set_listener_.CurrentControlSet().AssertEquals(
          current_instruction.ControlSet(
              InstructionCycle.kExecuteOperation2, psr_.Read()));
    }
  }

  private void TestStoreResult(Instruction current_instruction) {
    assertEquals(true, state_machine_.IsRunning());
    // Go to FetchOperands1
    state_machine_.ExecuteCurrentCycle();
    control_set_listener_.CurrentControlSet().AssertEquals(
        current_instruction.ControlSet(
            InstructionCycle.kStoreResult1, psr_.Read()));
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
