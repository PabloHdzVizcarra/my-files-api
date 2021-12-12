package jvm.pablohdz.myfilesapi.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;

import io.jsonwebtoken.Jwts;
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
    byte[] apiKeySecretBytes =
        DatatypeConverter.parseBase64Binary(passwordStorageToken.getPasswordToSignToken());
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

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
            new Date(System.currentTimeMillis() * passwordStorageToken.getExpirationTokenTime()))
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
  public String getSecretKey() {
    return passwordStorageToken.getPasswordToSignToken();
  }
}
