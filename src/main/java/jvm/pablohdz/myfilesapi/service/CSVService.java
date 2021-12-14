package jvm.pablohdz.myfilesapi.service;

import jvm.pablohdz.myfilesapi.dto.CSVFileDto;
import org.springframework.web.multipart.MultipartFile;

public interface CSVService {
  CSVFileDto uploadFileCSV(MultipartFile csvFile);
}
