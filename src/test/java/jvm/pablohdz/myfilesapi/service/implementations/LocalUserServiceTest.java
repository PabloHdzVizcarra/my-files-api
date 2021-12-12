package jvm.pablohdz.myfilesapi.service.implementations;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import jvm.pablohdz.myfilesapi.dto.UserRequest;
import jvm.pablohdz.myfilesapi.exception.DataAlreadyRegistered;
import jvm.pablohdz.myfilesapi.exception.ValidationTokenNotFound;
import jvm.pablohdz.myfilesapi.model.User;
import jvm.pablohdz.myfilesapi.model.VerificationToken;
import jvm.pablohdz.myfilesapi.repository.UserRepository;
import jvm.pablohdz.myfilesapi.repository.VerificationTokenRepository;
import jvm.pablohdz.myfilesapi.service.EmailService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocalUserServiceTest {
    private LocalUserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        userService = new LocalUserService(userRepository, passwordEncoder, verificationTokenRepository, emailService);
    }

    @Test
    void givenCorrectData_whenCreateUser() {
        // Arrange
        User user = createMockUser();
        UserRequest userRequest = createMockUserRequest();
        // Act
        when(userRepository.save(any())).thenReturn(user);
        // Assert
        Assertions.assertThatCode(() -> userService.create(userRequest)).doesNotThrowAnyException();
    }

    @Test
    void givenUserExisting_whenCreate_thenThrownException() {
        // Arrange
        UserRequest mockUserRequest = createMockUserRequest();
        // Act
        when(userRepository.save(any())).thenThrow(DataAlreadyRegistered.class);
        // Assert
        Assertions.assertThatThrownBy(() -> userService.create(mockUserRequest))
                .isInstanceOf(DataAlreadyRegistered.class)
                .hasMessageContaining(mockUserRequest.getEmail());
    }

    private UserRequest createMockUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("terminator");
        userRequest.setFirstName("john");
        userRequest.setLastName("connor");
        userRequest.setNumEmployee("00907810");
        userRequest.setEmail("example@example.com");
        return userRequest;
    }

    private User createMockUser() {
        User user = new User();
        user.setFirstname("john");
        user.setLastname("connor");
        user.setUsername("terminator");
        user.setNumberEmployee(Integer.valueOf("00907810"));
        user.setEmail("example@example.com");
        return user;
    }

    @Test
    void givenInvalidToken_whenActiveAccount_thenThrownException() {
        //Arrange
        String validationToken = "wrong-token";
        //Act
        when(verificationTokenRepository.findByToken(validationToken))
                .thenReturn(Optional.empty());
        //Assert
        Assertions.assertThatThrownBy(() -> userService.activeAccount(validationToken))
                .isInstanceOf(ValidationTokenNotFound.class)
                .hasMessageContaining(validationToken);
    }

    @Test
    void givenValidToken_whenActiveAccount_thenChangeStatusUserActive() {
        //Arrange
        String validationToken = "valid-token";
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(validationToken);
        verificationToken.setId(1L);
        User mockUser = createMockUser();
        verificationToken.setUser(mockUser);
        //Act
        when(verificationTokenRepository.findByToken(validationToken))
                .thenReturn(Optional.of(verificationToken));
        when(userRepository.save(mockUser))
                .thenReturn(mockUser);
        userService.activeAccount(validationToken);
        Boolean actualActiveStatusUser = mockUser.getActive();
        //Assert
        Assertions.assertThat(actualActiveStatusUser)
                .withFailMessage("the status of the user is not changed")
                .isTrue();
    }
}