package proyecto.multiplayer.events;

import java.io.Serializable;
import proyecto.model.AirBlock;
import proyecto.model.Block;
import proyecto.model.BrickBlock;
import proyecto.model.WallBlock;

public class BlockData implements Serializable {
  String type;
  String entity;

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
