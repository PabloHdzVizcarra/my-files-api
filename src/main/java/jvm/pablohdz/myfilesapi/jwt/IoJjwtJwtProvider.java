package jvm.pablohdz.myfilesapi.jwt;

import static io.jsonwebtoken.Jwts.parserBuilder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class IoJjwtJwtProvider implements JwtProvider {
  private final PasswordStorageToken passwordStorageToken;

  private KeyStore keyStore;

  public IoJjwtJwtProvider(PasswordStorageToken passwordStorageToken) {
    this.passwordStorageToken = passwordStorageToken;
    try {
      keyStore = KeyStore.getInstance("JKS");
      InputStream resourceAsStream = getClass().getResourceAsStream("/my-files.jks");
      keyStore.load(resourceAsStream, passwordStorageToken.getPasswordToSignToken().toCharArray());
    } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String generateToken(String username) {
    List<GrantedAuthority> grantedAuthorities =
        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

    return Jwts.builder()
        .setId("my.files.jwt")
        .setSubject(username)
        .claim(
            "authorities",
            grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(
            Date.from(Instant.now().plusMillis(passwordStorageToken.getExpirationTokenTime())))
        .signWith(getPrivateKey())
        .compact();
  }

  private PrivateKey getPrivateKey() {
    try {
      return (PrivateKey)
          keyStore.getKey("my-files", passwordStorageToken.getPasswordToSignToken().toCharArray());
    } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  @Override
  public long getTimeExpirationToken() {
    return passwordStorageToken.getExpirationTokenTime();
  }

  @Override
  public String getUsernameFromToken(String token) {
    Jws<Claims> jwtParser =
        parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(token);

    return jwtParser.getBody().getSubject();
  }

  @Override
  public boolean validateToken(String token) {
    parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(token);
    return true;
  }

  private PublicKey getPublicKey() {
    try {
      return keyStore.getCertificate("my-files").getPublicKey();
    } catch (KeyStoreException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}
