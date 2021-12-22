package jvm.pablohdz.myfilesapi.entity;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public class FileData {
  private String filename;
  private MultipartFile data;
  private InputStreamResource dataStreamResource;
  private byte[] bytes;
  private String contentType;

  public FileData(String filename, byte[] bytes, String contentType) {
    this.filename = filename;
    this.bytes = bytes;
    this.contentType = contentType;
  }

  public FileData(String filename, MultipartFile data) {
    this.filename = filename;
    this.data = data;
  }

  public FileData(String filename, InputStreamResource dataStreamResource) {
    this.filename = filename;
    this.dataStreamResource = dataStreamResource;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public MultipartFile getData() {
    return data;
  }

  public void setData(MultipartFile data) {
    this.data = data;
  }

  public InputStreamResource getDataStreamResource() {
    return dataStreamResource;
  }

  public void setDataStreamResource(InputStreamResource dataStreamResource) {
    this.dataStreamResource = dataStreamResource;
  }

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    this.bytes = bytes;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }
}
