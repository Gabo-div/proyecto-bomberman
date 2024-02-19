package proyecto.model;

public class BrickBlock extends SolidBlock {

  public BrickBlock(Coord<Integer> coord) { super(coord); }

  @Override
  public boolean isBreakable() {
    return true;
  }
}
