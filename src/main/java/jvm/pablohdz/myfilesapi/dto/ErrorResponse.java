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
    private Integer errorCode;
    private String message;
    private String cause;
    private Map<String, List<String>> error;
    private String timestamp;

    public ErrorResponse() {

    }

    public ErrorResponse(int value, String reasonPhrase, HttpStatus status, String message,
                         Map<String, List<String>> errors) {
        this.errorCode = value;
        this.cause = reasonPhrase;
        this.message = message;
        this.status = status;
        this.error = errors;
        this.timestamp = LocalDateTime
            .now()
            .format(DateTimeFormatter.ofPattern("hh:mm:ss yyyy-MM-dd"));
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Map<String, List<String>> getError() {
        return error;
    }

    public void setError(Map<String, List<String>> error) {
        this.error = error;
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
