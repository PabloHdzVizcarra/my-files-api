package jvm.pablohdz.myfilesapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

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
    public ResponseEntity<String> create(@Valid @RequestBody UserRequest userRequest) throws UnknownHostException {
        userService.create(userRequest);
        URI location = URI.create("/api/users" + userRequest.getUsername());
        return ResponseEntity.created(location).build();
    }

}
