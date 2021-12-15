package jvm.pablohdz.myfilesapi.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ErrorStandardResponse {
  private String type;
  private String code;
  private String message;
  private List<Map<String, List<String>>> param;
  private String timestamp;

  public ErrorStandardResponse() {
    TimeZone timeZone = TimeZone.getDefault();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    dateFormat.setTimeZone(timeZone);
    this.timestamp = dateFormat.format(new Date());
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
