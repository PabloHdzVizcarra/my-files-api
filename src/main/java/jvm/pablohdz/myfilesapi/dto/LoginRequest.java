package jvm.pablohdz.myfilesapi.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginRequest {
  @NotNull(message = "username is required")
  @Size(min = 3, max = 55, message = "the username be greater than 3 characters")
  private String username;

  @Pattern(
      regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
      message =
          "the password must be contain an integer, an upper case letter, a lower case letter a "
              + "special character and minimum eight in length")
  private String password;

  public LoginRequest() {}

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
