package proyecto.sockets;

/**
 * Esta interfaz define un evento que será invocado cuando se reciba un mensaje del cliente.
 */
@FunctionalInterface
public interface ClientEventListener {

    /**
     * Método que maneja el evento y recibe un arreglo de bytes como datos.
     *
     * @param data Los datos recibidos del cliente como un arreglo de bytes.
     */
    void onEvent(byte[] data);
}
