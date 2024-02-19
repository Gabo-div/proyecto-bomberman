package proyecto.model;

public class AirBlock extends Block {

  Entity<Integer> entity;

  Coord<Integer> coord;

  public AirBlock(Coord<Integer> coord) { this.coord = coord; }

  @Override
  public Coord<Integer> getCoord() {
    return coord;
  }

  public Entity<Integer> getEntity() { return entity; }

  public void setEntity(Entity<Integer> entity) { this.entity = entity; }

  public boolean hasEntity() { return entity != null; }
}
