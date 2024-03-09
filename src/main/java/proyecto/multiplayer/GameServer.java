package proyecto.multiplayer;

import java.util.ArrayList;
import java.util.HashMap;
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
import proyecto.sockets.ClientHandler;
import proyecto.sockets.ServerSocket;
import proyecto.sockets.SocketEventListener;
import proyecto.sockets.SocketSerializer;

/**
 * Clase que representa el servidor del juego para el modo multijugador.
 */
public class GameServer {
  private static GameServer instance;

  private ServerState serverState = ServerState.DISCONNECTED;
  private ServerSocket socketServer;

  private Integer roomSize;
  private User hostUser;
  private HashMap<ClientHandler, User> users = new HashMap<>();

  private MultiplayerGame game = MultiplayerGame.getInstance();

  private Consumer<ServerState> onStateChange;
  private Consumer<List<User>> onUsersChange;
  private Consumer<Message> onMessage;

  /**
   * Constructor privado para seguir el patrón Singleton.
   */
  private GameServer() {}

  /**
   * Método estático para obtener la instancia única del servidor de juego.
   * @return La instancia única del servidor de juego.
   */
  public static GameServer getInstance() {
    if (instance == null) {
      instance = new GameServer();
    }
    return instance;
  }

  /**
   * Método para iniciar el servidor de juego.
   * @param roomSize Tamaño máximo de la sala de juego.
   */
  public void start(int roomSize) {
    try {
      this.roomSize = roomSize;
      socketServer = new ServerSocket(0, 9000);

      // Configuración de los listeners de eventos del socket del servidor
      socketServer.addListener("newUser", newUserHandler);
      socketServer.addListener("chatMessage", chatMessageHandler);
      socketServer.addListener("changeColor", colorHandler);
      socketServer.addListener("movement", movementHandler);
      socketServer.addListener("bomb", bombHandler);

      // Listener para manejar desconexiones de clientes
      socketServer.addListener("disconnected", (data, handler) -> {
        if (users.containsKey(handler)) {
          users.remove(handler);
          sendUsersData();

          if (onUsersChange != null) {
            onUsersChange.accept(new ArrayList<>(users.values()));
          }
        }
      });

      // Iniciar el servidor
      socketServer.run();
      changeState(ServerState.CONNECTED);

    } catch (Exception e) {
      changeState(ServerState.ERROR);
      e.printStackTrace();
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
   * Método para detener el servidor de juego.
   */
  public void stop() {
    if (socketServer != null) {
      changeState(ServerState.DISCONNECTED);
      users.clear();
      socketServer.close();
    }
  }

  // Manejador de eventos para nuevos usuarios
  private SocketEventListener newUserHandler = (data, handler) -> {
    NewUserData userData = (NewUserData)SocketSerializer.deserialize(data);

    if (users.size() >= roomSize) {
      handler.emit("roomFull", null);
      return;
    }

    for (User user : users.values()) {
      if (user.getName().equals(userData.name)) {
        handler.emit("nameTaken", null);
        return;
      }
    }

    if (users.containsKey(handler)) {
      return;
    }

    User newUser = new User(userData.name, userData.color);
    users.put(handler, newUser);

    sendUsersData();

    if (onUsersChange != null) {
      onUsersChange.accept(new ArrayList<>(users.values()));
    }
  };

  // Manejador de eventos para mensajes de chat
  private SocketEventListener chatMessageHandler = (data, handler) -> {
    NewMessageData messageData =
        (NewMessageData)SocketSerializer.deserialize(data);

    for (User user : users.values()) {
      if (user.getName().equals(messageData.name)) {
        socketServer.emit("chatMessage", data);
        if (onMessage != null) {
          onMessage.accept(new Message(messageData.message, user));
        }
        break;
      }
    }
  };

  // Manejador de eventos para cambios de color de usuario
  private SocketEventListener colorHandler = (data, handler) -> {
    String colorName = (String)SocketSerializer.deserialize(data);
    CharacterColor color = CharacterColor.valueOf(colorName);
    users.get(handler).setColor(color);

    sendUsersData();
    if (onUsersChange != null) {
      onUsersChange.accept(new ArrayList<>(users.values()));
    }
  };

  // Manejador de eventos para movimientos de jugadores
  private SocketEventListener movementHandler = (data, handler) -> {
    MovementData movementData =
        (MovementData)SocketSerializer.deserialize(data);

    if (game.getGameState() != GameState.RUNNING) {
      return;
    }

    User user = users.get(handler);
    Player player = game.getPlayer(user.getName());
    player.setMovementStateX(movementData.stateX);
    player.setMovementStateY(movementData.stateY);
  };

  // Manejador de eventos para colocación de bombas
  private SocketEventListener bombHandler = (data, handler) -> {
    User user = users.get(handler);
    game.addBomb(user.getName());
  };

  /**
   * Método para obtener el estado del servidor.
   * @return El estado del servidor.
   */
  public ServerState getServerState() { return serverState; }

  // Método para enviar datos de usuarios a todos los clientes
  private void sendUsersData() {
    UsersData usersData = new UsersData();
    usersData.users = new ArrayList<>();

    for (User user : users.values()) {
      NewUserData userData = new NewUserData();
      userData.name = user.getName();
      userData.color = user.getColor();
      usersData.users.add(userData);
    }

    socketServer.emit("users", SocketSerializer.serialize(usersData));
  }

  // Métodos para enviar mensajes de chat y cambiar colores de usuarios al host
  public void sendMessage(String message) {
    NewMessageData data = new NewMessageData();

    data.message = message;
    data.name = hostUser.getName();

    if (onMessage != null) {
      onMessage.accept(new Message(data.message, hostUser));
    }

    socketServer.emit("chatMessage", SocketSerializer.serialize(data));
  }

  public void changeColor(CharacterColor color) {
    hostUser.setColor(color);
    sendUsersData();

    if (onUsersChange != null) {
      onUsersChange.accept(new ArrayList<>(users.values()));
    }
  }

  // Método para sincronizar datos de clientes con el juego
  public void syncClients() {
    ArrayList<Player> players = game.getPlayers();
    ArrayList<Bomb> bombs = game.getBombs();
    Level level = game.getLevel();
    GameState gameState = game.getGameState();

    byte[] playersData = SocketSerializer.serialize(players);
    byte[] bombsData = SocketSerializer.serialize(bombs);
    byte[] levelData = SocketSerializer.serialize(level);
    byte[] gameStateData = SocketSerializer.serialize(gameState);

    socketServer.emit("syncPlayers", playersData);
    socketServer.emit("syncBombs", bombsData);
    socketServer.emit("syncLevel", levelData);
    socketServer.emit("syncState", gameStateData);
  }

  // Métodos para iniciar y finalizar el juego
  public void startGame() {
    if (users.size() < 2) {
      return;
    }

    changeState(ServerState.INGAME);
    socketServer.emit("startGame", null);
  }

  public void endGame() {
    changeState(ServerState.CONNECTED);
    game.end();
    socketServer.emit("endGame", null);
  }

  // Método para obtener la lista de clientes conectados
  public ArrayList<User> getClients() {
    return new ArrayList<>(users.values());
  }

  // Método para configurar al host del juego
  public void setHost(User user) {
    hostUser = user;
    users.put(null, user);

    sendUsersData();

    if (onUsersChange != null) {
      onUsersChange.accept(new ArrayList<>(users.values()));
    }
  }

  // Método para obtener al host del juego
  public User getHost() { return hostUser; }

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
  public List<User> getUsers() { return new ArrayList<>(users.values()); }

  /**
   * Método para obtener el puerto al que está conectado el servidor.
   * @return El puerto al que está conectado el servidor.
   */
  public int getPort() { return socketServer.getPort(); }

  /**
   * Método para obtener el estado del servidor.
   * @return El estado del servidor.
   */
  public ServerState getState() { return serverState; }
}
