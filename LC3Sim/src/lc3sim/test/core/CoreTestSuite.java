package lc3sim.test.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ControlPlaneTest.class, MemoryTest.class, MultiplexerTest.class,
               PropagatorTest.class, RegisterFileTest.class, RegisterTest.class,
               TriStateBufferTest.class})
public class CoreTestSuite {

}
