package jvm.pablohdz.myfilesapi.entity;

import org.springframework.core.io.InputStreamResource;

public class FileCSVData {
  private final String fileName;
  private final InputStreamResource data;

  public FileCSVData(String fileName, InputStreamResource data) {
    this.fileName = fileName;
    this.data = data;
  }

  public String getFileName() {
    return fileName;
  }

  public InputStreamResource getData() {
    return data;
  }
}
