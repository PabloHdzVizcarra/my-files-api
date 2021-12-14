package jvm.pablohdz.myfilesapi.jwt;

import io.jsonwebtoken.Claims;

public interface JwtProvider {
  String generateToken(String username);

  long getTimeExpirationToken();

  /**
   * Get the username signed from token
   * @param token A valid token
   * @return The username signed in the token
   */
  String getUsernameFromToken(String token);

  boolean validateToken(String token);

  Claims getBodyFromJwt(String token);
}
