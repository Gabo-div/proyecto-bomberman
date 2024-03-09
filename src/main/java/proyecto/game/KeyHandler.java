package proyecto.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Manejador de teclas para el juego.
 */
public class KeyHandler {
    // Escena actual
    private static Scene scene;
    // Conjunto de teclas actualmente presionadas
    private static final Set<KeyCode> keysCurrentlyDown = new HashSet<>();
    // Mapa de teclas y sus manejadores
    private static final Map<KeyCode, ArrayList<Runnable>> keyHandlers = new HashMap<>();

    // Instancia única del manejador de teclas
    private static KeyHandler instance;

    // Constructor privado para garantizar que solo haya una instancia
    private KeyHandler() {}

    /**
     * Devuelve la instancia única del manejador de teclas.
     * @return La instancia del manejador de teclas.
     */
    public static KeyHandler getInstance() {
        if (instance == null) {
            instance = new KeyHandler();
        }
        return instance;
    }

    /**
     * Limpia el estado de las teclas y los manejadores de teclas actuales, y establece la escena.
     * @param scene La escena en la que se manejarán las teclas.
     */
    public void pollScene(Scene scene) {
        clearKeys();
        clearHandlers();
        removeCurrentKeyHandlers();
        setScene(scene);
    }

/**
 * Limpia las teclas actualmente presionadas.
 */
private void clearKeys() {
    keysCurrentlyDown.clear();
}

/**
 * Limpia los manejadores de teclas.
 */
private void clearHandlers() {
    keyHandlers.clear();
}

/**
 * Elimina los manejadores de teclas actuales de la escena.
 */
private void removeCurrentKeyHandlers() {
    if (scene != null) {
        scene.setOnKeyPressed(null);
        scene.setOnKeyReleased(null);
    }
}

/**
 * Establece la escena y los manejadores de teclas en ella.
 * 
 * @param scene La escena en la que establecer los manejadores de teclas.
 */
private void setScene(Scene scene) {
    KeyHandler.scene = scene;
    KeyHandler.scene.setOnKeyPressed((keyEvent) -> {
        keysCurrentlyDown.add(keyEvent.getCode());
        ArrayList<Runnable> handlers = keyHandlers.get(keyEvent.getCode());
        if (handlers == null) {
            return;
        }
        for (Runnable handler : handlers) {
            handler.run();
        }
    });
    KeyHandler.scene.setOnKeyReleased((keyEvent) -> {
        keysCurrentlyDown.remove(keyEvent.getCode());
    });
}

    /**
     * Comprueba si una tecla está actualmente presionada.
     * @param keyCode El código de la tecla.
     * @return true si la tecla está presionada, false de lo contrario.
     */
    public boolean isDown(KeyCode keyCode) {
        return keysCurrentlyDown.contains(keyCode);
    }

    /**
     * Asigna un manejador de teclas a una tecla específica.
     * @param keyCode El código de la tecla.
     * @param runnable El manejador de teclas que se ejecutará cuando se presione la tecla.
     */
    public void onPressed(KeyCode keyCode, Runnable runnable) {
        keyHandlers.putIfAbsent(keyCode, new ArrayList<>());
        keyHandlers.get(keyCode).add(runnable);
    }
}
