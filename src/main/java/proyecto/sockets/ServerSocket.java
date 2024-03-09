package proyecto.sockets;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerSocket {
  private DatagramSocket socket;
  private byte[] buffer = new byte[2048];

  Integer timeoutMs = 5 * 1000;
  boolean shouldClose = false;

  private HashMap<String, ClientHandler> clients = new HashMap<>();
  private HashMap<String, Long> timeouts = new HashMap<>();

  private HashMap<String, ArrayList<SocketEventListener>> listeners =
      new HashMap<>();

  public ServerSocket(int port, int packetSize) throws SocketException {
    this.socket = new DatagramSocket(port);
    this.listeners = new HashMap<>();
    this.buffer = new byte[packetSize];
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
    new Thread(() -> handleTimeoutLoop()).start();
  }

  public void close() {
    emit("disconnected", null);
    clients.clear();
    socket.close();
    shouldClose = true;
  }

  private void receiveEventsLoop() {
    while (true) {
      try {
        if (shouldClose) {
          break;
        }
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        SocketEvent event =
            (SocketEvent)SocketSerializer.deserialize(packet.getData());

        String eventName = event.getName();
        String clientName = getClientName(packet);

        if (eventName.equals("connect") && !clients.containsKey(clientName)) {
          handleConnection(packet);
          continue;
        }

        if (eventName.equals("disconnect") && clients.containsKey(clientName)) {
          handleDisconnection(packet);
          continue;
        }

        if (clients.containsKey(clientName)) {
          ClientHandler handler = clients.get(clientName);
          timeouts.put(clientName, System.currentTimeMillis() + timeoutMs);
          runListeners(event, handler);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void handleTimeoutLoop() {
    while (true) {
      if (shouldClose) {
        break;
      }

      try {

        Thread.sleep(1000);
        for (String clientName : timeouts.keySet()) {
          if (timeouts.get(clientName) < System.currentTimeMillis()) {
            runListeners(new SocketEvent("disconnected", null),
                         clients.get(clientName));
            clients.remove(clientName);
            timeouts.remove(clientName);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void handleConnection(DatagramPacket packet) {
    InetAddress packetAddress = packet.getAddress();
    int packetPort = packet.getPort();
    String clientName = getClientName(packet);

    ClientHandler handler =
        new ClientHandler(socket, packetAddress, packetPort, buffer.length);

    clients.put(clientName, handler);
    timeouts.put(clientName, System.currentTimeMillis() + timeoutMs);

    handler.emit("connected", null);
    runListeners(new SocketEvent("connected", null), handler);
  }

  private void handleDisconnection(DatagramPacket packet) {
    String clientName = getClientName(packet);

    ClientHandler handler = clients.get(clientName);

    clients.remove(clientName);
    timeouts.remove(clientName);

    runListeners(new SocketEvent("disconnected", null), handler);
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

  public int getPort() { return socket.getLocalPort(); }
}
