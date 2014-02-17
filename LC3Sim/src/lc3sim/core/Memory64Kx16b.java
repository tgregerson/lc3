package lc3sim.core;

// A memory with 64K entries and 16-bit word size.
public class Memory64Kx16b {
  public Memory64Kx16b() {
    Init();
  }
  
  public void Init() {
    data_ = new short[v64k];
  }
  
  public short Read(short address) {
    return data_[address];
  }
  
  public void Write(short address, short data) {
    data_[address] = data;
  }
  
  private short[] data_;
  private static final int v64k = 65536;
}
