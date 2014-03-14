package lc3sim.core;

import java.util.HashMap;
import java.util.Map;

public class Multiplexer extends AbstractPropagator {
  public Multiplexer(int num_addr_bits, int num_data_bits,
                     AddressBinding[] bindings, InputId select_id,
                     OutputId output_id) {
    num_addr_bits_ = num_addr_bits;
    num_data_bits_ = num_data_bits;
    out_id_ = output_id;
    num_inputs_ = 1 << num_addr_bits;
    select_id_ = select_id;
    assert bindings != null && bindings.length <= num_inputs_ &&
           bindings.length > 0;
    address_bindings_ = new HashMap<InputId, Integer>();
    for (int i = 0; i < bindings.length; ++i) {
      assert bindings[i].address.num_bits() <= num_addr_bits;
      address_bindings_.put(bindings[i].input,
                            bindings[i].address.ToInt());
    }
    Init();
  }
  
  public void Init() {
    address_buffer_ = new BitWord(num_addr_bits_);
    input_buffers_ = new BitWord[num_inputs_];
    for (int i = 0; i < num_inputs_; ++i) {
      input_buffers_[i] = new BitWord(num_data_bits_);
    }
  }
  
  // BasicPropagator interface
  public void Notify(BitWord data, OutputId sender, InputId receiver,
                     Object arg) {
    if (address_bindings_.containsKey(receiver)) {
      input_buffers_[address_bindings_.get(receiver)] =
          data.Resize(num_data_bits_, false); 
    } else if (receiver == select_id_){
      address_buffer_ = data.Resize(num_addr_bits_, false);
      assert address_buffer_.ToInt() < input_buffers_.length;
    } else {
      assert false;
    }
    UpdateOutput(out_id_);
  }
  
  public BitWord ComputeOutput(OutputId unused) {
    return input_buffers_[address_buffer_.ToInt()];
  }
  
  // Binds the address 'address' to 'input'. The address must be
  // within the range of legal addresses based on the number of address bits.
  public static class AddressBinding {
    public AddressBinding(BitWord addr, InputId in) {
      address = addr;
      input = in;
    }
    public BitWord address;
    public InputId input;
  };
  
  private BitWord address_buffer_;
  private BitWord[] input_buffers_;
  private final Map<InputId, Integer> address_bindings_;
  private final InputId select_id_;
  private final int num_addr_bits_;
  private final int num_data_bits_;
  private final int num_inputs_;
  private final OutputId out_id_;
}
