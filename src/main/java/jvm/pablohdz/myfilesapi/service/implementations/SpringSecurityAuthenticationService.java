package jvm.pablohdz.myfilesapi.service.implementations;

import java.util.Optional;
import jvm.pablohdz.myfilesapi.model.User;
import jvm.pablohdz.myfilesapi.repository.UserRepository;
import jvm.pablohdz.myfilesapi.security.UtilSecurityContext;
import jvm.pablohdz.myfilesapi.service.AuthenticationService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SpringSecurityAuthenticationService implements AuthenticationService {
  private final UtilSecurityContext utilSecurityContext;
  private final UserRepository userRepository;

  public SpringSecurityAuthenticationService(
      UtilSecurityContext utilSecurityContext, UserRepository userRepository) {
    this.utilSecurityContext = utilSecurityContext;
    this.userRepository = userRepository;
  }

  @Override
  public User getCurrentUser() {
    String username = utilSecurityContext.getCurrentUsername();
    Optional<User> optionalUser = userRepository.findByUsername(username);
    return optionalUser.orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
