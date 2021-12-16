package jvm.pablohdz.myfilesapi.service;

import jvm.pablohdz.myfilesapi.dto.CSVFileDto;
import jvm.pablohdz.myfilesapi.entity.FileCSVData;
import org.springframework.web.multipart.MultipartFile;

public interface CSVService {
  CSVFileDto uploadFileCSV(MultipartFile csvFile);

  FileCSVData downloadById(String s);

  FileCSVData update(String id, MultipartFile file);
}
