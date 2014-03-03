package lc3sim.core;

public class ProcessorStatusRegister extends Register {
  public static int kNumBits = 16;
  
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
          assert data.num_bits() > n_index_;
          d_ = d_.SetBit(n_index_, data.TestBit(n_index_)).
                  SetBit(z_index_, data.TestBit(z_index_)).
                  SetBit(p_index_, data.TestBit(p_index_));
          break;
        case Pc:
          // Set the privilege bit to 0 when in kernel memory regions:
          // 0x0000 ~ 0x3000.
          Boolean user_region = data.ToInt() >= 0x03000;
          d_ = d_.SetBit(privilege_index_, user_region);
        // TODO: Add load enable
        default:
          assert false;
      }
    }
  }
  
  public Boolean supervisor_mode() {
    return !Read().TestBit(privilege_index_);
  }

  private final int privilege_index_ = 15;
  private final int n_index_ = 2;
  private final int z_index_ = 1;
  private final int p_index_ = 0;
}
