package lc3sim.ui;

import javafx.beans.property.SimpleStringProperty;

public class UIState {
  public static class MemoryEntry {
    public MemoryEntry(int address, int data) {
      dataString = new SimpleStringProperty(IntTo4DigitHex(data));
      addressString = new SimpleStringProperty(IntTo4DigitHex(address));
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
      } catch (NumberFormatException e) {
        // If input is invalid, just leave the old value.
        return;
      }
      // TODO: Call memory listener to update core state.
      dataString.set(data);
    }

    public void setData(int data) {
      setDataString(Integer.toHexString(data));
    }

    private final SimpleStringProperty addressString;
    private final SimpleStringProperty dataString;
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
}
