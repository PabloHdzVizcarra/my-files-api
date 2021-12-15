package jvm.pablohdz.myfilesapi.exception;

public class FileCSVNotFoundException extends RuntimeException {

  public FileCSVNotFoundException(String fileId) {
    super("the file with the id: " + fileId + " is not exists, please check the data");
  }
}
