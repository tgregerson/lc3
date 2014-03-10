package lc3sim.core;

public class ProcessorStatusRegister extends Register {
  public static final int kNumBits = 16;
  public static final int kPrivilegeBit = 15;
  public static final int kNBit = 2;
  public static final int kZBit = 1;
  public static final int kPBit = 0;
  
  public ProcessorStatusRegister() {
    super(kNumBits, InputId.DontCare, InputId.PsrLoad, OutputId.Psr);
  }
  
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    if (sender == OutputId.External) {
      // Asynchronous set command. Immediately update.
      q_ = data.Resize(16, false);
      UpdateOutput(out_id_);
    } else {
      switch (sender) {
        case NzpLogic:
          assert data.num_bits() > kNBit;
          d_ = d_.SetBit(kNBit, data.TestBit(kNBit)).
                  SetBit(kZBit, data.TestBit(kZBit)).
                  SetBit(kPBit, data.TestBit(kPBit));
          break;
        case Pc:
          // Set the privilege bit to 0 when in kernel memory regions:
          // 0x0000 ~ 0x3000.
          Boolean user_region = data.ToInt() >= 0x03000;
          d_ = d_.SetBit(kPrivilegeBit, user_region);
        // TODO: Add load enable
        default:
          assert false;
      }
    }
  }
  
  public Boolean supervisor_mode() {
    return !Read().TestBit(kPrivilegeBit);
  }

}
