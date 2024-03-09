package proyecto.sockets;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Esta clase representa el servidor de sockets UDP.
 */
public class ServerSocket {
    private DatagramSocket socket; // Socket del servidor.
    private byte[] buffer = new byte[2048]; // Búfer para almacenar los datos recibidos.

    Integer timeoutMs = 5 * 1000; // Tiempo de espera para la desconexión del cliente.
    boolean shouldClose = false; // Indica si el servidor debe cerrarse.

    private HashMap<String, ClientHandler> clients = new HashMap<>(); // Clientes conectados al servidor.
    private HashMap<String, Long> timeouts = new HashMap<>(); // Tiempo de espera para la desconexión de cada cliente.

    private HashMap<String, ArrayList<SocketEventListener>> listeners = new HashMap<>(); // Listeners para eventos del servidor.

    /**
     * Constructor que inicializa el servidor con el puerto y el tamaño del paquete.
     *
     * @param port       El puerto del servidor.
     * @param packetSize El tamaño del paquete.
     * @throws SocketException Si ocurre un error al crear el socket.
     */
    public ServerSocket(int port, int packetSize) throws SocketException {
        this.socket = new DatagramSocket(port); // Se crea el socket del servidor.
        this.listeners = new HashMap<>();
        this.buffer = new byte[packetSize]; // Se inicializa el búfer con el tamaño especificado.
    }

    /**
     * Método para obtener la lista de clientes conectados.
     *
     * @return La lista de clientes conectados.
     */
    public List<ClientHandler> getClients() {
        return new ArrayList<>(clients.values());
    }

    /**
     * Método para agregar un listener para un evento específico.
     *
     * @param name     El nombre del evento.
     * @param listener El listener a agregar.
     */
    public void addListener(String name, SocketEventListener listener) {
        if (listeners.containsKey(name)) {
            listeners.get(name).add(listener);
        } else {
            ArrayList<SocketEventListener> l = new ArrayList<>();
            l.add(listener);
            listeners.put(name, l);
        }
    }

    /**
     * Método para emitir un evento a todos los clientes.
     *
     * @param name El nombre del evento.
     * @param data Los datos asociados al evento.
     */
    public void emit(String name, byte[] data) {
        for (ClientHandler client : clients.values()) {
            client.emit(name, data);
        }
    }

    /**
     * Método para ejecutar el servidor.
     */
    public void run() {
        System.out.println("Server running in port " + socket.getLocalPort());
        new Thread(this::receiveEventsLoop).start();
        new Thread(this::handleTimeoutLoop).start();
    }

    /**
     * Método para cerrar el servidor.
     */
    public void close() {
        emit("disconnected", null);
        clients.clear();
        socket.close();
        shouldClose = true;
    }

    /**
     * Método para recibir eventos del cliente.
     */
    private void receiveEventsLoop() {
        while (true) {
            try {
                if (shouldClose) {
                    break;
                }
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                SocketEvent event =
                        (SocketEvent) SocketSerializer.deserialize(packet.getData());

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

    /**
     * Método para manejar el tiempo de espera para la desconexión de los clientes.
     */
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

    /**
     * Método para manejar la conexión de un cliente.
     *
     * @param packet El paquete recibido.
     */
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

    /**
     * Método para manejar la desconexión de un cliente.
     *
     * @param packet El paquete recibido.
     */
    private void handleDisconnection(DatagramPacket packet) {
        String clientName = getClientName(packet);

        ClientHandler handler = clients.get(clientName);

        clients.remove(clientName);
        timeouts.remove(clientName);

        runListeners(new SocketEvent("disconnected", null), handler);
    }

    /**
     * Método para obtener el nombre del cliente a partir del paquete recibido.
     *
     * @param packet El paquete recibido.
     * @return El nombre del cliente.
     */
    private String getClientName(DatagramPacket packet) {
        return packet.getAddress() + ":" + packet.getPort();
    }

    /**
     * Método para ejecutar los listeners para un evento específico.
     *
     * @param event   El evento a procesar.
     * @param handler El manejador del cliente asociado al evento.
     */
    private void runListeners(SocketEvent event, ClientHandler handler) {
        if (listeners.containsKey(event.getName())) {
            for (SocketEventListener listener : listeners.get(event.getName())) {
                listener.onEvent(event.getData(), handler);
            }
        }
    }

    /**
     * Método para obtener el puerto del servidor.
     *
     * @return El puerto del servidor.
     */
    public int getPort() {
        return socket.getLocalPort();
    }
}
