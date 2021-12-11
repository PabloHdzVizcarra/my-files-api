package jvm.pablohdz.myfilesapi.exception;

public class DataAlreadyRegistered extends RuntimeException {
    public DataAlreadyRegistered(String property) {
        super("the data: " + property + " already been registered");
    }
}
