package proyecto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {

  private int width;
  private int height;
  private Block[][] level;

  ArrayList<Integer> availablePositions = new ArrayList<>();

  private ArrayList<Character> characters = new ArrayList<>();

  public Level(int width, int height, Player player) {
    this.width = width;
    this.height = height;
    level = new Block[height][width];

    Coord<Integer> playerCoord = Coord.round(player.getCoord());

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        boolean isBorder =
            y == 0 || y == height - 1 || x == 0 || x == width - 1;
        boolean isEven = x % 2 == 0 && y % 2 == 0;

        if (isBorder || isEven) {
          level[y][x] = new WallBlock(new Coord<>(x, y));
          continue;
        }

        level[y][x] = new AirBlock(new Coord<>(x, y));

        availablePositions.add(x + y * width);
      }
    }

    characters.add(player);
    clearPositionsNearby(playerCoord.x, playerCoord.y);

    int enemiesCount = 0;
    while (enemiesCount < 5) {
      double enemydX = Math.random() * width - 1;
      double enemydY = Math.random() * height - 1;

      int enemyX = (int)enemydX;
      int enemyY = (int)enemydY;

      Coord<Double> enemyCoord = new Coord<>((double)enemyX, (double)enemyY);

      if (!(level[enemyY][enemyX] instanceof AirBlock)) {
        continue;
      }

      Direction direction = Direction.random();

      if (level[enemyY - 1][enemyX] instanceof WallBlock &&
          level[enemyY + 1][enemyX] instanceof WallBlock) {
        direction = Direction.randomX();
      }

      if (level[enemyY][enemyX - 1] instanceof WallBlock &&
          level[enemyY][enemyX + 1] instanceof WallBlock) {
        direction = Direction.randomY();
      }

      BasicEnemy enemy = new BasicEnemy(enemyCoord);

      enemy.setDirection(direction);
      characters.add(enemy);

      clearPositionsNearby(enemyX, enemyY);

      enemiesCount++;
    }

    Collections.shuffle(availablePositions);
    List<Integer> brickPositions = availablePositions.subList(0, 35);

    for (int i = 0; i < brickPositions.size(); i++) {
      int x = brickPositions.get(i) % width;
      int y = brickPositions.get(i) / width;
      level[y][x] = new BrickBlock(new Coord<>(x, y));
    }
  }

  private void clearPositionsNearby(int x, int y) {
    for (int oy = -1; oy <= 1; oy++) {
      for (int ox = -1; ox <= 1; ox++) {
        int posX = x + ox;
        int posY = y + oy;

        int index = availablePositions.indexOf(posX + posY * width);

        if (index == -1) {
          continue;
        }

        availablePositions.remove(index);
      }
    }
  }

  public List<Character> getCharacters() { return characters; }

  public void addCharacter(Character character) { characters.add(character); }

  public void removeCharacter(Character character) {
    characters.remove(character);
  }

  public int getWidth() { return width; }

  public int getHeight() { return height; }

  public Block getBlock(int x, int y) { return level[y][x]; }

  public Block getBlock(Coord<Integer> coord) {
    return level[coord.y][coord.x];
  }

  public void setBlock(Block block) {
    level[block.getCoord().y][block.getCoord().x] = block;
  }

  public boolean checkCollisionX(int x, double y) {
    int yFloor = (int)Math.floor(y);
    int yCeil = (int)Math.ceil(y);

    Block blockFloor = getBlock(x, yFloor);
    Block blockCeil = getBlock(x, yCeil);

    boolean isFloorAir = blockFloor instanceof AirBlock;
    boolean isCeilAir = blockCeil instanceof AirBlock;

    if (!isFloorAir || !isCeilAir) {
      return true;
    }

    boolean hasFloorEntity = ((AirBlock)blockFloor).hasEntity();
    boolean hasCeilEntity = ((AirBlock)blockCeil).hasEntity();

    if (hasFloorEntity || hasCeilEntity) {
      return true;
    }

    return false;
  }

  public boolean checkCollisionY(double x, int y) {
    int xFloor = (int)Math.floor(x);
    int xCeil = (int)Math.ceil(x);

    Block blockFloor = getBlock(xFloor, y);
    Block blockCeil = getBlock(xCeil, y);

    boolean isFloorAir = blockFloor instanceof AirBlock;
    boolean isCeilAir = blockCeil instanceof AirBlock;

    if (!isFloorAir || !isCeilAir) {
      return true;
    }

    boolean hasFloorEntity = ((AirBlock)blockFloor).hasEntity();
    boolean hasCeilEntity = ((AirBlock)blockCeil).hasEntity();

    if (hasFloorEntity || hasCeilEntity) {
      return true;
    }

    return false;
  }

  public Character checkCharacterCollisions(double x, double y) {
    for (Character character : characters) {
      if (character instanceof Player) {
        continue;
      }

      if (character.isDead()) {
        continue;
      }

      Coord<Double> pCoord = new Coord<Double>(x, y);
      Coord<Double> cCoord = character.getCoord();

      double x1 = pCoord.x;
      double y1 = pCoord.y;

      double x2 = cCoord.x;
      double y2 = cCoord.y;

      double size = 0.5;

      if (x1 > x2 + size || x1 + size < x2 || y1 > y2 + size ||
          y1 + size < y2) {
        continue;
      }

      return character;
    }

    return null;
  }
}
