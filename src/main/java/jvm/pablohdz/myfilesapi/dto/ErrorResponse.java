package jvm.pablohdz.myfilesapi.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Universal request when occurs any error of the application
 */
public class ErrorResponse {
    private HttpStatus status;
    private Integer httpCode;
    private String message;
    private Map<String, List<String>> errors;
    private String timestamp;

    public ErrorResponse() {

    }

    public ErrorResponse(int value, HttpStatus status, String message,
        Map<String, List<String>> errors) {
        this.httpCode = value;
        this.message = message;
        this.status = status;
        this.errors = errors;
        this.timestamp = LocalDateTime
            .now()
            .format(DateTimeFormatter.ofPattern("hh:mm:ss yyyy-MM-dd"));
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }




}
