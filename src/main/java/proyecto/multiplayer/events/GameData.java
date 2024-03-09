package proyecto.multiplayer.events;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import proyecto.game.MultiplayerGame;
import proyecto.model.Bomb;
import proyecto.model.Player;
import proyecto.multiplayer.User;

public class GameData implements Serializable {
  public ArrayList<PlayerData> playersData;
  public ArrayList<BombData> bombsData;

  public GameData(Collection<User> users, MultiplayerGame game) {
    playersData = new ArrayList<>();
    bombsData = new ArrayList<>();

    for (User user : users) {
      String name = user.getName();
      Player player = game.getPlayer(name);
      PlayerData playerData = new PlayerData(name, player);
      playersData.add(playerData);
    }

    for (Bomb bomb : game.getBombs()) {
      BombData bombData = new BombData(bomb);
      bombsData.add(bombData);
    }
  }
}
