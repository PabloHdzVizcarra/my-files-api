package jvm.pablohdz.myfilesapi.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
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
  @Value("${jwt.secret-key}")
  private String SECRET_KEY;

  @Value("${jwt.expiration-time}")
  private Integer EXPIRATION_TIME;

  @Override
  public String generateToken(String username) {
    List<GrantedAuthority> grantedAuthorities =
        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
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
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(signatureAlgorithm, signingKey)
        .compact();
  }

  @Override
  public long getTimeExpirationToken() {
    return EXPIRATION_TIME;
  }
}
