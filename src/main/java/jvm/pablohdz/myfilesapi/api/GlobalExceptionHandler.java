package jvm.pablohdz.myfilesapi.api;

import static jvm.pablohdz.myfilesapi.api.ErrorCode.FILE_EXTENSION_INVALID;
import static jvm.pablohdz.myfilesapi.api.ErrorCode.FILE_ID_INVALID;
import static jvm.pablohdz.myfilesapi.api.ErrorCode.WEBHOOK_ERROR;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import jvm.pablohdz.myfilesapi.dto.ErrorResponseBuilder;
import jvm.pablohdz.myfilesapi.exception.CSVFileAlreadyRegisteredException;
import jvm.pablohdz.myfilesapi.exception.FileInvalidExtension;
import jvm.pablohdz.myfilesapi.exception.FileNotRegisterException;
import jvm.pablohdz.myfilesapi.exception.WebHookException;
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
  public ResponseEntity<ErrorResponseBuilder> handleCSVFileAlreadyRegisteredException(
      CSVFileAlreadyRegisteredException exception) {
    Map<String, List<String>> errors = new HashMap<>();
    errors.put("file", List.of(exception.getMessage(), "you can change the name of the file"));

    ErrorResponseBuilder response =
        ErrorResponseBuilder.builder()
            .code(ErrorCode.FILE_ALREADY_REGISTERED)
            .type(ErrorType.ENTITY_ERROR)
            .message("you are trying to save a file already registered")
            .timestamp(getCurrentTime())
            .param(errors)
            .build();

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(FileNotRegisterException.class)
  public ResponseEntity<ErrorResponseBuilder> handleFileCSVNotFoundException(
      FileNotRegisterException exception) {

    Map<String, List<String>> errors = new HashMap<>();
    errors.put("id", List.of(exception.getMessage()));
    ErrorResponseBuilder responseBuilder =
        ErrorResponseBuilder.builder()
            .message("you try found a file with invalid file id")
            .code(FILE_ID_INVALID)
            .type(ErrorType.INVALID_REQUEST_ERROR)
            .timestamp(getCurrentTime())
            .param(errors)
            .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBuilder);
  }

  @ExceptionHandler(FileInvalidExtension.class)
  public ResponseEntity<ErrorResponseBuilder> handleFileInvalidExtension(
      FileInvalidExtension exception) {
    Map<String, List<String>> errors = new HashMap<>();
    errors.put("file", List.of(exception.getMessage()));
    ErrorResponseBuilder errorResponse =
        ErrorResponseBuilder.builder()
            .message("You try upload a file with an invalid extension")
            .code(FILE_EXTENSION_INVALID)
            .type(ErrorType.INVALID_REQUEST_ERROR)
            .timestamp(getCurrentTime())
            .param(errors)
            .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(WebHookException.class)
  public ResponseEntity<ErrorResponseBuilder> handleWebHookException(WebHookException exception) {
    Map<String, List<String>> errors = new HashMap<>();
    errors.put("file", List.of(exception.getMessage()));
    ErrorResponseBuilder errorResponse =
        ErrorResponseBuilder.builder()
            .message("An error occurred with the execution of webhook")
            .code(WEBHOOK_ERROR)
            .type(ErrorType.API_ERROR)
            .timestamp(getCurrentTime())
            .param(errors)
            .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  private String getCurrentTime() {
    TimeZone timeZone = TimeZone.getDefault();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    dateFormat.setTimeZone(timeZone);
    return dateFormat.format(new Date());
  }
}
