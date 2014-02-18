package lc3sim.core;

import java.util.BitSet;

// BitWords are immutable data words that support bit-level operations. Because
// they are immutable, any operation that changes a BitWord returns a new object
// rather than altering the state of the current BitWord.
public class BitWord {
  
  public BitWord(int num_bits) {
    bits_ = new BitSet(num_bits);
  }
  
  public BitWord(BitSet bit_set) {
    bits_ = (BitSet)bit_set.clone();
  }
  
  public BitWord(BitWord bit_word) {
    bits_ = (BitSet)bit_word.bit_set().clone();
  }
  
  public Boolean TestBit(int bit_index) {
    return bits_.get(bit_index);
  }
  
  // Returns true if this and 'cmp' have the same value. If they are not
  // the same length, the shorter will be sign-extended.
  public Boolean IsEqual(BitWord cmp) {
    int length = num_bits() > cmp.num_bits() ? num_bits() : cmp.num_bits();
    BitWord copy = Resize(length, true);
    cmp = cmp.Resize(length, true);
    Boolean equal = true;
    for (int i = 0; i < length; ++i) {
      if (copy.TestBit(i) != cmp.TestBit(i)) {
        equal = false;
        break;
      }
    }
    return equal;
  }
  
  public BitWord SetBit(int bit_index) {
    BitSet bit_set = (BitSet)bits_.clone();
    bit_set.set(bit_index);
    return new BitWord(bit_set);
  }
  
  public Boolean IsNegative() {
    return bits_.get(bits_.size() - 1);
  }
  
  public BitWord GetBitRange(int high_bit, int low_bit) {
    BitSet bit_set = bits_.get(low_bit, high_bit + 1);
    return new BitWord(bit_set);
  }
  
  public int num_bits() {
    return bits_.size();
  }
  
  public BitWord Resize(int new_num_bits, Boolean sign_extend) {
    if (new_num_bits == num_bits()) {
      return new BitWord(bits_);
    } else if (new_num_bits > num_bits()) {
      BitSet bit_set = new BitSet(new_num_bits);
      Boolean extend_val = sign_extend ? IsNegative() : false;
      for (int i = 0; i < bit_set.size(); ++i) {
        if (i <= num_bits()) {
          bit_set.set(i, bits_.get(i));
        } else {
          bit_set.set(i, extend_val);
        }
      }
      return new BitWord(bit_set);
    } else {
      return GetBitRange(new_num_bits - 1, 0);
    }
  }
  
  public BitWord Invert() {
    BitSet bit_set = (BitSet)bits_.clone();
    bit_set.flip(0, bit_set.size() - 1);
    return new BitWord(bit_set);
  }

  
  public BitSet bit_set() {
    return bits_;
  }
  
  public static BitWord FromBoolean(Boolean bool) {
    BitSet one_bit = new BitSet(1);
    one_bit.set(0, bool);
    return new BitWord(one_bit);
  }
  
  public static BitWord FromShort(short value) {
    long value_long[] = {value};
    BitSet bit_set = BitSet.valueOf(value_long);
    return (new BitWord(bit_set)).Resize(16, false);
  }
  
  public static BitWord FromInt(int value) {
    long value_long[] = {value};
    BitSet bit_set = BitSet.valueOf(value_long);
    return (new BitWord(bit_set)).Resize(16, false);
  }
  
  private BitSet bits_;
}
