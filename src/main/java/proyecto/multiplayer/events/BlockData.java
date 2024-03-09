package proyecto.multiplayer.events;

import java.io.Serializable;
import proyecto.model.AirBlock;
import proyecto.model.Block;
import proyecto.model.BrickBlock;
import proyecto.model.WallBlock;

/**
 * Clase que representa los datos de un bloque para la transmisión en red.
 */
public class BlockData implements Serializable {
  String type;
  String entity;

  /**
   * Constructor que inicializa los datos del bloque.
   * 
   * @param block El bloque del cual se obtienen los datos.
   */
  public BlockData(Block block) {
    if (block instanceof WallBlock) {
      type = "WALL";
      entity = null;
      return;
    }

    if (block instanceof AirBlock) {
      type = "BRICK";
      entity = ((AirBlock)block).getEntity().toString();
    } else if (block instanceof BrickBlock) {
      type = "BRICK";
    }
  }
}
