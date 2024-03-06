package proyecto.sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientSocket {
  DatagramSocket socket;
  InetAddress address;
  int port;

  boolean isConnected = false;
  boolean shouldClose = false;

  byte[] inBuffer = new byte[1000];
  byte[] outBuffer = new byte[1000];

  Integer timeoutMs = 5 * 1000;
  Long connectionTimeout;

  HashMap<String, ArrayList<ClientEventListener>> listeners = new HashMap<>();

  public ClientSocket(InetAddress address, int port) throws IOException {
    this.socket = new DatagramSocket();
    this.address = address;
    this.port = port;
  }

  public void connect() {
    new Thread(() -> receiveEventsLoop()).start();
    new Thread(() -> connectionLoop()).start();

    connectionTimeout = System.currentTimeMillis() + timeoutMs;
    emit("connect", null);
  }

  public void disconnect() {
    emit("disconnect", null);
    shouldClose = true;
    socket.close();
  }

  private void receiveEventsLoop() {
    while (true) {
      if (shouldClose) {
        break;
      }

      try {
        DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
        socket.receive(packet);

        SocketEvent event =
            (SocketEvent)SocketSerializer.deserialize(packet.getData());

        String eventName = event.getName();

        if (eventName.equals("connected") && !isConnected) {
          isConnected = true;
          connectionTimeout = null;
        }

        runListeners(event);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void connectionLoop() {
    while (true) {
      if (shouldClose) {
        break;
      }

      try {
        Thread.sleep(1000);

        if (connectionTimeout != null &&
            System.currentTimeMillis() > connectionTimeout) {
          shouldClose = true;
          socket.close();
          runListeners(new SocketEvent("connectionError", null));
          break;
        }

        emit("ping", null);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void addListener(String name, ClientEventListener listener) {
    if (listeners.containsKey(name)) {
      listeners.get(name).add(listener);
    } else {
      ArrayList<ClientEventListener> l = new ArrayList<>();
      l.add(listener);
      listeners.put(name, l);
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
