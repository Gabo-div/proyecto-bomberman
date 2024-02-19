package proyecto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {

  private int width;
  private int height;
  private Block[][] level;

  public Level(int width, int height, Coord<Double> playerCoord) {
    this.width = width;
    this.height = height;
    level = new Block[height][width];

    ArrayList<Integer> availablePositions = new ArrayList<>();

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

    for (int oy = -1; oy <= 1; oy++) {
      for (int ox = -1; ox <= 1; ox++) {
        int posX = (int)Math.round(playerCoord.x) + ox;
        int posY = (int)Math.round(playerCoord.y) + oy;

        int index = availablePositions.indexOf(posX + posY * width);

        if (index == -1) {
          continue;
        }

        availablePositions.remove(index);
      }
    }

    Collections.shuffle(availablePositions);
    List<Integer> brickPositions = availablePositions.subList(0, 35);

    for (int i = 0; i < brickPositions.size(); i++) {
      int x = brickPositions.get(i) % width;
      int y = brickPositions.get(i) / width;
      level[y][x] = new BrickBlock(new Coord<>(x, y));
    }
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

  public boolean checkPlayerCollisionX(int x, double y) {
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

  public boolean checkPlayerCollisionY(double x, int y) {
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
}
