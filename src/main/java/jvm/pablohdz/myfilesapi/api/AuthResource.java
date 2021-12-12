package jvm.pablohdz.myfilesapi.api;

import jvm.pablohdz.myfilesapi.dto.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import jvm.pablohdz.myfilesapi.dto.LoginRequest;
import jvm.pablohdz.myfilesapi.service.UserService;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthResource {
  private final UserService userService;

  @Autowired
  public AuthResource(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(value = "/active.account/{token}")
  public void activeUser(@PathVariable(name = "token") String token) {
    userService.activeAccount(token);
  }

  @PostMapping(value = "/login")
  public ResponseEntity<AuthenticationResponse> login(
      @Valid @RequestBody LoginRequest loginRequest) {
    AuthenticationResponse response = userService.login(loginRequest);
    return ResponseEntity.ok(response);
  }
}
