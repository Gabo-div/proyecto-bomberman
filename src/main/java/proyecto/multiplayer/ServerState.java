package proyecto.multiplayer;

/**
 * Enumeración que representa los estados del servidor en el juego multijugador.
 */
public enum ServerState {
  CONNECTED, // Conectado
  CONNECTING, // Conectándose
  INGAME, // En juego
  DISCONNECTED, // Desconectado
  ERROR, // Error
}
