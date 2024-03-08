package proyecto.sockets;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientHandler {
  DatagramSocket socket;
  InetAddress address;
  int port;

  byte[] buffer = new byte[2048];

  public ClientHandler(DatagramSocket serverSocket, InetAddress address,
                       int port, int packetSize) {
    this.socket = serverSocket;
    this.address = address;
    this.port = port;
    this.buffer = new byte[packetSize];
  }

  public void emit(String name, byte[] data) {
    try {
      SocketEvent event = new SocketEvent(name, data);
      buffer = SocketSerializer.serialize(event);
      DatagramPacket packet =
          new DatagramPacket(buffer, buffer.length, address, port);
      socket.send(packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
