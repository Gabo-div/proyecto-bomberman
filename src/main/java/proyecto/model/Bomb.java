package proyecto.model;

public abstract class Bomb {

    protected Coord<Integer> coord;
    protected boolean exploded = false;

    public Bomb(Coord<Integer> coord) {
        this.coord = coord;
    }

    public Coord<Integer> getCoord() {
        return coord;
    }

    public boolean isExploded() {
        return exploded;
    }

    public abstract int getDelay();

    public abstract int getRadius();

}
