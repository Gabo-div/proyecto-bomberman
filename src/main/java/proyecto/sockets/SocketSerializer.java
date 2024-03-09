package proyecto.sockets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Clase utilitaria para serializar y deserializar objetos.
 */
public class SocketSerializer {

    /**
     * Método para serializar un objeto.
     *
     * @param event El objeto a serializar.
     * @return Un arreglo de bytes que representa el objeto serializado.
     */
    public static byte[] serialize(Serializable event) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(event);
            oos.flush();
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método para deserializar un objeto.
     *
     * @param data El arreglo de bytes que representa el objeto serializado.
     * @return El objeto deserializado.
     */
    public static Serializable deserialize(byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (Serializable) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
