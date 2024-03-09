package proyecto.sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Esta clase implementa la funcionalidad de un cliente UDP para la comunicación con un servidor.
 */
public class ClientSocket {
    DatagramSocket socket; // Socket para la comunicación UDP.
    InetAddress address; // Dirección IP del servidor.
    int port; // Puerto del servidor.
    boolean isConnected = false; // Indica si el cliente está conectado.
    boolean shouldClose = false; // Indica si el cliente debe cerrarse.
    byte[] inBuffer = new byte[2048]; // Búfer de entrada para almacenar los datos recibidos.
    byte[] outBuffer = new byte[2048]; // Búfer de salida para almacenar los datos a enviar.
    Integer timeoutMs = 5 * 1000; // Tiempo de espera para la conexión en milisegundos.
    Long connectionTimeout; // Tiempo de espera de la conexión.
    HashMap<String, ArrayList<ClientEventListener>> listeners = new HashMap<>(); // Mapa para almacenar los listeners por nombre del evento.

    /**
     * Constructor que inicializa el cliente con la dirección y el puerto del servidor, y el tamaño del paquete.
     *
     * @param address    La dirección IP del servidor.
     * @param port       El puerto del servidor.
     * @param packetSize El tamaño del paquete.
     * @throws IOException Si ocurre un error de E/S.
     */
    public ClientSocket(InetAddress address, int port, int packetSize)
            throws IOException {
        this.socket = new DatagramSocket(); // Se crea un nuevo socket UDP.
        this.address = address; // Se asigna la dirección IP del servidor.
        this.port = port; // Se asigna el puerto del servidor.
        this.inBuffer = new byte[packetSize]; // Se inicializa el búfer de entrada con el tamaño especificado.
        this.outBuffer = new byte[packetSize]; // Se inicializa el búfer de salida con el tamaño especificado.
    }

    /**
     * Método para establecer la conexión con el servidor.
     */
    public void connect() {
        // Se inicia un hilo para el bucle de recepción de eventos.
        new Thread(this::receiveEventsLoop).start();
        // Se inicia un hilo para el bucle de conexión.
        new Thread(this::connectionLoop).start();
        // Se establece el tiempo de espera de la conexión.
        connectionTimeout = System.currentTimeMillis() + timeoutMs;
        // Se emite el evento de conexión.
        emit("connect", null);
    }

    /**
     * Método para desconectar el cliente del servidor.
     */
    public void disconnect() {
        // Se emite el evento de desconexión.
        emit("disconnect", null);
        // Se indica que el cliente debe cerrarse.
        shouldClose = true;
        // Se cierra el socket.
        socket.close();
    }

    /**
     * Bucle de recepción de eventos.
     */
    private void receiveEventsLoop() {
        while (true) {
            if (shouldClose) {
                break;
            }
            try {
                // Se crea un paquete DatagramPacket para recibir datos.
                DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
                // Se recibe un paquete del servidor.
                socket.receive(packet);
                // Se deserializa el evento recibido.
                SocketEvent event =
                        (SocketEvent) SocketSerializer.deserialize(packet.getData());
                // Se obtiene el nombre del evento.
                String eventName = event.getName();
                // Si el evento es de conexión y el cliente no estaba conectado, se actualiza el estado de conexión.
                if (eventName.equals("connected") && !isConnected) {
                    isConnected = true;
                    connectionTimeout = null;
                }
                // Se ejecutan los listeners asociados al evento recibido.
                runListeners(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Bucle de conexión.
     */
    private void connectionLoop() {
        while (true) {
            if (shouldClose) {
                break;
            }
            try {
                Thread.sleep(1000); // Se espera un segundo.
                // Si hay un tiempo de espera de conexión y este ha expirado, se cierra el cliente y se emite un evento de error de conexión.
                if (connectionTimeout != null &&
                        System.currentTimeMillis() > connectionTimeout) {
                    shouldClose = true;
                    socket.close();
                    runListeners(new SocketEvent("connectionError", null));
                    break;
                }
                // Se emite un ping para mantener la conexión activa.
                emit("ping", null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para agregar un listener para un evento específico.
     *
     * @param name     El nombre del evento.
     * @param listener El listener a agregar.
     */
    public void addListener(String name, ClientEventListener listener) {
        if (listeners.containsKey(name)) {
            listeners.get(name).add(listener);
        } else {
            ArrayList<ClientEventListener> l = new ArrayList<>();
            l.add(listener);
            listeners.put(name, l);
        }
    }

    /**
     * Método para ejecutar los listeners asociados a un evento.
     *
     * @param event El evento a procesar.
     */
    private void runListeners(SocketEvent event) {
        if (listeners.containsKey(event.getName())) {
            for (ClientEventListener listener : listeners.get(event.getName())) {
                listener.onEvent(event.getData());
            }
        }
    }

    /**
     * Método para emitir un evento al servidor.
     *
     * @param name El nombre del evento.
     * @param data Los datos a enviar al servidor.
     */
    public void emit(String name, byte[] data) {
        // Se crea un evento de socket con el nombre y los datos especificados.
        SocketEvent event = new SocketEvent(name, data);
        try {
            // Se serializa el evento en un arreglo de bytes.
            outBuffer = SocketSerializer.serialize(event);
            // Se crea un paquete DatagramPacket con los datos serializados, la dirección y el puerto del servidor.
            DatagramPacket packet =
                    new DatagramPacket(outBuffer, outBuffer.length, address, port);
            // Se envía el paquete al servidor.
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para obtener el puerto del servidor.
     *
     * @return El puerto del servidor.
     */
    public int getPort() {
        return port;
    }
}
