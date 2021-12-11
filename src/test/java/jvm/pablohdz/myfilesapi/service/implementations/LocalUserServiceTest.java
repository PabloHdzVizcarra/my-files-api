package jvm.pablohdz.myfilesapi.service.implementations;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jvm.pablohdz.myfilesapi.dto.UserRequest;
import jvm.pablohdz.myfilesapi.model.User;
import jvm.pablohdz.myfilesapi.repository.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocalUserServiceTest {
    private LocalUserService userService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userService = new LocalUserService(userRepository);
    }

    @Test
    void givenCorrectData_whenCreateUser() {
        // Arrange
        User user = createMockUser();
        UserRequest userRequest = createMockUserRequest();
        when(userRepository.save(any()))
            .thenReturn(user);

        // Act
        // Assert
        Assertions.assertThatCode(() -> userService.create(userRequest))
            .doesNotThrowAnyException();
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
}