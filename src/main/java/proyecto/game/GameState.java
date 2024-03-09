package proyecto.game;

/**
 * Enumeración que representa los posibles estados del juego.
 */
public enum GameState {
    /** 
     * El juego está en curso. 
     * */
    RUNNING,
    /** 
     * El juego está pausado. 
     * */
    PAUSED,
    /** 
     * El juego ha terminado con derrota. 
     * */
    GAMEOVER,
    /** 
     * El juego ha terminado con victoria. 
     * */
    WIN,
    /** 
     * No hay un estado definido. 
     * */
    NONE,
    /** 
     * El juego ha terminado completamente. 
     * */
    END
}
