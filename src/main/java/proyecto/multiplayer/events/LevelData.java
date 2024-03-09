package proyecto.multiplayer.events;

import java.io.Serializable;
import proyecto.model.Level;

/**
 * Clase que representa los datos de un nivel para la transmisión en red.
 */
public class LevelData implements Serializable {
  int width;
  int height;
  BlockData[][] blocks;

  /**
   * Constructor que inicializa los datos del nivel.
   * 
   * @param level El nivel del cual se obtienen los datos.
   */
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
