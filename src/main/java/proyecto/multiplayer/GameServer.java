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

  private GameServer() {}

  public static GameServer getInstance() {
    if (instance == null) {
      instance = new GameServer();
    }
    return instance;
  }

  public void start(int roomSize) {
    try {
      this.roomSize = roomSize;
      socketServer = new ServerSocket(0, 9000);

      socketServer.addListener("newUser", newUserHandler);
      socketServer.addListener("chatMessage", chatMessageHandler);
      socketServer.addListener("changeColor", colorHandler);
      socketServer.addListener("movement", movementHandler);
      socketServer.addListener("bomb", bombHandler);

      socketServer.addListener("disconnected", (data, handler) -> {
        if (users.containsKey(handler)) {
          users.remove(handler);
          sendUsersData();

          if (onUsersChange != null) {
            onUsersChange.accept(new ArrayList<>(users.values()));
          }
        }
      });

      socketServer.run();
      changeState(ServerState.CONNECTED);

    } catch (Exception e) {
      changeState(ServerState.ERROR);
      e.printStackTrace();
    }
  }

  private void changeState(ServerState state) {
    serverState = state;
    if (onStateChange != null) {
      onStateChange.accept(serverState);
    }
  }

  public void stop() {
    if (socketServer != null) {
      socketServer.close();
    }
  }

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

  public ServerState getServerState() { return serverState; }

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

  private SocketEventListener colorHandler = (data, handler) -> {
    String colorName = (String)SocketSerializer.deserialize(data);
    CharacterColor color = CharacterColor.valueOf(colorName);
    users.get(handler).setColor(color);

    sendUsersData();
    if (onUsersChange != null) {
      onUsersChange.accept(new ArrayList<>(users.values()));
    }
  };

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

  private SocketEventListener bombHandler = (data, handler) -> {
    User user = users.get(handler);
    game.addBomb(user.getName());
  };

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

  public void startGame() {
    if (users.size() < 2) {
      return;
    }

    changeState(ServerState.INGAME);
    socketServer.emit("startGame", null);
  }

  public ArrayList<User> getClients() {
    return new ArrayList<>(users.values());
  }

  public void setHost(User user) {
    hostUser = user;
    users.put(null, user);

    sendUsersData();

    if (onUsersChange != null) {
      onUsersChange.accept(new ArrayList<>(users.values()));
    }
  }

  public User getHost() { return hostUser; }

  public void setOnStateChange(Consumer<ServerState> onStateChange) {
    this.onStateChange = onStateChange;
  }

  public void setOnUsersChange(Consumer<List<User>> onUsersChange) {
    this.onUsersChange = onUsersChange;
  }

  public void setOnMessage(Consumer<Message> onMessage) {
    this.onMessage = onMessage;
  }

  public List<User> getUsers() { return new ArrayList<>(users.values()); }

  public int getPort() { return socketServer.getPort(); }

  public ServerState getState() { return serverState; }
}
