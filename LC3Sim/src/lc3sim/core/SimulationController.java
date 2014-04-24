package lc3sim.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lc3sim.core.ArchitecturalState.ListenerBinding;

public class SimulationController implements Listener {
  
  public SimulationController() {}
  
  public void SetModel(ArchitecturalState model) {
    if (model_ != null) {
      // If we are replacing the existing model, stop listening to the old one.
      model_.ResetExternalListenerBindings();
    }
    model_ = model;
    BindToModel();
  }
  
  public void SetView(lc3sim.ui.UIFXMain view) {
    view_ = view;
  }
  
  public int Run() {
    return AdvanceToAddr(breakpoints_);
  }
  
  // Ignores breakpoints.
  public int StepOver() {
    Set<Integer> address = new HashSet<Integer>(); 
    address.add(ModuloSum(model_.Pc(), 1, ArchitecturalState.kWordSize));
    return AdvanceToAddr(address);
  }
  
  public int StepInto() {
    return model_.ExecuteInstruction();
  }
  
  public void AddBreakpoint(int address) {
    breakpoints_.add(address);
  }
  
  public void RemoveBreakpoint(int address) {
    breakpoints_.remove(address);
  }
  
  public void RemoveAllBreakpoints() {
    breakpoints_.clear();
  }

  private int ModuloSum(int a, int b, int bits) {
    return (a + b) % (1 << (bits - 1));
  }

  // Advances the state machine until the PC is equal to one of the values in
  // 'addresses' (or halt). Stops at the beginning of the next cycle.
  private int AdvanceToAddr(Set<Integer> addresses) {
    while (true) {
      int current_address = StepInto();
      if (addresses.contains(current_address)) {
        return current_address;
      }
    }
  }
  
  private void BindToModel() {
    model_.ResetExternalListenerBindings();
    List<ListenerBinding> bindings = new ArrayList<ListenerBinding>();
    
    // Add always-on bindings.
    bindings.add(new ListenerBinding(this, OutputId.MemoryInternal));
    
    // Add optional bindings.
    if (options_.show_internal_signals) {
      // TODO bind internal signals
    }

    model_.AddExternalListenerBindings(bindings);
  }

  @Override
  public void Notify(BitWord bit_word, OutputId sender, InputId receiver,
                     Object arg) {
    switch (sender) {
      case MemoryInternal:
        if (!(arg instanceof Memory.MemoryStateUpdate)) {
          throw new IllegalArgumentException(
              "Expected MemoryStateUpdate from memory.");
        }
        Memory.MemoryStateUpdate update = (Memory.MemoryStateUpdate)(arg);
        view_.UpdateMemory(update.address, update.value);
        break;
      default:
        throw new IllegalArgumentException(
            "Unexpected notification from: " + sender);
    }
  }
  
  private class Options {
    // If true, controller monitors and relays changes to combinational signals.
    public boolean show_internal_signals = false;
  };
  
  private Options options_;
  
  private lc3sim.ui.UIFXMain view_;
  private ArchitecturalState model_;
  
  private Set<Integer> breakpoints_;

}
