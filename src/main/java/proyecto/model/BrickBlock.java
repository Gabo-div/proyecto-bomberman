package proyecto.model;

public class BrickBlock extends SolidBlock {

  Entity<Integer> entity;

  public BrickBlock(Coord<Integer> coord) { super(coord); }

  public Entity<Integer> getEntity() { return entity; }

  public void setEntity(Entity<Integer> entity) { this.entity = entity; }

  @Override
  public boolean isBreakable() {
    return true;
  }
}
