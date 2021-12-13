package jvm.pablohdz.myfilesapi.security;

import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jvm.pablohdz.myfilesapi.jwt.IoJjwtJwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
  private static final String PREFIX = "Bearer ";
  private final IoJjwtJwtProvider ioJjwtJwtProvider;

  public JWTAuthorizationFilter(IoJjwtJwtProvider ioJjwtJwtProvider) {
    this.ioJjwtJwtProvider = ioJjwtJwtProvider;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (checkJWTToken(request, response)) {
      Claims claims = validateToken(request);
      if (claims.get("authorities") != null) {
        setUpSpringAuthentication(claims);
      } else {
        SecurityContextHolder.clearContext();
      }
    } else {
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
  }

  private void setUpSpringAuthentication(Claims claims) {
    List<String> authorities = (List) claims.get("authorities");

    UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken(
            claims.getSubject(),
            null,
            authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  private Claims validateToken(HttpServletRequest request) {
    String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION).replace(PREFIX, "");
    ioJjwtJwtProvider.validateToken(jwtToken);
    return ioJjwtJwtProvider.getBodyFromJwt(jwtToken);
  }

  private boolean checkJWTToken(HttpServletRequest request, HttpServletResponse response) {
    String authenticationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    return authenticationHeader != null && authenticationHeader.startsWith(PREFIX);
  }
}
