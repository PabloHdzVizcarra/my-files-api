package jvm.pablohdz.myfilesapi.exception;

public class CSVFileAlreadyRegisteredException extends RuntimeException {

  public CSVFileAlreadyRegisteredException(String filename) {
    super("the file with name: " + filename + " has already been registered");
  }
}
