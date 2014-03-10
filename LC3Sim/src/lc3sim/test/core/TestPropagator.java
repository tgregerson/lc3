package lc3sim.test.core;

import lc3sim.core.*;

// Test-only Propagator for use in testing the underlying implementation of
// AbstractPropagator.
public class TestPropagator extends AbstractPropagator {
  public TestPropagator(BitWord initial_bw) {
    last_bitword_ = initial_bw;
  }
  
  public void Notify(BitWord bw, OutputId oid, InputId iid, Object obj) {
    last_bitword_ = bw;
    UpdateOutput(oid);
  }
  
  public BitWord ComputeOutput(OutputId unused) {
    return last_bitword_;
  }
  
  private BitWord last_bitword_;
}
