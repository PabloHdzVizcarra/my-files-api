package jvm.pablohdz.myfilesapi.service;

import jvm.pablohdz.myfilesapi.dto.LoginRequest;
import jvm.pablohdz.myfilesapi.dto.UserRequest;

public interface UserService {
  /**
   * Sign Up new user in the application
   *
   * @param request request data with valid values
   */
  void create(UserRequest request);

  /**
   * Active a user that has already been registered
   *
   * @param token a validationToken already saved
   */
  void activeAccount(String token);

  /**
   * Login a user with username and password
   *
   * @param loginRequest data provided from the request
   */
  void login(LoginRequest loginRequest);
}
