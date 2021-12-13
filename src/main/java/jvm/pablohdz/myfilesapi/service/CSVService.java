package jvm.pablohdz.myfilesapi.service;

import org.springframework.web.multipart.MultipartFile;

public interface CSVService {
  void uploadFileCSV(MultipartFile csvFile);
}
