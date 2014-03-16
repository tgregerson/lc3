package lc3sim.core;

// An architectural element that performs zero or sign extension.
//
// This class is largely a wrapper around the Resize() method of
// BitWord, but is included as a separate class to allow for
// easier visibility to Listeners.
public class BitExtender extends AbstractPropagator {
  public BitExtender(OutputId output_id, int input_bits, int output_bits,
                      Boolean sign_extend) {
    assert input_bits < output_bits;
    input_bits_ = input_bits;
    output_bits_ = output_bits;
    out_id_ = output_id;
    sign_extend_ = sign_extend;
    Init();
  }
  
  public void Init() {
    input_buffer_ = BitWord.Zeroes(output_bits_);
    UpdateOutput(out_id_);
  }
  
  public void Notify(BitWord bit_word, OutputId sender, InputId receiver,
                     Object arg) {
    input_buffer_.Resize(input_bits_, sign_extend_);
    UpdateOutput(out_id_);
  }
  
  protected BitWord ComputeOutput(OutputId unused) {
    return input_buffer_.Resize(output_bits_, sign_extend_);
  }
  
  // Sign extends 'in_value', which has 'in_bits' to 16-bits.
  public static short SignExtend16(short in_value, int in_bits) {
    int most_significant_bit = in_value >> (in_bits - 1);
    if (most_significant_bit == 0) {
      // Value is positive or zero. No extension necessary.
      return in_value;
    } else {
      // Value is negative. Create sign extension mask.
      short mask = Short.MIN_VALUE;
      mask = (short)(mask >> (16 - in_bits - 1));
      short ret = (short)(mask | in_value);
      assert ret < 0;
      return ret;
    }
  }
  
  private BitWord input_buffer_;
  private final OutputId out_id_;
  private final int input_bits_;
  private final int output_bits_;
  private final Boolean sign_extend_;
}
