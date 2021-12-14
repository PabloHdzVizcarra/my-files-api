package jvm.pablohdz.myfilesapi.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocalPasswordStorageToken implements PasswordStorageToken {
  @Value("${jwt-token.secret-key}")
  private String SECRET_KEY;

  @Value("${jwt-token.expiration-time}")
  private Integer EXPIRATION_TIME;

  public LocalPasswordStorageToken() {}

  @Override
  public String getPasswordToSignToken() {
    return this.SECRET_KEY;
  }

  @Override
  public int getExpirationTokenTime() {
    return this.EXPIRATION_TIME;
  }
}
