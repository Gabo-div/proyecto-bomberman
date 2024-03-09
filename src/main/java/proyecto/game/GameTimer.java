package proyecto.game;

import javafx.animation.AnimationTimer;

/**
 * Clase abstracta que define un temporizador de juego.
 */
public abstract class GameTimer extends AnimationTimer {
    // Variables para el seguimiento del tiempo
    long lastNano = 0;
    double max = -1;
    double min = -1;
    double media = 0;

    /**
     * Método que se llama cuando se inicia el temporizador.
     */
    @Override
    public void start() {
        super.start();
        // Reiniciar las variables de seguimiento del tiempo
        lastNano = 0;
        max = -1;
        min = -1;
        media = 0;
    }

    /**
     * Método que maneja el tiempo del juego.
     * 
     * @param nowNano El tiempo actual en nanosegundos.
     */
    @Override
    public void handle(long nowNano) {
        // Calcular el tiempo transcurrido desde el último fotograma
        if (lastNano == 0) {
            lastNano = nowNano;
            return;
        }
        double deltaMs = (nowNano - lastNano) / 1000000; // Convertir nanosegundos a milisegundos
        lastNano = nowNano;

        // Actualizar los valores máximo, mínimo y medio del delta
        if (deltaMs > max || max == -1)
            max = deltaMs;
        if (deltaMs < min || min == -1)
            min = deltaMs;
        media = (max + min) / 2;

        // Llamar al método abstracto para realizar las operaciones de juego
        tick(deltaMs);
    }

    /**
     * Método abstracto que realiza las operaciones de juego.
     * 
     * @param deltaMs El tiempo transcurrido desde el último fotograma en milisegundos.
     */
    public abstract void tick(double deltaMs);
}
