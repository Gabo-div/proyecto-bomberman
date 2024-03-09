package proyecto.model;

/**
 * Esta clase abstracta representa un personaje en el juego.
 */
public abstract class Character extends Entity<Double> {

  /**
   * La coordenada del personaje.
   */
  protected Coord<Double> coord;

  /**
   * La cantidad de vidas del personaje.
   */
  protected Integer lifes;

  /**
   * La velocidad del personaje.
   */
  protected Double speed;

  /**
   * La dirección en la que se mueve el personaje.
   */
  protected Direction direction;

  /**
   * El estado de la dirección del personaje.
   */
  protected Integer directionState = 1;

  /**
   * El contador de estado de dirección del personaje.
   */
  protected Integer directionStateCounter = 0;

  /**
   * Indica si el personaje es invulnerable.
   */
  protected Boolean invincible = false;

  /**
   * La cantidad de ticks de invulnerabilidad del personaje.
   */
  protected Integer invincibilityTicks = 0;

  /**
   * Constructor para inicializar un personaje con una coordenada, velocidad y cantidad de vidas específicas.
   * @param coord La coordenada del personaje.
   * @param speed La velocidad del personaje.
   * @param lifes La cantidad de vidas del personaje.
   */
  public Character(Coord<Double> coord, double speed, int lifes) {
    this.coord = coord;
    this.lifes = lifes;
    this.speed = speed;
    this.direction = Direction.RIGHT;
  }

  /**
   * Método para obtener la coordenada del personaje.
   * @return La coordenada del personaje.
   */
  @Override
  public Coord<Double> getCoord() {
    return new Coord<>(coord.x, coord.y);
  }

  /**
   * Método para verificar si el personaje es invulnerable.
   * @return Verdadero si el personaje es invulnerable, falso de lo contrario.
   */
  public Boolean isInvincible() {
    return invincible;
  }

  /**
   * Método para establecer si el personaje es invulnerable.
   * @param invincible Valor que indica si el personaje es invulnerable.
   */
  public void setInvincible(Boolean invincible) {
    this.invincible = invincible;
  }

  /**
   * Método para obtener la cantidad de ticks de invulnerabilidad del personaje.
   * @return La cantidad de ticks de invulnerabilidad del personaje.
   */
  public Integer getInvincibilityTicks() {
    return invincibilityTicks;
  }

  /**
   * Método para establecer la cantidad de ticks de invulnerabilidad del personaje.
   * @param invincibilityTicks La cantidad de ticks de invulnerabilidad del personaje.
   */
  public void setInvincibilityTicks(Integer invincibilityTicks) {
    this.invincibilityTicks = invincibilityTicks;
  }

  /**
   * Método para obtener la dirección del personaje.
   * @return La dirección del personaje.
   */
  public Direction getDirection() {
    return direction;
  }

  /**
   * Método para establecer la dirección del personaje.
   * @param direction La dirección del personaje.
   */
  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  /**
   * Método para obtener el estado de la dirección del personaje.
   * @return El estado de la dirección del personaje.
   */
  public Integer getDirectionState() {
    return directionState;
  }

  /**
   * Método para establecer el estado de la dirección del personaje.
   * @param directionState El estado de la dirección del personaje.
   */
  public void setDirectionState(Integer directionState) {
    this.directionState = directionState;
  }

  /**
   * Método para obtener el contador de estado de dirección del personaje.
   * @return El contador de estado de dirección del personaje.
   */
  public Integer getDirectionStateCounter() {
    return directionStateCounter;
  }

  /**
   * Método para establecer el contador de estado de dirección del personaje.
   * @param directionStateCounter El contador de estado de dirección del personaje.
   */
  public void setDirectionStateCounter(Integer directionStateCounter) {
    this.directionStateCounter = directionStateCounter;
  }

  /**
   * Método para establecer la coordenada del personaje.
   * @param coord La coordenada del personaje.
   */
  public void setCoord(Coord<Double> coord) {
    this.coord = coord;
  }

  /**
   * Método para establecer la cantidad de vidas del personaje.
   * @param lifes La cantidad de vidas del personaje.
   */
  public void setLifes(int lifes) {
    this.lifes = lifes;
  }

  /**
   * Método para obtener la cantidad de vidas del personaje.
   * @return La cantidad de vidas del personaje.
   */
  public Integer getLifes() {
    return this.lifes;
  }

  /**
   * Método para verificar si el personaje está muerto.
   * @return Verdadero si el personaje está muerto, falso de lo contrario.
   */
  public boolean isDead() {
    return this.lifes <= 0;
  }

  /**
   * Método para establecer la velocidad del personaje.
   * @param speed La velocidad del personaje.
   */
  public void setSpeed(Double speed) {
    this.speed = speed;
  }

  /**
   * Método para obtener la velocidad del personaje.
   * @return La velocidad del personaje.
   */
  public Double getSpeed() {
    return this.speed;
  }
}
