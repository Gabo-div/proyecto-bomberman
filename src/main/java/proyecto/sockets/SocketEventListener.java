package proyecto.sockets;

/**
 * Interfaz funcional para manejar eventos de socket.
 */
@FunctionalInterface
public interface SocketEventListener {
    /**
     * Método para manejar un evento de socket.
     *
     * @param data    Los datos del evento.
     * @param handler El controlador de cliente asociado al evento.
     */
    void onEvent(byte[] data, ClientHandler handler);
}
