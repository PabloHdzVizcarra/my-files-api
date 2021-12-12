package jvm.pablohdz.myfilesapi.service.implementations;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import jvm.pablohdz.myfilesapi.dto.AuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import jvm.pablohdz.myfilesapi.dto.LoginRequest;
import jvm.pablohdz.myfilesapi.dto.UserRequest;
import jvm.pablohdz.myfilesapi.exception.AuthenticationCredentialsInvalid;
import jvm.pablohdz.myfilesapi.exception.DataAlreadyRegistered;
import jvm.pablohdz.myfilesapi.exception.ValidationTokenNotFound;
import jvm.pablohdz.myfilesapi.jwt.JwtProvider;
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
  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;

  @Autowired
  public LocalUserService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      VerificationTokenRepository verificationTokenRepository,
      EmailService emailService,
      AuthenticationManager authenticationManager,
      JwtProvider jwtProvider) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.verificationTokenRepository = verificationTokenRepository;
    this.emailService = emailService;
    this.authenticationManager = authenticationManager;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public void create(UserRequest request) {
    User user = createUserToSaved(request);
    User userSaved = createNewUser(user);

    logger.info(
        "a new user was created with name: {} and email: {}", user.getFirstname(), user.getEmail());

    VerificationToken token = new VerificationToken();
    String tokenId = UUID.randomUUID().toString();
    token.setToken(tokenId);
    token.setUser(userSaved);

    verificationTokenRepository.save(token);
    emailService.sendEmail(
        new NotificationEmail(
            "please active your account " + userSaved.getEmail(),
            userSaved.getEmail(),
            "http://localhost:8080/api/auth/active.account/" + tokenId));
  }

  @Override
  public void activeAccount(String token) {
    VerificationToken foundVerificationToken = isValidToken(token);
    updateActiveStatusFromTheUser(foundVerificationToken);

    logger.info(
        "updated status of the user: {}, the user is currently active now",
        foundVerificationToken.getUser().getFirstname());
  }

  @Override
  @Transactional(readOnly = true)
  public AuthenticationResponse login(LoginRequest loginRequest) {
    String username = loginRequest.getUsername();
    String password = loginRequest.getPassword();
    User userFound = isValidUsername(username);

    // TODO: 12/12/2021 setup user to context security
//    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//        new UsernamePasswordAuthenticationToken(username, password);
//    Authentication authenticate =
//        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

    // TODO: 12/12/2021 return token generated
    String token = jwtProvider.generateToken(userFound.getUsername());
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    return new AuthenticationResponse(
        token, username, dtf.format(LocalDateTime.now()));
  }

  private User isValidUsername(String username) {
    Optional<User> optionalUser = userRepository.findByUsername(username);

    if (optionalUser.isEmpty())
      throw new AuthenticationCredentialsInvalid("the username provided is not valid");

    return optionalUser.get();
  }

  private void updateActiveStatusFromTheUser(VerificationToken foundVerificationToken) {
    User user = foundVerificationToken.getUser();
    user.setActive(true);
    userRepository.save(user);
  }

  private VerificationToken isValidToken(String token) {
    Optional<VerificationToken> optionalVerificationToken =
        verificationTokenRepository.findByToken(token);

    if (optionalVerificationToken.isEmpty()) throw new ValidationTokenNotFound(token);

    return optionalVerificationToken.get();
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
      logger.error("you try save an user already been registered with email: {}", user.getEmail());
      throw new DataAlreadyRegistered(user.getEmail());
    }
  }
}
