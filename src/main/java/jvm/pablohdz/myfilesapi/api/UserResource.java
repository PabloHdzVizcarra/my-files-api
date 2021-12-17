package jvm.pablohdz.myfilesapi.api;

import java.util.Collection;
import java.util.List;
import jvm.pablohdz.myfilesapi.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import javax.validation.Valid;

import jvm.pablohdz.myfilesapi.dto.UserRequest;
import jvm.pablohdz.myfilesapi.service.UserService;

@RestController
@RequestMapping("/api")
public class UserResource {
  private final UserService userService;
  private final CSVService csvService;

  @Autowired
  public UserResource(UserService userService, CSVService csvService) {
    this.userService = userService;
    this.csvService = csvService;
  }

  @PostMapping(
      value = "/users",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> create(@Valid @RequestBody UserRequest userRequest) {
    userService.create(userRequest);
    URI location = URI.create("/api/users" + userRequest.getUsername());
    return ResponseEntity.created(location).build();
  }

  @GetMapping(value = "/user/{userId}/files", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<String>> getAllFiles(@PathVariable("userId") String userId) {
    csvService.getAllFilesByUserId(userId);
    return ResponseEntity.ok(List.of());
  }
}
