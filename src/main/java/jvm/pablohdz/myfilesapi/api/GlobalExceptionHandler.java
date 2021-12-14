package jvm.pablohdz.myfilesapi.api;

import jvm.pablohdz.myfilesapi.exception.CSVFileAlreadyRegisteredException;
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
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.toString(),
            HttpStatus.BAD_REQUEST,
            "",
            errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(CSVFileAlreadyRegisteredException.class)
  public ResponseEntity<ErrorResponse> handleCSVFileAlreadyRegisteredException(
      CSVFileAlreadyRegisteredException exception) {
    String errorMessage = exception.getMessage();
    Map<String, List<String>> errors = new HashMap<>();
    errors.put("file", List.of(errorMessage));

    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.toString(),
            HttpStatus.CONFLICT,
            "only can save a file with a different name please,  change the file or change the name of the file",
            errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}
