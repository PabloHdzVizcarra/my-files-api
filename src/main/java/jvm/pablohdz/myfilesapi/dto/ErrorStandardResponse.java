package jvm.pablohdz.myfilesapi.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ErrorStandardResponse {
  private String type;
  private String code;
  private String message;
  private List<Map<String, List<String>>> param;
  private String timestamp;

  public ErrorStandardResponse() {
    this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss yyyy-MM-dd"));
  }

  public ErrorStandardResponse(
      String type, String code, String message, List<Map<String, List<String>>> param) {
    this.type = type;
    this.code = code;
    this.message = message;
    this.param = param;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<Map<String, List<String>>> getParam() {
    return param;
  }

  public void setParam(List<Map<String, List<String>>> param) {
    this.param = param;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
}
