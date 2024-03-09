package proyecto.multiplayer;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import proyecto.game.GameState;
import proyecto.game.MultiplayerGame;
import proyecto.model.Bomb;
import proyecto.model.Level;
import proyecto.model.Player;
import proyecto.multiplayer.events.MovementData;
import proyecto.multiplayer.events.NewMessageData;
import proyecto.multiplayer.events.NewUserData;
import proyecto.multiplayer.events.UsersData;
import proyecto.sockets.ClientEventListener;
import proyecto.sockets.ClientSocket;
import proyecto.sockets.SocketSerializer;

/**
 * Clase que representa un cliente de juego para el modo multijugador.
 */
public class GameClient {
  private static GameClient instance;

/**
 * Estado del servidor al que está conectado el cliente.
 */
private ServerState serverState = ServerState.DISCONNECTED;

/**
 * Socket del cliente.
 */
private ClientSocket clientSocket;

/**
 * Usuario del cliente.
 */
private User clientUser;

/**
 * Lista de usuarios en el servidor.
 */
private ArrayList<User> users = new ArrayList<>();

/**
 * Consumidor de eventos para cambios de estado del servidor.
 */
private Consumer<ServerState> onStateChange;

/**
 * Consumidor de eventos para cambios en la lista de usuarios.
 */
private Consumer<List<User>> onUsersChange;

/**
 * Consumidor de eventos para mensajes recibidos.
 */
private Consumer<Message> onMessage;

/**
 * Instancia del juego multijugador.
 */
private MultiplayerGame game = MultiplayerGame.getInstance();


  /**
   * Constructor privado para seguir el patrón Singleton.
   */
  private GameClient() {}

  /**
   * Método estático para obtener la instancia única del cliente de juego.
   * @return La instancia única del cliente de juego.
   */
  public static GameClient getInstance() {
    if (instance == null) {
      instance = new GameClient();
    }
    return instance;
  }

  /**
   * Método para iniciar el cliente de juego.
   * @param port El puerto al que se conectará el cliente.
   */
  public void start(int port) {
    try {
      InetAddress address = InetAddress.getByName("localhost");

      changeState(ServerState.CONNECTING);
      clientSocket = new ClientSocket(address, port, 9000);

      // Configuración de los listeners de eventos del socket del cliente
      clientSocket.addListener("users", usersHandler);
      clientSocket.addListener("chatMessage", chatMessageHandler);

      // Listeners para eventos de conexión y desconexión
      clientSocket.addListener(
          "connected", (data) -> { changeState(ServerState.CONNECTED); });

      clientSocket.addListener("connectionError",
                               (data) -> { changeState(ServerState.ERROR); });

      clientSocket.addListener("roomFull",
                               (data) -> { changeState(ServerState.ERROR); });

      clientSocket.addListener("nameTaken",
                               (data) -> { changeState(ServerState.ERROR); });

      clientSocket.addListener(
          "disconnected", (data) -> { changeState(ServerState.DISCONNECTED); });

      // Listeners para eventos de inicio y fin de juego
      clientSocket.addListener("startGame",
                               (data) -> { changeState(ServerState.INGAME); });

      clientSocket.addListener(
          "endGame", (data) -> { changeState(ServerState.CONNECTED); });

      // Listeners para sincronización de estado del juego
      clientSocket.addListener("syncState", (data) -> {
        GameState state = (GameState)SocketSerializer.deserialize(data);
        game.setGameState(state);
      });

      // Sincronización de jugadores, bombas y nivel del juego
      clientSocket.addListener("syncPlayers", (data) -> {
        ArrayList<Player> players =
            (ArrayList<Player>)SocketSerializer.deserialize(data);

        game.syncPlayers(players);
      });

      clientSocket.addListener("syncBombs", (data) -> {
        ArrayList<Bomb> bombs =
            (ArrayList<Bomb>)SocketSerializer.deserialize(data);

        game.syncBombs(bombs);
      });

      clientSocket.addListener("syncLevel", (data) -> {
        Level level = (Level)SocketSerializer.deserialize(data);
        game.syncLevel(level);
      });

      // Conexión al servidor
      clientSocket.connect();
    } catch (Exception e) {
      changeState(ServerState.ERROR);
      e.printStackTrace();
    }
  }

  /**
   * Método para detener el cliente de juego.
   */
  public void stop() {
    if (clientSocket != null) {
      clientSocket.disconnect();
    }
  }

  /**
   * Método para cambiar el estado del servidor.
   * @param state El nuevo estado del servidor.
   */
  private void changeState(ServerState state) {
    serverState = state;
    if (onStateChange != null) {
      onStateChange.accept(serverState);
    }
  }

/**
 * Manejador de eventos de usuarios.
 */
ClientEventListener usersHandler = (data) -> {
  UsersData usersData = (UsersData)SocketSerializer.deserialize(data);

  users.clear();
  for (NewUserData userData : usersData.users) {
      User newUser = new User(userData.name, userData.color);
      users.add(newUser);
  }

  if (onUsersChange != null) {
      onUsersChange.accept(users);
  }
};

/**
* Manejador de eventos de mensajes de chat.
*/
ClientEventListener chatMessageHandler = (data) -> {
  NewMessageData messageData =
          (NewMessageData)SocketSerializer.deserialize(data);

  for (User user : users) {
      if (user.getName().equals(messageData.name)) {
          if (onMessage != null) {
              onMessage.accept(new Message(messageData.message, user));
          }
          break;
      }
  }
};


  /**
   * Método para unirse al lobby del servidor con un nombre de usuario.
   * @param name El nombre del usuario.
   */
  public void joinLobby(String name) {
    clientUser = new User(name, CharacterColor.WHITE);

    NewUserData userData = new NewUserData();
    userData.name = name;
    userData.color = clientUser.getColor();
    clientSocket.emit("newUser", SocketSerializer.serialize(userData));
  }

  /**
   * Método para enviar un mensaje al chat del servidor.
   * @param message El mensaje a enviar.
   */
  public void sendMessage(String message) {
    NewMessageData data = new NewMessageData();

    data.message = message;
    data.name = clientUser.getName();

    clientSocket.emit("chatMessage", SocketSerializer.serialize(data));
  }

  /**
   * Método para cambiar el color del usuario en el servidor.
   * @param color El nuevo color del usuario.
   */
  public void changeColor(CharacterColor color) {
    String colorName = color.name();
    clientSocket.emit("changeColor", SocketSerializer.serialize(colorName));
  }

  /**
   * Método para enviar información de movimiento al servidor.
   * @param stateX Estado del movimiento en el eje X.
   * @param stateY Estado del movimiento en el eje Y.
   */
  public void sendMovement(int stateX, int stateY) {
    MovementData movementData = new MovementData();
    movementData.stateX = stateX;
    movementData.stateY = stateY;
    clientSocket.emit("movement", SocketSerializer.serialize(movementData));
  }

  /**
   * Método para enviar una solicitud de colocar una bomba al servidor.
   */
  public void sendBomb() { clientSocket.emit("bomb", null); }

  /**
   * Método para configurar el consumidor de eventos de cambio de estado del servidor.
   * @param onStateChange El consumidor de eventos de cambio de estado del servidor.
   */
  public void setOnStateChange(Consumer<ServerState> onStateChange) {
    this.onStateChange = onStateChange;
  }

  /**
   * Método para configurar el consumidor de eventos de cambio de usuarios en el servidor.
   * @param onUsersChange El consumidor de eventos de cambio de usuarios en el servidor.
   */
  public void setOnUsersChange(Consumer<List<User>> onUsersChange) {
    this.onUsersChange = onUsersChange;
  }

  /**
   * Método para configurar el consumidor de eventos de mensajes de chat.
   * @param onMessage El consumidor de eventos de mensajes de chat.
   */
  public void setOnMessage(Consumer<Message> onMessage) {
    this.onMessage = onMessage;
  }

  /**
   * Método para obtener la lista de usuarios en el servidor.
   * @return La lista de usuarios en el servidor.
   */
  public ArrayList<User> getUsers() { return users; }

  /**
   * Método para obtener el estado del servidor.
   * @return El estado del servidor.
   */
  public ServerState getState() { return serverState; }

  /**
   * Método para obtener el usuario cliente del cliente de juego.
   * @return El usuario cliente del cliente de juego.
   */
  public User getClientUser() { return clientUser; }

  /**
   * Método para obtener el puerto al que está conectado el cliente.
   * @return El puerto al que está conectado el cliente.
   */
  public int getPort() { return clientSocket.getPort(); }
}
