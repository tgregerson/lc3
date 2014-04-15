package lc3sim.core;

public class ProcessorStatusRegister extends Register {
  public static final int kNumBits = 16;
  public static final int kPrivilegeBit = 15;
  public static final int kNBit = 2;
  public static final int kZBit = 1;
  public static final int kPBit = 0;
  
  public ProcessorStatusRegister() {
    super(kNumBits, InputId.Psr, InputId.PsrLoad, OutputId.Psr);
  }
  
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    // Sanity-check updates to NZP bits before forwarding to superclass.
    if (receiver == InputId.Psr) {
      int set_count = 0;
      if (data.TestBit(kNBit)) set_count++;
      if (data.TestBit(kZBit)) set_count++;
      if (data.TestBit(kPBit)) set_count++;
      if (set_count != 1) {
        throw new IllegalStateException(
            "PSR contains " + set_count + " set bits.");
      }
    }
    super.Notify(data, sender, receiver, arg);
  }
  
  public Boolean supervisor_mode() {
    return !Read().TestBit(kPrivilegeBit);
  }

}
