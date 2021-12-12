package jvm.pablohdz.myfilesapi.exception;

public class ValidationTokenNotFound extends RuntimeException {
    public ValidationTokenNotFound(String token) {
        super("the verification token with value: " +
                token +
                " is not exists");
    }
}
