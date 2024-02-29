package proyecto.sockets;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@FunctionalInterface
interface SocketEventListener {
  void onEvent(byte[] data, ClientHandler handler);
}

class Server {
  DatagramSocket socket;
  byte[] buffer = new byte[1000];

  HashMap<String, ClientHandler> clients = new HashMap<>();
  HashMap<String, ArrayList<SocketEventListener>> listeners = new HashMap<>();

  public Server(int port) throws SocketException {
    this.socket = new DatagramSocket(port);
    this.listeners = new HashMap<>();
  }

  public List<ClientHandler> getClients() {
    return new ArrayList<>(clients.values());
  }

  public void addListener(String name, SocketEventListener listener) {
    if (listeners.containsKey(name)) {
      listeners.get(name).add(listener);
    } else {
      ArrayList<SocketEventListener> l = new ArrayList<>();
      l.add(listener);
      listeners.put(name, l);
    }
  }

  public void emit(String name, byte[] data) {
    for (ClientHandler client : clients.values()) {
      client.emit(name, data);
    }
  }

  public void run() {
    System.out.println("Server running in port " + socket.getLocalPort());
    new Thread(() -> receiveEventsLoop()).start();
  }

  private void receiveEventsLoop() {
    while (true) {
      try {
        DatagramPacket packet = new DatagramPacket(buffer, 1000);
        socket.receive(packet);

        InetAddress packetAddress = packet.getAddress();
        int packetPort = packet.getPort();

        SocketEvent event =
            (SocketEvent)SocketSerializer.deserialize(packet.getData());

        String eventName = event.getName();
        String clientName = getClientName(packet);

        if (eventName.equals("connect") && !clients.containsKey(clientName)) {
          ClientHandler handler =
              new ClientHandler(socket, packetAddress, packetPort);
          clients.put(clientName, handler);
          handler.emit("connected", null);
          runListeners(new SocketEvent("connected", null), handler);
          continue;
        }

        if (clients.containsKey(clientName)) {
          ClientHandler handler = clients.get(clientName);
          runListeners(event, handler);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private String getClientName(DatagramPacket packet) {
    return packet.getAddress() + ":" + packet.getPort();
  }

  private void runListeners(SocketEvent event, ClientHandler handler) {
    if (listeners.containsKey(event.getName())) {
      for (SocketEventListener listener : listeners.get(event.getName())) {
        listener.onEvent(event.getData(), handler);
      }
    }
  }
}
