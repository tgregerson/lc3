package lc3sim.ui;

import javafx.beans.property.SimpleStringProperty;
import lc3sim.core.BitWord;
import lc3sim.core.instructions.Instruction;

public class UIState {
  public static class HexDataEntry {
    public HexDataEntry(String name, int data) {
      nameString = new SimpleStringProperty(name);
      dataString = new SimpleStringProperty(IntTo4DigitHex(data));
    }
    
    public String getNameString() {
      return nameString.get();
    }
    
    public void setNameString(String address) {
      nameString.set(address);
    }
    
    public Integer getData() {
      return Hex4ToInt(getDataString());
    }
    
    public String getDataString() {
      return dataString.get();
    }

    public void setDataString(String data) {
      if (data.isEmpty()) {
        return;
      }
      // Detect radix and strip leading type indicator
      int radix = 16;
      switch (data.charAt(0)) {
        case '#': radix = 10;  // FALLTHROUGH-INTENDED
        case 'x':              // FALLTHROUGH-INTENDED
        case 'X': data = data.substring(1);
      }
      try {
        int value = Integer.parseInt(data, radix);
        data = IntTo4DigitHex(value);
      } catch (NumberFormatException e) {
        // If input is invalid, just leave the old value.
        return;
      }
      dataString.set(data);
    }

    public void setData(Integer data) {
      setDataString(Integer.toHexString(data));
    }

    protected final SimpleStringProperty nameString;
    protected final SimpleStringProperty dataString;
  }

  // Uses its address, in 4-digit hex format prefixed with 'x', as its name.
  public static class MemoryEntry extends HexDataEntry {
    public MemoryEntry(int address, int data) {
      super(IntTo4DigitHex(address), data);
    }
    
    public int getAddress() {
      return Hex4ToInt(getAddressString());
    }
    
    public String getAddressString() {
      return getNameString();
    }
    
    public String getInstructionString() {
      return DataToInstruction(getData());
    }
  }

  public static class RegisterEntry extends HexDataEntry {
    public RegisterEntry(String name, int data) {
      super(name, data);
    }
  }

  public static String IntTo4DigitHex(int val) {
    String val_str = Integer.toHexString(val);
    if (val_str.length() > 4) {
      val_str = val_str.substring(val_str.length() - 4, val_str.length());
    }
    StringBuilder leading_zeroes = new StringBuilder();
    for (int i = val_str.length(); i < 4; ++i) {
      leading_zeroes.insert(0, '0');
    }
    return "x" + leading_zeroes.toString() + val_str;
  }
  
  // Return null if data string does not match recognized formats.
  public static Integer DataStringToInt(String data) {
    Integer value = null;
    if (!data.isEmpty()) {
      // Detect radix and strip leading type indicator
      int radix = 16;
      switch (data.charAt(0)) {
        case '#': radix = 10;  // FALLTHROUGH-INTENDED
        case 'x':              // FALLTHROUGH-INTENDED
        case 'X': data = data.substring(1);
      }
      try {
        value = Integer.parseInt(data, radix);
      } catch (NumberFormatException e) {
      }
    }
    return value;
  }
  
  public static int Hex4ToInt(String hex4) {
    if (hex4.charAt(0) == 'x' || hex4.charAt(0) == 'X') {
      hex4 = hex4.substring(1);
    }
    return Integer.parseUnsignedInt(hex4, 16);
  }
  
  public static String DataToInstruction(int data) {
    BitWord b =
        BitWord.FromInt(data, lc3sim.core.instructions.Instruction.kNumBits);
    Instruction i = Instruction.FromBitWord(b);
    return i.toString();
  }
}
