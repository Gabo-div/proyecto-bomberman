package proyecto.model;

public class BasicBomb extends Bomb {

    public BasicBomb(Coord<Integer> coord) {
        super(coord);
    }

    @Override
    public int getRadius() {
        return 1;
    }

    @Override
    public int getDelay() {
        return 3;
    }
}
