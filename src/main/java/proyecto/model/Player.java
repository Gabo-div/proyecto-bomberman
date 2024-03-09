package proyecto.model;

import proyecto.multiplayer.CharacterColor;

/**
 * Clase que representa al jugador en el juego.
 */
public class Player extends Character {
  private BombType bombType = BombType.BASIC;
  private Integer firepower;
  private Integer availableBombs;
  private Integer movementStateX = 0;
  private Integer movementStateY = 0;
  private CharacterColor color = CharacterColor.WHITE;
  private String name;

  /**
   * Constructor para crear un jugador con una coordenada, una cantidad de vidas y un poder de explosión.
   * @param coord Coordenada inicial del jugador.
   * @param lifes Cantidad de vidas iniciales del jugador.
   * @param firepower Poder de explosión inicial del jugador.
   */
  public Player(Coord<Double> coord, int lifes, Integer firepower) {
    super(coord, 0.25 / 2, lifes);
    this.bombType = BombType.BASIC;
    this.firepower = firepower;
    this.availableBombs = 1;
  }

  /**
   * Constructor para crear un jugador con coordenadas, una cantidad de vidas y un poder de explosión.
   * @param x Coordenada x inicial del jugador.
   * @param y Coordenada y inicial del jugador.
   * @param lifes Cantidad de vidas iniciales del jugador.
   * @param firepower Poder de explosión inicial del jugador.
   */
  public Player(double x, double y, int lifes, Integer firepower) {
    super(new Coord<>(x, y), 4, lifes);
    this.bombType = BombType.BASIC;
    this.firepower = firepower;
    this.availableBombs = 1;
  }

  /**
   * Método para hacer al jugador invencible durante un cierto número de ticks.
   * @param ticks Número de ticks durante los cuales el jugador será invencible.
   */
  public void makeInvincibleUntil(Integer ticks) {
    invincible = true;
    invincibilityTicks = ticks;
  }

  // Getters y setters
  public Integer getAvailableBombs() { return availableBombs; }

  public void setAvailableBombs(Integer availableBombs) {
    this.availableBombs = availableBombs;
  }

  public BombType getBombType() { return bombType; }

  public void setBombType(BombType bombType) { this.bombType = bombType; }

  public Integer getFirepower() { return firepower; }

  public void addFirepower(Integer value) { this.firepower += value; }

  public Integer getMovementStateX() { return movementStateX; }

  public Integer getMovementStateY() { return movementStateY; }

  public void setMovementStateX(Integer movementStateX) {
    this.movementStateX = movementStateX;
  }

  public void setMovementStateY(Integer movementStateY) {
    this.movementStateY = movementStateY;
  }

  public CharacterColor getColor() { return color; }

  public void setColor(CharacterColor color) { this.color = color; }

  public String getName() { return name; }

  public void setName(String name) { this.name = name; }
}
