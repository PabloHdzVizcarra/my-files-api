package jvm.pablohdz.myfilesapi.api;

public enum ErrorCode {
  WEBHOOK_ERROR("webhook_error"),
  FILE_EXTENSION_INVALID("file_extension_invalid"),
  FILE_ALREADY_REGISTERED("file_already_registered"),
  FILE_ID_INVALID("file_id_invalid");

  private final String code;

  ErrorCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
