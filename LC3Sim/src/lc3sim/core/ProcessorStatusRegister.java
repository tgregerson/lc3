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
  
  public void Init() {
    q_ = BitWord.Zeroes(kNumBits).SetBit(kZBit, true);
    d_ = q_;
    UpdateOutput(OutputId.Psr);
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
          int set_count = 0;
          if (d_.TestBit(kNBit)) set_count++;
          if (d_.TestBit(kZBit)) set_count++;
          if (d_.TestBit(kPBit)) set_count++;
          assert set_count == 1;
          break;
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
