package jvm.pablohdz.myfilesapi.jwt;

public interface JwtProvider {
  String generateToken(String username);

  long getTimeExpirationToken();

  String getSecretKey();
}
