package jvm.pablohdz.myfilesapi.dto;

import org.springframework.core.io.ByteArrayResource;

public class FileServiceDataResponse {
  private String filename;
  private ByteArrayResource resource;

  public FileServiceDataResponse() {}

  public FileServiceDataResponse(String filename, ByteArrayResource resource) {
    this.filename = filename;
    this.resource = resource;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public ByteArrayResource getResource() {
    return resource;
  }

  public void setResource(ByteArrayResource resource) {
    this.resource = resource;
  }
}
