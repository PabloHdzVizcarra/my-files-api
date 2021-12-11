package jvm.pablohdz.myfilesapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import jvm.pablohdz.myfilesapi.dto.UserRequest;
import jvm.pablohdz.myfilesapi.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserResource {
    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String create(@Valid @RequestBody UserRequest userRequest) {
        userService.create(userRequest);
        return "create user its work";
    }

}
