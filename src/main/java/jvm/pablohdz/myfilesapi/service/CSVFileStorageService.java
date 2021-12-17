package jvm.pablohdz.myfilesapi.service;

import org.springframework.core.io.InputStreamResource;

public interface CSVFileStorageService {
  String upload(byte[] file, String fileName, String username);

  InputStreamResource getFile(String storageId);

  void update(String storageId, byte[] bytes, String filename);
}
