package jvm.pablohdz.myfilesapi.jwt;

import static org.mockito.Mockito.when;

import java.util.Objects;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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
  void givenValidToken_whenValidate() {
    String expectedUsername = "john";

    String actualToken = jwtProvider.generateToken(expectedUsername);
    String actualUsername = jwtProvider.getUsernameFromToken(actualToken);

    Assertions.assertThat(Objects.equals(actualUsername, expectedUsername)).isTrue();
  }
}
