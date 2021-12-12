package jvm.pablohdz.myfilesapi.exception;

public class AuthenticationCredentialsInvalid extends RuntimeException {
  public AuthenticationCredentialsInvalid(String message) {
    super(message);
  }
}
