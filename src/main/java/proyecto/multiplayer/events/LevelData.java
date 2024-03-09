package proyecto.multiplayer.events;

import java.io.Serializable;
import proyecto.model.Level;

public class LevelData implements Serializable {
  int width;
  int height;
  BlockData[][] blocks;

  public LevelData(Level level) {
    this.width = level.getWidth();
    this.height = level.getHeight();
    this.blocks = new BlockData[height][width];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        blocks[y][x] = new BlockData(level.getBlock(x, y));
      }
    }
  }
}
