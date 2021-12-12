package jvm.pablohdz.myfilesapi.service;

import jvm.pablohdz.myfilesapi.dto.UserRequest;

public interface UserService {
    /**
     * Sign Up new user in the application
     *
     * @param request request data with valid values
     */
    void create(UserRequest request);

    /**
     * active a user already registered
     *
     * @param token a validationToken already saved
     */
    void activeAccount(String token);
}
