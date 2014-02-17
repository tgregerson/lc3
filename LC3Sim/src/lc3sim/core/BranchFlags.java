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
    z = true;
    n = p = false;
  }
  
  // Updates flags assuming 'reg_data' was written to the register file.
  public void Update(short reg_data) {
    n = reg_data < 0;
    z = reg_data == 0;
    p = reg_data > 0;
  }
  
  public Boolean IsN() {
    return n;
  }
  
  public Boolean IsP() {
    return p;
  }
  
  public Boolean IsZ() {
    return z;
  }
  
  public void SetN() {
    n = true;
    z = p = false;
  }
  
  public void SetZ() {
    z = true;
    n = p = false;
  }
  
  public void SetP() {
    p = true;
    n = z = false;
  }
  
  private Boolean n, z, p;
}
