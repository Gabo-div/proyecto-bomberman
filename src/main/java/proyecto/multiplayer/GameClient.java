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

public class GameClient {
  private static GameClient instance;

  private ServerState serverState = ServerState.DISCONNECTED;
  private ClientSocket clientSocket;

  private User clientUser;
  private ArrayList<User> users = new ArrayList<>();

  private Consumer<ServerState> onStateChange;
  private Consumer<List<User>> onUsersChange;
  private Consumer<Message> onMessage;

  private MultiplayerGame game = MultiplayerGame.getInstance();

  public GameClient() {}

  public static GameClient getInstance() {
    if (instance == null) {
      instance = new GameClient();
    }
    return instance;
  }

  public void start(int port) {
    try {
      InetAddress address = InetAddress.getByName("localhost");

      changeState(ServerState.CONNECTING);
      clientSocket = new ClientSocket(address, port, 9000);

      clientSocket.addListener("users", usersHandler);
      clientSocket.addListener("chatMessage", chatMessageHandler);

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

      clientSocket.addListener("startGame",
                               (data) -> { changeState(ServerState.INGAME); });

      clientSocket.addListener(
          "endGame", (data) -> { changeState(ServerState.CONNECTED); });

      clientSocket.addListener("syncState", (data) -> {
        GameState state = (GameState)SocketSerializer.deserialize(data);
        game.setGameState(state);
      });

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

      clientSocket.connect();
    } catch (Exception e) {
      changeState(ServerState.ERROR);
      e.printStackTrace();
    }
  }

  public void stop() {
    if (clientSocket != null) {
      clientSocket.disconnect();
    }
  }

  private void changeState(ServerState state) {
    serverState = state;
    if (onStateChange != null) {
      onStateChange.accept(serverState);
    }
  }

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

  public void joinLobby(String name) {
    clientUser = new User(name, CharacterColor.WHITE);

    NewUserData userData = new NewUserData();
    userData.name = name;
    userData.color = clientUser.getColor();
    clientSocket.emit("newUser", SocketSerializer.serialize(userData));
  }

  public void sendMessage(String message) {
    NewMessageData data = new NewMessageData();

    data.message = message;
    data.name = clientUser.getName();

    clientSocket.emit("chatMessage", SocketSerializer.serialize(data));
  }

  public void changeColor(CharacterColor color) {
    String colorName = color.name();
    clientSocket.emit("changeColor", SocketSerializer.serialize(colorName));
  }

  public void sendMovement(int stateX, int stateY) {
    MovementData movementData = new MovementData();
    movementData.stateX = stateX;
    movementData.stateY = stateY;
    clientSocket.emit("movement", SocketSerializer.serialize(movementData));
  }

  public void sendBomb() { clientSocket.emit("bomb", null); }

  public void setOnStateChange(Consumer<ServerState> onStateChange) {
    this.onStateChange = onStateChange;
  }

  public void setOnUsersChange(Consumer<List<User>> onUsersChange) {
    this.onUsersChange = onUsersChange;
  }

  public void setOnMessage(Consumer<Message> onMessage) {
    this.onMessage = onMessage;
  }

  public ArrayList<User> getUsers() { return users; }

  public ServerState getState() { return serverState; }

  public User getClientUser() { return clientUser; }

  public int getPort() { return clientSocket.getPort(); }
}
