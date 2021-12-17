package jvm.pablohdz.myfilesapi.jwt;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Claims;
import java.util.Objects;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Disabled("Because use a file from the local server")
class IoJjwtJwtProviderTest {
  private JwtProvider jwtProvider;
  @Mock private PasswordStorageToken passwordStorageToken;

  @BeforeEach
  void setUp() {
    when(passwordStorageToken.getPasswordToSignToken()).thenReturn("secret-password");
    when(passwordStorageToken.getExpirationTokenTime()).thenReturn(1000 * 60 * 10);
    jwtProvider = new IoJjwtJwtProvider(passwordStorageToken);
  }

  @Test
  void whenCreateToken_thenTokenString() {
    String username = "john";
    String actualToken = jwtProvider.generateToken(username);

    Assertions.assertThat(actualToken).isInstanceOf(String.class);
  }

  @Test
  void givenValidToken_whenGetUsernameFromToken() {
    String expectedUsername = "john";

    String actualToken = jwtProvider.generateToken(expectedUsername);
    String actualUsername = jwtProvider.getUsernameFromToken(actualToken);

    Assertions.assertThat(Objects.equals(actualUsername, expectedUsername)).isTrue();
  }

  @Test
  void givenValidToken_whenValidate() {
    String token = jwtProvider.generateToken("iron.man");

    boolean isValidToken = jwtProvider.validateToken(token);

    Assertions.assertThat(isValidToken).isTrue();
  }

  @Test
  void givenValidToken_whenGetBody_thenCorrectBody() {
    // Arrange
    String expectedSubject = "iron.man";
    String token = jwtProvider.generateToken(expectedSubject);
    // Act
    Claims body = jwtProvider.getBodyFromJwt(token);
    String currentSubject = body.getSubject();
    // Assert
    assertThat(expectedSubject.equals(currentSubject)).isTrue();
  }
}
