package jvm.pablohdz.myfilesapi.webhook;

public enum TypeEvent {
  FILE_ADDED("added"),
  FILE_UPDATE("update"),
  FILE_DELETE("delete"),
  FILE_DOWNLOAD("file_download");

  private final String type;

  TypeEvent(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
