package lc3sim.core;

public class ALU extends AbstractPropagator {
  public static final int kNumModeBits = 2;
  public static final int kWordSize = ArchitecturalState.kWordSize;

  public ALU() {
    Init();
  }
  
  public void Init() {
    op_a_ = BitWord.Zeroes(kWordSize);
    op_b_ = BitWord.Zeroes(kWordSize);
    alu_k_ = BitWord.Zeroes(kNumModeBits);
    RefreshOutput();
  }

  public void RefreshOutput() {
    ForceUpdateOutput(out_id_);
  }
  
  @Override
  public void Notify(BitWord bit_word, OutputId sender,
                     InputId receiver, Object arg) {
    switch (receiver) {
      case AluA:
        op_a_ = bit_word;
        break;
      case AluB:
        op_b_ = bit_word;
        break;
      case AluK:
        alu_k_ = bit_word;
        break;
      default:
        throw new IllegalArgumentException(
            "Invalid receiver ID specified in ALU: " + receiver);
    }
    UpdateOutput(out_id_);
  }
  
  @Override
  protected BitWord ComputeOutput(OutputId unused) {
    if (kAddMode.IsEqual(alu_k_, false)) {
      return op_a_.AddFixedWidth(op_b_, kWordSize);
    } else if (kAndMode.IsEqual(alu_k_, false)) {
      return op_a_.AndFixedWidth(op_b_, kWordSize);
    } else {
      return op_a_.Invert();
    }
  }
  
  private final BitWord kAddMode = BitWord.FromInt(0, kNumModeBits);
  private final BitWord kAndMode = BitWord.FromInt(1, kNumModeBits);
  // All other permutations are considered NotMode.

  // Buffered inputs
  private BitWord op_a_;  // From register file
  private BitWord op_b_;  // From register file or immediate
  private BitWord alu_k_;  // ALU mode select signal.

  private final OutputId out_id_ = OutputId.Alu;
}
