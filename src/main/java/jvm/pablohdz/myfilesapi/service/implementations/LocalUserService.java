package jvm.pablohdz.myfilesapi.service.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import jvm.pablohdz.myfilesapi.dto.UserRequest;
import jvm.pablohdz.myfilesapi.exception.DataAlreadyRegistered;
import jvm.pablohdz.myfilesapi.model.NotificationEmail;
import jvm.pablohdz.myfilesapi.model.User;
import jvm.pablohdz.myfilesapi.model.VerificationToken;
import jvm.pablohdz.myfilesapi.repository.UserRepository;
import jvm.pablohdz.myfilesapi.repository.VerificationTokenRepository;
import jvm.pablohdz.myfilesapi.service.EmailService;
import jvm.pablohdz.myfilesapi.service.UserService;

@Service
public class LocalUserService implements UserService {
    private final Logger logger = LoggerFactory.getLogger(LocalUserService.class.getSimpleName());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;


    @Autowired
    public LocalUserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                            VerificationTokenRepository verificationTokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailService = emailService;
    }

    @Override
    public void create(UserRequest request) {
        User user = createUserToSaved(request);
        User userSaved = createNewUser(user);

        logger.info("a new user was created with name: {} and email: {}",
                user.getFirstname(), user.getEmail());

        VerificationToken token = new VerificationToken();
        String tokenId = UUID.randomUUID().toString();
        token.setToken(tokenId);
        token.setUser(userSaved);

        verificationTokenRepository.save(token);
        emailService.sendEmail(new NotificationEmail("please active your account " +
                userSaved.getEmail(), userSaved.getEmail(),
                "http://localhost:8080/api/auth/account.verification/" + tokenId));

    }

    private User createUserToSaved(UserRequest request) {
        User user = new User();
        user.setFirstname(request.getFirstName());
        user.setLastname(request.getLastName());
        user.setUsername(request.getUsername());
        user.setNumberEmployee(Integer.valueOf(request.getNumEmployee()));
        user.setEmail(request.getEmail());
        String hashPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashPassword);
        return user;
    }

    private User createNewUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception exception) {
            logger.error("you try save an user already been registered with email: {}",
                    user.getEmail());
            throw new DataAlreadyRegistered(user.getEmail());
        }
    }
}
