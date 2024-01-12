package proyecto.model;

public class Player {
    Coord<Double> coord;
    BombType bombType;

    public Player(double x, double y) {
        coord = new Coord<>(x, y);
    }

    public Coord<Double> getCoord() {
        return coord;
    }

    public void setCoord(Coord<Double> coord) {
        this.coord = coord;
    }

    public BombType getBombType() {
        return bombType;
    }

    public void setBombType(BombType bombType) {
        this.bombType = bombType;
    }
}
