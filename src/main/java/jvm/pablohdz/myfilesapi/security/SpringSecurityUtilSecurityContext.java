package jvm.pablohdz.myfilesapi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityUtilSecurityContext implements UtilSecurityContext {

  @Override
  public String getCurrentUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User principal = (User) authentication.getPrincipal();
    return principal.getUsername();
  }
}
