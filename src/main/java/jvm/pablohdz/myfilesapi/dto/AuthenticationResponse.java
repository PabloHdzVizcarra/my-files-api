package jvm.pablohdz.myfilesapi.dto;

public class AuthenticationResponse {
  private String token;
  private String username;
  private String time;

  public AuthenticationResponse() {}

  public AuthenticationResponse(String token, String username, String time) {
    this.token = token;
    this.username = username;
    this.time = time;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
