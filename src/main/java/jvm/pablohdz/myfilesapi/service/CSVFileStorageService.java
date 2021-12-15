package jvm.pablohdz.myfilesapi.service;

import org.springframework.core.io.InputStreamResource;

public interface CSVFileStorageService {
  String upload(byte[] file, String fileName);

  InputStreamResource getFile(String storageId);
}
