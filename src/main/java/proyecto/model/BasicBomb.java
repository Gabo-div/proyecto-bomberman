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

      Block block = level.getBlock(coord.x + x, coord.y);

      if (block instanceof WallBlock) {
        break;
      }

      if (block instanceof AirBlock) {
        Entity<Integer> entity = ((AirBlock)block).getEntity();

        if (entity instanceof Bomb) {
          Bomb bomb = (Bomb)entity;

          if (bomb.exploded()) {
            continue;
          }

          bomb.setExplosionTick(explosionTick + 1);
        }
      }

      if (block instanceof BrickBlock) {
        level.setBlock(new AirBlock(new Coord<>(coord.x + x, coord.y)));
      }
    }

    for (int x = 0; x >= -radius; x--) {

      if (coord.x + x < 0) {
        continue;
      }

      Block block = level.getBlock(coord.x + x, coord.y);

      if (block instanceof WallBlock) {
        break;
      }

      if (block instanceof AirBlock) {
        Entity<Integer> entity = ((AirBlock)block).getEntity();

        if (entity instanceof Bomb) {
          Bomb bomb = (Bomb)entity;

          if (bomb.exploded()) {
            continue;
          }

          bomb.setExplosionTick(explosionTick + 1);
        }
      }

      if (block instanceof BrickBlock) {
        level.setBlock(new AirBlock(new Coord<>(coord.x + x, coord.y)));
      }
    }

    for (int y = 0; y <= radius; y++) {

      if (coord.y + y >= level.getHeight()) {
        continue;
      }

      Block block = level.getBlock(coord.x, coord.y + y);

      if (block instanceof WallBlock) {
        break;
      }

      if (block instanceof AirBlock) {
        Entity<Integer> entity = ((AirBlock)block).getEntity();

        if (entity instanceof Bomb) {
          Bomb bomb = (Bomb)entity;

          if (bomb.exploded()) {
            continue;
          }

          bomb.setExplosionTick(explosionTick + 1);
        }
      }

      if (block instanceof BrickBlock) {
        level.setBlock(new AirBlock(new Coord<>(coord.x, coord.y + y)));
      }
    }

    for (int y = 0; y >= -radius; y--) {

      if (coord.y + y < 0) {
        continue;
      }

      Block block = level.getBlock(coord.x, coord.y + y);

      if (block instanceof WallBlock) {
        break;
      }

      if (block instanceof AirBlock) {
        Entity<Integer> entity = ((AirBlock)block).getEntity();

        if (entity instanceof Bomb) {
          Bomb bomb = (Bomb)entity;

          if (bomb.exploded()) {
            continue;
          }

          bomb.setExplosionTick(explosionTick + 1);
        }
      }

      if (block instanceof BrickBlock) {
        level.setBlock(new AirBlock(new Coord<>(coord.x, coord.y + y)));
      }
    }
  }
}
