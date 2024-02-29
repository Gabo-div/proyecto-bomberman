package proyecto.sockets;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class ClientHandler {
  DatagramSocket socket;
  InetAddress address;
  int port;

  byte[] buffer = new byte[1000];

  public ClientHandler(DatagramSocket serverSocket, InetAddress address,
                       int port) {
    this.socket = serverSocket;
    this.address = address;
    this.port = port;
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
