package lc3sim.core;

// Models the LC3 branch flags register.
// Consists of 3 bits, n, z, and p, representing whether the last value
// written to the register file was negative, zero, or positive, respectively.
// As such, only one of n, z, and p may ever be set to true. Initializes to z
// being true.
public class BranchFlagLogic extends AbstractPropagator{ 
  public BranchFlagLogic() {
    Init();
  }
  
  public void Init() {
    z_ = true;
    n_ = p_ = false;
  }
  
  // Updates flags assuming 'reg_data' was written to the register file.
  public void Notify (BitWord data, OutputId sender, InputId receiver,
                      Object arg) {
    assert data.num_bits() == 16;
    n_ = data.TestBit(15);
    z_ = !data.ToBoolean();
    p_ = !(n_ || z_);
    UpdateOutput(OutputId.NzpLogic);
  }
  
  public BitWord ComputeOutput(OutputId unused) {
    BitWord out = BitWord.Zeroes(3);
    out = out.SetBit(n_index_, n_);
    out = out.SetBit(z_index_, z_);
    out = out.SetBit(p_index_, p_);
    return out;
  }
  
  private Boolean n_, z_, p_;
  private final int n_index_ = 2;
  private final int z_index_ = 1;
  private final int p_index_ = 0;
}
