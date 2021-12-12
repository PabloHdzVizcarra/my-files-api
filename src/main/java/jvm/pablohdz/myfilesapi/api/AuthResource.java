package jvm.pablohdz.myfilesapi.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthResource {

    @GetMapping(value = "/active.account/{token}")
    public void activeUser(@PathVariable(name = "token") String token) {

    }
}
