package proyecto.sockets;

import java.io.Serializable;

class SocketEvent implements Serializable {
  private String name;
  private byte[] data;

  public SocketEvent(String name, byte[] data) {
    this.name = name;
    this.data = data;
  }

  public String getName() { return name; }

  public byte[] getData() { return data; }
}
