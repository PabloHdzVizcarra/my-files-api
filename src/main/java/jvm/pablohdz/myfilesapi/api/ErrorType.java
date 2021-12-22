package jvm.pablohdz.myfilesapi.api;

public enum ErrorType {
  API_ERROR("api error"),
  ENTITY_ERROR("entity error"),
  INVALID_REQUEST_ERROR("invalid request error");

  private final String type;

  ErrorType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
