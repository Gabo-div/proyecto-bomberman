package proyecto.sockets;

import java.io.Serializable;

/**
 * Representa un evento de socket serializable.
 */
class SocketEvent implements Serializable {
    private String name; // Nombre del evento
    private byte[] data; // Datos del evento

    /**
     * Constructor.
     *
     * @param name El nombre del evento.
     * @param data Los datos del evento.
     */
    public SocketEvent(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }

    /**
     * Devuelve el nombre del evento.
     *
     * @return El nombre del evento.
     */
    public String getName() {
        return name;
    }

    /**
     * Devuelve los datos del evento.
     *
     * @return Los datos del evento.
     */
    public byte[] getData() {
        return data;
    }
}
