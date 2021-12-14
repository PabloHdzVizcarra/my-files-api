package jvm.pablohdz.myfilesapi.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import jvm.pablohdz.myfilesapi.dto.ErrorResponse;

@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request
    ) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        List<String> listErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(getFieldErrorStringFunction())
            .collect(Collectors.toUnmodifiableList());
        errors.put("errors", listErrors);

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            "invalid data form the request",
            errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private Function<FieldError, String> getFieldErrorStringFunction() {
        return fieldError -> {
            if (fieldError.getDefaultMessage() != null)
                return fieldError.getDefaultMessage();
            return "some error";
        };
    }
}
