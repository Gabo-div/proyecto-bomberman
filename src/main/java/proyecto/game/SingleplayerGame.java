package proyecto.game;

import java.util.ArrayList;
import proyecto.model.*;

public class SingleplayerGame {

  private SpriteSheet spriteSheet;
  private Level level;
  private Player player;
  private ArrayList<Bomb> bombs;

  public SingleplayerGame() {
    spriteSheet = SpriteSheet.getInstance();
    player = new Player(1, 1);
    player.setBombType(BombType.BASIC);
    bombs = new ArrayList<Bomb>();
    level = new Level(15, 13, player.getCoord());
  }

  public SpriteSheet getSpriteSheet() { return spriteSheet; }

  public Level getLevel() { return level; }

  public Player getPlayer() { return player; }

  public ArrayList<Bomb> getBombs() { return bombs; }

  public void addBomb(BombType bombType, Coord<Integer> coords) {
    Bomb newBomb = new BasicBomb(coords);

    // if (bombType == BombType.BASIC) {
    // newBomb = new BasicBomb(coords);
    // }

    bombs.add(newBomb);
  }
}
