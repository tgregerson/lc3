package lc3sim.core;

import java.util.Arrays;

// BitWords are immutable data words that support bit-level operations. Because
// they are immutable, any operation that changes a BitWord returns a new object
// rather than altering the state of the current BitWord.
public class BitWord {
  public static final BitWord TRUE = new BitWord(new boolean[]{true});
  public static final BitWord FALSE = new BitWord(new boolean[]{false});
  public static final BitWord EMPTY = new BitWord(new boolean[]{});
  
  private BitWord(boolean bits[]) {
    bits_ = java.util.Arrays.copyOf(bits, bits.length);
  }
  
  public String toString() {
    assert bits_ != null;
    String ret = "";
    for (int i = 0; i < bits_.length; ++i) {
      if (i != 0 && i % 4 == 0) {
        ret = "_".concat(ret);
      }
      if (bits_[i]) {
        ret = "1".concat(ret);
      } else {
        ret = "0".concat(ret);
      }
    }
    return "[" + ret + "]";
  }
  
  public boolean TestBit(int bit_index) {
    assert (bit_index < bits_.length);
    return bits_[bit_index];
  }
  
  // Returns true if this and 'cmp' have the same value even if they have
  // different lengths. If 'signed', values are treated as 2's complement.
  public boolean IsEqual(BitWord cmp, boolean signed) {
    if (cmp == null) {
      return false;
    }
    if (num_bits() == 0 || cmp.num_bits() == 0) {
      return num_bits() == cmp.num_bits();
    }
    int length = this.num_bits() > cmp.num_bits() ?
                 this.num_bits() : cmp.num_bits();
    BitWord copy = Resize(length, signed);
    cmp = cmp.Resize(length, signed);
    boolean equal = true;
    for (int i = 0; i < length; ++i) {
      if (copy.TestBit(i) != cmp.TestBit(i)) {
        equal = false;
        break;
      }
    }
    return equal;
  }
  
  // Checks if both value and number of bits are the same. Is not a check
  // on address.
  public boolean IsIdentical(BitWord cmp) {
    if (cmp == null) {
      return false;
    } else {
      return (num_bits() == cmp.num_bits()) && IsEqual(cmp, false);
    }
  }
  
  // Default equals method treats comparison as unsigned.
  @Override
  public boolean equals(Object other) {
    if (other != null && other instanceof BitWord) {
      return this.IsEqual((BitWord)other, false);
    } else {
      return false;
    }
  }
  
  // Add treats operands as 2's complement and sign extends, or truncates as
  // necessary, and stores the results using the specified number of bits.
  public BitWord AddFixedWidth(BitWord other, int num_bits) {
    // Avoid issues with type conversion by doing bit-wise addition rather than
    // converting to intermediate int/long types.
    BitWord op1 = this.Resize(num_bits, true);
    BitWord op2 = other.Resize(num_bits, true);
    boolean[] sum = new boolean[num_bits];
    boolean carry = false;
    for (int i = 0; i < num_bits; ++ i) {
      boolean bit_sum = op1.TestBit(i) ^ op2.TestBit(i) ^ carry;
      sum[i] = bit_sum;
      carry = (op1.TestBit(i) && op2.TestBit(i)) ||
              (op1.TestBit(i) && carry) ||
              (op2.TestBit(i) && carry);
    }
    return FromBooleanArray(sum);
  }
  
  // Sign extends operands to 'num_bits' and returns their bitwise logic AND.
  public BitWord AndFixedWidth(BitWord other, int num_bits) {
    BitWord op1 = this.Resize(num_bits, true);
    BitWord op2 = other.Resize(num_bits, true);
    boolean[] result = new boolean[num_bits];
    for (int i = 0; i < num_bits; ++ i) {
      result[i] = op1.TestBit(i) && op2.TestBit(i);
    }
    return FromBooleanArray(result);
  }
  
  public BitWord SetBit(int bit_index, boolean value) {
    assert bit_index < bits_.length;
    boolean[] modified = bits_.clone();
    modified[bit_index] = value;
    return FromBooleanArray(modified);
  }
  
  public boolean IsNegative() {
    if (bits_.length == 0) {
      return false;
    } else {
      return bits_[bits_.length - 1];
    }
  }
  
  public BitWord GetBitRange(int high_bit, int low_bit) {
    return FromBooleanArray(
        java.util.Arrays.copyOfRange(bits_, low_bit, high_bit + 1));
  }
  
  public int num_bits() {
    return bits_.length;
  }
  
  public BitWord Append(BitWord suffix) {
    boolean[] new_bits = Arrays.copyOf(
        bits_, bits_.length + suffix.bits().length);
    System.arraycopy(
        suffix.bits(), 0, new_bits, bits_.length, suffix.bits().length);
    return FromBooleanArray(new_bits);
  }
  
  public BitWord Resize(int new_num_bits, Boolean sign_extend) {
    if (new_num_bits == bits_.length) {
      return this;
    } else if (new_num_bits > bits_.length) {
      boolean[] bits = new boolean[new_num_bits];
      boolean extend_val = sign_extend ? IsNegative() : false;
      for (int i = 0; i < new_num_bits; ++i) {
        if (i < bits_.length) {
          bits[i] = bits_[i];
        } else {
          bits[i] = extend_val;
        }
      }
      return FromBooleanArray(bits);
    } else {
      return GetBitRange(new_num_bits - 1, 0);
    }
  }
  
  public BitWord Invert() {
    boolean[] bits = new boolean[bits_.length];
    for (int i = 0; i < bits.length; ++i) {
      bits[i] = !bits[i];
    }
    return FromBooleanArray(bits);
  }

  
  public boolean[] bits() {
    return java.util.Arrays.copyOf(bits_, bits_.length);
  }
  
  // Returns true if value is non-zero.
  public boolean ToBoolean() {
    for (int i = 0; i < num_bits(); ++i) {
      if (TestBit(i)) {
        return true;
      }
    }
    return false;
  }
  
  // Does not sign extend BitWords < 32 bits.
  // Truncates BitWords > 32 bits.
  public int ToInt() {
    assert bits_.length <= 32;
    int residual = 0;
    for (int i = 0; i < num_bits(); ++i) {
      if (bits_[i]) {
        residual += (1 << i);
      }
    }
    return residual;
  }
  
  // Convenience method for avoiding null-checking when comparing two BitWords
  // where either could be null.
  public static boolean Identical(BitWord a, BitWord b) {
    if (a == null) {
      return b == null;
    } else {
      return a.IsIdentical(b);
    }
  }
  
  // Returns a bitword with the specified number of bits, set to all zero.
  public static BitWord Zeroes(int num_bits) {
    boolean[] bits = new boolean[num_bits];
    Arrays.fill(bits, false);
    return FromBooleanArray(bits);
  }

  // Returns a bitword with the specified number of bits, set to all one.
  public static BitWord Ones(int num_bits) {
    boolean[] bits = new boolean[num_bits];
    Arrays.fill(bits, true);
    return FromBooleanArray(bits);
  }
  
  // Like the boolean[] constructor, but returns static instances for TRUE,
  // FALSE, and EMPTY.
  public static BitWord FromBooleanArray(boolean[] bools) {
    if (bools.length == 0) {
      return BitWord.EMPTY;
    } else if (bools.length == 1) {
      if (bools[0]) {
        return BitWord.TRUE;
      } else {
        return BitWord.FALSE;
      }
    } else {
      return new BitWord(bools);
    }
  }
  
  public static BitWord FromBoolean(boolean bool) {
    if (bool) {
      return BitWord.TRUE;
    } else {
      return BitWord.FALSE;
    }
  }
  
  public static BitWord FromInt(int value, int num_bits) {
    boolean[] bits = new boolean[num_bits];
    for (int i = 0; i < bits.length; ++i) {
      bits[i] = value % 2 != 0;
      value = value >> 1;
    }
    return FromBooleanArray(bits);
  }

  public static BitWord Concatenate(BitWord[] bitwords) {
    if (bitwords.length == 0) {
      return BitWord.EMPTY;
    } else if (bitwords.length == 1) {
      return bitwords[0];
    } else {
      BitWord concatenated = bitwords[0];
      for (int i = 1; i < bitwords.length; ++i) {
        concatenated = concatenated.Append(bitwords[i]);
      }
      return concatenated;
    }
  }
  
  
  private boolean[] bits_;
}
