package lc3sim.core;

public class ALU extends AbstractPropagator {
  // ALU Interface
  public ALU() {
    Init();
  }
  
  public void Init() {
    op_a_ = new BitWord(bitwidth_);
    op_b_ = new BitWord(bitwidth_);
    op_code_ = new BitWord(op_code_width_);
    SetCurrentOutput(out_id_, new BitWord(bitwidth_));
  }
  
  @Override
  public void Notify(BitWord bit_word, OutputId sender,
                     InputId receiver, Object arg) {
    if (receiver == in_id_a_) {
      op_a_ = bit_word;
    } else if (receiver == in_id_b_){
      op_b_ = bit_word;
    }
    UpdateOutput(out_id_);
  }
  
  @Override
  protected BitWord ComputeOutput(OutputId unused) {
    if (op_code_.IsEqual(OpCode.kADD.as_BitWord())) {
      return op_a_.AddFixedWidth(op_b_, bitwidth_);
    } else if (op_code_.IsEqual(OpCode.kAND.as_BitWord())){
      return op_a_.AndFixedWidth(op_b_, bitwidth_);
    } else if (op_code_.IsEqual(OpCode.kNOT.as_BitWord())) {
      return op_a_.Invert();
    } else {
      assert false;
      return null;
    }
  }
  
  // Buffered inputs
  private BitWord op_a_; // From register file
  private BitWord op_b_; // From register file or immediate
  private BitWord op_code_;

  private final int bitwidth_ = 16;
  private final int op_code_width_ = 4;
  private final InputId in_id_a_ = InputId.AluA;
  private final InputId in_id_b_ = InputId.AluB;
  private final OutputId out_id_ = OutputId.Alu;
}
