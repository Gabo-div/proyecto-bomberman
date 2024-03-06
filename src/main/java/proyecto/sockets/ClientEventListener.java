package proyecto.sockets;

@FunctionalInterface
public interface ClientEventListener {
  void onEvent(byte[] data);
}
