package jvm.pablohdz.myfilesapi.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import jvm.pablohdz.myfilesapi.dto.ErrorResponseBuilder;
import jvm.pablohdz.myfilesapi.dto.ErrorStandardResponse;
import jvm.pablohdz.myfilesapi.exception.CSVFileAlreadyRegisteredException;
import jvm.pablohdz.myfilesapi.exception.FileInvalidExtension;
import jvm.pablohdz.myfilesapi.exception.FileNotRegisterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jvm.pablohdz.myfilesapi.dto.ErrorResponse;
import jvm.pablohdz.myfilesapi.exception.DataAlreadyRegistered;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DataAlreadyRegistered.class)
  public ResponseEntity<ErrorResponse> handleDataAlreadyRegistered(
      DataAlreadyRegistered exception) {
    String errorMessage = exception.getMessage();
    Map<String, List<String>> errors = new HashMap<>();
    errors.put("user", List.of(errorMessage));

    ErrorResponse errorResponse =
        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "", errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(CSVFileAlreadyRegisteredException.class)
  public ResponseEntity<ErrorStandardResponse> handleCSVFileAlreadyRegisteredException(
      CSVFileAlreadyRegisteredException exception) {
    String errorMessage = exception.getMessage();
    ErrorStandardResponse errorStandardResponse = new ErrorStandardResponse();
    errorStandardResponse.setCode("file_already_registered");
    errorStandardResponse.setType("entity_error");
    errorStandardResponse.setMessage(
        "you try save a file already been registered, you can change the file or change the name of the file");
    Map<String, List<String>> errors = new HashMap<>();
    errors.put("file", List.of(errorMessage));
    errorStandardResponse.setParam(List.of(errors));

    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorStandardResponse);
  }

  @ExceptionHandler(FileNotRegisterException.class)
  public ResponseEntity<ErrorStandardResponse> handleFileCSVNotFoundException(
      FileNotRegisterException exception) {
    String errorMessage = exception.getMessage();
    ErrorStandardResponse errorStandardResponse = new ErrorStandardResponse();
    errorStandardResponse.setCode("file_invalid_id");
    errorStandardResponse.setType("invalid_request_error");
    errorStandardResponse.setMessage("You are trying to find a file, with a file id invalid");
    Map<String, List<String>> errors = new HashMap<>();
    errors.put("file", List.of(errorMessage));
    errorStandardResponse.setParam(List.of(errors));

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorStandardResponse);
  }

  @ExceptionHandler(FileInvalidExtension.class)
  public ResponseEntity<ErrorResponseBuilder> handleFileInvalidExtension(
      FileInvalidExtension exception) {
    Map<String, List<String>> errors = new HashMap<>();
    errors.put("file", List.of(exception.getMessage()));
    ErrorResponseBuilder errorResponse =
        ErrorResponseBuilder.builder()
            .message("You try upload a file with an invalid extension")
            .code("file_extension_invalid")
            .type("invalid_request_error")
            .timestamp(getCurrentTime())
            .param(List.of(errors))
            .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  private String getCurrentTime() {
    TimeZone timeZone = TimeZone.getDefault();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    dateFormat.setTimeZone(timeZone);
    return dateFormat.format(new Date());
  }
}
