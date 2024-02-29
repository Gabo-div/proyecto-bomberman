package proyecto.sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

@FunctionalInterface
interface ClientEventListener {
  void onEvent(byte[] data);
}

class Client {
  DatagramSocket socket;
  InetAddress address;
  int port;

  byte[] inBuffer = new byte[1000];
  byte[] outBuffer = new byte[1000];

  HashMap<String, ArrayList<ClientEventListener>> listeners = new HashMap<>();

  public Client(InetAddress address, int port) throws IOException {
    this.socket = new DatagramSocket();
    this.address = address;
    this.port = port;
  }

  public void connect() {
    new Thread(() -> receiveEventsLoop()).start();
    emit("connect", null);
  }

  private void receiveEventsLoop() {
    while (true) {
      try {
        DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
        socket.receive(packet);

        SocketEvent event =
            (SocketEvent)SocketSerializer.deserialize(packet.getData());

        System.out.println(event.getName());

        runListeners(event);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void runListeners(SocketEvent event) {
    if (listeners.containsKey(event.getName())) {
      for (ClientEventListener listener : listeners.get(event.getName())) {
        listener.onEvent(event.getData());
      }
    }
  }

  public void emit(String name, byte[] data) {
    SocketEvent event = new SocketEvent(name, data);
    try {
      outBuffer = SocketSerializer.serialize(event);
      DatagramPacket packet =
          new DatagramPacket(outBuffer, outBuffer.length, address, port);
      socket.send(packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
