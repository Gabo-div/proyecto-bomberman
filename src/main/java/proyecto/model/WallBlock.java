package proyecto.model;

public class WallBlock extends SolidBlock {

  public WallBlock(Coord<Integer> coord) { super(coord); }

  @Override
  public boolean isBreakable() {
    return false;
  }
}
