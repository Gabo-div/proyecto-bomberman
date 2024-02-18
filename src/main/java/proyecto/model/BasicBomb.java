package proyecto.model;

import proyecto.game.GameConstants;

public class BasicBomb extends Bomb {
  private int explosionDelaySeconds = 5;
  private int radius = 2;

  public BasicBomb(Coord<Integer> coord) { super(coord); }

  @Override
  public int getRadius() {
    return radius;
  }

  @Override
  public int getDelayTicks() {
    return (explosionDelaySeconds * 1000) / GameConstants.TICK_DURATION_MS;
  }

  @Override
  public void explode(Level level) {
    exploded = true;

    for (int x = 0; x <= radius; x++) {

      if (coord.x + x >= level.getWidth()) {
        continue;
      }

      if (level.getTile(coord.x + x, coord.y) == 1) {
        break;
      }

      level.setTile(coord.x + x, coord.y, 0);
    }

    for (int x = 0; x >= -radius; x--) {

      if (coord.x + x < 0) {
        continue;
      }

      if (level.getTile(coord.x + x, coord.y) == 1) {
        break;
      }

      level.setTile(coord.x + x, coord.y, 0);
    }

    for (int y = 0; y <= radius; y++) {

      if (coord.y + y >= level.getHeight()) {
        continue;
      }

      if (level.getTile(coord.x, coord.y + y) == 1) {
        break;
      }

      level.setTile(coord.x, coord.y + y, 0);
    }

    for (int y = 0; y >= -radius; y--) {

      if (coord.y + y < 0) {
        continue;
      }

      if (level.getTile(coord.x, coord.y + y) == 1) {
        break;
      }

      level.setTile(coord.x, coord.y + y, 0);
    }
  }
}
