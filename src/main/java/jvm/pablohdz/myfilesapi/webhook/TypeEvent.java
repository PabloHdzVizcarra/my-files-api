package jvm.pablohdz.myfilesapi.webhook;

public enum TypeEvent {
  ADDED("added"),
  UPDATE("update"),
  DELETE("delete");

  private final String type;

  TypeEvent(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
