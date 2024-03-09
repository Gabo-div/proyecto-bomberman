package proyecto.sockets;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Esta clase maneja la comunicación con un cliente UDP.
 */
public class ClientHandler {
    DatagramSocket socket; // Socket para la comunicación con el cliente.
    InetAddress address; // Dirección IP del cliente.
    int port; // Puerto del cliente.
    byte[] buffer = new byte[2048]; // Búfer para almacenar los datos recibidos.

    /**
     * Constructor que inicializa el cliente con el socket del servidor, la dirección y el puerto del cliente, y el tamaño del paquete.
     *
     * @param serverSocket El socket del servidor.
     * @param address       La dirección IP del cliente.
     * @param port          El puerto del cliente.
     * @param packetSize    El tamaño del paquete.
     */
    public ClientHandler(DatagramSocket serverSocket, InetAddress address,
                         int port, int packetSize) {
        this.socket = serverSocket;
        this.address = address;
        this.port = port;
        this.buffer = new byte[packetSize];
    }

    /**
     * Método para enviar un mensaje al cliente.
     *
     * @param name El nombre del evento.
     * @param data Los datos a enviar al cliente.
     */
    public void emit(String name, byte[] data) {
        try {
            // Se crea un evento de socket con el nombre y los datos especificados.
            SocketEvent event = new SocketEvent(name, data);
            // Se serializa el evento en un arreglo de bytes.
            buffer = SocketSerializer.serialize(event);
            // Se crea un paquete DatagramPacket con los datos serializados, la dirección y el puerto del cliente.
            DatagramPacket packet =
                    new DatagramPacket(buffer, buffer.length, address, port);
            // Se envía el paquete al cliente.
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
