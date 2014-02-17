package lc3sim.core;

// Models the LC3 branch flags register.
// Consists of 3 bits, n, z, and p, representing whether the last value
// written to the register file was negative, zero, or positive, respectively.
// As such, only one of n, z, and p may ever be set to true. Initializes to z
// being true.
public class BranchFlags {
  public BranchFlags() {
    Init();
  }
  
  public void Init() {
    z_ = true;
    n_ = p_ = false;
  }
  
  // Updates flags assuming 'reg_data' was written to the register file.
  public void Update(short reg_data) {
    n_ = reg_data < 0;
    z_ = reg_data == 0;
    p_ = reg_data > 0;
  }
  
  public Boolean get_n() {
    return n_;
  }
  
  public Boolean get_p() {
    return p_;
  }
  
  public Boolean get_z() {
    return z_;
  }
  
  // Setter methods enforce requirement that only one of n, z, p,
  // can be set at once. Therefore it is only possible to set to
  // 'true', and the other flags will be set to false.
  public void set_n() {
    n_ = true;
    z_ = p_ = false;
  }
  
  public void set_z() {
    z_ = true;
    n_ = p_ = false;
  }
  
  public void set_p() {
    p_ = true;
    n_ = z_ = false;
  }
  
  private Boolean n_, z_, p_;
}
