package jvm.pablohdz.myfilesapi.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import jvm.pablohdz.myfilesapi.dto.UserRequest;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String create(@Valid @RequestBody UserRequest userRequest) {
        return "create user its work";
    }

}
