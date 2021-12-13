package jvm.pablohdz.myfilesapi.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import jvm.pablohdz.myfilesapi.model.User;
import jvm.pablohdz.myfilesapi.repository.UserRepository;
import jvm.pablohdz.myfilesapi.security.UtilSecurityContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class SpringSecurityAuthenticationServiceTest {
  private AuthenticationService authenticationService;
  @Mock private UtilSecurityContext utilSecurityContext;
  @Mock private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    authenticationService =
        new SpringSecurityAuthenticationService(utilSecurityContext, userRepository);
  }

  @Test
  void whenGetCurrentValidUser_thenReturnUser() {
    // Arrange
    String username = "john";
    User user = new User();
    user.setUsername(username);
    // Act
    when(utilSecurityContext.getCurrentUsername()).thenReturn(username);
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    User currentUser = authenticationService.getCurrentUser();
    // Assert
    assertThat(currentUser).isNotNull().isInstanceOf(User.class);
  }

  @Test
  void givenInvalidUser_whenGetCurrentUser_thenThrownException() {
    String username = "invalid-username";

    when(utilSecurityContext.getCurrentUsername()).thenReturn(username);
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> authenticationService.getCurrentUser())
        .hasMessageContaining(username)
        .isInstanceOf(UsernameNotFoundException.class);
  }
}
