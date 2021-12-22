package jvm.pablohdz.myfilesapi.service;

import java.util.Collection;
import jvm.pablohdz.myfilesapi.dto.CSVFileDataDto;
import jvm.pablohdz.myfilesapi.dto.FileDto;
import jvm.pablohdz.myfilesapi.dto.FileServiceDataResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
  FileDto upload(MultipartFile csvFile);

  FileServiceDataResponse download(String s);

  CSVFileDataDto update(String id, MultipartFile file);

  Collection<FileDto> getFiles(String userId);

  void delete(String id);
}
