package jvm.pablohdz.myfilesapi.exception;

public class FileInvalidExtension extends RuntimeException {

  public FileInvalidExtension(String filename) {
    super(
        "The file with name: "
            + filename
            + "isn't valid, only can upload a files with extension .csv");
  }
}
