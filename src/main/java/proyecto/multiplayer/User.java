package proyecto.multiplayer;

public class User {
  private String name;
  private CharacterColor color;

  public User(String name, CharacterColor color) {
    this.name = name;
    this.color = color;
  }

  public String getName() { return name; }

  public CharacterColor getColor() { return color; }

  public void setColor(CharacterColor color) { this.color = color; }
}
