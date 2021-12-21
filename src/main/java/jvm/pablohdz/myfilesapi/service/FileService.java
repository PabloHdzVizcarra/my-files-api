package jvm.pablohdz.myfilesapi.service;

import java.util.Collection;
import jvm.pablohdz.myfilesapi.dto.CSVFileDataDto;
import jvm.pablohdz.myfilesapi.dto.CSVFileDto;
import jvm.pablohdz.myfilesapi.dto.FileServiceDataResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
  CSVFileDto uploadFile(MultipartFile csvFile);

  FileServiceDataResponse download(String s);

  CSVFileDataDto update(String id, MultipartFile file);

  Collection<CSVFileDto> getAllFilesByUserId(String userId);

  void deleteFile(String id);
}
