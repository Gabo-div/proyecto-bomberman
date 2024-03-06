package proyecto.multiplayer;

public class Message {
  private String message;
  private User user;

  public Message(String message, User user) {
    this.message = message;
    this.user = user;
  }

  public String getMessage() { return message; }

  public User getUser() { return user; }
}
