package jvm.pablohdz.myfilesapi.exception;

public class FileNotRegisterException extends RuntimeException {

  public FileNotRegisterException(String fileId) {
    super("the file with the id: " + fileId + " is not exists, please check the data");
  }
}
