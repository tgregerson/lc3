package lc3sim.ui;

import javafx.beans.property.SimpleStringProperty;
import lc3sim.core.BitWord;
import lc3sim.core.instructions.Instruction;

public class UIState {
  public static class MemoryEntry {
    public MemoryEntry(int address, int data) {
      addressString = new SimpleStringProperty(IntTo4DigitHex(address));
      dataString = new SimpleStringProperty(IntTo4DigitHex(data));
      instructionString = new SimpleStringProperty(DataToInstruction(data));
    }
    
    public String getAddressString() {
      return addressString.get();
    }
    
    public void setAddressString(String address) {
      addressString.set(address);
    }
    
    public void setAddress(int address) {
      addressString.set(IntTo4DigitHex(address));
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
        if (value > 0x00FF) {
          // Treat as instruction
          instructionString.set(DataToInstruction(value));
        } else {
          // Treat as char.
          char c = (char)value;
          String printchar;
          switch (c) {
            case '\0':
              printchar = "\\0";
              break;
            case '\n':
              printchar = "\\n";
              break;
            default:
              printchar = "" + c;
          }
          instructionString.set(printchar);
        }
      } catch (NumberFormatException e) {
        // If input is invalid, just leave the old value.
        return;
      }
      // TODO: Call memory listener to update core state.
      dataString.set(data);
    }

    public String getInstructionString() {
      return instructionString.get();
    }
    
    public void setData(int data) {
      setDataString(Integer.toHexString(data));
    }

    private final SimpleStringProperty addressString;
    private final SimpleStringProperty dataString;
    private final SimpleStringProperty instructionString;
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
