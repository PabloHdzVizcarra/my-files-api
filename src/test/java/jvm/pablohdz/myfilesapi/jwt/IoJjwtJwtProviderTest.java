package jvm.pablohdz.myfilesapi.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class IoJjwtJwtProviderTest {
  private JwtProvider jwtProvider;

  @BeforeEach
  void setUp() {
    jwtProvider = new IoJjwtJwtProvider();
    MockitoAnnotations.openMocks(this);
    String secretKey = "^BW5-QQ,MBs^kU)@~An#(WHEJL2{7he?kav/,vJPr28qrxbNZk~+tL;PW@3#>[.";
    Integer expirationTime = 60000;
    ReflectionTestUtils.setField(jwtProvider, "SECRET_KEY", secretKey);
    ReflectionTestUtils.setField(jwtProvider, "EXPIRATION_TIME", expirationTime);
  }

  @Test
  void whenCreateToken_thenTokenString() {
    String username = "john";
    String actualToken = jwtProvider.generateToken(username);

    Assertions.assertThat(actualToken)
        .isInstanceOf(String.class);
  }

}