package proyecto.model;

public abstract class SolidBlock extends Block {

  Coord<Integer> coord;

  public SolidBlock(Coord<Integer> coord) { this.coord = coord; }

  public abstract boolean isBreakable();

  @Override
  public Coord<Integer> getCoord() {
    return coord;
  }
}
