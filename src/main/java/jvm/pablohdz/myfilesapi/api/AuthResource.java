package jvm.pablohdz.myfilesapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
